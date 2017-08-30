package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.*;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.*;
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
import com.jkm.hss.merchant.enums.EnumMessageTemplate;
import com.jkm.hss.merchant.enums.EnumMessageType;
import com.jkm.hss.merchant.enums.EnumSource;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
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
    @Autowired
    private BusinessOrderService businessOrderService;
    @Autowired
    private AppMessageService appMessageService;

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
        final SettleAccountFlow settleAccountFlow = this.settleAccountFlowService.getBySettlementRecordId(settlementRecordId).get(0);
        final Order payOrder = this.orderService.getByOrderNo(settleAccountFlow.getOrderNo()).get();
        if (settlementRecord.isWaitWithdraw()) {
            final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage( payOrder,EnumProductType.HSS, merchantId, payChannelSign);
            final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
            paymentSdkDaiFuRequest.setAppId(settlementRecord.getAppId());
            paymentSdkDaiFuRequest.setOrderNo(settlementRecord.getSettleNo());
            paymentSdkDaiFuRequest.setTotalAmount(settlementRecord.getSettleAmount().subtract(merchantWithdrawPoundage).toPlainString());
            paymentSdkDaiFuRequest.setTradeType(EnumPayChannelSign.idOf(payChannelSign).getSettleType().getType());
            paymentSdkDaiFuRequest.setIsCompany("0");
            paymentSdkDaiFuRequest.setMobile(accountBank.getReserveMobile());
            paymentSdkDaiFuRequest.setBankCity(accountBank.getBranchCityName() == null ? "北京市" : accountBank.getBranchCityName());
            paymentSdkDaiFuRequest.setBankName(merchant.getBankName());
            paymentSdkDaiFuRequest.setBankCode(accountBank.getBankBin());
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
                    EnumAppType.HSS.getId(), settleAccountFlow.getTradeDate(), settleAccountFlow.getSettleDate(), EnumAccountUserType.MERCHANT.getId());
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowDecreaseId, settlementRecordId);
            this.orderService.updateSettleStatus(payOrder.getId(), EnumSettleStatus.SETTLED.getId());
            this.orderService.updateRemark(payOrder.getId(), "提现成功");
            this.settlementRecordService.updateSettleStatus(settlementRecordId, EnumSettleStatus.SETTLED.getId());
            this.settlementRecordService.updateStatus(settlementRecordId, EnumSettlementRecordStatus.WITHDRAW_SUCCESS.getId());

            BigDecimal merchantWithdrawPoundage = new BigDecimal("0.00");
            if (EnumSettleModeType.CHANNEL_SETTLE.getId() != settlementRecord.getSettleMode()) {
                merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(payOrder,EnumProductType.HSS, merchant.getId(), payOrder.getPayChannelSign());
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
            if (EnumSource.APIREG.getId() == merchant.getSource()) {
                //api通知商户
                final Optional<BusinessOrder> businessOrderOptional = this.businessOrderService.getByOrderNoAndMerchantId(payOrder.getBusinessOrderNo(), merchant.getId());
                if (!businessOrderOptional.isPresent()) {
                    log.error("商户[{}]-商户订单号[{}]-交易订单号[{}]，结算成功，发消息-业务订单businessOrder不存在", merchant.getMarkCode(), payOrder.getBusinessOrderNo(), payOrder.getOrderNo());
                }
                log.info("商户[{}]-商户订单号[{}]-交易订单号[{}]，结算成功，发消息通知商户", merchant.getMarkCode(), payOrder.getBusinessOrderNo(), payOrder.getOrderNo());
                final BusinessOrder businessOrder = businessOrderOptional.get();
                final JSONObject requestJsonObject = new JSONObject();
                requestJsonObject.put("orderId", payOrder.getId());
                requestJsonObject.put("businessOrderId", businessOrder.getId());
                requestJsonObject.put("count", 0);
                MqProducer.produce(requestJsonObject, MqConfig.API_SETTLE_CALLBACK, 10);
            } else {
                final AccountBank accountBank = this.accountBankService.getDefault(merchant.getAccountId());
                final String bankNo = accountBank.getBankNo();
                try {
                    final UserInfo user = userInfoService.selectByMerchantId(merchant.getId()).get();
                    log.info("商户[{}], 结算单[{}], 提现成功", merchant.getId(), settlementRecord.getSettleNo());
                    this.sendMsgService.sendPushMessage(settlementRecord.getSettleAmount(), settlementRecord.getCreateTime(),
                            merchantWithdrawPoundage, bankNo.substring(bankNo.length() - 4), user.getOpenId());
                } catch (final Throwable e) {
                    log.error("商户号[" + merchant.getMarkCode() + "], 结算单号[" + settlementRecord.getSettleNo() + "]结算完成，开始通知商户-通知异常", e);
                }
                try {
                    final HashMap<String, String> params = new HashMap<>();
                    params.put("amount", settlementRecord.getSettleAmount().toPlainString());
                    params.put("fee", merchantWithdrawPoundage.toPlainString());
                    params.put("lastnumber", bankNo.substring(bankNo.length() - 4));
                    this.appMessageService.insertMessageInfoAndPush(merchant.getId(), EnumMessageType.WITHDRAW_MESSAGE, EnumMessageTemplate.WITHDRAW_TEMPLATE, params);
                } catch (final Throwable e) {
                    log.error("商户号[" + merchant.getMarkCode() + "], 结算单号[" + settlementRecord.getSettleNo() + "]结算完成，记录消息异常", e);
                }
            }
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
    public void merchantPoundageSettle(final SettlementRecord settlementRecord, final int payChannelSign, final BigDecimal poundage, final MerchantInfo receiveMerchant) {
        final Map<String, Triple<Long, BigDecimal, String>> shallProfitMap =
                this.shallProfitDetailService.withdrawProfitCount(EnumProductType.HSS, settlementRecord.getSettleNo(), settlementRecord.getSettleAmount(), payChannelSign, receiveMerchant.getId());
        final Triple<Long, BigDecimal, String> basicMoneyTriple = shallProfitMap.get("basicMoney");
        final Triple<Long, BigDecimal, String> channelMoneyTriple = shallProfitMap.get("channelMoney");
        final Triple<Long, BigDecimal, String> productMoneyTriple = shallProfitMap.get("productMoney");
        final Triple<Long, BigDecimal, String> firstMoneyTriple = shallProfitMap.get("firstMoney");
        final Triple<Long, BigDecimal, String> secondMoneyTriple = shallProfitMap.get("secondMoney");

        final BigDecimal basicMoney = null == basicMoneyTriple ? new BigDecimal("0.00") : basicMoneyTriple.getMiddle();
        final BigDecimal channelMoney = null == channelMoneyTriple ? new BigDecimal("0.00") : channelMoneyTriple.getMiddle();
        final BigDecimal productMoney = null == productMoneyTriple ? new BigDecimal("0.00") : productMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0.00") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0.00") : secondMoneyTriple.getMiddle();
        log.info("结算单[{}], 提现分润[{}]，成本[{}], 通道[{}], 产品[{}], 一级代理[{}], 二级代理[{}]", settlementRecord.getId(), poundage, basicMoney, channelMoney, productMoney, firstMoney, secondMoney);
        Preconditions.checkState(poundage.compareTo(basicMoney.add(channelMoney).add(productMoney).add(firstMoney).add(secondMoney)) >= 0, "提现-手续费总额不可以小于分润总和");
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
            if (EnumSource.APIREG.getId() != receiveMerchant.getSource()) {
                final Optional<MerchantInfo> dealerMerchantOptional = this.merchantInfoService.selectBySuperDealerId(dealer.getId());
                if (dealerMerchantOptional.isPresent()) {
                    final MerchantInfo merchant = dealerMerchantOptional.get();
                    if (receiveMerchant.getFirstMerchantId() > 0) {
                        //直接
                        log.info("hss提现商户-[{}]，直接一级-分润代理商-[{}]，分润金额-[{}]", receiveMerchant.getMarkCode(), dealer.getMarkCode(), firstMoneyTriple.getMiddle().toPlainString());
                        final HashMap<String, String> params = new HashMap<>();
                        params.put("amount", firstMoneyTriple.getMiddle().toPlainString());
                        this.appMessageService.insertMessageInfoAndPush(merchant.getId(), EnumMessageType.BENEFIT_MESSAGE, EnumMessageTemplate.SUPER_DEALER_DIRECT_MERCHAN_BENEFIT_TEMPLATE, params);
                    } else {
                        //间接
                        log.info("hss提现商户-[{}]，直接一级-分润代理商-[{}]，分润金额-[{}]", receiveMerchant.getMarkCode(), dealer.getMarkCode(), firstMoneyTriple.getMiddle().toPlainString());
                        final HashMap<String, String> params = new HashMap<>();
                        params.put("amount", firstMoneyTriple.getMiddle().toPlainString());
                        this.appMessageService.insertMessageInfoAndPush(merchant.getId(), EnumMessageType.BENEFIT_MESSAGE, EnumMessageTemplate.SUPER_DEALER_INDIRECT_MERCHAN_BENEFIT_TEMPLATE, params);
                    }

                }
            }
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
            if (EnumSource.APIREG.getId() != receiveMerchant.getSource()) {
                final Optional<MerchantInfo> dealerMerchantOptional = this.merchantInfoService.selectBySuperDealerId(dealer.getId());
                if (dealerMerchantOptional.isPresent()) {
                    final MerchantInfo merchant = dealerMerchantOptional.get();
                    if (receiveMerchant.getFirstMerchantId() > 0) {
                        //直接
                        log.info("hss提现商户-[{}]，直接二级-分润代理商-[{}]，分润金额-[{}]", receiveMerchant.getMarkCode(), dealer.getMarkCode(), secondMoneyTriple.getMiddle().toPlainString());
                        final HashMap<String, String> params = new HashMap<>();
                        params.put("amount", secondMoneyTriple.getMiddle().toPlainString());
                        this.appMessageService.insertMessageInfoAndPush(merchant.getId(), EnumMessageType.BENEFIT_MESSAGE, EnumMessageTemplate.DEALER_DIRECT_MERCHAN_BENEFIT_TEMPLATE, params);
                    } else {
                        //间接
                        log.info("hss提现商户-[{}]，间接二级-分润代理商-[{}]，分润金额-[{}]", receiveMerchant.getMarkCode(), dealer.getMarkCode(), secondMoneyTriple.getMiddle().toPlainString());
                        final HashMap<String, String> params = new HashMap<>();
                        params.put("amount", secondMoneyTriple.getMiddle().toPlainString());
                        this.appMessageService.insertMessageInfoAndPush(merchant.getId(), EnumMessageType.BENEFIT_MESSAGE, EnumMessageTemplate.DEALER_INDIRECT_MERCHAN_BENEFIT_TEMPLATE, params);
                    }

                }
            }
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
