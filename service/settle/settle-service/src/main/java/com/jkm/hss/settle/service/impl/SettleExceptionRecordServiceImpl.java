package com.jkm.hss.settle.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.entity.WithdrawOrder;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.requestparam.OrderBalanceStatistics;
import com.jkm.hss.bill.helper.requestparam.QuerySettlementRecordParams;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.bill.service.WithdrawOrderService;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.product.enums.EnumUpperChannel;
import com.jkm.hss.settle.dao.SettleExceptionRecordDao;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import com.jkm.hss.settle.entity.SettleExceptionRecord;
import com.jkm.hss.settle.enums.EnumAccountCheckStatus;
import com.jkm.hss.settle.enums.EnumSettleStatus;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import com.jkm.hss.settle.service.SettleExceptionRecordService;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-07-21.
 */
@Service
@Slf4j
public class SettleExceptionRecordServiceImpl implements SettleExceptionRecordService {

    @Autowired
    private SettleExceptionRecordDao settleExceptionRecordDao;
    @Autowired
    private WithdrawOrderService withdrawOrderService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private AccountSettleAuditRecordService accountSettleAuditRecordService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Override
    public void insert(SettleExceptionRecord settleExceptionRecord) {

        this.settleExceptionRecordDao.insert(settleExceptionRecord);
    }

    @Override
    public void update(SettleExceptionRecord settleExceptionRecord) {
        this.settleExceptionRecordDao.update(settleExceptionRecord);
    }

    @Override
    public PageModel<SettleExceptionRecord> listSettlementRecordByParam(QuerySettlementRecordParams querySettlementRecordParams) {
        final PageModel<SettleExceptionRecord> result = new PageModel<>(querySettlementRecordParams.getPageNo(), querySettlementRecordParams.getPageSize());
        querySettlementRecordParams.setOffset(result.getFirstIndex());
        querySettlementRecordParams.setCount(result.getPageSize());
        querySettlementRecordParams.setStartDate(querySettlementRecordParams.getStartDate() + " 00:00:00");
        querySettlementRecordParams.setEndDate(querySettlementRecordParams.getEndDate() + " 23:59:59");
        final int count = this.settleExceptionRecordDao.selectCountByParam(querySettlementRecordParams);
        final List<SettleExceptionRecord> records = this.settleExceptionRecordDao.selectByParam(querySettlementRecordParams);
        result.setCount(count);
        result.setRecords(records);
        return result;
    }

    @Override
    public SettleExceptionRecord selectByIdWithLock(long id) {
        return this.settleExceptionRecordDao.selectByIdWithLock(id);
    }

    @Transactional
    @Override
    public Pair<Integer, String> handleRecord(long settleExceptionId) {
        SettleExceptionRecord settleExceptionRecord =
                this.selectByIdWithLock(settleExceptionId);
        //校验结算单对应的提现状态
        final Order withdrawOrder = this.orderService.getByIdWithLock(settleExceptionRecord.getWithdrawOrderId()).get();
        if (withdrawOrder.isWithDrawComplete()){
            //对挂起订单进行结算
            final String[] orders = withdrawOrder.getGoodsDescribe().split(",");
            final Order order = this.orderService.getBySn(orders[0]);
            //生成结算单，
            final List<Long> list = this.generateHsySettleRecordTask(order.getSettleTime(), withdrawOrder.getPayer());
            // 更新结算状态
            for (Long auditId : list){
                this.updateSettleStatus(auditId);
            }
        }
        return Pair.of(-1, "处理失败,存在未处理的提现订单");
    }

    private List<Long> generateHsySettleRecordTask(Date date, long accountId) {
        final List<Long> auditIds = new ArrayList<>();
        Date settleDate = date;
        if (null == settleDate) {
            settleDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd) , DateFormatUtil.yyyy_MM_dd);
        }
        final List<OrderBalanceStatistics> merchantOrderBalanceStatistics = this.orderService.statisticsPendingBalanceOrder(settleDate, null,accountId);
        final ArrayList<SettlementRecord> merchantSettlementRecords = new ArrayList<>();
        if (!CollectionUtils.isEmpty(merchantOrderBalanceStatistics)) {
            for (OrderBalanceStatistics merchantStatistics : merchantOrderBalanceStatistics) {
                if (merchantStatistics.getUpperChannel() != EnumUpperChannel.XMMS_BANK.getId()){
                    merchantStatistics.setSettleChannel(EnumSettleChannel.ALL.getId());
                }
                final AccountSettleAuditRecord accountSettleAuditRecord = new AccountSettleAuditRecord();
                final SettlementRecord settlementRecord = new SettlementRecord();
                final AppBizShop shop = this.hsyShopDao.findAppBizShopByAccountID(merchantStatistics.getAccountId()).get(0);
                accountSettleAuditRecord.setUserNo(shop.getGlobalID());
                accountSettleAuditRecord.setUserName(shop.getShortName());
                accountSettleAuditRecord.setAccountUserType(EnumAccountUserType.MERCHANT.getId());
                accountSettleAuditRecord.setAccountId(merchantStatistics.getAccountId());
                accountSettleAuditRecord.setTradeDate(merchantStatistics.getTradeEndTime());
                accountSettleAuditRecord.setBalanceStartTime(merchantStatistics.getTradeStartTime());
                accountSettleAuditRecord.setBalanceEndTime(merchantStatistics.getTradeEndTime());
                accountSettleAuditRecord.setUpperChannel(merchantStatistics.getUpperChannel());
                accountSettleAuditRecord.setTradeNumber(merchantStatistics.getCount());
                accountSettleAuditRecord.setSettleAmount(merchantStatistics.getAmount().subtract(merchantStatistics.getPoundage()));
                accountSettleAuditRecord.setAccountCheckStatus(EnumAccountCheckStatus.DUE_ACCOUNT_CHECK.getId());
                accountSettleAuditRecord.setSettleDate(settleDate);
                accountSettleAuditRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
                this.accountSettleAuditRecordService.add(accountSettleAuditRecord);
                auditIds.add(accountSettleAuditRecord.getId());
                //生成结算单
                settlementRecord.setAccountUserType(EnumAccountUserType.MERCHANT.getId());
                settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_CARD.getId());
                settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(settlementRecord.getAccountUserType(), settlementRecord.getSettleDestination()));
                settlementRecord.setSettleAuditRecordId(accountSettleAuditRecord.getId());
                settlementRecord.setAccountId(merchantStatistics.getAccountId());
                settlementRecord.setUserNo(accountSettleAuditRecord.getUserNo());
                settlementRecord.setUserName(accountSettleAuditRecord.getUserName());
                settlementRecord.setAppId(EnumAppType.HSY.getId());
                settlementRecord.setSettleDate(accountSettleAuditRecord.getSettleDate());
                settlementRecord.setTradeNumber(accountSettleAuditRecord.getTradeNumber());
                settlementRecord.setTradeAmount(merchantStatistics.getAmount());
                settlementRecord.setSettleAmount(merchantStatistics.getAmount().subtract(merchantStatistics.getPoundage()));
                settlementRecord.setSettlePoundage(merchantStatistics.getPoundage());
                settlementRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
                settlementRecord.setSettleMode(EnumSettleModeType.CHANNEL_SETTLE.getId());
                settlementRecord.setStatus(EnumSettlementRecordStatus.WAIT_WITHDRAW.getId());
                settlementRecord.setUpperChannel(accountSettleAuditRecord.getUpperChannel());
                settlementRecord.setBalanceStartTime(accountSettleAuditRecord.getBalanceStartTime());
                settlementRecord.setBalanceEndTime(accountSettleAuditRecord.getBalanceEndTime());
                settlementRecord.setSettleChannel(merchantStatistics.getSettleChannel());
                final long settlementRecordId = this.settlementRecordService.add(settlementRecord);
                merchantSettlementRecords.add(settlementRecord);
                final int updateCount = this.orderService.markOrder2SettlementIng(settleDate, merchantStatistics.getAccountId(),
                        settlementRecordId, EnumSettleStatus.SETTLE_ING.getId(), merchantStatistics.getUpperChannel());
                Preconditions.checkState(updateCount == merchantStatistics.getCount(), "将结算单id更新到交易中，个数不一致");
                log.info("账户[{}], 生成结算单后，将其id[{}]保存到交易中,更新记录数[{}]", merchantStatistics.getAccountId(), settlementRecordId, updateCount);
            }
        }
        this.generateSettlementAuditRecordSendMsg(merchantSettlementRecords);
        return auditIds;
    }

    private void generateSettlementAuditRecordSendMsg(final ArrayList<SettlementRecord> settlementRecords) {
        for (SettlementRecord settlementRecord : settlementRecords) {
            try {
                final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(settlementRecord.getAccountId()).get(0);
                final AppBizShop appBizShop = this.hsyShopDao.findAppBizShopByAccountID(settlementRecord.getAccountId()).get(0);
                final AppBizCard appBizCard = new AppBizCard();
                appBizCard.setSid(appBizShop.getId());
                final AppBizCard appBizCard1 = this.hsyShopDao.findAppBizCardByParam(appBizCard).get(0);
                final String cardNO = appBizCard1.getCardNO();
                final SimpleDateFormat dateFormat = new SimpleDateFormat("MM月dd日");
                final Map<String, String> params = ImmutableMap.of("date", dateFormat.format(settlementRecord.getSettleDate()),
                        "amount", settlementRecord.getSettleAmount().toPlainString(),
                        "bankCardNo", cardNO.substring(cardNO.length() - 4));
                final EnumSettleChannel enumSettleChannel = EnumSettleChannel.of(settlementRecord.getSettleChannel());
                switch (enumSettleChannel){
                    case WECHANT:
                        this.sendMessageService.sendMessage(SendMessageParams.builder()
                                .mobile(appAuUser.getCellphone())
                                .uid(settlementRecord.getId() + "")
                                .data(params)
                                .userType(EnumUserType.FOREGROUND_USER)
                                .noticeType(EnumNoticeType.SETTLEMENT_SUCCESS_WX)
                                .build()
                        );
                        break;
                    case ALIPAY:
                        this.sendMessageService.sendMessage(SendMessageParams.builder()
                                .mobile(appAuUser.getCellphone())
                                .uid(settlementRecord.getId() + "")
                                .data(params)
                                .userType(EnumUserType.FOREGROUND_USER)
                                .noticeType(EnumNoticeType.SETTLEMENT_SUCCESS_ZFB)
                                .build()
                        );
                        break;
                    case ALL:
                        this.sendMessageService.sendMessage(SendMessageParams.builder()
                                .mobile(appAuUser.getCellphone())
                                .uid(settlementRecord.getId() + "")
                                .data(params)
                                .userType(EnumUserType.FOREGROUND_USER)
                                .noticeType(EnumNoticeType.SETTLEMENT_SUCCESS)
                                .build()
                        );
                        break;
                }

            } catch (final Throwable e) {
                log.error("hsy结算单[" + settlementRecord.getId() + "],发送短信失败", e);
            }
        }
    }

    private void updateSettleStatus(long recordId) {
        log.info("结算审核记录[{}], 开始结算", recordId);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final AccountSettleAuditRecord accountSettleAuditRecord = this.accountSettleAuditRecordService.getByIdWithLock(recordId).get();
        final SettlementRecord settlementRecord = this.settlementRecordService.getBySettleAuditRecordId(accountSettleAuditRecord.getId()).get();
        if (!accountSettleAuditRecord.isDueSettle()) {
            log.info("结算审核记录[{}],状态不是待结算，不可以进行结算", recordId);
            return;
        }
        final Account account = this.accountService.getByIdWithLock(accountSettleAuditRecord.getAccountId()).get();
        //待结算金额减少
        Preconditions.checkState(account.getDueSettleAmount().compareTo(accountSettleAuditRecord.getSettleAmount()) >= 0,
                "账户的待结算总金额不可以小于单笔结算审核记录[{}]的结算金额", recordId);
        this.accountService.decreaseSettleAmount(account.getId(), accountSettleAuditRecord.getSettleAmount());
        this.settleAccountFlowService.addSettleAccountFlow(account.getId(), settlementRecord.getSettleNo(), accountSettleAuditRecord.getSettleAmount(),
                "T1结算出款", EnumAccountFlowType.DECREASE, settlementRecord.getAppId(), settlementRecord.getBalanceEndTime(),
                settlementRecord.getSettleDate(), settlementRecord.getAccountUserType());
        this.accountService.decreaseTotalAmount(account.getId(), accountSettleAuditRecord.getSettleAmount());
        final int updateCount = this.orderService.markOrder2SettlementSuccess(settlementRecord.getId(), EnumSettleStatus.SETTLED_ALL.getId(), EnumSettleStatus.SETTLE_ING.getId());
        Preconditions.checkState(updateCount == settlementRecord.getTradeNumber(),
                    "对结算审核记录[{}]-更新交易结算状态，数目不一致[{}]--[{}]", recordId, settlementRecord.getTradeNumber(), updateCount);
        this.accountSettleAuditRecordService.updateSettleStatus(recordId, EnumSettleStatus.SETTLED_ALL.getId());
        this.settlementRecordService.updateSettleStatus(settlementRecord.getId(), EnumSettleStatus.SETTLED_ALL.getId());
        stopWatch.stop();
        log.info("结算审核记录[{}],结算结束，用时[{}]", recordId, stopWatch.getTime());
    }
}
