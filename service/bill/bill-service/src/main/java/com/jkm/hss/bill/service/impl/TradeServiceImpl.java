package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.PaymentSdkPlaceOrderResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.*;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.TradeService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.BasicChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/2.
 */
@Slf4j
@Service
public class TradeServiceImpl implements TradeService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private BaseTradeService baseTradeService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private BaseSplitProfitService baseSplitProfitService;

    /**
     * {@inheritDoc}
     *
     * @param rechargeParams
     * @return
     */
    @Override
    public Pair<Integer, String> recharge(final RechargeParams rechargeParams) {
        log.info("业务方[{}],通过渠道[{}]进行充值[{}],实付金额[{}]，充值账户[{}]，收款账户[{}], 会员标识[{}], 商户号[{}]",
                rechargeParams.getAppId(), rechargeParams.getChannel(), rechargeParams.getTradeAmount(), rechargeParams.getRealPayAmount(),
                rechargeParams.getMemberAccountId(), rechargeParams.getPayeeAccountId(), rechargeParams.getMemberId(), rechargeParams.getMerchantNo());
        //TODO
        final String channelCode = this.basicChannelService.selectCodeByChannelSign(rechargeParams.getChannel(), rechargeParams.getMerchantPayType());
        final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(rechargeParams.getChannel());
        final Order order = new Order();
        order.setBusinessOrderNo(rechargeParams.getBusinessOrderNo());
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.RECHARGE.getId()));
        order.setTradeAmount(rechargeParams.getTradeAmount());
        order.setRealPayAmount(rechargeParams.getRealPayAmount());
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
    public Pair<Integer, String> pay(final PayParams payParams) {
        log.info("业务方[{}],通过渠道[{}]进行支付[{}],实付金额[{}]", payParams.getAppId(), payParams.getChannel(), payParams.getTradeAmount(), payParams.getRealPayAmount());
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
        order.setServiceType(EnumServiceType.RECEIVE_MONEY.getId());
        order.setPayer(0);
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
    @Transactional
    public Pair<Integer, String> splitProfitImpl(final SplitProfitParams splitProfitParams) {
        Preconditions.checkState(splitProfitParams.getSplitProfitDetails().size() > 0);
        final Optional<Order> orderOptional = this.orderService.getByOrderNo(splitProfitParams.getOrderNo());
        if (!orderOptional.isPresent()) {
            return Pair.of(-1, "交易单不存在");
        }
        log.info("交易单[{}]，执行分润", splitProfitParams.getOrderNo());

        return this.baseSplitProfitService.exePaySplitAccount(splitProfitParams);
    }

    @Override
    public Pair<Integer, String> withdraw() {
        return null;
    }
}
