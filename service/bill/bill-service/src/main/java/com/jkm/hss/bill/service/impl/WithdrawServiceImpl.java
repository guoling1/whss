package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.entity.UnFrozenRecord;
import com.jkm.hss.account.enums.*;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.PaymentSdkDaiFuRequest;
import com.jkm.hss.bill.entity.PaymentSdkDaiFuResponse;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.*;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.SendMsgService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/25.
 */
@Slf4j
@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private ShallProfitDetailService shallProfitDetailService;
    @Autowired
    private SplitAccountRecordService splitAccountRecordService;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private SendMsgService sendMsgService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private MerchantWithdrawService merchantWithdrawService;
    @Autowired
    private AccountBankService accountBankService;

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param settlementRecordId 交易单（支付单）id
     * @param payOrderSn 支付中心支付流水号
     * @param payChannelSign 结算周期
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> merchantWithdrawBySettlementRecord(final long merchantId, final long settlementRecordId, final String payOrderSn, final int payChannelSign) {
        log.info("商户[{}]，对结算单[{}], 进行提现", merchantId, settlementRecordId);
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        final AccountBank accountBank = this.accountBankService.getDefault(merchant.getAccountId());
        final SettlementRecord settlementRecord = this.settlementRecordService.getById(settlementRecordId).get();
        if (settlementRecord.isWaitWithdraw()) {
            final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSS, merchantId, payChannelSign);
            final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
            paymentSdkDaiFuRequest.setAppId(settlementRecord.getAppId());
            paymentSdkDaiFuRequest.setOrderNo(settlementRecord.getSettleNo());
            paymentSdkDaiFuRequest.setTotalAmount(settlementRecord.getSettleAmount().subtract(merchantWithdrawPoundage).toPlainString());
            paymentSdkDaiFuRequest.setTradeType(EnumBalanceTimeType.D0.getType());
            paymentSdkDaiFuRequest.setIsCompany("0");
            paymentSdkDaiFuRequest.setMobile(accountBank.getReserveMobile());
            paymentSdkDaiFuRequest.setBankName(merchant.getBankName());
            paymentSdkDaiFuRequest.setAccountName(merchant.getName());
            paymentSdkDaiFuRequest.setAccountNumber(accountBank.getBankNo());
            paymentSdkDaiFuRequest.setIdCard(MerchantSupport.decryptIdentity(merchant.getIdentity()));
            paymentSdkDaiFuRequest.setPlayMoneyChannel(EnumPayChannelSign.idOf(payChannelSign).getUpperChannel().getId());
            paymentSdkDaiFuRequest.setNote(merchant.getMerchantName());
            paymentSdkDaiFuRequest.setSystemCode(settlementRecord.getAppId());
            paymentSdkDaiFuRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL);
            paymentSdkDaiFuRequest.setPayOrderSn(payOrderSn);
            //请求网关
            PaymentSdkDaiFuResponse response;
            try {
                final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_WITHDRAW,
                        SdkSerializeUtil.convertObjToMap(paymentSdkDaiFuRequest));
                log.info("结算单[" + settlementRecord.getSettleNo() + "],  返回结果[{}]", content);
                response = JSON.parseObject(content, PaymentSdkDaiFuResponse.class);
            } catch (final Throwable e) {
                log.error("结算单[" + settlementRecord.getSettleNo() + "], 请求网关支付异常", e);
                return Pair.of(-1, "请求网关异常， 提现失败");
            }
            this.settlementRecordService.updateStatus(settlementRecordId, EnumSettlementRecordStatus.WITHDRAWING.getId());
            final SettleAccountFlow settleAccountFlow = this.settleAccountFlowService.getBySettlementRecordId(settlementRecordId).get(0);
            final Order payOrder = this.orderService.getByOrderNo(settleAccountFlow.getOrderNo()).get();
            this.orderService.updateSettleStatus(payOrder.getId(), EnumSettleStatus.SETTLE_ING.getId());
            return this.handleWithdrawResult(settlementRecordId, response);
        }
        log.error("商户[{}]，结算单[{}]不可以提现, 状态[{}]", merchantId, settlementRecordId, settlementRecord.getStatus());
        return Pair.of(-1, "当前结算单不可以提现");
    }


    /**
     * {@inheritDoc}
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    @Override
    @Transactional
    public void handleWithdrawCallbackMsg(final PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse) {
        Optional<SettlementRecord> settlementRecordOptional;
        if (paymentSdkWithdrawCallbackResponse.getAutoSettle()) {
            //渠道结算
            final Order payOrder = this.orderService.getByOrderNo(paymentSdkWithdrawCallbackResponse.getOrderNo()).get();
            final SettleAccountFlow settleAccountFlow = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(payOrder.getOrderNo(),
                    payOrder.getPayee(), EnumAccountFlowType.INCREASE.getId()).get();
            settlementRecordOptional = this.settlementRecordService.getById(settleAccountFlow.getSettlementRecordId());
        } else {
            settlementRecordOptional = this.settlementRecordService.getBySettleNo(paymentSdkWithdrawCallbackResponse.getOrderNo());

            if (!settlementRecordOptional.isPresent()){
                //结算单不存在，属于商户提现
                this.merchantWithdrawService.handleMerchantWithdrawCallbackMsg(paymentSdkWithdrawCallbackResponse);
                return;
            }
        }
        if (settlementRecordOptional.get().isWithdrawing()) {
            this.handleWithdrawResult(settlementRecordOptional.get().getId(), paymentSdkWithdrawCallbackResponse);
        }
    }


    /**
     * 处理提现结果
     *
     * @param settlementRecordId 结算单id
     * @param response
     * @return
     */
    private Pair<Integer, String> handleWithdrawResult(final long settlementRecordId, final PaymentSdkDaiFuResponse response) {
        final SettlementRecord settlementRecord = this.settlementRecordService.getByIdWithLock(settlementRecordId).get();
        if (settlementRecord.isWithdrawing()) {
            final EnumBasicStatus status = EnumBasicStatus.of(response.getStatus());
            switch (status) {
                case SUCCESS:
                    this.markWithdrawSuccess(settlementRecord.getId());
                    return Pair.of(0, response.getMessage());
                case FAIL:
                    log.info("结算单号[{}]，提现失败", settlementRecord.getSettleNo());
                    this.markWithdrawFail(settlementRecord.getId());
                    return Pair.of(-1, response.getMessage());
                case HANDLING:
                    log.info("结算单号[{}]，提现处理中", settlementRecord.getSettleNo());
                    return Pair.of(1,  response.getMessage());
                default:
                    log.error("#####结算单号[{}]，返回状态异常######", settlementRecord.getSettleNo());
                    break;
            }
        }
        return Pair.of(-1, response.getMessage());
    }

    /**
     * 提现成功
     *
     * @param settlementRecordId
     */
    private void markWithdrawSuccess(final long settlementRecordId) {
        final SettlementRecord settlementRecord = this.settlementRecordService.getByIdWithLock(settlementRecordId).get();
        if (settlementRecord.isWithdrawing()) {
            final List<SettleAccountFlow> settleAccountFlows = this.settleAccountFlowService.getBySettlementRecordId(settlementRecordId);
            final SettleAccountFlow settleAccountFlow = settleAccountFlows.get(0);
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(settleAccountFlow.getAccountId()).get();
            final Order payOrder = this.orderService.getByOrderNo(settleAccountFlow.getOrderNo()).get();

            //商户结算（对待结算单）
            final Account merchantAccount = this.accountService.getByIdWithLock(merchant.getAccountId()).get();
            this.accountService.decreaseTotalAmount(merchantAccount.getId(), settleAccountFlow.getIncomeAmount());
            this.accountService.decreaseSettleAmount(merchantAccount.getId(), settleAccountFlow.getIncomeAmount());
            final long settleAccountFlowDecreaseId = this.settleAccountFlowService.addSettleAccountFlow(merchantAccount.getId(), settleAccountFlow.getOrderNo(),
                    settleAccountFlow.getIncomeAmount(), "提现结算", EnumAccountFlowType.DECREASE,
                    EnumAppType.HSS.getId(), settleAccountFlow.getTradeDate(), EnumAccountUserType.MERCHANT.getId());
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowDecreaseId, settlementRecordId);
            this.orderService.updateSettleStatus(payOrder.getId(), EnumSettleStatus.SETTLED.getId());
            this.settlementRecordService.updateSettleStatus(settlementRecordId, EnumSettleStatus.SETTLED.getId());
            this.settlementRecordService.updateStatus(settlementRecordId, EnumSettlementRecordStatus.WITHDRAW_SUCCESS.getId());

            BigDecimal merchantWithdrawPoundage = new BigDecimal("0.00");
            if (EnumSettleModeType.CHANNEL_SETTLE.getId() != settlementRecord.getSettleMode()) {
                merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSS, merchant.getId(), payOrder.getPayChannelSign());
                //手续费账户入可用余额
                final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
                this.accountService.increaseTotalAmount(poundageAccount.getId(), merchantWithdrawPoundage);
                this.accountService.increaseAvailableAmount(poundageAccount.getId(), merchantWithdrawPoundage);
                this.accountFlowService.addAccountFlow(poundageAccount.getId(), settlementRecord.getSettleNo(), merchantWithdrawPoundage,
                        "提现分润", EnumAccountFlowType.INCREASE);
                log.info("结算单[{}], 提现分润--结算", settlementRecord.getSettleNo());
                //手续费结算
                this.merchantPoundageSettle(settlementRecord, payOrder.getPayChannelSign(), merchantWithdrawPoundage, merchant);
            }

            final UserInfo user = userInfoService.selectByMerchantId(merchant.getId()).get();
            log.info("商户[{}], 结算单[{}], 提现成功", merchant.getId(), settlementRecord.getSettleNo());
            final AccountBank accountBank = this.accountBankService.getDefault(merchant.getAccountId());
            final String bankNo = accountBank.getBankNo();
            this.sendMsgService.sendPushMessage(settlementRecord.getSettleAmount(), settlementRecord.getCreateTime(),
                    merchantWithdrawPoundage, bankNo.substring(bankNo.length() - 4), user.getOpenId());
        }
    }

    /**
     * 提现失败
     *
     * @param orderId
     */
    private void  markWithdrawFail(final long orderId) {
        log.error("###########【Impossible】#########提现单[{}]出现--好收收暂时不会出现的一种情况：打款最终结果，上游返回失败，不可以再次补发提现####################", orderId);
    }

    /**
     * 商户提现结算
     *
     * @param settlementRecord
     * @param payChannelSign
     */
    @Override
    @Transactional
    public void merchantPoundageSettle(final SettlementRecord settlementRecord, final int payChannelSign, final BigDecimal poundage, final MerchantInfo merchant) {
        final Map<String, Triple<Long, BigDecimal, String>> shallProfitMap =
                this.shallProfitDetailService.withdrawProfitCount(EnumProductType.HSS, settlementRecord.getSettleNo(), settlementRecord.getSettleAmount(), payChannelSign, merchant.getId());
        final Triple<Long, BigDecimal, String> channelMoneyTriple = shallProfitMap.get("channelMoney");
        final Triple<Long, BigDecimal, String> productMoneyTriple = shallProfitMap.get("productMoney");
        final Triple<Long, BigDecimal, String> firstMoneyTriple = shallProfitMap.get("firstMoney");
        final Triple<Long, BigDecimal, String> secondMoneyTriple = shallProfitMap.get("secondMoney");
        final BigDecimal channelMoney = null == channelMoneyTriple ? new BigDecimal("0.00") : channelMoneyTriple.getMiddle();
        final BigDecimal productMoney = null == productMoneyTriple ? new BigDecimal("0.00") : productMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0.00") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0.00") : secondMoneyTriple.getMiddle();
        Preconditions.checkState(poundage.compareTo(channelMoney.add(productMoney).add(firstMoney).add(secondMoney)) >= 0, "分账金额[{}]错误", poundage);
        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(poundage.compareTo(poundageAccount.getAvailable()) <= 0, "该笔订单的分账手续费不可以大于手续费账户的可用余额总和");
        //分账
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), poundage);
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), poundage);
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), settlementRecord.getSettleNo(), poundage,
                "提现分润", EnumAccountFlowType.DECREASE);
        //分账业务类型
        final String splitBusinessType = EnumSplitBusinessType.HSSWITHDRAW.getId();

        //增加分账记录
        //通道利润--到结算--到可用余额
        if (null != channelMoneyTriple) {
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(splitBusinessType, settlementRecord.getSettleNo(), settlementRecord.getSettleNo(),
                    settlementRecord.getSettleAmount(), poundage, channelMoneyTriple, "通道账户",
                    EnumTradeType.WITHDRAW.getValue(), EnumSplitAccountUserType.JKM.getId());
            final Account account = this.accountService.getById(channelMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.increaseAvailableAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountFlowService.addAccountFlow(account.getId(), settlementRecord.getSettleNo(), channelMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);
        }
        //产品利润--到结算--可用余额
        if (null != productMoneyTriple) {
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(splitBusinessType, settlementRecord.getSettleNo(), settlementRecord.getSettleNo(),
                    settlementRecord.getSettleAmount(), poundage, productMoneyTriple, "产品账户",
                    EnumTradeType.WITHDRAW.getValue(), EnumSplitAccountUserType.JKM.getId());
            final Account account = this.accountService.getById(productMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.increaseAvailableAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountFlowService.addAccountFlow(account.getId(), settlementRecord.getSettleNo(), productMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);
        }
        //一级代理商利润--到结算--可用余额
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(splitBusinessType, settlementRecord.getSettleNo(), settlementRecord.getSettleNo(),
                    settlementRecord.getSettleAmount(), poundage, firstMoneyTriple, dealer.getProxyName(),
                    EnumTradeType.WITHDRAW.getValue(), EnumSplitAccountUserType.FIRST_DEALER.getId());
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseAvailableAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountFlowService.addAccountFlow(account.getId(), settlementRecord.getSettleNo(), firstMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);
        }
        //二级代理商利润--到结算--可用余额
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(splitBusinessType, settlementRecord.getSettleNo(), settlementRecord.getSettleNo(),
                    settlementRecord.getSettleAmount(), poundage, secondMoneyTriple, dealer.getProxyName(),
                    EnumTradeType.WITHDRAW.getValue(), EnumSplitAccountUserType.SECOND_DEALER.getId());
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseAvailableAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountFlowService.addAccountFlow(account.getId(), settlementRecord.getSettleNo(), secondMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);
        }
    }

    //判断分账的业务类型
    private String getSplitBusinessType(Order order){

        if (order.getAppId().equals(EnumAppType.HSS.getId())){
            if (order.getTradeType() == EnumTradeType.PAY.getId()){

                return EnumSplitBusinessType.HSSPAY.getId();
            }else if (order.getTradeType() == EnumTradeType.WITHDRAW.getId()){

                return EnumSplitBusinessType.HSSWITHDRAW.getId();
            }else{
                return "";
            }

        }else{

            return EnumSplitBusinessType.HSYPAY.getId();
        }
    }
}
