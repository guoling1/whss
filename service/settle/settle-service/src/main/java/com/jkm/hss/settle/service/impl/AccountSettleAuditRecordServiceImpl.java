package com.jkm.hss.settle.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.helper.selectresponse.SettleAccountFlowStatistics;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.entity.WithdrawOrder;
import com.jkm.hss.bill.enums.EnumSettleChannel;
import com.jkm.hss.bill.enums.EnumSettleDestinationType;
import com.jkm.hss.bill.enums.EnumSettleModeType;
import com.jkm.hss.bill.enums.EnumSettlementRecordStatus;
import com.jkm.hss.bill.helper.requestparam.OrderBalanceStatistics;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.bill.service.WithdrawOrderService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumUpperChannel;
import com.jkm.hss.settle.dao.AccountSettleAuditRecordDao;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import com.jkm.hss.settle.entity.SettleExceptionRecord;
import com.jkm.hss.settle.enums.EnumAccountCheckStatus;
import com.jkm.hss.settle.enums.EnumSettleStatus;
import com.jkm.hss.settle.helper.requestparam.ListSettleAuditRecordRequest;
import com.jkm.hss.settle.helper.responseparam.AppSettleRecordDetailResponse;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import com.jkm.hss.settle.service.SettleExceptionRecordService;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yulong.zhang on 2017/1/12.
 */
@Slf4j
@Service(value = "accountSettleAuditRecordService")
public class AccountSettleAuditRecordServiceImpl implements AccountSettleAuditRecordService {

    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private AccountSettleAuditRecordDao accountSettleAuditRecordDao;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private SendMessageService sendMessageService;
    @Autowired
    private WithdrawOrderService withdrawOrderService;
    @Autowired
    private SettleExceptionRecordService settleExceptionRecordService;
     /**
     * {@inheritDoc}
     *
     * @param record
     */
    @Override
    public void add(final AccountSettleAuditRecord record) {
        this.accountSettleAuditRecordDao.insert(record);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public int updateAccountCheckStatus(final long id, final int status) {
        return this.accountSettleAuditRecordDao.updateAccountCheckStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public int updateSettleStatus(final long id, final int status) {
        return this.accountSettleAuditRecordDao.updateSettleStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<AccountSettleAuditRecord> getById(final long id) {
        return Optional.fromNullable(this.accountSettleAuditRecordDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<AccountSettleAuditRecord> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.accountSettleAuditRecordDao.selectByIdWithLock(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param recordIds
     * @return
     */
    @Override
    public List<AccountSettleAuditRecord> getByIds(final List<Long> recordIds) {
        if (CollectionUtils.isEmpty(recordIds)) {
            return Collections.emptyList();
        }
        return this.accountSettleAuditRecordDao.selectByIds(recordIds);
    }

    /**
     * {@inheritDoc}
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    @Override
    public String appSettleRecordList(final String dataParam, final AppParam appParam) {
        final JSONObject paramJo = JSONObject.parseObject(dataParam);
        final int pageNo = paramJo.getIntValue("pageNo");
        final int pageSize = paramJo.getIntValue("pageSize");
        final long accountId = paramJo.getLongValue("accountId");
        final PageModel<JSONObject> pageModel = new PageModel<>(pageNo, pageSize);
        final PageModel<SettlementRecord> settlementRecordPageModel = this.settlementRecordService.listSettlementRecordByAccountId(accountId, pageNo, pageSize);
        if (CollectionUtils.isEmpty(settlementRecordPageModel.getRecords())) {
            pageModel.setCount(0);
            pageModel.setRecords(Collections.<JSONObject>emptyList());
            return JSON.toJSONString(pageModel);
        }
        final List<JSONObject> recordList = new ArrayList<>();
        pageModel.setRecords(recordList);
        for (SettlementRecord record : settlementRecordPageModel.getRecords()) {
            final JSONObject jo = new JSONObject();
            recordList.add(jo);
            jo.put("recordId", record.getId());
            jo.put("settleDate", record.getSettleDate());
            jo.put("number", record.getTradeNumber());
            jo.put("settleAmount", record.getSettleAmount().toPlainString());
            jo.put("settleStatus", record.getSettleStatus());
            jo.put("settleStatusValue", EnumSettleStatus.of(record.getSettleStatus()));
        }
        return JSON.toJSONString(pageModel);
    }

    /**
     * {@inheritDoc}
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    @Override
    public String appSettleRecordDetail(final String dataParam, final AppParam appParam) {
        final JSONObject paramJo = JSONObject.parseObject(dataParam);
        final long recordId = paramJo.getLongValue("recordId");
        final SettlementRecord settlementRecord = this.settlementRecordService.getById(recordId).get();
        final AppSettleRecordDetailResponse appSettleRecordDetailResponse = new AppSettleRecordDetailResponse();
        appSettleRecordDetailResponse.setRecordId(recordId);
        appSettleRecordDetailResponse.setSettleAmount(settlementRecord.getSettleAmount().toPlainString());
        appSettleRecordDetailResponse.setNumber(settlementRecord.getTradeNumber());
        appSettleRecordDetailResponse.setTradeAmount(settlementRecord.getTradeAmount().toPlainString());
        appSettleRecordDetailResponse.setFeeAmount(settlementRecord.getSettlePoundage().toPlainString());
        appSettleRecordDetailResponse.setTradeDate(settlementRecord.getBalanceEndTime());
        appSettleRecordDetailResponse.setTradeStartDate(settlementRecord.getBalanceStartTime());
        appSettleRecordDetailResponse.setTradeEndDate(settlementRecord.getBalanceEndTime());
        appSettleRecordDetailResponse.setSettleDate(settlementRecord.getSettleDate());
        return JSON.toJSONString(appSettleRecordDetailResponse);
    }

    /**
     * {@inheritDoc}
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    @Override
    public String appGetOrderListByRecordId(final String dataParam, final AppParam appParam) {
        final JSONObject paramJo = JSONObject.parseObject(dataParam);
        final int pageNo = paramJo.getIntValue("pageNo");
        final int pageSize = paramJo.getIntValue("pageSize");
        final long recordId = paramJo.getLongValue("recordId");
        final PageModel<JSONObject> pageModel = new PageModel<>(pageNo, pageSize);
        final int count = this.orderService.getOrderCountBySettlementRecordId(recordId);
        final List<Order> records = this.orderService.getOrderBySettlementRecordId(recordId, pageModel.getFirstIndex(), pageSize);
        if (CollectionUtils.isEmpty(records)) {
            pageModel.setCount(0);
            pageModel.setRecords(Collections.<JSONObject>emptyList());
            return JSON.toJSONString(pageModel);
        }
        pageModel.setCount(count);
        final List<JSONObject> jsonObjects = Lists.transform(records, new Function<Order, JSONObject>() {
            @Override
            public JSONObject apply(Order order) {
                final JSONObject jo = new JSONObject();
                jo.put("tradeAmount", order.getTradeAmount().toPlainString());
                jo.put("tradeDate", order.getPaySuccessTime());
                jo.put("feeAmount", order.getPoundage());
                final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(order.getPayChannelSign());
                jo.put("type", payChannelSign.getPaymentChannel().getId());
                return jo;
            }
        });
        pageModel.setRecords(jsonObjects);
        return JSON.toJSONString(pageModel);
    }

    /**
     * {@inheritDoc}
     *
     * @param date 结算日期
     */
    @Override
    @Transactional
    synchronized public Pair<Integer, String> generateHsySettleAuditRecordTask(final Date date) {
        Date settleDate = date;
        if (null == settleDate) {
            settleDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd) , DateFormatUtil.yyyy_MM_dd);
        }
        //挂起的
        List<Long> accountIdList = this.handleWithdrawIngSettle();
        final List<OrderBalanceStatistics> merchantOrderBalanceStatistics = this.orderService.statisticsPendingBalanceOrder(settleDate, accountIdList,0);
        log.info("今日[{}]商户生成结算审核记录,个数[{}]", settleDate, merchantOrderBalanceStatistics.size());
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
                this.add(accountSettleAuditRecord);

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
        //代理商，公司账户生成结算审核记录
        final List<SettleAccountFlowStatistics> settleAccountFlowStatistics = this.settleAccountFlowService.statisticsYesterdayFlow(settleDate);
        log.info("今日[{}]的待结算流水-生成（代理商/公司）结算审核记录,个数[{}]", settleDate, settleAccountFlowStatistics.size());
        final ArrayList<Long> dealerAccountIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(settleAccountFlowStatistics)) {
            for (SettleAccountFlowStatistics statistics : settleAccountFlowStatistics) {
                if (EnumAccountUserType.DEALER.getId() == statistics.getAccountUserType()) {
                    dealerAccountIds.add(statistics.getAccountId());
                }
            }
            final List<Dealer> dealers = this.dealerService.getByAccountIds(dealerAccountIds);
            final Map<Long, Dealer> dealerMap = Maps.uniqueIndex(dealers, new Function<Dealer, Long>() {
                @Override
                public Long apply(Dealer input) {
                    return input.getAccountId();
                }
            });
            for (SettleAccountFlowStatistics statistics : settleAccountFlowStatistics) {
                final EnumAccountUserType accountUserType = EnumAccountUserType.of(statistics.getAccountUserType());
                final AccountSettleAuditRecord accountSettleAuditRecord = new AccountSettleAuditRecord();
                final SettlementRecord settlementRecord = new SettlementRecord();
                switch (accountUserType) {
                    case COMPANY:
                        final Account account = this.accountService.getById(statistics.getAccountId()).get();
                        accountSettleAuditRecord.setUserNo("");
                        accountSettleAuditRecord.setUserName(account.getUserName());
                        break;
                    case DEALER:
                        final Dealer dealer = dealerMap.get(statistics.getAccountId());
                        accountSettleAuditRecord.setUserNo(dealer.getMarkCode());
                        accountSettleAuditRecord.setUserName(dealer.getProxyName());
                        break;
                    default:
                        log.error("账户[{}]，生成结算审核记录时，出现未知流水", statistics.getAccountId());
                        break;
                }
                accountSettleAuditRecord.setAccountUserType(accountUserType.getId());
                accountSettleAuditRecord.setAccountId(statistics.getAccountId());
                accountSettleAuditRecord.setTradeDate(statistics.getTradeEndDate());
                accountSettleAuditRecord.setBalanceStartTime(statistics.getTradeStartDate());
                accountSettleAuditRecord.setBalanceEndTime(statistics.getTradeEndDate());
                accountSettleAuditRecord.setTradeNumber(statistics.getCount());
                accountSettleAuditRecord.setSettleAmount(statistics.getAmount());
                accountSettleAuditRecord.setAccountCheckStatus(EnumAccountCheckStatus.DUE_ACCOUNT_CHECK.getId());
                accountSettleAuditRecord.setSettleDate(settleDate);
                accountSettleAuditRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
                this.add(accountSettleAuditRecord);
                final int updateCount = this.settleAccountFlowService.updateSettleAuditRecordIdBySettleDateAndAccountId(settleDate,
                        accountSettleAuditRecord.getAccountId() , accountSettleAuditRecord.getId());
                Preconditions.checkState(updateCount == statistics.getCount(), "将待结算审核记录更新到结算流水，个数不一致");
                log.info("账户[{}],生成结算审核记录后，将其id[{}]保存到结算流水,更新记录数[{}]", statistics.getAccountId(),
                        accountSettleAuditRecord.getId(), updateCount);

                //生成结算单
                settlementRecord.setAccountUserType(accountUserType.getId());
                settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
                settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(settlementRecord.getAccountUserType(), settlementRecord.getSettleDestination()));
                settlementRecord.setSettleAuditRecordId(accountSettleAuditRecord.getId());
                settlementRecord.setAccountId(statistics.getAccountId());
                settlementRecord.setUserNo(accountSettleAuditRecord.getUserNo());
                settlementRecord.setUserName(accountSettleAuditRecord.getUserName());
                settlementRecord.setAppId(EnumAppType.HSY.getId());
                settlementRecord.setSettleDate(accountSettleAuditRecord.getSettleDate());
                settlementRecord.setTradeNumber(accountSettleAuditRecord.getTradeNumber());
                settlementRecord.setTradeAmount(new BigDecimal("0.00"));
                settlementRecord.setSettleAmount(accountSettleAuditRecord.getSettleAmount());
                settlementRecord.setSettlePoundage(new BigDecimal("0.00"));
                settlementRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
                settlementRecord.setSettleMode(EnumSettleModeType.CHANNEL_SETTLE.getId());
                settlementRecord.setStatus(EnumSettlementRecordStatus.WAIT_WITHDRAW.getId());
                settlementRecord.setBalanceStartTime(accountSettleAuditRecord.getBalanceStartTime());
                settlementRecord.setBalanceEndTime(accountSettleAuditRecord.getBalanceEndTime());
                final long settlementRecordId = this.settlementRecordService.add(settlementRecord);
                final int updateCount2 = this.settleAccountFlowService.updateSettlementRecordIdBySettleAuditRecordId(accountSettleAuditRecord.getId(), settlementRecordId);
                Preconditions.checkState(updateCount == updateCount2, "将结算单id更新到结算流水，个数异常");
            }
        }
        //this.generateSettlementAuditRecordSendMsg(merchantSettlementRecords);

        return Pair.of(0, "success");
    }

    private List<Long> handleWithdrawIngSettle() {
        final Date date = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd) + " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        List<Order> list =  this.orderService.selectWithdrawingOrderByBefore(date);
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        //生成结算挂起单, 提现异常， 当天订单全部挂起
        List<Long> accountIds = Lists.transform(list, new Function<Order, Long>() {
            @Override
            public Long apply(Order input) {
                return input.getPayer();
            }
        });
        for (Order withdrawOrder : list){
            final String withdrawDate = DateFormatUtil.format(withdrawOrder.getCreateTime(), DateFormatUtil.yyyy_MM_dd);
            final SettleExceptionRecord record = new SettleExceptionRecord();
            record.setSettleTargetNo(withdrawOrder.getMerchantNo());
            record.setWithdrawOrderId(withdrawOrder.getId());
            record.setSettleTargetName(withdrawOrder.getGoodsName());
            record.setSettleTargetType(EnumAccountUserType.MERCHANT.getId());
            record.setBeginTime(DateFormatUtil.parse(withdrawDate+" 00:00:00",DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
            record.setEndTime(DateFormatUtil.parse(withdrawDate+" 23:59:59",DateFormatUtil.yyyy_MM_dd_HH_mm_ss));
            record.setRemarks("挂起");
            this.settleExceptionRecordService.insert(record);
        }
        return accountIds;
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

    /**
     * {@inheritDoc}
     *
     * @param date 结算日期
     */
    @Override
    @Transactional
    public void handleSettleAuditRecordTask(final Date date) {
        Date settleDate = date;
        if (null == settleDate) {
            settleDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd) , DateFormatUtil.yyyy_MM_dd);
        }
        //查询结算单--结算
        final List<Long> pendingIds = this.accountSettleAuditRecordDao.selectPendingSettleAuditRecordIdsBySettleDateAndSettleStatus(settleDate, EnumSettleStatus.DUE_SETTLE.getId());
        log.info("今日[{}], 可以结算的结算审核记录[{}]", settleDate, pendingIds);
        for (int i= 0; i < pendingIds.size(); i++) {
            final long settleAuditRecordId = pendingIds.get(i);
            log.info("结算审核记录[{}],发消息进行结算", settleAuditRecordId);
            final JSONObject requestParam = new JSONObject();
            requestParam.put("recordId", settleAuditRecordId);
            MqProducer.produce(requestParam, MqConfig.NORMAL_SETTLE, 5000 * i);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param recordId
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> normalSettle(final long recordId) {
        final AccountSettleAuditRecord accountSettleAuditRecord = this.getByIdWithLock(recordId).get();
        if (!accountSettleAuditRecord.isDueSettle()) {
            return Pair.of(-1, "此记录不是待结算状态");
        }
        log.info("正常结算-结算审核记录[{]]", recordId);
        final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByAuditRecordId(recordId);
        if (!CollectionUtils.isEmpty(flows)) {
            final Pair<Integer, String> checkResult = this.checkFlowIsIncrease(flows);
            if (0 != checkResult.getLeft()) {
                return checkResult;
            }
            log.info("开始正常结算流水");
//            this.dealerAndCompanySettleImpl(flows);
            this.updateSettleStatus(recordId, EnumSettleStatus.SETTLED_ALL.getId());
            final SettlementRecord settlementRecord = this.settlementRecordService.getBySettleAuditRecordId(recordId).get();
            this.settlementRecordService.updateSettleStatus(settlementRecord.getId(), EnumSettleStatus.SETTLED_ALL.getId());
            return Pair.of(0, "success");
        }
        return Pair.of(-1, "非法操作");
    }


    /**
     * {@inheritDoc}
     *
     * @param recordId
     * @param checkedOrderNos
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> settleCheckedPart(final long recordId, final List<String> checkedOrderNos) {
        final AccountSettleAuditRecord accountSettleAuditRecord = this.getByIdWithLock(recordId).get();
        log.info("结算已经对账的结算流水[{}]", checkedOrderNos);
        if (accountSettleAuditRecord.isDueSettle()) {
            final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByAuditRecordId(recordId);
            if (!CollectionUtils.isEmpty(flows)) {
                final List<SettleAccountFlow> checkedFlows = new ArrayList<>();
                for (SettleAccountFlow settleAccountFlow : flows) {
                    if (checkedOrderNos.contains(settleAccountFlow.getOrderNo())) {
                        checkedFlows.add(settleAccountFlow);
                    }
                }
                final Pair<Integer, String> checkResult = this.checkFlowIsIncrease(checkedFlows);
                if (0 != checkResult.getLeft()) {
                    return checkResult;
                }
//                this.dealerAndCompanySettleImpl(flows);
                this.updateSettleStatus(recordId, EnumSettleStatus.SETTLE_PART.getId());
                final SettlementRecord settlementRecord = this.settlementRecordService.getBySettleAuditRecordId(recordId).get();
                this.settlementRecordService.updateSettleStatus(settlementRecord.getId(), EnumSettleStatus.SETTLE_PART.getId());
                return Pair.of(0, "success");
            }
        }
        return Pair.of(-1, "非法操作");
    }

    /**
     * {@inheritDoc}
     *
     * @param recordId
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> forceSettleAll(final long recordId) {
        final AccountSettleAuditRecord accountSettleAuditRecord = this.getById(recordId).get();
        log.info("强制结算-结算审核记录[{}]", recordId);
        if (accountSettleAuditRecord.isDueSettle()) {
            final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByAuditRecordId(recordId);
            if (!CollectionUtils.isEmpty(flows)) {
                log.info("开始强制结算流水");
                final Pair<Integer, String> checkResult = this.checkFlowIsIncrease(flows);
                if (0 != checkResult.getLeft()) {
                    return checkResult;
                }
//                this.dealerAndCompanySettleImpl(flows);
                this.updateSettleStatus(recordId, EnumSettleStatus.SETTLED_ALL.getId());
                final SettlementRecord settlementRecord = this.settlementRecordService.getBySettleAuditRecordId(recordId).get();
                this.settlementRecordService.updateSettleStatus(settlementRecord.getId(), EnumSettleStatus.SETTLED_ALL.getId());
                return Pair.of(0, "success");
            }
        }
        return Pair.of(-1, "非法操作");
    }

    /**
     * {@inheritDoc}
     *
     * @param recordIds
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> batchSettle(final List<Long> recordIds) {
        final List<AccountSettleAuditRecord> records = this.getByIds(recordIds);
        for (int i = 0; i < records.size(); i++) {
//            final AccountSettleAuditRecord record = records.get(i);
//            final JSONObject requestParam = new JSONObject();
//            requestParam.put("recordId", record.getId());
//            MqProducer.produce(requestParam, MqConfig.NORMAL_SETTLE, 500 * i);
        }
        return Pair.of(0, "success");
    }

    /**
     * 校验
     *
     * @param flows
     */
    private Pair<Integer, String> checkFlowIsIncrease(final List<SettleAccountFlow> flows) {
        for (SettleAccountFlow settleAccountFlow : flows) {
            if (EnumAccountFlowType.INCREASE.getId() != settleAccountFlow.getType()) {
                return Pair.of(-1, "校验待结算流水出现已经待结算的流水");
            }
        }
        return Pair.of(0, "");
    }


    /**
     * {@inheritDoc}
     *
     * @param recordId
     */
    @Override
    @Transactional
    public void settleImpl(final long recordId) {
        log.info("结算审核记录[{}], 开始结算", recordId);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final AccountSettleAuditRecord accountSettleAuditRecord = this.accountSettleAuditRecordDao.selectByIdWithLock(recordId);
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
        if (EnumAccountUserType.MERCHANT.getId() != settlementRecord.getAccountUserType()) {
            this.accountService.increaseAvailableAmount(account.getId(), accountSettleAuditRecord.getSettleAmount());
            this.accountFlowService.addAccountFlow(account.getId(), settlementRecord.getSettleNo(), accountSettleAuditRecord.getSettleAmount(),
                    "T1结算入款", EnumAccountFlowType.INCREASE);
        } else {
            this.accountService.decreaseTotalAmount(account.getId(), accountSettleAuditRecord.getSettleAmount());
            final int updateCount = this.orderService.markOrder2SettlementSuccess(settlementRecord.getId(), EnumSettleStatus.SETTLED_ALL.getId(), EnumSettleStatus.SETTLE_ING.getId());
            Preconditions.checkState(updateCount == settlementRecord.getTradeNumber(),
                    "对结算审核记录[{}]-更新交易结算状态，数目不一致[{}]--[{}]", recordId, settlementRecord.getTradeNumber(), updateCount);
        }
        this.updateSettleStatus(recordId, EnumSettleStatus.SETTLED_ALL.getId());
        this.settlementRecordService.updateSettleStatus(settlementRecord.getId(), EnumSettleStatus.SETTLED_ALL.getId());
        stopWatch.stop();
        log.info("结算审核记录[{}],结算结束，用时[{}]", recordId, stopWatch.getTime());
    }

    /**
     * {@inheritDoc}
     *
     * @param settleAuditRecordRequest
     * @return
     */
    @Override
    public PageModel<AccountSettleAuditRecord> listByParam(final ListSettleAuditRecordRequest settleAuditRecordRequest) {
        final PageModel<AccountSettleAuditRecord> result = new PageModel<>(settleAuditRecordRequest.getPageNo(), settleAuditRecordRequest.getPageSize());
        settleAuditRecordRequest.setOffset(result.getFirstIndex());
        settleAuditRecordRequest.setCount(result.getPageSize());
        final long count = this.accountSettleAuditRecordDao.selectCountByParam(settleAuditRecordRequest);
        final List<AccountSettleAuditRecord> records = this.accountSettleAuditRecordDao.selectByParam(settleAuditRecordRequest);
        result.setCount(count);
        result.setRecords(records);
        return result;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void synchronousDataTest() {
        final List<SettlementRecord> settlementRecords = this.settlementRecordService.getAll();
        log.info("同步数据个数[{}]", settlementRecords.size());
        for (SettlementRecord settlementRecord : settlementRecords) {
            log.info("同步结算单[{}]-开始", settlementRecord.getId());
            final List<SettleAccountFlow> settleAccountFlows = this.settleAccountFlowService.getBySettlementRecordId(settlementRecord.getId());
            final Set<String> orderNos = new HashSet<>();
            for (SettleAccountFlow settleAccountFlow : settleAccountFlows) {
                orderNos.add(settleAccountFlow.getOrderNo());
            }
            if (CollectionUtils.isEmpty(orderNos)) {
                log.error("!!!结算单[{}],对应的交易不存在", settlementRecord.getId());
                continue;
            }
            final List<Order> orders = this.orderService.getByOrderNos(new ArrayList<>(orderNos));
            if (orders.size() != orderNos.size()) {
                log.error("!!!结算单[{}],查询交易数量不一致[{}]--[{}]", settlementRecord.getId(), orderNos.size(), orders.size());
                continue;
            }
            BigDecimal tradeAmount = new BigDecimal("0.00");
            BigDecimal poundage = new BigDecimal("0.00");
            Date startDate = orders.get(0).getPaySuccessTime();
            Date endDate = orders.get(0).getPaySuccessTime();;
            for (Order order : orders) {
                if (startDate.after(order.getPaySuccessTime())) {
                    startDate = order.getPaySuccessTime();
                }
                if (endDate.before(order.getPaySuccessTime())) {
                    endDate = order.getPaySuccessTime();
                }
                tradeAmount = tradeAmount.add(order.getTradeAmount());
                poundage = poundage.add(order.getPoundage());
            }
            if (tradeAmount.compareTo(settlementRecord.getSettleAmount().add(poundage)) != 0) {
                log.error("!!!结算单[{}], 交易总额 ！= 结算总额 + 手续费", settlementRecord.getId());
            }
            settlementRecord.setBalanceStartTime(startDate);
            settlementRecord.setBalanceEndTime(endDate);
            settlementRecord.setTradeAmount(tradeAmount);
            settlementRecord.setSettlePoundage(poundage);
            settlementRecord.setUpperChannel(8);
            this.settlementRecordService.update(settlementRecord);

            final AccountSettleAuditRecord accountSettleAuditRecord = this.accountSettleAuditRecordDao.selectById(settlementRecord.getSettleAuditRecordId());
            accountSettleAuditRecord.setBalanceStartTime(startDate);
            accountSettleAuditRecord.setBalanceEndTime(endDate);
            accountSettleAuditRecord.setUpperChannel(8);
            this.accountSettleAuditRecordDao.update(accountSettleAuditRecord);

            final int count = this.orderService.updateSettlementRecordIdByOrderNos(new ArrayList<>(orderNos), settlementRecord.getId());
            if (count != settlementRecord.getTradeNumber()) {
                log.error("!!!结算单[{}],同步交易数量不一致[{}]--[{}]", settlementRecord.getId(), settlementRecord.getTradeNumber(), count);
            }
            log.info("同步结算单[{}]-结束", settlementRecord.getId());
        }
    }


}
