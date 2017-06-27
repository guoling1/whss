package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.hss.account.entity.*;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.enums.EnumUnfrozenType;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.*;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.bill.service.HSYTransactionService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.UpgradeRecommendRulesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/3.
 */
@Slf4j
@Service
public class BaseTradeServiceImpl implements BaseTradeService {

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private HttpClientFacade httpClientFacade;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private FrozenRecordService frozenRecordService;
    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private HSYTransactionService hsyTransactionService;
    @Autowired
    private UnfrozenRecordService unfrozenRecordService;
    @Autowired
    private BaseSettlementService baseSettlementService;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private MemberAccountFlowService memberAccountFlowService;
    @Autowired
    private BaseSettlementDateService baseSettlementDateService;
    @Autowired
    private UpgradeRecommendRulesService upgradeRecommendRulesService;
    @Autowired
    private ReceiptMemberMoneyAccountService receiptMemberMoneyAccountService;
    @Autowired
    private ReceiptMemberMoneyAccountFlowService receiptMemberMoneyAccountFlowService;
    /**
     * {@inheritDoc}
     *
     * @param placeOrderParams
     * @param order 交易单
     * @return
     */
    @Override
    public PaymentSdkPlaceOrderResponse requestPlaceOrder(final PlaceOrderParams placeOrderParams, final Order order) {
        final PaymentSdkPlaceOrderRequest placeOrderRequest = new PaymentSdkPlaceOrderRequest();
        placeOrderRequest.setAppId(order.getAppId());
        placeOrderRequest.setOrderNo(order.getOrderNo());
        placeOrderRequest.setGoodsDescribe(order.getGoodsDescribe());
        placeOrderRequest.setReturnUrl(placeOrderParams.getReturnUrl());
        placeOrderRequest.setNotifyUrl(placeOrderParams.getNotifyUrl());
        placeOrderRequest.setMerName(order.getGoodsName());
        placeOrderRequest.setMerNo(placeOrderParams.getMerchantNo());
        placeOrderRequest.setTotalAmount(order.getTradeAmount().toPlainString());
        placeOrderRequest.setChannel(order.getPayType());
        placeOrderRequest.setWxAppId(placeOrderParams.getWxAppId());
        placeOrderRequest.setMemberId(order.getPayAccount());
        placeOrderRequest.setSubAppId(placeOrderParams.getSubAppId());
        placeOrderRequest.setSubMerchantId(placeOrderParams.getSubMerchantId());
        placeOrderRequest.setSubMemberId(placeOrderParams.getSubMemberId());

        placeOrderRequest.setSettleNotifyUrl(placeOrderParams.getSettleNotifyUrl());
        placeOrderRequest.setBankCode(placeOrderParams.getBankBranchCode());
        placeOrderRequest.setCardNo(placeOrderParams.getBankCardNo());
        placeOrderRequest.setPayerName(placeOrderParams.getRealName());
        placeOrderRequest.setIdCardNo(placeOrderParams.getIdCard());
        try {
            final String content = this.httpClientFacade.jsonPost(PaymentSdkConstants.SDK_PAY_PLACE_ORDER, SdkSerializeUtil.convertObjToMap(placeOrderRequest));
            return JSON.parseObject(content, PaymentSdkPlaceOrderResponse.class);
        } catch (final Throwable e){
            log.error("业务方[{}]-订单[{}]，下单失败-请求网关下单异常", order.getAppId(), order.getId());
            final PaymentSdkPlaceOrderResponse paymentSdkPlaceOrderResponse = new PaymentSdkPlaceOrderResponse();
            paymentSdkPlaceOrderResponse.setCode(EnumBasicStatus.FAIL.getId());
            paymentSdkPlaceOrderResponse.setMessage("请求网关下单异常");
            return paymentSdkPlaceOrderResponse;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param placeOrderResponse
     * @param merchantPayType  公众号或者二维码
     * @param order
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayResponse handlePlaceOrderResult(final PaymentSdkPlaceOrderResponse placeOrderResponse,
                                                        final EnumMerchantPayType merchantPayType, final Order order) {
        final EnumBasicStatus enumBasicStatus = EnumBasicStatus.of(placeOrderResponse.getCode());
        final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
        Preconditions.checkState(orderOptional.get().isDuePay());
        final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(order.getPayChannelSign());
        final PayResponse payResponse = new PayResponse();
        payResponse.setTradeOrderId(order.getId());
        payResponse.setBusinessOrderNo(order.getBusinessOrderNo());
        payResponse.setTradeOrderNo(order.getOrderNo());
        switch (enumBasicStatus) {
            case SUCCESS:
                log.info("业务方[{}],订单[{}]-下单成功【{}】", order.getAppId(), order.getId(), placeOrderResponse);
                order.setRemark(placeOrderResponse.getMessage());
                if (payChannelSign.getNeedPackage() && merchantPayType.equals(EnumMerchantPayType.MERCHANT_JSAPI)) {
                    order.setPayInfo(placeOrderResponse.getPayInfo());
                    order.setPaySalt(RandomStringUtils.randomAlphanumeric(16));
                    order.setPaySign(order.getSignCode());
                    this.orderService.update(order);
                    final String url = "/trade/pay?" + "o_n=" + order.getOrderNo() + "&sign=" + order.getPaySign();
                    payResponse.setCode(EnumBasicStatus.SUCCESS.getId());
                    payResponse.setUrl(url);
                    payResponse.setMessage("下单成功");
                    return payResponse;
                }
                this.orderService.update(order);
                payResponse.setCode(EnumBasicStatus.SUCCESS.getId());
                payResponse.setUrl(placeOrderResponse.getPayUrl());
                payResponse.setMessage("下单成功");
                return payResponse;
            case FAIL:
                log.info("业务方[{}],订单[{}]-下单失败【{}】", order.getAppId(), order.getId(), placeOrderResponse.getMessage());
                this.orderService.updateRemark(order.getId(), placeOrderResponse.getMessage());
                payResponse.setCode(EnumBasicStatus.FAIL.getId());
                payResponse.setMessage(placeOrderResponse.getMessage());
                return payResponse;
        }
        payResponse.setCode(EnumBasicStatus.FAIL.getId());
        payResponse.setMessage("网关异常");
        return payResponse;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public PayResponse memberPayImpl(final long orderId) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        final PayResponse payResponse = new PayResponse();
        payResponse.setBusinessOrderNo(order.getBusinessOrderNo());
        payResponse.setTradeOrderNo(order.getOrderNo());
        if (order.isDuePay()) {
            final MemberAccount memberAccount = this.memberAccountService.getByIdWithLock(order.getMemberAccountId()).get();
            final ReceiptMemberMoneyAccount receiptMemberMoneyAccount = this.receiptMemberMoneyAccountService.getByIdWithLock(order.getMerchantReceiveAccountId()).get();
            if (memberAccount.getAvailable().compareTo(order.getRealPayAmount()) < 0) {
                payResponse.setCode(EnumBasicStatus.FAIL.getId());
                payResponse.setMessage("金额不足");
                return payResponse;
            }
            //会员账户额度减少
            final MemberAccount updateMemberAccount = new MemberAccount();
            updateMemberAccount.setId(memberAccount.getId());
            updateMemberAccount.setConsumeTotalAmount(memberAccount.getConsumeTotalAmount().add(order.getRealPayAmount()));
            updateMemberAccount.setAvailable(memberAccount.getAvailable().subtract(order.getRealPayAmount()));
            this.memberAccountService.update(updateMemberAccount);
            //会员账户额度减少-添加流水
            final MemberAccountFlow memberAccountFlow = new MemberAccountFlow();
            memberAccountFlow.setAccountId(memberAccount.getId());
            memberAccountFlow.setFlowNo(this.memberAccountFlowService.getFlowNo());
            memberAccountFlow.setOrderNo(order.getOrderNo());
            memberAccountFlow.setBeforeAmount(memberAccount.getAvailable());
            memberAccountFlow.setAfterAmount(memberAccount.getAvailable().subtract(order.getRealPayAmount()));
            memberAccountFlow.setOutAmount(order.getRealPayAmount());
            memberAccountFlow.setIncomeAmount(new BigDecimal("0.00"));
            memberAccountFlow.setChangeTime(new Date());
            memberAccountFlow.setType(EnumAccountFlowType.DECREASE.getId());
            memberAccountFlow.setRemark("消费减少");
            this.memberAccountFlowService.add(memberAccountFlow);
            //商户收会员款账户增加
            this.receiptMemberMoneyAccountService.increaseIncomeAmount(receiptMemberMoneyAccount.getId(), order.getRealPayAmount().subtract(order.getPoundage()));
            //商户收会员款账户增加-添加流水
            final ReceiptMemberMoneyAccountFlow receiptMemberMoneyAccountFlow = new ReceiptMemberMoneyAccountFlow();
            receiptMemberMoneyAccountFlow.setAccountId(receiptMemberMoneyAccount.getId());
            receiptMemberMoneyAccountFlow.setFlowNo(this.receiptMemberMoneyAccountFlowService.getFlowNo());
            receiptMemberMoneyAccountFlow.setOrderNo(order.getOrderNo());
            receiptMemberMoneyAccountFlow.setBeforeAmount(receiptMemberMoneyAccount.getIncomeToTalAmount());
            receiptMemberMoneyAccountFlow.setAfterAmount(receiptMemberMoneyAccount.getIncomeToTalAmount().add(order.getRealPayAmount()).subtract(order.getPoundage()));
            receiptMemberMoneyAccountFlow.setOutAmount(new BigDecimal("0.00"));
            receiptMemberMoneyAccountFlow.setIncomeAmount(order.getRealPayAmount().subtract(order.getPoundage()));
            receiptMemberMoneyAccountFlow.setChangeTime(new Date());
            receiptMemberMoneyAccountFlow.setType(EnumAccountFlowType.INCREASE.getId());
            receiptMemberMoneyAccountFlow.setRemark("营业增加");
            this.receiptMemberMoneyAccountFlowService.add(receiptMemberMoneyAccountFlow);
            //更新交易
            final Order updateOrder = new Order();
            updateOrder.setPaySuccessTime(new Date());
            updateOrder.setRemark("会员卡支付成功");
            updateOrder.setSn("");
            updateOrder.setStatus(EnumOrderStatus.PAY_SUCCESS.getId());
            //TODO
            updateOrder.setSettleTime(new Date());
            updateOrder.setSettleStatus(EnumSettleStatus.SETTLED.getId());
            this.orderService.update(updateOrder);

            payResponse.setCode(EnumBasicStatus.SUCCESS.getId());
            payResponse.setMessage("支付成功");
            return payResponse;
        }
        payResponse.setCode(EnumBasicStatus.FAIL.getId());
        payResponse.setMessage("交易状态异常");
        return payResponse;
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param orderId
     */
    @Override
    @Transactional
    public void handlePayOrRechargeCallbackMsgImpl(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final long orderId) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        if (EnumTradeType.PAY.getId() == order.getTradeType()) {
            this.handlePayCallbackMsgImpl(paymentSdkPayCallbackResponse, order);
            return;
        } else if (EnumTradeType.RECHARGE.getId() == order.getTradeType()) {
            this.handleRechargeCallbackMsgImpl(paymentSdkPayCallbackResponse, order);
            return;
        }
        log.error("业务方[{}]-交易单[{}], 处理支付中心回调，交易类型[{}],错误", order.getAppId(), order.getOrderNo(), order.getTradeType());
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void handlePayCallbackMsgImpl(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        final EnumBasicStatus status = EnumBasicStatus.of(paymentSdkPayCallbackResponse.getStatus());
        switch (status) {
            case SUCCESS:
                log.info("业务方[{}]-交易单[{}], 支付成功回调处理", order.getAppId(), order.getOrderNo());
                this.markPaySuccess(paymentSdkPayCallbackResponse, order);
                break;
            case FAIL:
                log.info("业务方[{}]-交易单[{}], 支付失败回调处理", order.getAppId(), order.getOrderNo());
                this.markPayFail(paymentSdkPayCallbackResponse, order);
                break;
            case HANDLING:
                log.info("业务方[{}]-交易单[{}], 支付处理中回调处理", order.getAppId(), order.getOrderNo());
                this.markPayHandling(paymentSdkPayCallbackResponse, order);
                break;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void handleRechargeCallbackMsgImpl(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        final EnumBasicStatus status = EnumBasicStatus.of(paymentSdkPayCallbackResponse.getStatus());
        switch (status) {
            case SUCCESS:
                log.info("业务方[{}]-交易单[{}], 支付-->充值成功回调处理", order.getAppId(), order.getOrderNo());
                this.markRechargeSuccess(paymentSdkPayCallbackResponse, order);
                break;
            case FAIL:
                log.info("业务方[{}]-交易单[{}], 支付-->充值失败回调处理", order.getAppId(), order.getOrderNo());
                this.markRechargeFail(paymentSdkPayCallbackResponse, order);
                break;
            case HANDLING:
                log.info("业务方[{}]-交易单[{}], 支付-->充值处理中回调处理", order.getAppId(), order.getOrderNo());
                this.markPayHandling(paymentSdkPayCallbackResponse, order);
                break;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void markPaySuccess(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        log.info("业务方[{}]-交易订单[{}]，支付成功, 支付渠道[{}]", order.getAppId(), order.getOrderNo(), paymentSdkPayCallbackResponse.getPayType());
        final Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setPaySuccessTime(new DateTime(Long.valueOf(paymentSdkPayCallbackResponse.getPaySuccessTime())).toDate());
        updateOrder.setPayType(paymentSdkPayCallbackResponse.getPayType());
        updateOrder.setRemark(paymentSdkPayCallbackResponse.getMessage());
        updateOrder.setSn(paymentSdkPayCallbackResponse.getSn());
        updateOrder.setStatus(EnumOrderStatus.PAY_SUCCESS.getId());
        final EnumPayChannelSign enumPayChannelSign = this.basicChannelService.getEnumPayChannelSignByCode(paymentSdkPayCallbackResponse.getPayType());
        updateOrder.setPayChannelSign(enumPayChannelSign.getId());
        updateOrder.setSettleTime(this.baseSettlementDateService.getSettlementDate(order.getAppId(), updateOrder.getPaySuccessTime(), order.getSettleType(), enumPayChannelSign.getUpperChannel()));
        //TODO  hss-hsy
        if (EnumServiceType.APPRECIATION_PAY.getId() != order.getServiceType()) {
            final BigDecimal merchantPayPoundageRate = this.calculateService.getMerchantPayPoundageRate(EnumProductType.of(order.getAppId()),
                    order.getPayee(), enumPayChannelSign.getId());
            final BigDecimal merchantPayPoundage = this.calculateService.getMerchantPayPoundage(order.getTradeAmount(), merchantPayPoundageRate,
                    order.getPayChannelSign());
//            final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.of(order.getAppId()),
//                    order.getPayee(), enumPayChannelSign.getId());
            updateOrder.setPoundage(merchantPayPoundage);
            updateOrder.setPayRate(merchantPayPoundageRate);
//            updateOrder.setSettlePoundage(merchantWithdrawPoundage);
        }
        updateOrder.setBankTradeNo(paymentSdkPayCallbackResponse.getBankTradeNo());
        updateOrder.setTradeCardType(paymentSdkPayCallbackResponse.getTradeCardType());
        updateOrder.setTradeCardNo(paymentSdkPayCallbackResponse.getTradeCardNo());
        updateOrder.setWechatOrAlipayOrderNo(paymentSdkPayCallbackResponse.getWechatOrAlipayOrderNo());
        this.orderService.update(updateOrder);
        if (EnumAppType.HSS.getId().equalsIgnoreCase(order.getAppId()) && EnumServiceType.APPRECIATION_PAY.getId() == order.getServiceType()) {
            //TODO
            //回调商户升级业务
            try  {
//                this.merchantInfoService.toUpgrade(order.getBusinessOrderNo(), "S");
            } catch (final Throwable e) {
                log.error("##############商户升级支付成功，回调商户升级业务异常##############", e);
            }
            return;
        }
        final long settlementRecordId = this.record(order.getId());

        //判断商户交易金额--是否升级
        try  {
            //TODO
            if (EnumAppType.HSS.getId().equalsIgnoreCase(order.getAppId())) {
                final BigDecimal totalTradeAmount = this.orderService.getTotalTradeAmountByAccountId(order.getPayee(),
                        EnumAppType.HSS.getId(), EnumServiceType.RECEIVE_MONEY.getId());
                final BigDecimal merchantUpgradeMinAmount = this.upgradeRecommendRulesService.selectInviteStandard();

//            if (totalTradeAmount.compareTo(merchantUpgradeMinAmount) >= 0) {
//                this.merchantInfoService.toUpgradeByRecommend(merchant.getId());
//            }
            }
        } catch (final Throwable e) {
            log.error("##############商户交易金额达到升级标准，调用商户升级业务异常##############", e);
        }
        //TODO
        //如果是好收收，发起提现
        final boolean canWithdraw = EnumAppType.HSS.getId().equalsIgnoreCase(order.getAppId()) && !enumPayChannelSign.getAutoSettle()
                && enumPayChannelSign.getSettleType().getType().equalsIgnoreCase(EnumBalanceTimeType.D0.getType());
        if (canWithdraw) {
            final JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("settlementRecordId", settlementRecordId);
            requestJsonObject.put("payOrderSn", paymentSdkPayCallbackResponse.getSn());
            requestJsonObject.put("payChannelSign", enumPayChannelSign.getId());
            MqProducer.produce(requestJsonObject, MqConfig.MERCHANT_WITHDRAW_D0, 20000);
        }
        //TODO 通知业务
        final CallbackResponse callbackResponse = new CallbackResponse();
        callbackResponse.setBusinessOrderNo(order.getBusinessOrderNo());
        callbackResponse.setTradeOrderNo(order.getOrderNo());
        callbackResponse.setSn(updateOrder.getSn());
        callbackResponse.setSuccessTime(updateOrder.getPaySuccessTime());
        callbackResponse.setTradeAmount(order.getTradeAmount());
        callbackResponse.setPoundage(updateOrder.getPoundage());
        callbackResponse.setCode(EnumBasicStatus.SUCCESS.getId());
        callbackResponse.setMessage(updateOrder.getRemark());
        this.hsyTransactionService.handlePayCallbackMsg(callbackResponse);
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void markRechargeSuccess(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        log.info("业务方[{}]-交易订单[{}]，充值成功, 支付渠道[{}]", order.getAppId(), order.getOrderNo(), paymentSdkPayCallbackResponse.getPayType());
        final Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setPaySuccessTime(new DateTime(Long.valueOf(paymentSdkPayCallbackResponse.getPaySuccessTime())).toDate());
        updateOrder.setPayType(paymentSdkPayCallbackResponse.getPayType());
        updateOrder.setRemark(paymentSdkPayCallbackResponse.getMessage());
        updateOrder.setSn(paymentSdkPayCallbackResponse.getSn());
        updateOrder.setStatus(EnumOrderStatus.RECHARGE_SUCCESS.getId());
        final EnumPayChannelSign enumPayChannelSign = this.basicChannelService.getEnumPayChannelSignByCode(paymentSdkPayCallbackResponse.getPayType());
        updateOrder.setPayChannelSign(enumPayChannelSign.getId());
        //TODO  hss-hsy
        final BigDecimal merchantPayPoundageRate = this.calculateService.getMerchantPayPoundageRate(EnumProductType.of(order.getAppId()), order.getPayee(), enumPayChannelSign.getId());
        final BigDecimal merchantPayPoundage = this.calculateService.getMerchantPayPoundage(order.getTradeAmount(), merchantPayPoundageRate, order.getPayChannelSign());
        updateOrder.setPoundage(merchantPayPoundage);
        updateOrder.setPayRate(merchantPayPoundageRate);
        updateOrder.setBankTradeNo(paymentSdkPayCallbackResponse.getBankTradeNo());
        updateOrder.setTradeCardType(paymentSdkPayCallbackResponse.getTradeCardType());
        updateOrder.setTradeCardNo(paymentSdkPayCallbackResponse.getTradeCardNo());
        updateOrder.setWechatOrAlipayOrderNo(paymentSdkPayCallbackResponse.getWechatOrAlipayOrderNo());
        updateOrder.setSettleTime(this.baseSettlementDateService.getSettlementDate(order.getAppId(), updateOrder.getPaySuccessTime(), order.getSettleType(), enumPayChannelSign.getUpperChannel()));
        this.orderService.update(updateOrder);
        this.record(order.getId());
        //会员账户增加
        this.memberAccountIncrease(order);
        //TODO
        //通知业务
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void markPayFail(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        final Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setSn(paymentSdkPayCallbackResponse.getSn());
        updateOrder.setStatus(EnumOrderStatus.PAY_FAIL.getId());
        updateOrder.setRemark(paymentSdkPayCallbackResponse.getMessage());
        this.orderService.update(updateOrder);
        //TODO
        //回调商户升级业务
        if (EnumAppType.HSS.getId().equalsIgnoreCase(order.getAppId()) && EnumServiceType.APPRECIATION_PAY.getId() == order.getServiceType()) {
            try  {
//                this.merchantInfoService.toUpgrade(order.getBusinessOrderNo(), "F");
            } catch (final Throwable e) {
                log.error("##############商户升级支付成功，回调商户升级业务异常##############");
            }
        }
        //TODO 通知业务
        final CallbackResponse callbackResponse = new CallbackResponse();
        callbackResponse.setBusinessOrderNo(order.getBusinessOrderNo());
        callbackResponse.setTradeOrderNo(order.getOrderNo());
        callbackResponse.setSn(updateOrder.getSn());
        callbackResponse.setCode(EnumBasicStatus.FAIL.getId());
        callbackResponse.setMessage(updateOrder.getRemark());
        this.hsyTransactionService.handlePayCallbackMsg(callbackResponse);
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void markRechargeFail(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        final Order updateOrder = new Order();
        updateOrder.setId(order.getId());
        updateOrder.setSn(paymentSdkPayCallbackResponse.getSn());
        updateOrder.setStatus(EnumOrderStatus.RECHARGE_FAIL.getId());
        updateOrder.setRemark(paymentSdkPayCallbackResponse.getMessage());
        this.orderService.update(updateOrder);
        //TODO
        //通知业务
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void markPayHandling(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        this.orderService.updateRemark(order.getId(), paymentSdkPayCallbackResponse.getMessage());
        //TODO 通知业务
        final CallbackResponse callbackResponse = new CallbackResponse();
        callbackResponse.setBusinessOrderNo(order.getBusinessOrderNo());
        callbackResponse.setTradeOrderNo(order.getOrderNo());
        callbackResponse.setCode(EnumBasicStatus.HANDLING.getId());
        callbackResponse.setMessage("处理中");
        this.hsyTransactionService.handlePayCallbackMsg(callbackResponse);
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void markRechargeHandling(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        this.orderService.updateRemark(order.getId(), paymentSdkPayCallbackResponse.getMessage());
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     */
    @Override
    @Transactional
    public long record(final long orderId) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        log.info("交易单[{}], 进行入账操作", order.getOrderNo());
        if ((order.isPaySuccess() || order.isRechargeSuccess()) && order.isDueSettle()) {
            //手续费账户--可用余额
            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
            this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
            //可用余额流水增加
            final AccountFlow accountFlow = new AccountFlow();
            accountFlow.setAccountId(poundageAccount.getId());
            accountFlow.setOrderNo(order.getOrderNo());
            accountFlow.setType(EnumAccountFlowType.INCREASE.getId());
            accountFlow.setOutAmount(new BigDecimal("0.00"));
            accountFlow.setIncomeAmount(order.getPoundage());
            accountFlow.setBeforeAmount(poundageAccount.getAvailable());
            accountFlow.setAfterAmount(poundageAccount.getAvailable().add(accountFlow.getIncomeAmount()));
            accountFlow.setChangeTime(new Date());
            accountFlow.setRemark(EnumTradeType.of(order.getTradeType()).getValue());
            this.accountFlowService.add(accountFlow);
            //商户待结算增加
            final Account merchantAccount = this.accountService.getByIdWithLock(order.getPayee()).get();
            this.accountService.increaseTotalAmount(merchantAccount.getId(), order.getTradeAmount().subtract(order.getPoundage()));
            this.accountService.increaseSettleAmount(merchantAccount.getId(), order.getTradeAmount().subtract(order.getPoundage()));
            //待结算流水增加
            final SettleAccountFlow settleAccountFlow = new SettleAccountFlow();
            settleAccountFlow.setAccountId(merchantAccount.getId());
            settleAccountFlow.setOrderNo(order.getOrderNo());
            settleAccountFlow.setType(EnumAccountFlowType.INCREASE.getId());
            settleAccountFlow.setOutAmount(new BigDecimal("0.00"));
            settleAccountFlow.setIncomeAmount(order.getTradeAmount().subtract(order.getPoundage()));
            settleAccountFlow.setBeforeAmount(merchantAccount.getDueSettleAmount());
            settleAccountFlow.setAfterAmount(merchantAccount.getDueSettleAmount().add(settleAccountFlow.getIncomeAmount()));
            settleAccountFlow.setChangeTime(new Date());
            settleAccountFlow.setRemark(EnumTradeType.of(order.getTradeType()).getValue());
            settleAccountFlow.setAppId(order.getAppId());
            settleAccountFlow.setTradeDate(order.getPaySuccessTime());
            settleAccountFlow.setSettleDate(order.getSettleTime());
            settleAccountFlow.setAccountUserType(EnumAccountUserType.MERCHANT.getId());
            this.settleAccountFlowService.add(settleAccountFlow);
            //HSS生成结算单
            if (EnumAppType.HSS.getId().equals(order.getAppId())) {
                final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(order.getPayChannelSign());
                final SettlementRecord settlementRecord = new SettlementRecord();
                settlementRecord.setAccountId(merchantAccount.getId());
                settlementRecord.setUserNo(order.getMerchantNo());
                settlementRecord.setUserName(order.getMerchantName());
                settlementRecord.setAccountUserType(EnumAccountUserType.MERCHANT.getId());
                settlementRecord.setAppId(order.getAppId());
                settlementRecord.setSettleDate(order.getSettleTime());
                settlementRecord.setTradeNumber(1);
                settlementRecord.setSettleAmount(order.getTradeAmount().subtract(order.getPoundage()));
                settlementRecord.setSettlePoundage(order.getSettlePoundage());
                settlementRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
                if (payChannelSign.getAutoSettle()) {
                    settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.MERCHANT.getId(), EnumSettleDestinationType.CHANNEL_SETTLE.getId()));
                    settlementRecord.setSettleMode(EnumSettleModeType.CHANNEL_SETTLE.getId());
                    settlementRecord.setSettleDestination(EnumSettleDestinationType.CHANNEL_SETTLE.getId());
                    settlementRecord.setStatus(EnumSettlementRecordStatus.WITHDRAWING.getId());
                } else {
                    settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.MERCHANT.getId(), EnumSettleDestinationType.TO_CARD.getId()));
                    settlementRecord.setSettleMode(EnumSettleModeType.SELF_SETTLE.getId());
                    settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_CARD.getId());
                    settlementRecord.setStatus(EnumSettlementRecordStatus.WAIT_WITHDRAW.getId());
                }
                final long settlementRecordId = this.settlementRecordService.add(settlementRecord);
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlow.getId(), settlementRecordId);
                return settlementRecordId;
            }
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     */
    @Override
    @Transactional
    public void memberAccountIncrease(final Order order) {
        final MemberAccount memberAccount = this.memberAccountService.getByIdWithLock(order.getMemberAccountId()).get();
        final ReceiptMemberMoneyAccount receiptMemberMoneyAccount = this.receiptMemberMoneyAccountService.getByIdWithLock(order.getMerchantReceiveAccountId()).get();
        //会员账户额度增加
        final MemberAccount updateMemberAccount = new MemberAccount();
        updateMemberAccount.setId(memberAccount.getId());
        updateMemberAccount.setRechargeTotalAmount(memberAccount.getRechargeTotalAmount().add(order.getTradeAmount()).add(order.getMarketingAmount()));
        updateMemberAccount.setAvailable(memberAccount.getAvailable().add(order.getTradeAmount()).add(order.getMarketingAmount()));
        this.memberAccountService.update(updateMemberAccount);
        //会员账户额度增加-添加流水
        final MemberAccountFlow memberAccountFlow = new MemberAccountFlow();
        memberAccountFlow.setAccountId(memberAccount.getId());
        memberAccountFlow.setFlowNo(this.memberAccountFlowService.getFlowNo());
        memberAccountFlow.setOrderNo(order.getOrderNo());
        memberAccountFlow.setBeforeAmount(memberAccount.getAvailable());
        memberAccountFlow.setAfterAmount(memberAccount.getAvailable().add(order.getTradeAmount()).add(order.getMarketingAmount()));
        memberAccountFlow.setOutAmount(new BigDecimal("0.00"));
        memberAccountFlow.setIncomeAmount(order.getTradeAmount().add(order.getMarketingAmount()));
        memberAccountFlow.setChangeTime(new Date());
        memberAccountFlow.setType(EnumAccountFlowType.INCREASE.getId());
        memberAccountFlow.setRemark("充值增加");
        this.memberAccountFlowService.add(memberAccountFlow);
        //商户收会员款账户充值总额增加
        this.receiptMemberMoneyAccountService.increaseRechargeAmount(receiptMemberMoneyAccount.getId(), order.getTradeAmount().add(order.getMarketingAmount()));
    }

    /**
     * {@inheritDoc}
     *
     * @param settlementRecordId
     * @param payOrderSn
     * @param payChannelSign
     */
    @Override
    @Transactional
    public void withdrawBySettlement(final long settlementRecordId, final String payOrderSn, final int payChannelSign) {
        final Optional<SettlementRecord> settlementRecordOptional = this.settlementRecordService.getById(settlementRecordId);
        log.info("业务方[{}]，对结算单[{}], 进行提现", settlementRecordOptional.get().getAppId(), settlementRecordId);
        if (settlementRecordOptional.get().isWaitWithdraw()) {
            final SettlementRecord settlementRecord = this.settlementRecordService.getByIdWithLock(settlementRecordId).get();
            final AccountBank accountBank = this.accountBankService.getDefault(settlementRecord.getAccountId());
            if (settlementRecord.isWaitWithdraw()) {
                final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
                paymentSdkDaiFuRequest.setAppId(settlementRecord.getAppId());
                paymentSdkDaiFuRequest.setOrderNo(settlementRecord.getSettleNo());
                paymentSdkDaiFuRequest.setTotalAmount(settlementRecord.getSettleAmount().subtract(settlementRecord.getSettlePoundage()).toPlainString());
                paymentSdkDaiFuRequest.setTradeType(EnumPayChannelSign.idOf(payChannelSign).getSettleType().getType());
                paymentSdkDaiFuRequest.setIsCompany("0");
                paymentSdkDaiFuRequest.setMobile(accountBank.getReserveMobile());
                paymentSdkDaiFuRequest.setBankName(accountBank.getBankName());
                paymentSdkDaiFuRequest.setAccountName("开户名");
                paymentSdkDaiFuRequest.setAccountNumber(accountBank.getBankNo());
                paymentSdkDaiFuRequest.setIdCard("身份证号");
                paymentSdkDaiFuRequest.setPlayMoneyChannel(EnumPayChannelSign.idOf(payChannelSign).getUpperChannel().getId());
                paymentSdkDaiFuRequest.setNote(settlementRecord.getUserName());
                paymentSdkDaiFuRequest.setSystemCode(settlementRecord.getAppId());
                paymentSdkDaiFuRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL);
                paymentSdkDaiFuRequest.setPayOrderSn(payOrderSn);
                //请求网关
                PaymentSdkDaiFuResponse response;
                try {
                    final String content = this.httpClientFacade.jsonPost(PaymentSdkConstants.SDK_PAY_WITHDRAW,
                            SdkSerializeUtil.convertObjToMap(paymentSdkDaiFuRequest));
                    log.info("结算单[" + settlementRecord.getSettleNo() + "],  返回结果[{}]", content);
                    response = JSON.parseObject(content, PaymentSdkDaiFuResponse.class);
                } catch (final Throwable e) {
                    log.error("结算单[" + settlementRecord.getSettleNo() + "], 请求网关支付异常", e);
                    response = new PaymentSdkDaiFuResponse();
                    response.setStatus(EnumBasicStatus.FAIL.getId());
                    response.setMessage("请求网关异常");
                }
                this.settlementRecordService.updateStatus(settlementRecordId, EnumSettlementRecordStatus.WITHDRAWING.getId());
                final SettleAccountFlow settleAccountFlow = this.settleAccountFlowService.getBySettlementRecordId(settlementRecordId).get(0);
                final Order payOrder = this.orderService.getByOrderNo(settleAccountFlow.getOrderNo()).get();
                this.orderService.updateSettleStatus(payOrder.getId(), EnumSettleStatus.SETTLE_ING.getId());
                this.handleWithdrawBySettlementResult(settlementRecordId, response);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param withdrawParams
     * @param orderId
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Pair<Integer, String> withdrawImpl(final WithdrawParams withdrawParams, final long orderId) {
        final Order playMoneyOrder = this.orderService.getByIdWithLock(orderId).get();
        if (playMoneyOrder.isWithDrawing()) {
            final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
            paymentSdkDaiFuRequest.setAppId(playMoneyOrder.getAppId());
            paymentSdkDaiFuRequest.setOrderNo(playMoneyOrder.getOrderNo());
            paymentSdkDaiFuRequest.setTotalAmount(playMoneyOrder.getTradeAmount().subtract(playMoneyOrder.getPoundage()).toPlainString());
            paymentSdkDaiFuRequest.setTradeType("D0");
            paymentSdkDaiFuRequest.setIsCompany("0");
            paymentSdkDaiFuRequest.setMobile(withdrawParams.getMobile());
            paymentSdkDaiFuRequest.setBankName(withdrawParams.getBankName());
            paymentSdkDaiFuRequest.setAccountName(withdrawParams.getUserName());
            paymentSdkDaiFuRequest.setAccountNumber(withdrawParams.getBankCardNo());
            paymentSdkDaiFuRequest.setIdCard(withdrawParams.getIdentityCardNo());
            paymentSdkDaiFuRequest.setPlayMoneyChannel(EnumPayChannelSign.idOf(playMoneyOrder.getPayChannelSign()).getUpperChannel().getId());
            paymentSdkDaiFuRequest.setNote(withdrawParams.getNote());
            paymentSdkDaiFuRequest.setSystemCode(playMoneyOrder.getAppId());
            paymentSdkDaiFuRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL);
            paymentSdkDaiFuRequest.setPayOrderSn("");
            //请求网关
            PaymentSdkDaiFuResponse response = null;
            try {
                final String content = this.httpClientFacade.jsonPost(PaymentSdkConstants.SDK_PAY_WITHDRAW,
                        SdkSerializeUtil.convertObjToMap(paymentSdkDaiFuRequest));
                log.info("订单[{}]，向网关发起代付[{}]，返回[{}]", playMoneyOrder.getOrderNo(), paymentSdkDaiFuRequest, content);
                response = JSON.parseObject(content, PaymentSdkDaiFuResponse.class);
            } catch (final Throwable e) {
                log.error("交易订单[" + playMoneyOrder.getOrderNo() + "], 请求网关支付异常", e);
            }
            return this.handleWithdrawByAccountResult(playMoneyOrder, response);
        }
        return Pair.of(-1, "订单状态异常");
    }

    /**
     * {@inheritDoc}
     *
     * @param settlementRecordId
     * @param response
     */
    @Override
    @Transactional
    public void handleWithdrawBySettlementResult(final long settlementRecordId, final PaymentSdkDaiFuResponse response) {
        final SettlementRecord settlementRecord = this.settlementRecordService.getByIdWithLock(settlementRecordId).get();
        if (settlementRecord.isWithdrawing()) {
            final EnumBasicStatus status = EnumBasicStatus.of(response.getStatus());
            switch (status) {
                case SUCCESS:
                    this.markWithdrawBySettlementSuccess(settlementRecord);
                    break;
                case FAIL:
                    log.info("结算单号[{}]，提现失败", settlementRecord.getSettleNo());
                    log.error("###########【Impossible】#########结算单[{}]出现--不会出现的一种情况：打款最终结果，上游返回失败，不可以再次补发提现####################", settlementRecord.getSettleNo());
                    break;
                case HANDLING:
                    log.info("结算单号[{}]，提现处理中", settlementRecord.getSettleNo());
                    break;
                default:
                    log.error("#####结算单号[{}]，返回状态异常######", settlementRecord.getSettleNo());
                    break;
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     * @param response
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> handleWithdrawByAccountResult(final Order order, final PaymentSdkDaiFuResponse response) {
        if (order.isWithDrawing()) {
            final EnumBasicStatus status = EnumBasicStatus.of(response.getStatus());
            switch (status) {
                case SUCCESS:
                    log.info("提现单[{}]，提现成功", order.getOrderNo());
                    this.markWithdrawByAccountSuccess(order, response);
                    return Pair.of(0, response.getMessage());
                case FAIL:
                    log.error("提现单[{}]，提现失败", order.getOrderNo());
                    this.orderService.updateRemark(order.getId(), response.getMessage());
                    return Pair.of(-1, response.getMessage());
                case HANDLING:
                    log.info("提现单[{}]，提现处理中", order.getOrderNo());
                    this.orderService.updateRemark(order.getId(), response.getMessage());
                    return Pair.of(1,  response.getMessage());
                default:
                    log.error("#####提现单[{}]，返回状态异常######", order.getOrderNo());
                    return Pair.of(-1, "网关返回状态异常");
            }
        }
        return Pair.of(-1, "提现单状态异常");
    }


    /**
     * 按结算单提现成功
     *
     * @param settlementRecord
     */
    private void markWithdrawBySettlementSuccess(final SettlementRecord settlementRecord) {
        final SettleAccountFlow settleAccountFlow = this.settleAccountFlowService.getBySettlementRecordId(settlementRecord.getId()).get(0);
        final Order payOrder = this.orderService.getByOrderNo(settleAccountFlow.getOrderNo()).get();
        //商户结算（对待结算单）
        final Account merchantAccount = this.accountService.getByIdWithLock(settlementRecord.getAccountId()).get();
        final long decreaseSettleAccountFlowId = this.baseSettlementService.pendingSettleAccountFlowOutAccount(settleAccountFlow, merchantAccount, "提现结算");
        this.settleAccountFlowService.updateSettlementRecordIdById(decreaseSettleAccountFlowId, settlementRecord.getId());
        this.orderService.updateSettleStatus(payOrder.getId(), EnumSettleStatus.SETTLED.getId());
        this.orderService.updateRemark(payOrder.getId(), "提现成功");
        this.settlementRecordService.updateSettleStatus(settlementRecord.getId(), EnumSettleStatus.SETTLED.getId());
        this.settlementRecordService.updateStatus(settlementRecord.getId(), EnumSettlementRecordStatus.WITHDRAW_SUCCESS.getId());
        if (EnumSettleModeType.CHANNEL_SETTLE.getId() != settlementRecord.getSettleMode()) {//渠道结算没有提现费用
            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(poundageAccount.getId(), settlementRecord.getSettlePoundage());
            this.accountService.increaseAvailableAmount(poundageAccount.getId(), settlementRecord.getSettlePoundage());
            final AccountFlow accountFlow = new AccountFlow();
            accountFlow.setAccountId(poundageAccount.getId());
            accountFlow.setOrderNo(payOrder.getOrderNo());
            accountFlow.setRefundOrderNo("");
            accountFlow.setIncomeAmount(settlementRecord.getSettlePoundage());
            accountFlow.setOutAmount(new BigDecimal("0.00"));
            accountFlow.setBeforeAmount(poundageAccount.getAvailable());
            accountFlow.setAfterAmount(poundageAccount.getAvailable().add(accountFlow.getIncomeAmount()));
            accountFlow.setChangeTime(new Date());
            accountFlow.setType(EnumAccountFlowType.INCREASE.getId());
            accountFlow.setRemark("提现入账");
            this.accountFlowService.add(accountFlow);
            log.info("结算单[{}], 提现分润--结算", settlementRecord.getSettleNo());
            //提现分润--通知业务提现成功-可以分润
            //TODO
        }

//        final UserInfo user = userInfoService.selectByMerchantId(merchant.getId()).get();
//        log.info("商户[{}], 结算单[{}], 提现成功", merchant.getId(), settlementRecord.getSettleNo());
//        final AccountBank accountBank = this.accountBankService.getDefault(merchant.getAccountId());
//        final String bankNo = accountBank.getBankNo();
//        this.sendMsgService.sendPushMessage(settlementRecord.getSettleAmount(), settlementRecord.getCreateTime(),
//                merchantWithdrawPoundage, bankNo.substring(bankNo.length() - 4), user.getOpenId());
    }

    /**
     *
     *
     * @param order
     * @param response
     */
    private void markWithdrawByAccountSuccess(final Order order, final PaymentSdkDaiFuResponse response) {
        if (order.isWithDrawing()) {
            final Account account = this.accountService.getByIdWithLock(order.getPayer()).get();
            final Order updateOrder = new Order();
            updateOrder.setId(order.getId());
            updateOrder.setStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getId());
            updateOrder.setRemark(response.getMessage());
            updateOrder.setSn(response.getSn());
            this.orderService.update(updateOrder);
            final FrozenRecord frozenRecord = this.frozenRecordService.getByBusinessNo(response.getOrderNo()).get();
            //解冻金额
            final UnFrozenRecord unFrozenRecord = new UnFrozenRecord();
            unFrozenRecord.setAccountId(account.getId());
            unFrozenRecord.setFrozenRecordId(frozenRecord.getId());
            unFrozenRecord.setBusinessNo(order.getOrderNo());
            unFrozenRecord.setUnfrozenType(EnumUnfrozenType.CONSUME.getId());
            unFrozenRecord.setUnfrozenAmount(frozenRecord.getFrozenAmount());
            unFrozenRecord.setUnfrozenTime(new Date());
            unFrozenRecord.setRemark("提现成功");
            this.unfrozenRecordService.add(unFrozenRecord);
            //减少总金额,减少冻结金额
            Preconditions.checkState(account.getFrozenAmount().compareTo(frozenRecord.getFrozenAmount()) >= 0);
            Preconditions.checkState(account.getTotalAmount().compareTo(frozenRecord.getFrozenAmount()) >= 0);
            this.accountService.decreaseFrozenAmount(account.getId(), frozenRecord.getFrozenAmount());
            this.accountService.decreaseTotalAmount(account.getId(), frozenRecord.getFrozenAmount());
            //手续费-->利润账户
            final Account jkmAccount = this.accountService.getByIdWithLock(AccountConstants.JKM_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(jkmAccount.getId(), order.getPoundage());
            this.accountService.increaseAvailableAmount(jkmAccount.getId(), order.getPoundage());
            //可用余额流水增加
            final AccountFlow accountFlow = new AccountFlow();
            accountFlow.setAccountId(jkmAccount.getId());
            accountFlow.setOrderNo(order.getOrderNo());
            accountFlow.setType(EnumAccountFlowType.INCREASE.getId());
            accountFlow.setOutAmount(new BigDecimal("0.00"));
            accountFlow.setIncomeAmount(order.getPoundage());
            accountFlow.setBeforeAmount(jkmAccount.getAvailable());
            accountFlow.setAfterAmount(jkmAccount.getAvailable().add(accountFlow.getIncomeAmount()));
            accountFlow.setChangeTime(new Date());
            accountFlow.setRemark("账户余额提现手续费入账");
            this.accountFlowService.add(accountFlow);
        }
    }
}
