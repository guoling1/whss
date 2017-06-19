package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.hss.account.entity.*;
import com.jkm.hss.account.enums.*;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.RefundProfitParams;
import com.jkm.hss.bill.helper.SplitProfitParams;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.RefundOrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
    private RefundOrderService refundOrderService;
    @Autowired
    private BaseSettlementService baseSettlementService;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private SplitAccountRecordService splitAccountRecordService;
    @Autowired
    private SplitAccountRefundRecordService splitAccountRefundRecordService;

    /**
     * {@inheritDoc}
     *
     * @param splitProfitParams
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> exePaySplitProfit(final SplitProfitParams splitProfitParams) {
        log.info("交易单OR结算单[{}], 进行分润, 分润类型[{}]", splitProfitParams.getOrderNo(), splitProfitParams.getSplitType());
        final Order order = this.orderService.getByOrderNo(splitProfitParams.getOrderNo()).get();
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
        //通道利润--到结--(D0)到可用余额TODO
        if (null != basicChannelProfitDetail) {
            this.addSplitAccountRecord(basicChannelProfitDetail, order, splitProfitParams, "收单反润");
//            this.splitProfit4IncreaseAvailableAccount(basicChannelProfitDetail, order.getOrderNo(), "收单反润");
            this.splitProfit4IncreasePendingSettlementAccount(basicChannelProfitDetail, order, "收单反润");
        }
        //产品利润--到结--(D0)可用余额TODO
        if (null != productProfitDetail) {
            this.addSplitAccountRecord(productProfitDetail, order, splitProfitParams, "收单反润");
//            this.splitProfit4IncreaseAvailableAccount(productProfitDetail, order.getOrderNo(), "收单反润");
            this.splitProfit4IncreasePendingSettlementAccount(productProfitDetail, order, "收单反润");
        }
        //一级代理商利润--到结算--(D0)可用余额
        if (null != firstLevelDealerProfitDetail) {
            this.addSplitAccountRecord(firstLevelDealerProfitDetail, order, splitProfitParams, "收单反润");
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
            this.addSplitAccountRecord(secondLevelDealerProfitDetail, order, splitProfitParams, "收单反润");
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
            this.addSplitAccountRecord(directMerchantProfitDetail, order, splitProfitParams, "收单-直推");
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
            this.addSplitAccountRecord(indirectMerchantProfitDetail, order, splitProfitParams, "收单-间推");
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
     * @param splitProfitParams
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> exeWithdrawSplitProfit(final SplitProfitParams splitProfitParams) {
        log.info("交易单OR结算单[{}], 进行分润, 分润类型[{}]", splitProfitParams.getOrderNo(), splitProfitParams.getSplitType());
        final SettlementRecord settlementRecord = this.settlementRecordService.getBySettleNo(splitProfitParams.getOrderNo()).get();

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

        log.info("结算单[{}],分润总额[{}],成本[{}],通道[{}],产品[{}],直推商户[{}],间推商户[{}],一级代理商[{}],二级代理商[{}]",
                settlementRecord.getSettleNo(), settlementRecord.getSettlePoundage(), cost, channelProfit, productProfit, directMerchantProfit,
                indirectMerchantProfit, firstLevelDealerProfit, secondLevelDealerProfit);
        Preconditions.checkState(settlementRecord.getSettlePoundage().compareTo(cost.add(channelProfit).add(productProfit)
                        .add(directMerchantProfit).add(indirectMerchantProfit).add(firstLevelDealerProfit).add(secondLevelDealerProfit)) == 0,
                "分润的手续费总额不等于各方分润总和");
        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(settlementRecord.getSettlePoundage().compareTo(poundageAccount.getAvailable()) <= 0, "该笔交结算的分账手续费不可以大于手续费账户的可用余额");
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), settlementRecord.getSettlePoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), settlementRecord.getSettlePoundage());
        //可用余额流水减少
        final AccountFlow poundageAccountFlow = new AccountFlow();
        poundageAccountFlow.setAccountId(poundageAccount.getId());
        poundageAccountFlow.setOrderNo(settlementRecord.getSettleNo());
        poundageAccountFlow.setType(EnumAccountFlowType.DECREASE.getId());
        poundageAccountFlow.setOutAmount(settlementRecord.getSettlePoundage());
        poundageAccountFlow.setIncomeAmount(new BigDecimal("0.00"));
        poundageAccountFlow.setBeforeAmount(poundageAccount.getAvailable());
        poundageAccountFlow.setAfterAmount(poundageAccount.getAvailable().subtract(poundageAccountFlow.getOutAmount()));
        poundageAccountFlow.setChangeTime(new Date());
        poundageAccountFlow.setRemark("结算提现出账");
        this.accountFlowService.add(poundageAccountFlow);
        //通道利润--到可用余额
        if (null != basicChannelProfitDetail) {
            this.addSplitAccountRecordBySettlementRecord(basicChannelProfitDetail, settlementRecord, splitProfitParams, "提现反润");
            this.splitProfit4IncreaseAvailableAccount(basicChannelProfitDetail, settlementRecord.getSettleNo(), "提现反润");
        }
        //产品利润--可用余额
        if (null != productProfitDetail) {
            this.addSplitAccountRecordBySettlementRecord(productProfitDetail, settlementRecord, splitProfitParams, "提现反润");
            this.splitProfit4IncreaseAvailableAccount(productProfitDetail, settlementRecord.getSettleNo(), "提现反润");
        }
        //一级代理商利润--可用余额
        if (null != firstLevelDealerProfitDetail) {
            this.addSplitAccountRecordBySettlementRecord(firstLevelDealerProfitDetail, settlementRecord, splitProfitParams, "提现反润");
            this.splitProfit4IncreaseAvailableAccount(firstLevelDealerProfitDetail, settlementRecord.getSettleNo(), "提现反润");

        }
        //二级代理商利润--可用余额
        if (null != secondLevelDealerProfitDetail) {
            this.addSplitAccountRecordBySettlementRecord(secondLevelDealerProfitDetail, settlementRecord, splitProfitParams, "提现反润");
            this.splitProfit4IncreaseAvailableAccount(secondLevelDealerProfitDetail, settlementRecord.getSettleNo(), "提现反润");

        }
        //直推商户利润--可用余额
        if (null != directMerchantProfitDetail) {
            this.addSplitAccountRecordBySettlementRecord(directMerchantProfitDetail, settlementRecord, splitProfitParams, "提现-直推");
            this.splitProfit4IncreaseAvailableAccount(directMerchantProfitDetail, settlementRecord.getSettleNo(), "提现反润");

        }
        //间推商户利润--可用余额
        if (null != indirectMerchantProfitDetail) {
            this.addSplitAccountRecordBySettlementRecord(indirectMerchantProfitDetail, settlementRecord, splitProfitParams, "提现-间推");
            this.splitProfit4IncreaseAvailableAccount(indirectMerchantProfitDetail, settlementRecord.getSettleNo(), "提现反润");

        }
        return Pair.of(0, "success");
    }

    /**
     * {@inheritDoc}
     *
     * @param splitProfitParams
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> exeUpgradeSplitProfit(final SplitProfitParams splitProfitParams) {
        log.info("交易单[{}], 进行分润, 分润类型[{}]", splitProfitParams.getOrderNo(), splitProfitParams.getSplitType());
        final Order order = this.orderService.getByOrderNo(splitProfitParams.getOrderNo()).get();

        final SplitProfitParams.SplitProfitDetail companyProfitDetail = splitProfitParams.getCompanyProfitDetail();
        final SplitProfitParams.SplitProfitDetail directMerchantProfitDetail = splitProfitParams.getDirectMerchantProfitDetail();
        final SplitProfitParams.SplitProfitDetail indirectMerchantProfitDetail = splitProfitParams.getIndirectMerchantProfitDetail();
        final SplitProfitParams.SplitProfitDetail firstLevelDealerProfitDetail = splitProfitParams.getFirstLevelDealerProfitDetail();
        final SplitProfitParams.SplitProfitDetail secondLevelDealerProfitDetail = splitProfitParams.getSecondLevelDealerProfitDetail();

        final BigDecimal cost = null == splitProfitParams.getCost() ? new BigDecimal("0.00") : splitProfitParams.getCost();
        final BigDecimal companyProfit = null == companyProfitDetail ? new BigDecimal("0.00") : companyProfitDetail.getProfit();
        final BigDecimal directMerchantProfit = null == directMerchantProfitDetail ? new BigDecimal("0.00") : directMerchantProfitDetail.getProfit();
        final BigDecimal indirectMerchantProfit = null == indirectMerchantProfitDetail ? new BigDecimal("0.00") : indirectMerchantProfitDetail.getProfit();
        final BigDecimal firstLevelDealerProfit = null == firstLevelDealerProfitDetail ? new BigDecimal("0.00") : firstLevelDealerProfitDetail.getProfit();
        final BigDecimal secondLevelDealerProfit = null == secondLevelDealerProfitDetail ? new BigDecimal("0.00") : secondLevelDealerProfitDetail.getProfit();

        log.info("交易[{}],分润总额[{}],公司分润[{}],直推商户[{}],间推商户[{}],一级代理商[{}],二级代理商[{}]",
                order.getOrderNo(), order.getRealPayAmount(), cost, companyProfit, directMerchantProfit,
                indirectMerchantProfit, firstLevelDealerProfit, secondLevelDealerProfit);
        Preconditions.checkState(order.getRealPayAmount().compareTo(cost.add(companyProfit)
                        .add(directMerchantProfit).add(indirectMerchantProfit).add(firstLevelDealerProfit).add(secondLevelDealerProfit)) >= 0,
                "交易总额不大于等于各方分润总和");

        //产品利润--可用余额
        if (null != companyProfitDetail) {
            this.addSplitAccountRecord(companyProfitDetail, order, splitProfitParams, "商户升级-反润");
            this.splitProfit4IncreaseAvailableAccount(companyProfitDetail, order.getOrderNo(), "商户升级-反润");
        }
        //一级代理商利润--可用余额
        if (null != firstLevelDealerProfitDetail) {
            this.addSplitAccountRecord(firstLevelDealerProfitDetail, order, splitProfitParams, "商户升级-反润");
            this.splitProfit4IncreaseAvailableAccount(firstLevelDealerProfitDetail, order.getOrderNo(), "商户升级-反润");

        }
        //二级代理商利润--可用余额
        if (null != secondLevelDealerProfitDetail) {
            this.addSplitAccountRecord(secondLevelDealerProfitDetail, order, splitProfitParams, "商户升级-反润");
            this.splitProfit4IncreaseAvailableAccount(secondLevelDealerProfitDetail, order.getOrderNo(), "商户升级-反润");

        }
        //直推商户利润--可用余额
        if (null != directMerchantProfitDetail) {
            this.addSplitAccountRecord(directMerchantProfitDetail, order, splitProfitParams, "商户升级-直推");
            this.splitProfit4IncreaseAvailableAccount(directMerchantProfitDetail, order.getOrderNo(), "商户升级-直推");

        }
        //间推商户利润--可用余额
        if (null != indirectMerchantProfitDetail) {
            this.addSplitAccountRecord(indirectMerchantProfitDetail, order, splitProfitParams, "商户升级-间推");
            this.splitProfit4IncreaseAvailableAccount(indirectMerchantProfitDetail, order.getOrderNo(), "商户升级-间推");

        }
        return Pair.of(0, "success");
    }

    /**
     * {@inheritDoc}
     *
     * @param refundProfitParams
     * @param refundOrderId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Pair<Integer, String> exeRefundProfit(final RefundProfitParams refundProfitParams, final long refundOrderId) {
        if (EnumBoolean.TRUE.getCode() == refundProfitParams.getRefundAll()) {
            return this.exeRefundAllProfit(refundProfitParams, refundOrderId);
        } else {
            return this.exeRefundPartProfit(refundProfitParams, refundOrderId);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param refundProfitParams
     * @param refundOrderId
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> exeRefundAllProfit(RefundProfitParams refundProfitParams, long refundOrderId) {
        final RefundOrder refundOrder = this.refundOrderService.getByIdWithLock(refundOrderId).get();
        final Order order = this.orderService.getByIdWithLock(refundOrder.getPayOrderId()).get();
        this.refundAll2Poundage(refundOrder, order);
        this.refundAllMerchant(refundOrder, order);
        this.refundAllPoundage(refundOrder, order);
        return Pair.of(0, "success");
    }

    /**
     * {@inheritDoc}
     *
     * @param refundProfitParams
     * @param refundOrderId
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> exeRefundPartProfit(RefundProfitParams refundProfitParams, long refundOrderId) {
        log.info("部分退款还未实现");
        return Pair.of(-1, "部分退款还未实现");
    }

    /**
     * {@inheritDoc}
     *
     * @param splitProfitDetail
     * @param order
     * @param splitProfitParams
     * @param remark
     */
    @Override
    @Transactional
    public void addSplitAccountRecord(final SplitProfitParams.SplitProfitDetail splitProfitDetail,
                                      final Order order, final SplitProfitParams splitProfitParams, final String remark) {
        final SplitAccountRecord splitAccountRecord = new SplitAccountRecord();
        splitAccountRecord.setSettleType(splitProfitParams.getSettleType());
        splitAccountRecord.setBusinessType(splitProfitParams.getSplitType());
        splitAccountRecord.setOrderNo(order.getOrderNo());
        splitAccountRecord.setSplitOrderNo(order.getOrderNo());
        splitAccountRecord.setTotalAmount(order.getRealPayAmount());
        if (!EnumSplitBusinessType.HSSPROMOTE.getId().equalsIgnoreCase(splitProfitParams.getSplitType())) {
            splitAccountRecord.setOutMoneyAccountId(AccountConstants.POUNDAGE_ACCOUNT_ID);
            splitAccountRecord.setSplitTotalAmount(order.getPoundage());
        } else {
            splitAccountRecord.setSplitTotalAmount(order.getRealPayAmount());
        }
        splitAccountRecord.setReceiptMoneyAccountId(splitProfitDetail.getAccountId());
        splitAccountRecord.setReceiptMoneyUserName(splitProfitDetail.getUserName());
        splitAccountRecord.setSplitAmount(splitProfitDetail.getProfit());
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
     * @param settlementRecord
     * @param splitProfitParams
     * @param remark
     */
    @Override
    @Transactional
    public void addSplitAccountRecordBySettlementRecord(final SplitProfitParams.SplitProfitDetail splitProfitDetail,
                                                        final SettlementRecord settlementRecord, final SplitProfitParams splitProfitParams, final String remark) {
        final SplitAccountRecord splitAccountRecord = new SplitAccountRecord();
        splitAccountRecord.setSettleType(splitProfitParams.getSettleType());
        splitAccountRecord.setBusinessType(splitProfitParams.getSplitType());
        splitAccountRecord.setOrderNo(settlementRecord.getSettleNo());
        splitAccountRecord.setSplitOrderNo(settlementRecord.getSettleNo());
        splitAccountRecord.setTotalAmount(settlementRecord.getSettleAmount());
        splitAccountRecord.setOutMoneyAccountId(AccountConstants.POUNDAGE_ACCOUNT_ID);
        splitAccountRecord.setReceiptMoneyAccountId(splitProfitDetail.getAccountId());
        splitAccountRecord.setReceiptMoneyUserName(splitProfitDetail.getUserName());
        splitAccountRecord.setSplitAmount(splitProfitDetail.getProfit());
        splitAccountRecord.setSplitTotalAmount(settlementRecord.getSettlePoundage());
        splitAccountRecord.setSplitRate(splitProfitDetail.getRate());
        splitAccountRecord.setRemark(remark);
        splitAccountRecord.setSplitDate(new Date());
        splitAccountRecord.setAccountUserType(splitProfitDetail.getAccountUserType());
        if (EnumAccountUserType.DEALER.getId() == splitProfitDetail.getAccountUserType()) {
            if (2 == splitProfitDetail.getLevel()) {
                splitAccountRecord.setAccountUserType(EnumSplitAccountUserType.SECOND_DEALER.getId());
            } else if (1 == splitProfitDetail.getLevel()) {
                splitAccountRecord.setAccountUserType(EnumSplitAccountUserType.FIRST_DEALER.getId());
            }
        }
        this.splitAccountRecordService.add(splitAccountRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param splitProfitDetail
     * @param orderNo
     * @param remark
     */
    @Override
    @Transactional
    public void splitProfit4IncreaseAvailableAccount(final SplitProfitParams.SplitProfitDetail splitProfitDetail, final String orderNo,
                                            final String remark) {
        final Account account = this.accountService.getByIdWithLock(splitProfitDetail.getAccountId()).get();
        this.accountService.increaseTotalAmount(account.getId(), splitProfitDetail.getProfit());
        this.accountService.increaseAvailableAmount(account.getId(), splitProfitDetail.getProfit());
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(account.getId());
        accountFlow.setOrderNo(orderNo);
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
    }

    /**
     * 将利润全额退到手续费账户
     *
     * @param refundOrder
     * @param payOrder
     */
    private void refundAll2Poundage(final RefundOrder refundOrder, final Order payOrder) {
        final List<SplitAccountRecord> splitAccountRecords = this.splitAccountRecordService.getByOrderNo(payOrder.getOrderNo());
        if (!CollectionUtils.isEmpty(splitAccountRecords)) {
            for (SplitAccountRecord splitAccountRecord : splitAccountRecords) {
                final SplitAccountRefundRecord splitAccountRefundRecord = new SplitAccountRefundRecord();
                splitAccountRefundRecord.setPayOrderNo(payOrder.getOrderNo());
                splitAccountRefundRecord.setBatchNo(refundOrder.getBatchNo());
                splitAccountRefundRecord.setRefundOrderNo(refundOrder.getOrderNo());
                splitAccountRefundRecord.setAccountUserType(splitAccountRecord.getAccountUserType());
                splitAccountRefundRecord.setOriginalSplitSn(splitAccountRecord.getSplitSn());
                splitAccountRefundRecord.setRefundAmount(splitAccountRecord.getSplitAmount());
                splitAccountRefundRecord.setSplitAmount(splitAccountRecord.getSplitAmount());
                splitAccountRefundRecord.setSplitTotalAmount(splitAccountRecord.getSplitTotalAmount());
                splitAccountRefundRecord.setOutMoneyAccountId(splitAccountRecord.getReceiptMoneyAccountId());
                splitAccountRefundRecord.setOutMoneyUserName(splitAccountRecord.getReceiptMoneyUserName());
                splitAccountRefundRecord.setReceiptMoneyAccountId(splitAccountRecord.getOutMoneyAccountId());
                splitAccountRefundRecord.setRefundTime(new Date());
                splitAccountRefundRecord.setRemark("收单分润退款");
                this.splitAccountRefundRecordService.add(splitAccountRefundRecord);
            }
        }
        final List<SettleAccountFlow> settleAccountFlows = this.settleAccountFlowService.getByOrderNo(payOrder.getOrderNo());
        if (!CollectionUtils.isEmpty(settleAccountFlows)) {
            for (SettleAccountFlow settleAccountFlow: settleAccountFlows) {
                if (settleAccountFlow.getAccountId() != payOrder.getPayee()) {
                    Preconditions.checkState(EnumAccountFlowType.INCREASE.getId() == settleAccountFlow.getType(),
                            "订单[{}], 全额退款，出现已结算的待结算流水", payOrder.getId());
                    final Account account = this.accountService.getByIdWithLock(settleAccountFlow.getAccountId()).get();
                    Preconditions.checkState(account.getDueSettleAmount().compareTo(settleAccountFlow.getIncomeAmount()) >= 0, "账户[{}]余额不足，退分润失败", account.getId());
                    this.baseSettlementService.pendingSettleAccountFlowOutAccount(settleAccountFlow, account, "收单分润退款");
                }
            }
        }
        final Account account = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        this.accountService.increaseTotalAmount(account.getId(), refundOrder.getPoundageRefundAmount());
        this.accountService.increaseAvailableAmount(account.getId(), refundOrder.getPoundageRefundAmount());
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(account.getId());
        accountFlow.setOrderNo(payOrder.getOrderNo());
        accountFlow.setRefundOrderNo(refundOrder.getOrderNo());
        accountFlow.setIncomeAmount(refundOrder.getPoundageRefundAmount());
        accountFlow.setOutAmount(new BigDecimal("0.00"));
        accountFlow.setBeforeAmount(account.getAvailable());
        accountFlow.setAfterAmount(account.getAvailable().add(accountFlow.getIncomeAmount()));
        accountFlow.setChangeTime(new Date());
        accountFlow.setType(EnumAccountFlowType.INCREASE.getId());
        accountFlow.setRemark("收单分润退款");
        this.accountFlowService.add(accountFlow);
    }

    /**
     * 全额退商户款
     *
     * @param refundOrder
     * @param payOrder
     */
    private void refundAllMerchant(final RefundOrder refundOrder, final Order payOrder) {
        final Optional<SettleAccountFlow> decreaseSettleAccountFlowOptional = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(payOrder.getOrderNo(),
                payOrder.getPayee(), EnumAccountFlowType.DECREASE.getId());
        if (decreaseSettleAccountFlowOptional.isPresent()) {
            log.error("商户账户[{}],待结算[{}]流水，已结算", payOrder.getPayee(), decreaseSettleAccountFlowOptional.get().getId());
            return;
        }
        final Account account = this.accountService.getByIdWithLock(payOrder.getPayee()).get();
        Preconditions.checkState(account.getDueSettleAmount().compareTo(refundOrder.getMerchantRefundAmount()) >= 0,
                "退款单[{}]，商户账户[{}]待结算金额不足", refundOrder.getOrderNo(), account.getId());
        this.accountService.decreaseTotalAmount(account.getId(), refundOrder.getMerchantRefundAmount());
        this.accountService.decreaseSettleAmount(account.getId(), refundOrder.getMerchantRefundAmount());
        final SettleAccountFlow increaseSettleAccountFlow = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(payOrder.getOrderNo(),
                account.getId(), EnumAccountFlowType.INCREASE.getId()).get();
        final SettleAccountFlow decreaseSettleAccountFlow = new SettleAccountFlow();
        decreaseSettleAccountFlow.setFlowNo("");
        decreaseSettleAccountFlow.setAccountId(increaseSettleAccountFlow.getAccountId());
        decreaseSettleAccountFlow.setAccountUserType(increaseSettleAccountFlow.getAccountUserType());
        decreaseSettleAccountFlow.setOrderNo(payOrder.getOrderNo());
        decreaseSettleAccountFlow.setRefundOrderNo(refundOrder.getOrderNo());
        decreaseSettleAccountFlow.setBeforeAmount(account.getDueSettleAmount());
        decreaseSettleAccountFlow.setAfterAmount(account.getDueSettleAmount().subtract(increaseSettleAccountFlow.getIncomeAmount()));
        decreaseSettleAccountFlow.setOutAmount(increaseSettleAccountFlow.getIncomeAmount());
        decreaseSettleAccountFlow.setIncomeAmount(new BigDecimal("0.00"));
        decreaseSettleAccountFlow.setAppId(increaseSettleAccountFlow.getAppId());
        decreaseSettleAccountFlow.setTradeDate(increaseSettleAccountFlow.getTradeDate());
        decreaseSettleAccountFlow.setSettleDate(increaseSettleAccountFlow.getSettleDate());
        decreaseSettleAccountFlow.setChangeTime(new Date());
        decreaseSettleAccountFlow.setType(EnumAccountFlowType.DECREASE.getId());
        decreaseSettleAccountFlow.setRemark("支付退款");
        this.settleAccountFlowService.add(decreaseSettleAccountFlow);
        this.settleAccountFlowService.updateStatus(increaseSettleAccountFlow.getId(), EnumBoolean.TRUE.getCode());
    }

    /**
     * 退全部手续费
     *
     * @param refundOrder
     * @param payOrder
     */
    private void refundAllPoundage(final RefundOrder refundOrder, final Order payOrder) {
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(poundageAccount.getAvailable().compareTo(refundOrder.getPoundageRefundAmount()) >= 0);
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), refundOrder.getPoundageRefundAmount());
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), refundOrder.getPoundageRefundAmount());
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(poundageAccount.getId());
        accountFlow.setOrderNo(payOrder.getOrderNo());
        accountFlow.setRefundOrderNo(refundOrder.getOrderNo());
        accountFlow.setOutAmount(refundOrder.getPoundageRefundAmount());
        accountFlow.setIncomeAmount(new BigDecimal("0.00"));
        accountFlow.setBeforeAmount(poundageAccount.getAvailable());
        accountFlow.setAfterAmount(poundageAccount.getAvailable().subtract(accountFlow.getOutAmount()));
        accountFlow.setChangeTime(new Date());
        accountFlow.setType(EnumAccountFlowType.DECREASE.getId());
        accountFlow.setRemark("收单分润退款");
        this.accountFlowService.add(accountFlow);
    }

}
