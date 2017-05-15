package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.hss.account.entity.*;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.PaymentSdkPlaceOrderRequest;
import com.jkm.hss.bill.entity.PaymentSdkPlaceOrderResponse;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.HolidaySettlementConstants;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.PlaceOrderParams;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.bill.service.MergeTableSettlementDateService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.servcie.BasicChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private MemberAccountService memberAccountService;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private MemberAccountFlowService memberAccountFlowService;
    @Autowired
    private BaseSettlementDateService baseSettlementDateService;
    @Autowired
    private SettlementRecordService settlementRecordService;
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

        placeOrderRequest.setSettleNotifyUrl(placeOrderParams.getSettleNotifyUrl());
        placeOrderRequest.setBankCode(placeOrderParams.getBankBranchCode());
        placeOrderRequest.setCardNo(placeOrderParams.getBankCardNo());
        placeOrderRequest.setPayerName(placeOrderParams.getRealName());
        placeOrderRequest.setIdCardNo(placeOrderParams.getIdCard());
        try {
            final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_PLACE_ORDER, SdkSerializeUtil.convertObjToMap(placeOrderRequest));
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
    @Transactional
    public Pair<Integer, String> handlePlaceOrderResult(final PaymentSdkPlaceOrderResponse placeOrderResponse,
                                                        final EnumMerchantPayType merchantPayType, final Order order) {
        final EnumBasicStatus enumBasicStatus = EnumBasicStatus.of(placeOrderResponse.getCode());
        final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
        Preconditions.checkState(orderOptional.get().isDuePay());
        final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(order.getPayChannelSign());
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
                    return Pair.of(0, url);
                }
                this.orderService.update(order);
                return Pair.of(0, placeOrderResponse.getPayUrl());
            case FAIL:
                log.info("业务方[{}],订单[{}]-下单失败【{}】", order.getAppId(), order.getId(), placeOrderResponse.getMessage());
                this.orderService.updateRemark(order.getId(), placeOrderResponse.getMessage());
                return Pair.of(-1, placeOrderResponse.getMessage());
        }
        return Pair.of(-1, "下单网关返回状态异常");
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     */
    @Override
    @Transactional
    public Pair<Integer, String> memberPayImpl(final long orderId) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        if (order.isDuePay()) {
            final MemberAccount memberAccount = this.memberAccountService.getByIdWithLock(order.getMemberAccountId()).get();
            final ReceiptMemberMoneyAccount receiptMemberMoneyAccount = this.receiptMemberMoneyAccountService.getByIdWithLock(order.getMerchantReceiveAccountId()).get();
            if (memberAccount.getAvailable().compareTo(order.getRealPayAmount()) < 0) {
                return Pair.of(-1, "金额不足");
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
            return Pair.of(0, "支付成功");
        }
        return Pair.of(-1, "交易状态异常");
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
        updateOrder.setPayType(paymentSdkPayCallbackResponse.getPayType());
        updateOrder.setRemark(paymentSdkPayCallbackResponse.getMessage());
        updateOrder.setSn(paymentSdkPayCallbackResponse.getSn());
        updateOrder.setStatus(EnumOrderStatus.PAY_SUCCESS.getId());
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
        updateOrder.setSettleTime(this.baseSettlementDateService.getSettlementDate(order, enumPayChannelSign.getUpperChannel()));
        this.orderService.update(updateOrder);
        this.record(order.getId());
        //TODO

        //如果是好收收，发起提现

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
    public void markRechargeSuccess(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        log.info("业务方[{}]-交易订单[{}]，充值成功, 支付渠道[{}]", order.getAppId(), order.getOrderNo(), paymentSdkPayCallbackResponse.getPayType());
        final Order updateOrder = new Order();
        updateOrder.setId(order.getId());
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
        updateOrder.setSettleTime(this.baseSettlementDateService.getSettlementDate(order, enumPayChannelSign.getUpperChannel()));
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
        updateMemberAccount.setRechargeTotalAmount(memberAccount.getRechargeTotalAmount().add(order.getTradeAmount()));
        updateMemberAccount.setAvailable(memberAccount.getAvailable().add(order.getTradeAmount()));
        this.memberAccountService.update(updateMemberAccount);
        //会员账户额度增加-添加流水
        final MemberAccountFlow memberAccountFlow = new MemberAccountFlow();
        memberAccountFlow.setAccountId(memberAccount.getId());
        memberAccountFlow.setFlowNo(this.memberAccountFlowService.getFlowNo());
        memberAccountFlow.setOrderNo(order.getOrderNo());
        memberAccountFlow.setBeforeAmount(memberAccount.getAvailable());
        memberAccountFlow.setAfterAmount(memberAccount.getAvailable().add(order.getTradeAmount()));
        memberAccountFlow.setOutAmount(new BigDecimal("0.00"));
        memberAccountFlow.setIncomeAmount(order.getTradeAmount());
        memberAccountFlow.setChangeTime(new Date());
        memberAccountFlow.setType(EnumAccountFlowType.INCREASE.getId());
        memberAccountFlow.setRemark("充值增加");
        this.memberAccountFlowService.add(memberAccountFlow);
        //商户收会员款账户充值总额增加
        this.receiptMemberMoneyAccountService.increaseRechargeAmount(receiptMemberMoneyAccount.getId(), order.getRealPayAmount().subtract(order.getPoundage()));
    }
}
