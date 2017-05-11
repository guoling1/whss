package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.hss.account.entity.*;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.PaymentSdkPlaceOrderRequest;
import com.jkm.hss.bill.entity.PaymentSdkPlaceOrderResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.enums.EnumBasicStatus;
import com.jkm.hss.bill.enums.EnumOrderStatus;
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
    private MemberAccountService memberAccountService;
    @Autowired
    private MemberAccountFlowService memberAccountFlowService;
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
     * @param receiptMemberMoneyAccountId  商户收会员款账户id
     * @param orderId 此时，order对象中的payer是会员账户id;payee是商户基础账户的id
     */
    @Override
    @Transactional
    public Pair<Integer, String> memberPayImpl(final long receiptMemberMoneyAccountId, final long orderId) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        if (order.isDuePay()) {
            final MemberAccount memberAccount = this.memberAccountService.getByIdWithLock(order.getPayer()).get();
            final ReceiptMemberMoneyAccount receiptMemberMoneyAccount = this.receiptMemberMoneyAccountService.getByIdWithLock(receiptMemberMoneyAccountId).get();
            if (memberAccount.getAvailable().compareTo(order.getRealPayAmount()) < 0) {
                return Pair.of(-1, "金额不足");
            }
            //会员账户额度减少
            final MemberAccount updateMemberAccount = new MemberAccount();
            updateMemberAccount.setId(order.getPayer());
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
            receiptMemberMoneyAccountFlow.setAccountId(receiptMemberMoneyAccountId);
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
            this.orderService.update(updateOrder);
            return Pair.of(0, "支付成功");
        }
        return Pair.of(-1, "交易状态异常");
    }

    @Override
    public void handlePayOrRechargeCallbackMsgImpl(PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, long orderId) {

    }
}
