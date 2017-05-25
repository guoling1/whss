package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.*;
import com.jkm.hss.bill.helper.responseparam.RefundProfitResponse;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.RefundOrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.bill.service.TradeService;
import com.jkm.hss.merchant.service.SendMsgService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hss.product.servcie.BasicChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/5/2.
 */
@Slf4j
@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpClientFacade httpClientFacade;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private BaseTradeService baseTradeService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private BaseSplitProfitService baseSplitProfitService;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;

    /**
     * {@inheritDoc}
     *
     * @param rechargeParams
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PayResponse recharge(final RechargeParams rechargeParams) {
        log.info("业务方[{}],通过渠道[{}]进行充值[{}],实付金额[{}]，充值账户[{}]，收款账户[{}], 会员标识[{}], 商户号[{}]",
                rechargeParams.getAppId(), rechargeParams.getChannel(), rechargeParams.getTradeAmount(), rechargeParams.getRealPayAmount(),
                rechargeParams.getMemberAccountId(), rechargeParams.getPayeeAccountId(), rechargeParams.getMemberId(), rechargeParams.getMerchantNo());
        final Optional<Order> orderOptional = this.orderService.getByBusinessOrderNo(rechargeParams.getBusinessOrderNo());
        if (orderOptional.isPresent()) {
            final PayResponse payResponse = new PayResponse();
            payResponse.setCode(EnumBasicStatus.FAIL.getId());
            payResponse.setMessage("业务订单号重复");
            payResponse.setBusinessOrderNo(rechargeParams.getBusinessOrderNo());
            return payResponse;
        }
        final String channelCode = this.basicChannelService.selectCodeByChannelSign(rechargeParams.getChannel(), rechargeParams.getMerchantPayType());
        final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(rechargeParams.getChannel());
        final Order order = new Order();
        order.setBusinessOrderNo(rechargeParams.getBusinessOrderNo());
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.RECHARGE.getId()));
        order.setTradeAmount(rechargeParams.getTradeAmount());
        order.setRealPayAmount(rechargeParams.getRealPayAmount());
        order.setMarketingAmount(rechargeParams.getMarketingAmount());
        order.setAppId(rechargeParams.getAppId());
        order.setMerchantNo(rechargeParams.getMerchantNo());
        order.setMerchantName(rechargeParams.getMerchantName());
        order.setTradeType(EnumTradeType.RECHARGE.getId());
        order.setServiceType(EnumServiceType.RECEIVE_MONEY.getId());
        order.setPayer(0);
        order.setPayee(rechargeParams.getPayeeAccountId());
        order.setMemberAccountId(rechargeParams.getMemberAccountId());
        order.setMerchantReceiveAccountId(rechargeParams.getMerchantReceiveAccountId());
        order.setGoodsName(rechargeParams.getGoodsName());
        order.setGoodsDescribe(rechargeParams.getGoodsDescribe());
        order.setPayType(channelCode);
        order.setPayChannelSign(rechargeParams.getChannel());
        order.setPayAccountType(payChannelSign.getPaymentChannel().getId());
        order.setPayAccount(rechargeParams.getMemberId());
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleType(payChannelSign.getSettleType().getType());
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        this.orderService.add(order);

        final PlaceOrderParams placeOrderParams = PlaceOrderParams.builder()
                .merchantNo(rechargeParams.getMerchantNo())
                .returnUrl(PaymentSdkConstants.SDK_PAY_RETURN_URL + order.getTradeAmount() + "/" + order.getId())
                .notifyUrl(PaymentSdkConstants.SDK_PAY_NOTIFY_URL)
                .wxAppId(rechargeParams.getAppId())
                .memberId(rechargeParams.getMemberId())
                .build();
        final PaymentSdkPlaceOrderResponse paymentSdkPlaceOrderResponse = this.baseTradeService.requestPlaceOrder(placeOrderParams, order);
        return this.baseTradeService.handlePlaceOrderResult(paymentSdkPlaceOrderResponse, rechargeParams.getMerchantPayType(), order);
    }

    /**
     * 支付
     *
     * @param payParams
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public PayResponse pay(final PayParams payParams) {
        log.info("业务方[{}],通过渠道[{}]进行支付[{}],实付金额[{}]", payParams.getAppId(), payParams.getChannel(), payParams.getTradeAmount(), payParams.getRealPayAmount());
        final Optional<Order> orderOptional = this.orderService.getByBusinessOrderNo(payParams.getBusinessOrderNo());
        if (orderOptional.isPresent()) {
            final PayResponse payResponse = new PayResponse();
            payResponse.setBusinessOrderNo(payParams.getBusinessOrderNo());
            payResponse.setCode(EnumBasicStatus.FAIL.getId());
            payResponse.setMessage("业务订单号重复");
            return payResponse;
        }
        final String channelCode = this.basicChannelService.selectCodeByChannelSign(payParams.getChannel(), payParams.getMerchantPayType());
        final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(payParams.getChannel());
        final Order order = new Order();
        order.setBusinessOrderNo(payParams.getBusinessOrderNo());
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.PAY.getId()));
        order.setTradeAmount(payParams.getTradeAmount());
        order.setRealPayAmount(payParams.getRealPayAmount());
        order.setAppId(payParams.getAppId());
        order.setMerchantNo(payParams.getMerchantNo());
        order.setMerchantName(payParams.getMerchantName());
        order.setTradeType(EnumTradeType.PAY.getId());
        order.setServiceType(payParams.getServiceType());
        order.setPayer(payParams.getPayerAccountId());
        order.setPayee(payParams.getPayeeAccountId());
        order.setMemberAccountId(payParams.getMemberAccountId());
        order.setMerchantReceiveAccountId(payParams.getMerchantReceiveAccountId());
        order.setGoodsName(payParams.getGoodsName());
        order.setGoodsDescribe(payParams.getGoodsDescribe());
        order.setPayType(payParams.getMemberCardPay() ? payChannelSign.getCode() : channelCode);
        order.setPayChannelSign(payParams.getChannel());
        order.setPayAccountType(payChannelSign.getPaymentChannel().getId());
        order.setPayAccount(payParams.getMemberId());
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleType(payChannelSign.getSettleType().getType());
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        order.setPoundage(new BigDecimal("0.00"));
        this.orderService.add(order);
        if (payParams.getMemberCardPay()) {
            //从卡扣钱
            return this.baseTradeService.memberPayImpl(order.getId());
        } else {
            //获取支付url
            final PlaceOrderParams placeOrderParams = PlaceOrderParams.builder()
                    .merchantNo(payParams.getMerchantNo())
                    .returnUrl(PaymentSdkConstants.SDK_PAY_RETURN_URL + order.getTradeAmount() + "/" + order.getId())
                    .notifyUrl(PaymentSdkConstants.SDK_PAY_NOTIFY_URL)
                    .wxAppId(payParams.getAppId())
                    .memberId(payParams.getMemberId())
                    .bankBranchCode(payParams.getBankBranchCode())
                    .bankCardNo(payParams.getBankCardNo())
                    .realName(payParams.getRealName())
                    .idCard(payParams.getIdCard())
                    .settleNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL)
                    .build();
            final PaymentSdkPlaceOrderResponse paymentSdkPlaceOrderResponse = this.baseTradeService.requestPlaceOrder(placeOrderParams, order);
            return this.baseTradeService.handlePlaceOrderResult(paymentSdkPlaceOrderResponse, payParams.getMerchantPayType(), order);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @return
     */
    @Override
    @Transactional
    public void handlePayOrRechargeCallbackMsg(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse) {
        log.info("交易单[{}], 处理回调");
        final String orderNo = paymentSdkPayCallbackResponse.getOrderNo();
        final Optional<Order> orderOptional = this.orderService.getByOrderNo(orderNo);
        Preconditions.checkState(orderOptional.isPresent(), "交易订单[{}]不存在", orderNo);
        if (orderOptional.get().isDuePay() || orderOptional.get().isPaying()) {
            final Order order = this.orderService.getByIdWithLock(orderOptional.get().getId()).get();
            if (order.isDuePay() || order.isPaying()) {
                this.baseTradeService.handlePayOrRechargeCallbackMsgImpl(paymentSdkPayCallbackResponse, order.getId());
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param splitProfitParams
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Pair<Integer, String> splitProfitImpl(final SplitProfitParams splitProfitParams) {
        Preconditions.checkState(splitProfitParams.getSplitProfitDetails().size() > 0);
        if (EnumSplitBusinessType.HSSWITHDRAW.getId() == splitProfitParams.getSplitType()) {
            final Optional<SettlementRecord> settlementRecordOptional = this.settlementRecordService.getBySettleNo(splitProfitParams.getOrderNo());
            if (!settlementRecordOptional.isPresent()) {
                return Pair.of(-1, "结算单不存在");
            }
            log.info("结算单[{}]，执行分润操作, 分润类型[{}]", splitProfitParams.getOrderNo(), splitProfitParams.getSplitType());
            return this.baseSplitProfitService.exeWithdrawSplitProfit(splitProfitParams);
        } else if (EnumSplitBusinessType.HSSPROMOTE.getId() == splitProfitParams.getSplitType()) {
            final Optional<Order> orderOptional = this.orderService.getByOrderNo(splitProfitParams.getOrderNo());
            if (!orderOptional.isPresent()) {
                return Pair.of(-1, "交易单不存在");
            }
            log.info("交易单O，执行分润操作, 分润类型[{}]", splitProfitParams.getOrderNo(), splitProfitParams.getSplitType());
            return this.baseSplitProfitService.exeUpgradeSplitProfit(splitProfitParams);
        } else {
            final Optional<Order> orderOptional = this.orderService.getByOrderNo(splitProfitParams.getOrderNo());
            if (!orderOptional.isPresent()) {
                return Pair.of(-1, "交易单不存在");
            }
            log.info("交易单OR结算单[{}]，执行分润操作, 分润类型[{}]", splitProfitParams.getOrderNo(), splitProfitParams.getSplitType());
            return this.baseSplitProfitService.exePaySplitProfit(splitProfitParams);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param refundProfitParams
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public RefundProfitResponse refundProfitImpl(final RefundProfitParams refundProfitParams) {
        final Optional<Order> orderOptional = this.orderService.getByOrderNo(refundProfitParams.getOrderNo());
        final RefundProfitResponse refundProfitResponse = new RefundProfitResponse();
        if (!orderOptional.isPresent()) {
            refundProfitResponse.setTradeOrderNo(refundProfitParams.getOrderNo());
            refundProfitResponse.setCode(EnumBasicStatus.FAIL.getId());
            refundProfitResponse.setMessage("交易单不存在");
            return refundProfitResponse;
        }
        log.info("交易单[{}]，执行退分润操作", refundProfitParams.getOrderNo());
        final Order order = orderOptional.get();
        if (order.isRefundSuccess()) {
            refundProfitResponse.setTradeOrderNo(refundProfitParams.getOrderNo());
            refundProfitResponse.setCode(EnumBasicStatus.FAIL.getId());
            refundProfitResponse.setMessage("已经退款成功，不可以重复退款");
            return refundProfitResponse;
        }
        final Date payDate = DateFormatUtil.parse(DateFormatUtil.format(order.getPaySuccessTime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
        final Date refundDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
        if (order.isSettled() || order.isRefundSuccess() || payDate.compareTo(refundDate) != 0) {
            refundProfitResponse.setTradeOrderNo(refundProfitParams.getOrderNo());
            refundProfitResponse.setCode(EnumBasicStatus.FAIL.getId());
            refundProfitResponse.setMessage("只可以退当日订单");
            return refundProfitResponse;
        }
        if (this.refundOrderService.getUnRefundErrorCountByPayOrderId(order.getId()) > 0) {
            refundProfitResponse.setTradeOrderNo(refundProfitParams.getOrderNo());
            refundProfitResponse.setCode(EnumBasicStatus.FAIL.getId());
            refundProfitResponse.setMessage("当前交易已经存在退款单");
            return refundProfitResponse;
        }
        final RefundOrder refundOrder = new RefundOrder();
        refundOrder.setBatchNo("");
        refundOrder.setAppId(order.getAppId());
        refundOrder.setOrderNo(SnGenerator.generateRefundSn());
        refundOrder.setPayOrderId(order.getId());
        refundOrder.setPayOrderNo(order.getOrderNo());
        refundOrder.setSn("");
        refundOrder.setRefundAccountId(order.getPayee());
        refundOrder.setRefundAmount(order.getRealPayAmount());
        refundOrder.setMerchantRefundAmount(order.getRealPayAmount().subtract(order.getPoundage()));
        refundOrder.setPoundageRefundAmount(order.getPoundage());
        refundOrder.setUpperChannel(EnumPayChannelSign.idOf(order.getPayChannelSign()).getUpperChannel().getId());
        refundOrder.setStatus(EnumRefundOrderStatus.REFUNDING.getId());
        this.refundOrderService.add(refundOrder);
        final Pair<Integer, String> resultPair = this.baseSplitProfitService.exeRefundProfit(refundProfitParams, refundOrder.getId());
        if (0 == resultPair.getLeft()) {
            refundProfitResponse.setTradeOrderNo(refundProfitParams.getOrderNo());
            refundProfitResponse.setRefundOrderNo(refundOrder.getOrderNo());
            refundProfitResponse.setCode(EnumBasicStatus.SUCCESS.getId());
            refundProfitResponse.setMessage("success");
            return refundProfitResponse;
        }
        refundProfitResponse.setTradeOrderNo(refundProfitParams.getOrderNo());
        refundProfitResponse.setRefundOrderNo(refundOrder.getOrderNo());
        refundProfitResponse.setCode(EnumBasicStatus.FAIL.getId());
        refundProfitResponse.setMessage(resultPair.getRight());
        return refundProfitResponse;
    }

    /**
     * {@inheritDoc}
     *
     * @param refundOrderNo
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Pair<Integer, String> refund(final String refundOrderNo) {
        final Optional<RefundOrder> refundOrderOptional = this.refundOrderService.getByOrderNo(refundOrderNo);
        if (refundOrderOptional.get().isRefunding()) {
            final RefundOrder refundOrder = this.refundOrderService.getByIdWithLock(refundOrderOptional.get().getId()).get();
            final Order order = this.orderService.getByOrderNo(refundOrder.getPayOrderNo()).get();
            //请求退款
            final PaymentSdkRefundRequest paymentSdkRefundRequest = new PaymentSdkRefundRequest();
            paymentSdkRefundRequest.setAppId(refundOrder.getAppId());
            paymentSdkRefundRequest.setOrderNo(refundOrder.getPayOrderNo());
            paymentSdkRefundRequest.setRefundOrderNo(refundOrder.getOrderNo());
            paymentSdkRefundRequest.setAmount(refundOrder.getRefundAmount().toPlainString());
            final String content = this.httpClientFacade.jsonPost(PaymentSdkConstants.SDK_PAY_REFUND, SdkSerializeUtil.convertObjToMap(paymentSdkRefundRequest));
            final PaymentSdkRefundResponse paymentSdkRefundResponse = JSON.parseObject(content, PaymentSdkRefundResponse.class);
            final EnumBasicStatus status = EnumBasicStatus.of(paymentSdkRefundResponse.getCode());
            switch (status) {
                case FAIL:
                    log.error("交易[{}],退款[{}], 失败", refundOrder.getPayOrderNo(), refundOrder.getOrderNo());
                    final RefundOrder updateRefundOrder = new RefundOrder();
                    updateRefundOrder.setId(refundOrder.getId());
                    updateRefundOrder.setStatus(EnumRefundOrderStatus.REFUND_FAIL.getId());
                    updateRefundOrder.setRemark("退款失败");
                    updateRefundOrder.setMessage(paymentSdkRefundResponse.getMessage());
                    this.refundOrderService.update(updateRefundOrder);
                    return Pair.of(-1, paymentSdkRefundResponse.getMessage());
                case SUCCESS:
                    this.orderService.updateRefundInfo(order.getId(), refundOrder.getRefundAmount(), EnumOrderRefundStatus.REFUND_SUCCESS);
                    this.orderService.updateRemark(order.getId(), paymentSdkRefundResponse.getMessage());
                    final RefundOrder refundOrder2 = new RefundOrder();
                    refundOrder2.setId(refundOrder.getId());
                    refundOrder2.setStatus(EnumRefundOrderStatus.REFUND_SUCCESS.getId());
                    refundOrder2.setRemark("退款成功");
                    refundOrder2.setMessage(paymentSdkRefundResponse.getMessage());
                    refundOrder2.setFinishTime(DateFormatUtil.parse(paymentSdkRefundResponse.getSuccessTime(), DateFormatUtil.yyyyMMddHHmmss));
                    this.refundOrderService.update(refundOrder2);

                    //TODO

//                    if (EnumPaymentChannel.WECHAT_PAY.getId() == EnumPayChannelSign.idOf(order.getPayChannelSign()).getPaymentChannel().getId()) {
//                        try {
//                            this.sendMsgService.refundSendMessage(order.getOrderNo(), refundOrder.getRefundAmount(), order.getPayAccount());
//                        } catch (final Throwable e) {
//                            log.error("推送失败");
//                        }
//                    }
                    return Pair.of(0, "退款成功");
                default:
                    log.error("退款[{}]， 网关返回状态异常", refundOrder.getId());
                    return Pair.of(-1, "退款网关异常");
            }
        }
        return Pair.of(-1, "退款流水异常");
    }


    /**
     * {@inheritDoc}
     *
     * @param withdrawParams
     * @return
     */
    @Override
    public Pair<Integer, String> withdraw(final WithdrawParams withdrawParams) {
        log.info("业务方[{}],账户[{}],发起提现[{}]", withdrawParams.getAppId(), withdrawParams.getAccountId(), withdrawParams.getWithdrawAmount());
        final long orderId = this.orderService.createWithdrawOrder(withdrawParams);
        return this.baseTradeService.withdrawImpl(withdrawParams, orderId);
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
            settlementRecordOptional = this.settlementRecordService.getById(this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(payOrder.getOrderNo(),
                    payOrder.getPayee(), EnumAccountFlowType.INCREASE.getId()).get().getSettlementRecordId());
        } else {
            settlementRecordOptional = this.settlementRecordService.getBySettleNo(paymentSdkWithdrawCallbackResponse.getOrderNo());
        }

        if (settlementRecordOptional.isPresent()) {//结算单提现
            if (settlementRecordOptional.get().isWithdrawing()) {
                this.baseTradeService.handleWithdrawBySettlementResult(settlementRecordOptional.get().getId(), paymentSdkWithdrawCallbackResponse);
            }
        } else {//余额提现
            final Optional<Order> orderOptional = this.orderService.getByOrderNo(paymentSdkWithdrawCallbackResponse.getOrderNo());
            if (orderOptional.get().isWithDrawing()) {
                final Order order = this.orderService.getByIdWithLock(orderOptional.get().getId()).get();
                this.baseTradeService.handleWithdrawByAccountResult(order, paymentSdkWithdrawCallbackResponse);
            }
        }
    }

}
