package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.AccountFlow;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.entity.SplitAccountRecord;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.enums.EnumSplitAccountUserType;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.account.sevice.SplitAccountRecordService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.enums.EnumSettleDestinationType;
import com.jkm.hss.bill.enums.EnumSettleStatus;
import com.jkm.hss.bill.enums.EnumTradeType;
import com.jkm.hss.bill.helper.SplitProfitParams;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/12.
 *
 * 分润实现
 */
@Slf4j
@Service
public class BaseSplitProfitServiceImpl implements BaseSplitProfitService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private SplitAccountRecordService splitAccountRecordService;

    /**
     * {@inheritDoc}
     *
     * @param splitProfitParams
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> exePaySplitAccount(final SplitProfitParams splitProfitParams) {
        log.info("交易单[{}], 进行分润, 分润类型[{}]", splitProfitParams.getOrderNo(), splitProfitParams.getSplitType());
        final Optional<Order> orderOptional = this.orderService.getByOrderNo(splitProfitParams.getOrderNo());
        if (!orderOptional.isPresent()) {
            return Pair.of(-1, "交易不存在");
        }
        final Order order = orderOptional.get();
        final SplitProfitParams.SplitProfitDetail basicChannelProfitDetail = splitProfitParams.getBasicChannelProfitDetail();
        final SplitProfitParams.SplitProfitDetail productProfitDetail = splitProfitParams.getProductProfitDetail();
        final SplitProfitParams.SplitProfitDetail directMerchantProfitDetail = splitProfitParams.getDirectMerchantProfitDetail();
        final SplitProfitParams.SplitProfitDetail indirectMerchantProfitDetail = splitProfitParams.getIndirectMerchantProfitDetail();
        final SplitProfitParams.SplitProfitDetail firstLevelDealerProfitDetail = splitProfitParams.getFirstLevelDealerProfitDetail();
        final SplitProfitParams.SplitProfitDetail secondLevelDealerProfitDetail = splitProfitParams.getSecondLevelDealerProfitDetail();

        final BigDecimal cost = splitProfitParams.getCost();
        final BigDecimal channelProfit = null == basicChannelProfitDetail ? new BigDecimal("0.00") : basicChannelProfitDetail.getProfit();
        final BigDecimal productProfit = null == productProfitDetail ? new BigDecimal("0.00") : productProfitDetail.getProfit();
        final BigDecimal directMerchantProfit = null == directMerchantProfitDetail ? new BigDecimal("0.00") : directMerchantProfitDetail.getProfit();
        final BigDecimal indirectMerchantProfit = null == indirectMerchantProfitDetail ? new BigDecimal("0.00") : indirectMerchantProfitDetail.getProfit();
        final BigDecimal firstLevelDealerProfit = null == firstLevelDealerProfitDetail ? new BigDecimal("0.00") : firstLevelDealerProfitDetail.getProfit();
        final BigDecimal secondLevelDealerProfit = null == secondLevelDealerProfitDetail ? new BigDecimal("0.00") : secondLevelDealerProfitDetail.getProfit();

        log.info("交易[{}],分润总额[{}],成本[{}],通道[{}],产品[{}],直推商户[{}],间推商户[{}],一级代理商[{}],二级代理商[{}]",
                order.getOrderNo(), order.getPoundage(), cost, channelProfit, productProfit, directMerchantProfit,
                indirectMerchantProfit, firstLevelDealerProfit, secondLevelDealerProfit);
        Preconditions.checkState(order.getPoundage().compareTo(cost.add(channelProfit).add(productProfit)
                .add(directMerchantProfit).add(indirectMerchantProfit).add(firstLevelDealerProfit).add(secondLevelDealerProfit)) == 0,
                "分润的手续费总额不等于各方分润总和");

        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(order.getPoundage().compareTo(poundageAccount.getAvailable()) <= 0, "该笔交易的分账手续费不可以大于手续费账户的可用余额");
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), order.getPoundage());
        //可用余额流水减少
        final AccountFlow poundageAccountFlow = new AccountFlow();
        poundageAccountFlow.setAccountId(poundageAccount.getId());
        poundageAccountFlow.setOrderNo(order.getOrderNo());
        poundageAccountFlow.setType(EnumAccountFlowType.DECREASE.getId());
        poundageAccountFlow.setOutAmount(order.getPoundage());
        poundageAccountFlow.setIncomeAmount(new BigDecimal("0.00"));
        poundageAccountFlow.setBeforeAmount(poundageAccount.getAvailable());
        poundageAccountFlow.setAfterAmount(poundageAccount.getAvailable().subtract(poundageAccountFlow.getOutAmount()));
        poundageAccountFlow.setChangeTime(new Date());
        poundageAccountFlow.setRemark(EnumTradeType.of(order.getTradeType()).getValue());
        this.accountFlowService.add(poundageAccountFlow);
        //通道利润--到可用余额
        if (null != basicChannelProfitDetail) {
            this.addSplitAccountRecord(basicChannelProfitDetail, order, splitProfitParams.getSplitType(), "收单反润");
            this.splitProfit4IncreaseAvailableAccount(basicChannelProfitDetail, order, "收单反润");
        }
        //产品利润--可用余额
        if (null != productProfitDetail) {
            this.addSplitAccountRecord(productProfitDetail, order, splitProfitParams.getSplitType(), "收单反润");
            this.splitProfit4IncreaseAvailableAccount(productProfitDetail, order, "收单反润");
        }
        //一级代理商利润--到结算--(D0)可用余额
        if (null != firstLevelDealerProfitDetail) {
            this.addSplitAccountRecord(firstLevelDealerProfitDetail, order, splitProfitParams.getSplitType(), "收单反润");
            final long settleAccountFlowIncreaseId = this.splitProfit4IncreasePendingSettlementAccount(firstLevelDealerProfitDetail, order, "收单反润");
            if (EnumAppType.HSS.getId().equals(order.getAppId()) && EnumBalanceTimeType.D0.getType().equals(order.getSettleType())) {
                final SettlementRecord settlementRecord = new SettlementRecord();
                settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(firstLevelDealerProfitDetail.getAccountUserType(),
                        EnumSettleDestinationType.TO_ACCOUNT.getId()));
                settlementRecord.setAccountId(firstLevelDealerProfitDetail.getAccountId());
                settlementRecord.setUserNo(firstLevelDealerProfitDetail.getUserNo());
                settlementRecord.setUserName(firstLevelDealerProfitDetail.getUserName());
                settlementRecord.setAccountUserType(firstLevelDealerProfitDetail.getAccountUserType());
                settlementRecord.setAppId(order.getAppId());
                settlementRecord.setSettleDate(order.getSettleTime());
                settlementRecord.setTradeNumber(1);
                settlementRecord.setSettleAmount(firstLevelDealerProfitDetail.getProfit());
                settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
                settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
                this.settlementRecordService.add(settlementRecord);
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecord.getId());
                this.splitProfit4DecreasePendingSettlementAccount(firstLevelDealerProfitDetail, order, "收单反润", settlementRecord.getId());
                this.settleAccountFlowService.updateStatus(settleAccountFlowIncreaseId, EnumBoolean.TRUE.getCode());
            }
        }
        //二级代理商利润--到结算--(D0)可用余额
        if (null != secondLevelDealerProfitDetail) {
            this.addSplitAccountRecord(secondLevelDealerProfitDetail, order, splitProfitParams.getSplitType(), "收单反润");
            final long settleAccountFlowIncreaseId = this.splitProfit4IncreasePendingSettlementAccount(secondLevelDealerProfitDetail, order, "收单反润");
            if (EnumAppType.HSS.getId().equals(order.getAppId()) && EnumBalanceTimeType.D0.getType().equals(order.getSettleType())) {
                final SettlementRecord settlementRecord = new SettlementRecord();
                settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(secondLevelDealerProfitDetail.getAccountUserType(),
                        EnumSettleDestinationType.TO_ACCOUNT.getId()));
                settlementRecord.setAccountId(secondLevelDealerProfitDetail.getAccountId());
                settlementRecord.setUserNo(secondLevelDealerProfitDetail.getUserNo());
                settlementRecord.setUserName(secondLevelDealerProfitDetail.getUserName());
                settlementRecord.setAccountUserType(secondLevelDealerProfitDetail.getAccountUserType());
                settlementRecord.setAppId(order.getAppId());
                settlementRecord.setSettleDate(order.getSettleTime());
                settlementRecord.setTradeNumber(1);
                settlementRecord.setSettleAmount(secondLevelDealerProfitDetail.getProfit());
                settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
                settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
                this.settlementRecordService.add(settlementRecord);
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecord.getId());
                this.splitProfit4DecreasePendingSettlementAccount(secondLevelDealerProfitDetail, order, "收单反润", settlementRecord.getId());
                this.settleAccountFlowService.updateStatus(settleAccountFlowIncreaseId, EnumBoolean.TRUE.getCode());
            }
        }
        //直推商户利润--到结算--(D0)可用余额
        if (null != directMerchantProfitDetail) {
            this.addSplitAccountRecord(directMerchantProfitDetail, order, splitProfitParams.getSplitType(), "收单-直推");
            final long settleAccountFlowIncreaseId = this.splitProfit4IncreasePendingSettlementAccount(directMerchantProfitDetail, order, "收单-直推");
            if (EnumAppType.HSS.getId().equals(order.getAppId()) && EnumBalanceTimeType.D0.getType().equals(order.getSettleType())) {
                final SettlementRecord settlementRecord = new SettlementRecord();
                settlementRecord.setAccountId(directMerchantProfitDetail.getAccountId());
                settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(directMerchantProfitDetail.getAccountUserType(),
                        EnumSettleDestinationType.TO_ACCOUNT.getId()));
                settlementRecord.setUserNo(directMerchantProfitDetail.getUserNo());
                settlementRecord.setUserName(directMerchantProfitDetail.getUserName());
                settlementRecord.setAccountUserType(directMerchantProfitDetail.getAccountUserType());
                settlementRecord.setAppId(order.getAppId());
                settlementRecord.setSettleDate(order.getSettleTime());
                settlementRecord.setTradeNumber(1);
                settlementRecord.setSettleAmount(directMerchantProfitDetail.getProfit());
                settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
                settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
                this.settlementRecordService.add(settlementRecord);
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecord.getId());
                this.splitProfit4DecreasePendingSettlementAccount(directMerchantProfitDetail, order, "收单-直推", settlementRecord.getId());
                this.settleAccountFlowService.updateStatus(settleAccountFlowIncreaseId, EnumBoolean.TRUE.getCode());
            }
        }
        //间推商户利润--到结算--(D0)可用余额
        if (null != indirectMerchantProfitDetail) {
            this.addSplitAccountRecord(indirectMerchantProfitDetail, order, splitProfitParams.getSplitType(), "收单-间推");
            final long settleAccountFlowIncreaseId = this.splitProfit4IncreasePendingSettlementAccount(indirectMerchantProfitDetail, order, "收单-间推");
            if (EnumAppType.HSS.getId().equals(order.getAppId()) && EnumBalanceTimeType.D0.getType().equals(order.getSettleType())) {
                final SettlementRecord settlementRecord = new SettlementRecord();
                settlementRecord.setAccountId(indirectMerchantProfitDetail.getAccountId());
                settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(indirectMerchantProfitDetail.getAccountUserType(),
                        EnumSettleDestinationType.TO_ACCOUNT.getId()));
                settlementRecord.setUserNo(indirectMerchantProfitDetail.getUserNo());
                settlementRecord.setUserName(indirectMerchantProfitDetail.getUserName());
                settlementRecord.setAccountUserType(indirectMerchantProfitDetail.getAccountUserType());
                settlementRecord.setAppId(order.getAppId());
                settlementRecord.setSettleDate(order.getSettleTime());
                settlementRecord.setTradeNumber(1);
                settlementRecord.setSettleAmount(indirectMerchantProfitDetail.getProfit());
                settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
                settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
                this.settlementRecordService.add(settlementRecord);
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecord.getId());
                this.splitProfit4DecreasePendingSettlementAccount(indirectMerchantProfitDetail, order, "收单-间推", settlementRecord.getId());
                this.settleAccountFlowService.updateStatus(settleAccountFlowIncreaseId, EnumBoolean.TRUE.getCode());
            }
        }
        return Pair.of(0, "success");
    }

    /**
     * {@inheritDoc}
     *
     * @param splitProfitDetail
     * @param order
     * @param businessType
     * @param remark
     */
    @Override
    @Transactional
    public void addSplitAccountRecord(final SplitProfitParams.SplitProfitDetail splitProfitDetail,
                                      final Order order, final String businessType, final String remark) {
        final SplitAccountRecord splitAccountRecord = new SplitAccountRecord();
        splitAccountRecord.setSettleType(order.getSettleType());
        splitAccountRecord.setBusinessType(businessType);
        splitAccountRecord.setOrderNo(order.getOrderNo());
        splitAccountRecord.setSplitOrderNo(order.getOrderNo());
        splitAccountRecord.setTotalAmount(order.getTradeAmount());
        splitAccountRecord.setOutMoneyAccountId(AccountConstants.POUNDAGE_ACCOUNT_ID);
        splitAccountRecord.setReceiptMoneyAccountId(splitProfitDetail.getAccountId());
        splitAccountRecord.setReceiptMoneyUserName(splitProfitDetail.getUserName());
        splitAccountRecord.setSplitAmount(splitProfitDetail.getProfit());
        splitAccountRecord.setSplitTotalAmount(order.getPoundage());
        splitAccountRecord.setSplitRate(splitProfitDetail.getRate());
        splitAccountRecord.setRemark(remark);
        splitAccountRecord.setSplitDate(new Date());
        splitAccountRecord.setAccountUserType(splitProfitDetail.getAccountUserType());
        if (EnumAccountUserType.DEALER.getId() == splitProfitDetail.getAccountUserType()) {
            if (1 == splitProfitDetail.getLevel()) {
                splitAccountRecord.setAccountUserType(EnumSplitAccountUserType.FIRST_DEALER.getId());
            } else if (2 == splitProfitDetail.getLevel()) {
                splitAccountRecord.setAccountUserType(EnumSplitAccountUserType.SECOND_DEALER.getId());
            }
        }
        this.splitAccountRecordService.add(splitAccountRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param splitProfitDetail
     * @param order
     * @param remark
     */
    @Override
    @Transactional
    public void splitProfit4IncreaseAvailableAccount(final SplitProfitParams.SplitProfitDetail splitProfitDetail, final Order order,
                                            final String remark) {
        final Account account = this.accountService.getByIdWithLock(splitProfitDetail.getAccountId()).get();
        this.accountService.increaseTotalAmount(account.getId(), splitProfitDetail.getProfit());
        this.accountService.increaseAvailableAmount(account.getId(), splitProfitDetail.getProfit());
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(account.getId());
        accountFlow.setOrderNo(order.getOrderNo());
        accountFlow.setType(EnumAccountFlowType.INCREASE.getId());
        accountFlow.setOutAmount(new BigDecimal("0.00"));
        accountFlow.setIncomeAmount(splitProfitDetail.getProfit());
        accountFlow.setBeforeAmount(account.getAvailable());
        accountFlow.setAfterAmount(account.getAvailable().add(accountFlow.getIncomeAmount()));
        accountFlow.setChangeTime(new Date());
        accountFlow.setRemark(remark);
        this.accountFlowService.add(accountFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param splitProfitDetail
     * @param order
     * @param remark
     */
    @Override
    @Transactional
    public long splitProfit4IncreasePendingSettlementAccount(final SplitProfitParams.SplitProfitDetail splitProfitDetail,
                                                             final Order order, final String remark) {
        final Account account = this.accountService.getByIdWithLock(splitProfitDetail.getAccountId()).get();
        this.accountService.increaseTotalAmount(account.getId(), splitProfitDetail.getProfit());
        this.accountService.increaseSettleAmount(account.getId(), splitProfitDetail.getProfit());
        final SettleAccountFlow increaseSettleAccountFlow = new SettleAccountFlow();
        increaseSettleAccountFlow.setAccountId(account.getId());
        increaseSettleAccountFlow.setOrderNo(order.getOrderNo());
        increaseSettleAccountFlow.setType(EnumAccountFlowType.INCREASE.getId());
        increaseSettleAccountFlow.setOutAmount(new BigDecimal("0.00"));
        increaseSettleAccountFlow.setIncomeAmount(splitProfitDetail.getProfit());
        increaseSettleAccountFlow.setBeforeAmount(account.getDueSettleAmount());
        increaseSettleAccountFlow.setAfterAmount(account.getDueSettleAmount().add(increaseSettleAccountFlow.getIncomeAmount()));
        increaseSettleAccountFlow.setChangeTime(new Date());
        increaseSettleAccountFlow.setRemark(remark);
        increaseSettleAccountFlow.setAppId(order.getAppId());
        increaseSettleAccountFlow.setTradeDate(order.getPaySuccessTime());
        increaseSettleAccountFlow.setSettleDate(order.getSettleTime());
        increaseSettleAccountFlow.setAccountUserType(splitProfitDetail.getAccountUserType());
        this.settleAccountFlowService.add(increaseSettleAccountFlow);
        return increaseSettleAccountFlow.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param splitProfitDetail
     * @param order
     * @param remark
     * @param settlementRecordId
     */
    @Override
    @Transactional
    public void splitProfit4DecreasePendingSettlementAccount(final SplitProfitParams.SplitProfitDetail splitProfitDetail,
                                                             final Order order, final String remark, final long settlementRecordId) {
        final Account account = this.accountService.getByIdWithLock(splitProfitDetail.getAccountId()).get();
        this.accountService.increaseAvailableAmount(account.getId(), splitProfitDetail.getProfit());
        this.accountService.decreaseSettleAmount(account.getId(), splitProfitDetail.getProfit());
        final SettleAccountFlow decreaseSettleAccountFlow = new SettleAccountFlow();
        decreaseSettleAccountFlow.setAccountId(account.getId());
        decreaseSettleAccountFlow.setOrderNo(order.getOrderNo());
        decreaseSettleAccountFlow.setType(EnumAccountFlowType.DECREASE.getId());
        decreaseSettleAccountFlow.setOutAmount(splitProfitDetail.getProfit());
        decreaseSettleAccountFlow.setIncomeAmount(new BigDecimal("0.00"));
        decreaseSettleAccountFlow.setBeforeAmount(account.getDueSettleAmount());
        decreaseSettleAccountFlow.setAfterAmount(account.getDueSettleAmount().subtract(decreaseSettleAccountFlow.getOutAmount()));
        decreaseSettleAccountFlow.setChangeTime(new Date());
        decreaseSettleAccountFlow.setRemark(remark);
        decreaseSettleAccountFlow.setAppId(order.getAppId());
        decreaseSettleAccountFlow.setTradeDate(order.getPaySuccessTime());
        decreaseSettleAccountFlow.setSettleDate(order.getSettleTime());
        decreaseSettleAccountFlow.setAccountUserType(splitProfitDetail.getAccountUserType());
        decreaseSettleAccountFlow.setSettlementRecordId(settlementRecordId);
        this.settleAccountFlowService.add(decreaseSettleAccountFlow);

        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(account.getId());
        accountFlow.setOrderNo(order.getOrderNo());
        accountFlow.setType(EnumAccountFlowType.INCREASE.getId());
        accountFlow.setOutAmount(new BigDecimal("0.00"));
        accountFlow.setIncomeAmount(splitProfitDetail.getProfit());
        accountFlow.setBeforeAmount(account.getAvailable());
        accountFlow.setAfterAmount(account.getAvailable().add(accountFlow.getIncomeAmount()));
        accountFlow.setChangeTime(new Date());
        accountFlow.setRemark(remark);
        this.accountFlowService.add(accountFlow);
//        this.settleAccountFlowService.updateSettlementRecordIdById(decreaseSettleAccountFlow.getId(), settlementRecordId);
    }
}
