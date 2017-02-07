package com.jkm.hss.settle.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.settle.dao.AccountSettleAuditRecordDao;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import com.jkm.hss.settle.enums.EnumAccountCheckStatus;
import com.jkm.hss.settle.enums.EnumSettleStatus;
import com.jkm.hss.settle.helper.requestparam.ListSettleAuditRecordRequest;
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
                jo.put("settleAmount", record.getSettleAmount());
            }
        } else {
            pageModel.setRecords(Collections.<JSONObject>emptyList());
        }
        return JSON.toJSONString(pageModel);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void handleT1SettleTask() {
        final List<Date> tradeDateList = new ArrayList<>();
        final DateTime now = DateTime.now();
        tradeDateList.add(DateFormatUtil.parse(DateFormatUtil.format(now.minusDays(1).toDate(), DateFormatUtil.yyyy_MM_dd) , DateFormatUtil.yyyy_MM_dd));
        //商户昨日待结算记录
        final List<SettleAccountFlow> settleAccountFlows = this.settleAccountFlowService.getMerchantLastWordDayRecord(tradeDateList);
        if (!CollectionUtils.isEmpty(settleAccountFlows)) {
            final HashSet<Long> accountIds = new HashSet<>();
            for (SettleAccountFlow settleAccountFlow : settleAccountFlows) {
                accountIds.add(settleAccountFlow.getAccountId());
                if (settleAccountFlow.getSettleAuditRecordId() > 0) {
                    log.error("###############结算流水[{}], 已经存在结算审核记录#################", settleAccountFlow.getId());
                    return;
                }
            }
            final List<AppBizShop> shopList = this.hsyShopDao.findAppBizShopByAccountIDList(new ArrayList<>(accountIds));
            //accountId--shop(主)
            final Map<Long, AppBizShop> shopMap = Maps.uniqueIndex(shopList, new Function<AppBizShop, Long>() {
                @Override
                public Long apply(AppBizShop input) {
                    return input.getAccountID();
                }
            });
            //accountId--List<SettleAccountFlow>
            final Map<Long, List<SettleAccountFlow>> accountIdFlowMap = this.getAccountIdFlowMap(accountIds, settleAccountFlows);
            if (!accountIdFlowMap.isEmpty()) {
                final Set<Long> keySet = accountIdFlowMap.keySet();
                final Iterator<Long> keyIterator = keySet.iterator();
                while (keyIterator.hasNext()) {
                    final Long key = keyIterator.next();
                    final List<SettleAccountFlow> flows = accountIdFlowMap.get(key);
                    this.generateAuditRecord(key, flows, shopMap);
                }
            }
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
        log.info("商户[{}]， 结算交易", accountSettleAuditRecord.getMerchantNo());
        if (accountSettleAuditRecord.isSuccessAccountCheck() && accountSettleAuditRecord.isDueSettle()) {
            final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByAuditRecordId(recordId);
            if (!CollectionUtils.isEmpty(flows)) {
                final Pair<Integer, String> checkResult = this.checkFlowIsIncrease(flows);
                if (0 != checkResult.getLeft()) {
                    return checkResult;
                }
                this.updateSettleStatus(recordId, EnumSettleStatus.SETTLE_ING.getId());
                this.merchantSettle(flows, accountSettleAuditRecord.getMerchantNo(), accountSettleAuditRecord.getAccountId());
                this.updateSettleStatus(recordId, EnumSettleStatus.SETTLED_ALL.getId());
            }
            return Pair.of(0, "结算处理成功");
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
        log.info("商户[{}]， 结算已经对账的交易[{}]", accountSettleAuditRecord.getMerchantNo(), checkedOrderNos);
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
                this.updateSettleStatus(recordId, EnumSettleStatus.SETTLE_ING.getId());
                this.merchantSettle(checkedFlows, accountSettleAuditRecord.getMerchantNo(), accountSettleAuditRecord.getAccountId());
                this.updateSettleStatus(recordId, EnumSettleStatus.SETTLE_PART.getId());
                return Pair.of(0, "结算处理成功");
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
        log.info("商户[{}]， 强制结算所有交易[{}]", accountSettleAuditRecord.getMerchantNo());
        if (accountSettleAuditRecord.isDueSettle()) {
            final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByAuditRecordId(recordId);
            if (!CollectionUtils.isEmpty(flows)) {
                final Pair<Integer, String> checkResult = this.checkFlowIsIncrease(flows);
                if (0 == checkResult.getLeft()) {
                    this.updateSettleStatus(recordId, EnumSettleStatus.SETTLE_ING.getId());
                    this.merchantSettle(flows, accountSettleAuditRecord.getMerchantNo(), accountSettleAuditRecord.getAccountId());
                    this.updateSettleStatus(recordId, EnumSettleStatus.SETTLED_ALL.getId());
                    return Pair.of(0, "强制结算处理成功");
                }
                return checkResult;
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
        for (AccountSettleAuditRecord record : records) {
            final JSONObject requestParam = new JSONObject();
            requestParam.put("recordId", record.getId());
            MqProducer.produce(requestParam, MqConfig.NORMAL_SETTLE, 0);
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
                return Pair.of(-1, "校验待结算流水出现不是待结算的流水");
            }
        }
        return Pair.of(0, "");
    }


    /**
     * 商户结算
     *
     * @param flows
     * @param merchantNo
     * @param accountId
     */
    @Override
    @Transactional
    public void merchantSettle(final List<SettleAccountFlow> flows, final String merchantNo, final long accountId) {
        if (!CollectionUtils.isEmpty(flows)) {
            final List<String> orderNos = new ArrayList<>();
            for (SettleAccountFlow settleAccountFlow : flows) {
                if (EnumAccountUserType.MERCHANT.getId() == settleAccountFlow.getAccountUserType()) {
                    orderNos.add(settleAccountFlow.getOrderNo());
                }
            }
            log.info("商户[{}]，结算订单列表[{}]", merchantNo, orderNos);
            for (String orderNo : orderNos) {
                log.info("商户[{}]， 结算订单[{}]", merchantNo, orderNo);
                final SettleAccountFlow merchantSettleAccountFlow = this.getMerchantSettleAccountFlow(flows, orderNo);
                Preconditions.checkState(null != merchantSettleAccountFlow, "商户[{}],结算流水[{}]未找到,结算异常", merchantNo, orderNo);
                this.merchantSettleImpl(merchantSettleAccountFlow, orderNo, merchantNo, accountId);
            }

            for (int i = 0; i < orderNos.size(); i++) {
                //发消息更新交易的结算状态
                final String orderNo = orderNos.get(i);
                final JSONObject requestUpdateParam = new JSONObject();
                requestUpdateParam.put("orderNo", orderNo);
                MqProducer.produce(requestUpdateParam, MqConfig.UPDATE_ORDER_SETTLE_STATUS, 0);

                //发消息，手续费结算
                final JSONObject requestParam = new JSONObject();
                requestParam.put("orderNo", orderNo);
                MqProducer.produce(requestParam, MqConfig.POUNDAGE_SETTLE, i * 200);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param flow
     * @param orderNo
     * @param merchantNo
     * @param accountId
     */
    @Override
    @Transactional
    public void merchantSettleImpl(final SettleAccountFlow flow, final String orderNo, final String merchantNo, final long accountId) {
        final Optional<SettleAccountFlow> optional = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(orderNo,
                accountId, EnumAccountFlowType.DECREASE.getId());
        Preconditions.checkState(!optional.isPresent(), "商户[{}],结算流水[{}],已结算,结算异常", merchantNo, orderNo);

        final Account merchantAccount = this.accountService.getByIdWithLock(accountId).get();
        final SettleAccountFlow merchantIncreaseSettleAccountFlow =
                this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(orderNo, accountId,
                        EnumAccountFlowType.INCREASE.getId()).get();
        //待结算金额减少
        Preconditions.checkState(merchantAccount.getDueSettleAmount().compareTo(merchantIncreaseSettleAccountFlow.getIncomeAmount()) >= 0, "商户的待结算总金额金额不可以单笔订单的金额");
        this.accountService.increaseAvailableAmount(merchantAccount.getId(), merchantIncreaseSettleAccountFlow.getIncomeAmount());
        this.accountService.decreaseSettleAmount(merchantAccount.getId(), merchantIncreaseSettleAccountFlow.getIncomeAmount());
        this.settleAccountFlowService.addSettleAccountFlow(merchantAccount.getId(), orderNo, merchantIncreaseSettleAccountFlow.getIncomeAmount(),
                "支付结算", EnumAccountFlowType.DECREASE, merchantIncreaseSettleAccountFlow.getAppId(), merchantIncreaseSettleAccountFlow.getTradeDate(),
                merchantIncreaseSettleAccountFlow.getAccountUserType());
        //可用余额流水增加
        this.accountFlowService.addAccountFlow(merchantAccount.getId(), orderNo, merchantIncreaseSettleAccountFlow.getIncomeAmount(),
                "支付结算", EnumAccountFlowType.INCREASE);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> poundageSettle(final String orderNo) {
        final List<SettleAccountFlow> flows = this.settleAccountFlowService.getDealerOrCompanyFlowByOrderNo(orderNo);
        if (!CollectionUtils.isEmpty(flows)) {
            final Pair<Integer, String> checkResult = this.checkFlowIsIncrease(flows);
            Preconditions.checkState(0 == checkResult.getLeft(), "订单号[{}], 结算手续费时，校验出现不是待结算的流水", orderNo);
            for (SettleAccountFlow settleAccountFlow : flows) {
                final Account account = this.accountService.getByIdWithLock(settleAccountFlow.getAccountId()).get();
                //待结算--可用余额
                Preconditions.checkState(account.getDueSettleAmount().compareTo(settleAccountFlow.getIncomeAmount()) >= 0,
                        "订单[{}], 账户[{}]的待结算总金额金额不可以单笔订单的金额", orderNo, account.getId());
                this.accountService.increaseAvailableAmount(account.getId(), settleAccountFlow.getIncomeAmount());
                this.accountService.decreaseSettleAmount(account.getId(), settleAccountFlow.getIncomeAmount());
                this.settleAccountFlowService.addSettleAccountFlow(account.getId(), orderNo, settleAccountFlow.getIncomeAmount(),
                        "分润", EnumAccountFlowType.DECREASE, settleAccountFlow.getAppId(), settleAccountFlow.getTradeDate(),
                        settleAccountFlow.getAccountUserType());
                this.accountFlowService.addAccountFlow(account.getId(), orderNo, settleAccountFlow.getIncomeAmount(),
                        "分润", EnumAccountFlowType.INCREASE);
            }
        }
        return Pair.of(0, "success");
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

    private SettleAccountFlow getMerchantSettleAccountFlow(final List<SettleAccountFlow> flows, final String orderNo) {
        for (SettleAccountFlow settleAccountFlow : flows) {
            if (EnumAccountUserType.MERCHANT.getId() == settleAccountFlow.getAccountUserType()
                    && orderNo.equals(settleAccountFlow.getOrderNo())) {
                return settleAccountFlow;
            }
        }
        return null;
    }


    /**
     * 生成结算审核记录
     */
    private void generateAuditRecord(final long accountId, final List<SettleAccountFlow> flows,
                                     final Map<Long, AppBizShop> shopMap) {
        final AccountSettleAuditRecord accountSettleAuditRecord = new AccountSettleAuditRecord();
        final AppBizShop shop = shopMap.get(accountId);
        final AppAuUser appAuUser = this.hsyShopDao.findCorporateUserByShopID(shop.getId()).get(0);
        final Optional<Dealer> dealerOptional = this.dealerService.getById(appAuUser.getDealerID());
        accountSettleAuditRecord.setMerchantNo(shop.getGlobalID());
        accountSettleAuditRecord.setAccountId(shop.getAccountID());
        accountSettleAuditRecord.setMerchantName(shop.getName());
        if (dealerOptional.isPresent()) {
            final Dealer dealer = dealerOptional.get();
            accountSettleAuditRecord.setDealerNo(dealer.getMarkCode());
            accountSettleAuditRecord.setDealerName(dealer.getProxyName());
        }
        accountSettleAuditRecord.setAppId(flows.get(0).getAppId());
        accountSettleAuditRecord.setTradeDate(flows.get(0).getTradeDate());
        accountSettleAuditRecord.setTradeNumber(flows.size());
        BigDecimal totalAmount = new BigDecimal("0.00");
        final List<String> orderNos = new ArrayList<>();
        for (SettleAccountFlow settleAccountFlow : flows) {
            totalAmount = totalAmount.add(settleAccountFlow.getIncomeAmount());
            orderNos.add(settleAccountFlow.getOrderNo());
        }
        accountSettleAuditRecord.setAccountCheckStatus(EnumAccountCheckStatus.DUE_ACCOUNT_CHECK.getId());
        accountSettleAuditRecord.setSettleDate(new Date());
        accountSettleAuditRecord.setSettleAmount(totalAmount);
        accountSettleAuditRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        this.add(accountSettleAuditRecord);
        final int updateCount = this.settleAccountFlowService.updateSettleAuditRecordIdByOrderNos(orderNos, accountSettleAuditRecord.getId());
        log.info("店铺[{}], 账户[{}],生成结算审核记录后，将其id[{}]保存到结算流水,更新记录数[{}]", shop.getId(), accountId,
                accountSettleAuditRecord.getId(), updateCount);
    }


    /**
     * 获得accountId--List<SettleAccountFlow>的map
     *
     * @param accountIds
     * @param settleAccountFlows
     * @return
     */
    private Map<Long, List<SettleAccountFlow>> getAccountIdFlowMap(final HashSet<Long> accountIds,
                                                                   final List<SettleAccountFlow> settleAccountFlows) {
        final List<SettleAccountFlow> settleAccountFlowList = new ArrayList<>(settleAccountFlows);
        final Map<Long, List<SettleAccountFlow>> accountIdFlowMap = new HashMap<>(accountIds.size());
        for (long accountId : accountIds) {
            final List<SettleAccountFlow> flows = new ArrayList<>();
            accountIdFlowMap.put(accountId, flows);
            final Iterator<SettleAccountFlow> iterator = settleAccountFlowList.iterator();
            while (iterator.hasNext()) {
                final SettleAccountFlow settleAccountFlow = iterator.next();
                if (accountId == settleAccountFlow.getAccountId()) {
                    flows.add(settleAccountFlow);
                    iterator.remove();
                }
            }
        }
        return accountIdFlowMap;
    }


    /**
     * 获取指定日期的记录
     *
     * @param tradeDate
     * @param flows
     * @return
     */
    private List<SettleAccountFlow> getFlows(final Date tradeDate, final List<SettleAccountFlow> flows) {
        final List<SettleAccountFlow> result = new ArrayList<>();
        for (SettleAccountFlow settleAccountFlow : flows) {
            if (new DateTime(tradeDate).getDayOfYear() == new DateTime(settleAccountFlow.getTradeDate()).getDayOfYear()) {
                result.add(settleAccountFlow);
            }
        }
        return result;
    }
}
