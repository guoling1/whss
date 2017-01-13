package com.jkm.hss.settle.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.settle.dao.AccountSettleAuditRecordDao;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import com.jkm.hss.settle.enums.EnumSettleOptionType;
import com.jkm.hss.settle.enums.EnumSettleStatus;
import com.jkm.hss.settle.service.AccountSettleAuditRecordService;
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
@Service
public class AccountSettleAuditRecordServiceImpl implements AccountSettleAuditRecordService {

    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private DealerService dealerService;
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
     */
    @Override
    @Transactional
    public void handleT1SettleTask() {
        final List<Date> tradeDateList = new ArrayList<>();
        final DateTime now = DateTime.now();
        tradeDateList.add(now.minusDays(1).toDate());
        Preconditions.checkState(!(6 == now.dayOfWeek().get() || 7 == now.dayOfWeek().get()), "T1结算定时任务，在非法的日期启动");
        if (1 == now.dayOfWeek().get()) {
            tradeDateList.add(now.minusDays(2).toDate());
            tradeDateList.add(now.minusDays(3).toDate());
        }
        //商户昨日待结算记录
        final List<SettleAccountFlow> settleAccountFlows = this.settleAccountFlowService.getMerchantLastWordDayRecord(tradeDateList);
        if (!CollectionUtils.isEmpty(settleAccountFlows)) {
            final HashSet<Long> accountIds = new HashSet<>();
            for (SettleAccountFlow settleAccountFlow : settleAccountFlows) {
                accountIds.add(settleAccountFlow.getAccountId());
            }
            //TODO
            final List<MerchantInfo> merchants = this.merchantInfoService.batchGetByAccountIds(new ArrayList<>(accountIds));
            //accountId--merchant
            final Map<Long, MerchantInfo> merchantMap = Maps.uniqueIndex(merchants, new Function<MerchantInfo, Long>() {
                @Override
                public Long apply(MerchantInfo input) {
                    return input.getAccountId();
                }
            });
            final List<Long> dealerIds = Lists.transform(merchants, new Function<MerchantInfo, Long>() {
                @Override
                public Long apply(MerchantInfo input) {
                    return input.getDealerId();
                }
            });
            final List<Dealer> dealers = this.dealerService.getByIds(dealerIds);
            //dealerId--dealer
            final Map<Long, Dealer> dealerMap = Maps.uniqueIndex(dealers, new Function<Dealer, Long>() {
                @Override
                public Long apply(Dealer input) {
                    return input.getId();
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
                    //周一
                    if (1 == now.dayOfWeek().get()) {
                        for (Date date : tradeDateList) {
                            final List<SettleAccountFlow> flows1 = this.getFlows(date, flows);
                            this.generateAuditRecord(key, flows1, merchantMap, dealerMap);
                        }
                    } else {//
                        this.generateAuditRecord(key, flows, merchantMap, dealerMap);
                    }
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param recordId
     * @param option {@link com.jkm.hss.settle.enums.EnumSettleOptionType}
     * @return
     */
    @Override
    public Pair<Integer, String> settle(long recordId, int option) {
        final Optional<AccountSettleAuditRecord> recordOptional = this.getById(recordId);
        if (!recordOptional.isPresent()) {
            return Pair.of(-1, "结算审核记录不存在");
        }
        final AccountSettleAuditRecord accountSettleAuditRecord = recordOptional.get();
        if (accountSettleAuditRecord.isDueAccountCheck()) {
            return Pair.of(-1, "未对账");
        }
        if (accountSettleAuditRecord.isSideException() &&
                !(EnumSettleOptionType.SETTLE_ALL.getId() == option || EnumSettleOptionType.SETTLE_ACCOUNT_CHECKED.getId() == option)) {
            return Pair.of(-1, "对账结果有单边");
        }
        if (accountSettleAuditRecord.isSuccessAccountCheck() && EnumSettleOptionType.SETTLE_NORMAL.getId() != option) {
            return Pair.of(-1, "参数错误");
        }


        return null;
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
        final AccountSettleAuditRecord accountSettleAuditRecord = this.getById(recordId).get();
//        this.settleAccountFlowService.getBy

        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param recordId
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> settleCheckedPart(final long recordId) {
        final AccountSettleAuditRecord accountSettleAuditRecord = this.getById(recordId).get();

        return null;
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

        return null;
    }


    /**
     * 生成结算审核记录
     */
    private void generateAuditRecord(final long accountId, final List<SettleAccountFlow> flows,
                                     final Map<Long, MerchantInfo> merchantMap, final Map<Long, Dealer> dealerMap) {
        final AccountSettleAuditRecord accountSettleAuditRecord = new AccountSettleAuditRecord();
        final MerchantInfo merchant = merchantMap.get(accountId);
        accountSettleAuditRecord.setMerchantNo(merchant.getMarkCode());
        accountSettleAuditRecord.setMerchantName(merchant.getMerchantName());
        final Dealer dealer = dealerMap.get(merchant.getDealerId());
        if (null != dealer) {
//            accountSettleAuditRecord.setDealerNo(dealer.get);
            accountSettleAuditRecord.setDealerName(dealer.getProxyName());
        }
        accountSettleAuditRecord.setAppId(flows.get(0).getAppId());
        accountSettleAuditRecord.setTradeDate(flows.get(0).getTradeDate());
        accountSettleAuditRecord.setTradeNumber(flows.size());
        final BigDecimal totalAmount = new BigDecimal("0.00");
        final List<String> orderNos = new ArrayList<>();
        for (SettleAccountFlow settleAccountFlow : flows) {
            totalAmount.add(settleAccountFlow.getIncomeAmount());
            orderNos.add(settleAccountFlow.getOrderNo());
        }
        accountSettleAuditRecord.setSettleAmount(totalAmount);
        accountSettleAuditRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        this.add(accountSettleAuditRecord);
        final int updateCount = this.settleAccountFlowService.updateSettleAuditRecordIdByOrderNos(orderNos, accountSettleAuditRecord.getId());
        log.info("商户[{}], 账户[{}],生成结算审核记录后，将其id[{}]保存到结算流水,更新记录数[{}]", merchant.getId(), accountId,
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
