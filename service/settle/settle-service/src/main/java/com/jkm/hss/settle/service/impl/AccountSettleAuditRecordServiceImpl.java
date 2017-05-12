package com.jkm.hss.settle.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.base.common.util.SnGenerator;
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
import com.jkm.hss.bill.enums.EnumSettleDestinationType;
import com.jkm.hss.bill.enums.EnumSettleModeType;
import com.jkm.hss.bill.enums.EnumSettlementRecordStatus;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.settle.dao.AccountSettleAuditRecordDao;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import com.jkm.hss.settle.enums.EnumAccountCheckStatus;
import com.jkm.hss.settle.enums.EnumSettleStatus;
import com.jkm.hss.settle.helper.requestparam.ListSettleAuditRecordRequest;
import com.jkm.hss.settle.helper.responseparam.AppSettleRecordDetailResponse;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
        final long count = this.accountSettleAuditRecordDao.selectCountByAccountId(accountId);
        final List<AccountSettleAuditRecord> records = this.accountSettleAuditRecordDao.selectByAccountId(accountId, pageModel.getFirstIndex(), pageSize);
        pageModel.setCount(count);
        if (!CollectionUtils.isEmpty(records)) {
            final List<JSONObject> recordList = new ArrayList<>();
            pageModel.setRecords(recordList);
            for (AccountSettleAuditRecord record : records) {
                final JSONObject jo = new JSONObject();
                recordList.add(jo);
                jo.put("recordId", record.getId());
                jo.put("settleDate", record.getSettleDate());
                jo.put("number", record.getTradeNumber());
                jo.put("settleAmount", record.getSettleAmount().toPlainString());
            }
        } else {
            pageModel.setRecords(Collections.<JSONObject>emptyList());
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
        final AccountSettleAuditRecord accountSettleAuditRecord = this.getById(recordId).get();
        final AppSettleRecordDetailResponse appSettleRecordDetailResponse = new AppSettleRecordDetailResponse();
        appSettleRecordDetailResponse.setSettleAmount(accountSettleAuditRecord.getSettleAmount().toPlainString());
        appSettleRecordDetailResponse.setNumber(accountSettleAuditRecord.getTradeNumber());
        appSettleRecordDetailResponse.setRecordId(recordId);
        appSettleRecordDetailResponse.setSettleDate(accountSettleAuditRecord.getSettleDate());
        final List<String> orderNos = this.settleAccountFlowService.getOrderNoByAuditRecordId(recordId);
        final Map<String, BigDecimal> tradeAmountMap = this.orderService.getTradeAmountAndFeeByOrderNoList(orderNos);
        appSettleRecordDetailResponse.setTradeAmount(tradeAmountMap.get("tradeAmount").toPlainString());
        appSettleRecordDetailResponse.setFeeAmount(tradeAmountMap.get("poundage").toPlainString());
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
        final List<String> orderNos = this.settleAccountFlowService.getOrderNoByAuditRecordId(recordId);
        final PageModel<JSONObject> pageModel = new PageModel<>(pageNo, pageSize);
        final List<Order> orders = this.orderService.getOrderByOrderNos(orderNos, pageModel.getFirstIndex(), pageSize);
        pageModel.setCount(orderNos.size());
        final List<JSONObject> jsonObjects = Lists.transform(orders, new Function<Order, JSONObject>() {
            @Override
            public JSONObject apply(Order order) {
                final JSONObject jo = new JSONObject();
                jo.put("tradeAmount", order.getTradeAmount().toPlainString());
                jo.put("tradeDate", order.getCreateTime());
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
     */
    @Override
    @Transactional
    synchronized public Pair<Integer, String> generateHsySettleAuditRecordTask() {
        final Date settleDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd) , DateFormatUtil.yyyy_MM_dd);
        final int count = this.accountSettleAuditRecordDao.selectCountBySettleDate(settleDate);
        if (count > 0) {
            log.error("###############今日记录已经生成，不可以重复生成#################");
            return Pair.of(-1, "今日记录已经生成，不可以重复生成");
        }
        final List<SettleAccountFlowStatistics> settleAccountFlowStatisticses = this.settleAccountFlowService.statisticsYesterdayFlow(settleDate);
        log.info("今日[{}]的待结算流水-生成结算审核记录,个数[{}]", settleDate, settleAccountFlowStatisticses.size());
        final ArrayList<Long> dealerAccountIds = new ArrayList<>();
        final ArrayList<Long> shopAccountIds = new ArrayList<>();
        if (!CollectionUtils.isEmpty(settleAccountFlowStatisticses)) {
            for (SettleAccountFlowStatistics statistics : settleAccountFlowStatisticses) {
                if (EnumAccountUserType.DEALER.getId() == statistics.getAccountUserType()) {
                    dealerAccountIds.add(statistics.getAccountId());
                }
                if (EnumAccountUserType.MERCHANT.getId() == statistics.getAccountUserType()) {
                    shopAccountIds.add(statistics.getAccountId());
                }
            }
            final List<AppBizShop> shopList = this.hsyShopDao.findAppBizShopByAccountIDList(shopAccountIds);
            Preconditions.checkState(shopAccountIds.size() == shopList.size(), "通过账户查询店铺出现异常，数量不一致");
            //accountId--shop(主)
            final Map<Long, AppBizShop> shopMap = Maps.uniqueIndex(shopList, new Function<AppBizShop, Long>() {
                @Override
                public Long apply(AppBizShop input) {
                    return input.getAccountID();
                }
            });
            final List<Dealer> dealers = this.dealerService.getByAccountIds(dealerAccountIds);
            final Map<Long, Dealer> dealerMap = Maps.uniqueIndex(dealers, new Function<Dealer, Long>() {
                @Override
                public Long apply(Dealer input) {
                    return input.getAccountId();
                }
            });
            for (SettleAccountFlowStatistics statistics : settleAccountFlowStatisticses) {
                final EnumAccountUserType accountUserType = EnumAccountUserType.of(statistics.getAccountUserType());
                final AccountSettleAuditRecord accountSettleAuditRecord = new AccountSettleAuditRecord();
                final SettlementRecord settlementRecord = new SettlementRecord();
                switch (accountUserType) {
                    case COMPANY:
                        final Account account = this.accountService.getById(statistics.getAccountId()).get();
                        accountSettleAuditRecord.setUserNo("");
                        accountSettleAuditRecord.setUserName(account.getUserName());
                        settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
                        break;
                    case DEALER:
                        final Dealer dealer = dealerMap.get(statistics.getAccountId());
                        accountSettleAuditRecord.setUserNo(dealer.getMarkCode());
                        accountSettleAuditRecord.setUserName(dealer.getProxyName());
                        settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
                        break;
                    case MERCHANT:
                        final AppBizShop shop = shopMap.get(statistics.getAccountId());
                        accountSettleAuditRecord.setUserNo(shop.getGlobalID());
                        accountSettleAuditRecord.setUserName(shop.getShortName());
                        settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_CARD.getId());
                        break;
                    default:
                        log.error("账户[{}]，生成结算审核记录时，出现未知流水", statistics.getAccountId());
                        break;
                }
                accountSettleAuditRecord.setAccountUserType(accountUserType.getId());
                accountSettleAuditRecord.setAccountId(statistics.getAccountId());
                accountSettleAuditRecord.setTradeDate(statistics.getTradeDate());
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
                settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(accountUserType.getId(), EnumSettleDestinationType.TO_ACCOUNT.getId()));
                settlementRecord.setSettleAuditRecordId(accountSettleAuditRecord.getId());
                settlementRecord.setAccountId(statistics.getAccountId());
                settlementRecord.setUserNo(accountSettleAuditRecord.getUserNo());
                settlementRecord.setUserName(accountSettleAuditRecord.getUserName());
                settlementRecord.setAccountUserType(accountUserType.getId());
                settlementRecord.setAppId(EnumAppType.HSY.getId());
                settlementRecord.setSettleDate(accountSettleAuditRecord.getSettleDate());
                settlementRecord.setTradeNumber(accountSettleAuditRecord.getTradeNumber());
                settlementRecord.setSettleAmount(accountSettleAuditRecord.getSettleAmount());
                settlementRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
                settlementRecord.setSettleMode(EnumSettleModeType.CHANNEL_SETTLE.getId());
                settlementRecord.setStatus(EnumSettlementRecordStatus.WAIT_WITHDRAW.getId());
                final long settlementRecordId = this.settlementRecordService.add(settlementRecord);
                final int updateCount2 = this.settleAccountFlowService.updateSettlementRecordIdBySettleAuditRecordId(accountSettleAuditRecord.getId(), settlementRecordId);
                Preconditions.checkState(updateCount == updateCount2, "将结算单id更新到结算流水，个数异常");
            }
        }
        return Pair.of(0, "success");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void handleSettleAuditRecordTask() {
        final Date settleDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd) , DateFormatUtil.yyyy_MM_dd);
        //查询结算单--结算
        final List<Long> pendingIds = this.accountSettleAuditRecordDao.selectPendingSettleAuditRecordIdsBySettleDateAndSettleStatus(settleDate, EnumSettleStatus.DUE_SETTLE.getId());
        log.info("今日[{}], 可以结算的结算审核记录[{}]", settleDate, pendingIds);
        for (int i= 0; i < pendingIds.size(); i++) {
            final long settleAuditRecordId = pendingIds.get(i);
            log.info("结算审核记录[{}],发消息进行结算", settleAuditRecordId);
            final JSONObject requestParam = new JSONObject();
            requestParam.put("recordId", settleAuditRecordId);
            MqProducer.produce(requestParam, MqConfig.NORMAL_SETTLE, 1000 * i);
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
        final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByAuditRecordId(recordId);
        if (!CollectionUtils.isEmpty(flows)) {
            final Pair<Integer, String> checkResult = this.checkFlowIsIncrease(flows);
            Preconditions.checkState(0 == checkResult.getLeft(), checkResult.getRight());
            for (SettleAccountFlow settleAccountFlow : flows) {
                final Optional<SettleAccountFlow> optional = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(settleAccountFlow.getOrderNo(),
                        settleAccountFlow.getAccountId(), EnumAccountFlowType.DECREASE.getId());
                Preconditions.checkState(!optional.isPresent(), "账户[{}],结算流水[{}],已结算,结算异常", settleAccountFlow.getAccountId(), settleAccountFlow.getFlowNo());
                final Account account = this.accountService.getByIdWithLock(settleAccountFlow.getAccountId()).get();
                final SettleAccountFlow increaseSettleAccountFlow =
                        this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(settleAccountFlow.getOrderNo(), settleAccountFlow.getAccountId(),
                                EnumAccountFlowType.INCREASE.getId()).get();
                //待结算金额减少
                Preconditions.checkState(account.getDueSettleAmount().compareTo(increaseSettleAccountFlow.getIncomeAmount()) >= 0, "账户的待结算总金额不可以小于单笔结算流水的待结算金额");
                this.accountService.decreaseSettleAmount(account.getId(), increaseSettleAccountFlow.getIncomeAmount());
                final long settleAccountFlowDecreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), settleAccountFlow.getOrderNo(), increaseSettleAccountFlow.getIncomeAmount(),
                        increaseSettleAccountFlow.getRemark(), EnumAccountFlowType.DECREASE, increaseSettleAccountFlow.getAppId(), increaseSettleAccountFlow.getTradeDate(),
                        increaseSettleAccountFlow.getSettleDate(), increaseSettleAccountFlow.getAccountUserType());
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowDecreaseId, increaseSettleAccountFlow.getSettlementRecordId());
                this.settleAccountFlowService.updateSettleAuditRecordIdById(settleAccountFlowDecreaseId, increaseSettleAccountFlow.getSettleAuditRecordId());
                this.settleAccountFlowService.updateStatus(increaseSettleAccountFlow.getId(), EnumBoolean.TRUE.getCode());
                //可用余额流水增加
                if (EnumAccountUserType.MERCHANT.getId() != settleAccountFlow.getAccountUserType()) {
                    this.accountService.increaseAvailableAmount(account.getId(), increaseSettleAccountFlow.getIncomeAmount());
                    this.accountFlowService.addAccountFlow(account.getId(), settleAccountFlow.getOrderNo(), increaseSettleAccountFlow.getIncomeAmount(),
                            "支付结算", EnumAccountFlowType.INCREASE);
                } else {
                    this.accountService.decreaseTotalAmount(account.getId(), increaseSettleAccountFlow.getIncomeAmount());
                    final Optional<Order> orderOptional = this.orderService.getByOrderNo(settleAccountFlow.getOrderNo());
                    Preconditions.checkState(orderOptional.isPresent(), "结算成功，更新交易结算状态， 没有查询到交易记录[{}]", settleAccountFlow.getOrderNo());
                    if (this.orderService.getByIdWithLock(orderOptional.get().getId()).get().isDueSettle()) {
                        this.orderService.updateSettleStatus(orderOptional.get().getId(), EnumSettleStatus.SETTLED_ALL.getId());
                    }
                }
            }
            this.updateSettleStatus(recordId, EnumSettleStatus.SETTLED_ALL.getId());
            final SettlementRecord settlementRecord = this.settlementRecordService.getBySettleAuditRecordId(recordId).get();
            this.settlementRecordService.updateSettleStatus(settlementRecord.getId(), EnumSettleStatus.SETTLED_ALL.getId());
        }
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
}
