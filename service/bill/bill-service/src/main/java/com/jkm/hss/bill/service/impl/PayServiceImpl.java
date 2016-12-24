package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.spring.http.client.HttpClientFacade;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.*;
import com.jkm.hss.account.enums.EnumUnfrozenType;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.entity.callback.PayCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HttpClientFacade httpClientFacade;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private FrozenRecordService frozenRecordService;
    @Autowired
    private UnfrozenRecordService unfrozenRecordService;
    @Autowired
    private DealerService dealerService;
    /**
     * {@inheritDoc}
     *
     * @param totalAmount
     * @param channel  通道
     * @param merchantId
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> codeReceipt(final String totalAmount, final int channel, final long merchantId) {
        log.info("代理商[{}] 通过动态扫码， 支付一笔资金[{}]", merchantId, totalAmount);
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();

        final Order order = new Order();
        order.setOrderNo(SnGenerator.generate());
        order.setTradeAmount(new BigDecimal(totalAmount));
        order.setRealPayAmount(new BigDecimal(totalAmount));
        order.setTradeType(EnumTradeType.PAY.getId());
        order.setPayer(0);
        order.setPayee(merchantId);
        if (EnumPayChannelSign.YG_YINLIAN.getId() == channel) {
            order.setPayAccount(MerchantSupport.decryptBankCard(merchant.getBankNo()));
        }
        //TODO 手续费， 费率

        order.setGoodsName(merchant.getMerchantName());
        order.setGoodsDescribe(merchant.getMerchantName());
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleTime(new Date());
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        this.orderService.add(order);
        //请求支付中心下单
        final PaymentSdkPlaceOrderResponse placeOrderResponse = this.requestPlaceOrder(order, channel, merchant);
        final EnumBasicStatus enumBasicStatus = EnumBasicStatus.of(placeOrderResponse.getCode());
        final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
        Preconditions.checkState(orderOptional.get().isDuePay());
        switch (enumBasicStatus) {
            case SUCCESS:
                order.setRemark(placeOrderResponse.getMessage());
                this.orderService.update(order);
                return Pair.of(0, placeOrderResponse.getPayUrl());
            case FAIL:
                order.setRemark(placeOrderResponse.getMessage());
                this.orderService.update(order);
                return Pair.of(-1, order.getRemark());
        }
        return Pair.of(-1, "下单失败");
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param tradePeried 结算周期
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> merchantWithdraw(final long merchantId, final String tradePeried) {
        log.info("商户[{}]，进行提现", merchantId);
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        final Account account = this.accountService.getByIdWithLock(merchant.getId()).get();
        if (account.getAvailable().compareTo(new BigDecimal("2.00")) < 0) {
            log.info("商户[{}]账户余额不足2元", merchant.getId());
            return Pair.of(-1, "余额不足");
        }
        final Order order = new Order();
        order.setOrderNo(SnGenerator.generate());
        order.setTradeAmount(account.getAvailable());
        order.setTradeType(EnumTradeType.WITHDRAW.getId());
        order.setPayer(merchantId);
        order.setPayee(0);
        order.setPayAccount(tradePeried);
        //TODO 手续费， 费率

        order.setPoundage(new BigDecimal(""));

        order.setGoodsName(merchant.getMerchantName());
        order.setGoodsDescribe(merchant.getMerchantName());

        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleTime(new Date());
        order.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.orderService.add(order);
        //冻结金额
        this.accountService.decreaseAvailableAmount(account.getId(), account.getAvailable());
        this.accountService.increaseFrozenAmount(account.getId(), account.getAvailable());
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setBusinessNo(order.getOrderNo());
        frozenRecord.setFrozenAmount(account.getAvailable());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(account.getId());
        accountFlow.setOrderNo(order.getOrderNo());
        accountFlow.setBeforeAmount(account.getAvailable());
        accountFlow.setAfterAmount(account.getAvailable().subtract(order.getTradeAmount()));
        accountFlow.setOutAmount(order.getTradeAmount());
        accountFlow.setChangeTime(new Date());
        accountFlow.setRemark("提现");
        this.accountFlowService.add(accountFlow);

        final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
        paymentSdkDaiFuRequest.setAppId(PaymentSdkConstants.APP_ID);
        paymentSdkDaiFuRequest.setOrderNo(order.getOrderNo());
        paymentSdkDaiFuRequest.setTotalAmount(order.getTradeAmount().subtract(order.getPoundage()).toPlainString());
        paymentSdkDaiFuRequest.setTradeType(tradePeried);
        paymentSdkDaiFuRequest.setIsCompany("0");
        paymentSdkDaiFuRequest.setMobile(MerchantSupport.decryptMobile(merchant.getMobile()));
        paymentSdkDaiFuRequest.setBankName(merchant.getBankName());
        paymentSdkDaiFuRequest.setAccountName(merchant.getName());
        paymentSdkDaiFuRequest.setAccountNumber(MerchantSupport.decryptBankCard(merchant.getBankNo()));
        paymentSdkDaiFuRequest.setIdCard(MerchantSupport.decryptIdentity(merchant.getIdentity()));
        paymentSdkDaiFuRequest.setPlayMoneyChannel(EnumPlayMoneyChannel.SAOMI.getId());
        paymentSdkDaiFuRequest.setNote(merchant.getMerchantName());
        paymentSdkDaiFuRequest.setSystemCode(PaymentSdkConstants.APP_ID);
        paymentSdkDaiFuRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL);

        //请求三方
        final String content = this.requestImpl(PaymentSdkConstants.SDK_PAY_WITHDRAW, SdkSerializeUtil.convertObjToMap(paymentSdkDaiFuRequest));
        final PaymentSdkDaiFuResponse response = JSON.parseObject(content, PaymentSdkDaiFuResponse.class);


        return null;
    }

    /**
     * 处理提现结果
     *
     * @param orderId
     * @param accountId
     * @param response
     * @return
     */
    private Pair<Integer, String> handleWithdrawResult(final long orderId, final long accountId,
                                                       final PaymentSdkDaiFuResponse response) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        if (order.isWithDrawing()) {
            final EnumBasicStatus status = EnumBasicStatus.of(response.getCode());
            switch (status) {
                case SUCCESS:
                this.markWithdrawSuccess(orderId, accountId, response);
                    return Pair.of(0, "提现成功");
                case FAIL:
                    order.setRemark(response.getMessage());
                    this.orderService.update(order);
                    log.info("交易订单[{}]，提现暂未成功", order.getOrderNo());
                    break;
                default:
                    order.setRemark(response.getMessage());
                    this.orderService.update(order);
                    log.info("交易订单[{}]，提现暂未成功", order.getOrderNo());
                    break;
            }
        }
        return Pair.of(-1, "提现中");
    }

    private void markWithdrawSuccess(final long orderId, final long accountId,
                                     final PaymentSdkDaiFuResponse response) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        final Account account = this.accountService.getByIdWithLock(accountId).get();
        order.setStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getId());
        order.setRemark(response.getMessage());
        this.orderService.update(order);
        final FrozenRecord frozenRecord = this.frozenRecordService.getByBusinessNo(response.getOrderNo()).get();
        //解冻金额
        final UnfrozenRecord unfrozenRecord = new UnfrozenRecord();
        unfrozenRecord.setAccountId(account.getId());
        unfrozenRecord.setFrozenRecordId(frozenRecord.getId());
        unfrozenRecord.setBusinessNo(order.getOrderNo());
        unfrozenRecord.setUnfrozenType(EnumUnfrozenType.CONSUME.getId());
        unfrozenRecord.setUnfrozenAmount(frozenRecord.getFrozenAmount());
        unfrozenRecord.setUnfrozenTime(new Date());
        frozenRecord.setRemark("提现成功");
        this.unfrozenRecordService.add(unfrozenRecord);
        //减少总金额
        this.accountService.decreaseTotalAmount(accountId, frozenRecord.getFrozenAmount());
        //结算
        //TODO

    }

    /**
     * {@inheritDoc}
     *
     * @param payCallbackResponse
     */
    @Override
    @Transactional
    public void handlePayCallbackMsg(final PayCallbackResponse payCallbackResponse) {
        final String orderNo = payCallbackResponse.getOrderNo();
        final Optional<Order> orderOptional = this.orderService.getByOrderNo(orderNo);
        Preconditions.checkState(!orderOptional.isPresent(), "交易订单[{}]不存在", orderNo);
        if (orderOptional.get().isDuePay()) {
            final Order order = this.orderService.getByIdWithLock(orderOptional.get().getId()).get();
            if (order.isDuePay()) {
                this.handlePayCallbackMsgImpl(payCallbackResponse, order);
            }
        }
    }

    /**
     * 支付结果回调实现
     *
     * @param payCallbackResponse
     * @param order
     */
    private void handlePayCallbackMsgImpl(final PayCallbackResponse payCallbackResponse, final Order order) {
        final EnumBasicStatus status = EnumBasicStatus.of(payCallbackResponse.getStatus());
        switch (status) {
            case SUCCESS:
                this.markPaySuccess(payCallbackResponse, order);
                break;
            case FAIL:
                this.markPayFail(payCallbackResponse, order);
                break;
            default:
                this.markPayFail(payCallbackResponse, order);
                break;
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param payCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void markPaySuccess(final PayCallbackResponse payCallbackResponse, final Order order) {
        order.setPayType(payCallbackResponse.getPayType());
        order.setPaySuccessTime(new DateTime(Long.valueOf(payCallbackResponse.getPaySuccessTime())).toDate());
        order.setStatus(EnumOrderStatus.PAY_SUCCESS.getId());
        this.orderService.update(order);
        //商户入账
        final MerchantInfo merchant = this.merchantInfoService.selectById(order.getPayee()).get();
        this.merchantRecorded(order.getId(), merchant);
        //商户结算
        this.merchantSettle(order.getId(), merchant.getId());
        //手续费结算到代理商
        this.poundageSettle(order.getId(), merchant.getId());

    }

    /**
     * {@inheritDoc}
     *
     * @param payCallbackResponse
     * @param order
     */
    @Override
    @Transactional
    public void markPayFail(final PayCallbackResponse payCallbackResponse, final Order order) {
        order.setStatus(EnumOrderStatus.PAY_FAIL.getId());
        order.setRemark(payCallbackResponse.getMessage());
        this.orderService.update(order);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param merchant
     */
    @Override
    @Transactional
    public void merchantRecorded(final long orderId, final MerchantInfo merchant) {
        log.info("交易订单号[{}], 进行入账操作");
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        Preconditions.checkState(order.isPaySuccess());
        //TODO

        /**
         * 调用接口，获得入账金额
         *
         * 1. 代理商待结算余额增加
         * 2. 手续费账户余额增加
         */
        final long accountId1 = 0;
        final long accountId2 = 0;
        final Account account = this.accountService.getByIdWithLock(accountId1).get();
        this.accountService.increaseTotalAmount(accountId1, new BigDecimal("0"));
        this.accountService.increaseSettleAmount(accountId1, new BigDecimal("0"));
        final SettleAccountFlow merchantSettleAccountFlow = new SettleAccountFlow();
        merchantSettleAccountFlow.setAccountId(accountId1);
        merchantSettleAccountFlow.setOrderNo(order.getOrderNo());
        merchantSettleAccountFlow.setBeforeAmount(account.getDueSettleAmount());
        merchantSettleAccountFlow.setIncomeAmount(new BigDecimal("0"));
        merchantSettleAccountFlow.setAfterAmount(account.getDueSettleAmount().add(new BigDecimal("0")));
        merchantSettleAccountFlow.setChangeTime(new Date());
        merchantSettleAccountFlow.setRemark("商户待结算增加");
        this.settleAccountFlowService.add(merchantSettleAccountFlow);

        this.accountService.increaseTotalAmount(accountId2, new BigDecimal("0"));
        this.accountService.increaseSettleAmount(accountId2, new BigDecimal("0"));
        //TODO 手续费 需要放入待结算金额 ？

        final SettleAccountFlow poundageSettleAccountFlow = new SettleAccountFlow();
        poundageSettleAccountFlow.setAccountId(accountId1);
        poundageSettleAccountFlow.setOrderNo(order.getOrderNo());
        poundageSettleAccountFlow.setBeforeAmount(account.getDueSettleAmount());
        poundageSettleAccountFlow.setIncomeAmount(new BigDecimal("0"));
        poundageSettleAccountFlow.setAfterAmount(account.getDueSettleAmount().add(new BigDecimal("0")));
        poundageSettleAccountFlow.setChangeTime(new Date());
        poundageSettleAccountFlow.setRemark("商户待结算增加");
        this.settleAccountFlowService.add(poundageSettleAccountFlow);


    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param merchantId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void merchantSettle(final long orderId, final long merchantId) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        log.info("交易订单号[{}], 进行结算操作", order.getOrderNo());
        //商户结算
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        final Account account = this.accountService.getByIdWithLock(merchant.getId()).get();
        final SettleAccountFlow inSettleAccountFlow = this.settleAccountFlowService.getByOrderNo(order.getOrderNo()).get();
        //待结算金额减少
        Preconditions.checkState(order.getPoundage().compareTo(account.getDueSettleAmount()) <= 0);
        Preconditions.checkState(order.getPoundage().compareTo(inSettleAccountFlow.getIncomeAmount()) == 0);
        final SettleAccountFlow merchantSettleAccountFlow = new SettleAccountFlow();
        merchantSettleAccountFlow.setAccountId(account.getId());
        merchantSettleAccountFlow.setOrderNo(order.getOrderNo());
        merchantSettleAccountFlow.setBeforeAmount(account.getDueSettleAmount());
        merchantSettleAccountFlow.setOutAmount(inSettleAccountFlow.getIncomeAmount());
        merchantSettleAccountFlow.setAfterAmount(account.getDueSettleAmount().subtract(inSettleAccountFlow.getIncomeAmount()));
        merchantSettleAccountFlow.setChangeTime(new Date());
        merchantSettleAccountFlow.setRemark("商户待结算减少");
        this.settleAccountFlowService.add(merchantSettleAccountFlow);
        this.accountService.increaseAvailableAmount(account.getId(), inSettleAccountFlow.getIncomeAmount());
        this.accountService.decreaseSettleAmount(account.getId(), inSettleAccountFlow.getIncomeAmount());
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(merchant.getAccountId());
        accountFlow.setOrderNo(order.getOrderNo());
        accountFlow.setBeforeAmount(account.getAvailable());
        accountFlow.setIncomeAmount(inSettleAccountFlow.getIncomeAmount());
        accountFlow.setAfterAmount(account.getAvailable().add(inSettleAccountFlow.getIncomeAmount()));
        accountFlow.setChangeTime(new Date());
        accountFlow.setRemark(order.getGoodsName());
        this.accountFlowService.add(accountFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param merchantId
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void poundageSettle(final long orderId, final long merchantId) {
        //查询分润接口，获取信息
        //循环放入到代理商待结算余额，并创建流水记录

        //然后直接结算到余额， 并创建流水记录
    }



    /**
     * 请求支付中心下单
     *
     *  交易类型   JSAPI，NATIVE，APP，WAP,EPOS
     *
     * @param order
     * @param merchant
     */
    private PaymentSdkPlaceOrderResponse requestPlaceOrder(final Order order, final int channel, final MerchantInfo merchant) {
        final PaymentSdkPlaceOrderRequest placeOrderRequest = new PaymentSdkPlaceOrderRequest();
        placeOrderRequest.setAppId(PaymentSdkConstants.APP_ID);
        placeOrderRequest.setOrderNo(order.getOrderNo());
        placeOrderRequest.setGoodsDescribe(order.getGoodsDescribe());
        placeOrderRequest.setReturnUrl(PaymentSdkConstants.SDK_PAY_RETURN_URL + order.getTradeAmount() + "/" + order.getOrderNo());
        placeOrderRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_NOTIFY_URL);
        placeOrderRequest.setMerName(merchant.getMerchantName());
        //TODO
        placeOrderRequest.setMerNo(merchant.getId() + "");
        placeOrderRequest.setTotalAmount(order.getTradeAmount().toPlainString());
        if (EnumPayChannelSign.YG_WEIXIN.getId() == channel
                || EnumPayChannelSign.YG_ZHIFUBAO.getId() == channel) {
            placeOrderRequest.setTradeType("JSAPI");
        } else if (EnumPayChannelSign.YG_YINLIAN.getId() == channel) {
            placeOrderRequest.setTradeType("EPOS");
        }
        final String content = this.requestImpl(PaymentSdkConstants.SDK_PAY_PLACE_ORDER, SdkSerializeUtil.convertObjToMap(placeOrderRequest));
        return JSON.parseObject(content, PaymentSdkPlaceOrderResponse.class);

    }

    /**
     * 发送http请求
     *
     * @param url
     * @param paramMap
     * @return
     */
    private String requestImpl(String url, Map<String, String> paramMap) {
        try {
            final String responseContent = this.httpClientFacade.post(url, paramMap);
            return responseContent;
        } catch (final Throwable e) {
            log.error("url[{}], param[{}]请求支付中心异常", url, paramMap);
            throw e;
        }
    }
}
