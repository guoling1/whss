package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.hss.account.entity.MemberAccount;
import com.jkm.hss.account.sevice.MemberAccountService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.PaymentSdkPlaceOrderRequest;
import com.jkm.hss.bill.entity.PaymentSdkPlaceOrderResponse;
import com.jkm.hss.bill.enums.EnumBasicStatus;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.PlaceOrderParams;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.product.enums.EnumMerchantPayType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yulong.zhang on 2017/5/3.
 */
@Slf4j
@Service
public class BaseTradeServiceImpl implements BaseTradeService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MemberAccountService memberAccountService;
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
     * @param receiptMemberMoneyAccountId  商户收会员款账户id
     * @param order 此时，order对象中的payer是会员账户id;payee是商户基础账户的id
     */
    @Override
    @Transactional
    public Pair<Integer, String> memberPayImpl(final long receiptMemberMoneyAccountId, final Order order) {
        final MemberAccount memberAccount = this.memberAccountService.getByIdWithLock(order.getPayer()).get();
        if (memberAccount.getAvailable().compareTo(order.getRealPayAmount()) < 0) {
            return Pair.of(-1, "金额不足");
        }
        final MemberAccount updateMemberAccount = new MemberAccount();
        updateMemberAccount.setId(order.getPayer());


        return null;

    }
}
