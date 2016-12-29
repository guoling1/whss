package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.spring.http.client.HttpClientFacade;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.*;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.bill.service.WithdrawService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.SendMsgService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DecimalFormat;
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
    private CalculateService calculateService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SendMsgService sendMsgService;
    @Autowired
    private SplitAccountRecordService splitAccountRecordService;
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
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.PAY.getId()));
        order.setTradeAmount(new BigDecimal(totalAmount));
        order.setRealPayAmount(new BigDecimal(totalAmount));
        order.setTradeType(EnumTradeType.PAY.getId());
        order.setPayChannelSign(channel);
        order.setPayer(0);
        order.setPayee(merchantId);
        if (EnumPayChannelSign.YG_YINLIAN.getId() == channel) {
            order.setPayAccount(MerchantSupport.decryptBankCard(merchant.getBankNo()));
        }
        //手续费， 费率
        final BigDecimal merchantPayPoundageRate = this.calculateService.getMerchantPayPoundageRate(merchantId, channel);
        final BigDecimal merchantPayPoundage = this.calculateService.getMerchantPayPoundage(order.getTradeAmount(), merchantPayPoundageRate);
        order.setPoundage(merchantPayPoundage);
        order.setPayRate(merchantPayPoundageRate);
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
     * @param paymentSdkPayCallbackResponse
     */
    @Override
    @Transactional
    public void handlePayCallbackMsg(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse) {
        final String orderNo = paymentSdkPayCallbackResponse.getOrderNo();
        final Optional<Order> orderOptional = this.orderService.getByOrderNoAndTradeType(orderNo, EnumTradeType.PAY.getId());
        Preconditions.checkState(orderOptional.isPresent(), "交易订单[{}]不存在", orderNo);
        if (orderOptional.get().isDuePay()) {
            final Order order = this.orderService.getByIdWithLock(orderOptional.get().getId()).get();
            if (order.isDuePay()) {
                this.handlePayCallbackMsgImpl(paymentSdkPayCallbackResponse, order);
            }
        }
    }

    /**
     * 支付结果回调实现
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    private void handlePayCallbackMsgImpl(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        final EnumBasicStatus status = EnumBasicStatus.of(paymentSdkPayCallbackResponse.getStatus());
        switch (status) {
            case SUCCESS:
                this.markPaySuccess(paymentSdkPayCallbackResponse, order);
                break;
            case FAIL:
                this.markPayFail(paymentSdkPayCallbackResponse, order);
                break;
            case HANDLING:
                this.markPayHandling(paymentSdkPayCallbackResponse, order);
                break;
            default:
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
        order.setPayType(paymentSdkPayCallbackResponse.getPayType());
        order.setPaySuccessTime(new DateTime(Long.valueOf(paymentSdkPayCallbackResponse.getPaySuccessTime())).toDate());
        order.setRemark(StringUtils.isEmpty(paymentSdkPayCallbackResponse.getMessage()) ? "success" : paymentSdkPayCallbackResponse.getMessage());
        order.setStatus(EnumOrderStatus.PAY_SUCCESS.getId());
        this.orderService.update(order);
        //商户入账
        final MerchantInfo merchant = this.merchantInfoService.selectById(order.getPayee()).get();
        this.merchantRecorded(order.getId(), merchant);
        //商户结算
        final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
        if (orderOptional.get().isPaySuccess() && (orderOptional.get().isDueSettle() || orderOptional.get().isSettleing())) {
            log.info("交易订单号[{}], 进行结算操作", order.getOrderNo());
            //将交易单标记为结算中
            this.orderService.updateSettleStatus(orderOptional.get().getId(), EnumSettleStatus.SETTLE_ING.getId());
            this.merchantSettle(orderOptional.get(), merchant.getId());
            //手续费结算到代理商等，再到 可用余额
            this.poundageSettle(orderOptional.get(), merchant.getId());
            //结算完毕
            this.orderService.updateSettleStatus(orderOptional.get().getId(), EnumSettleStatus.SETTLED.getId());

        }
        //通知商户
        Optional<UserInfo> ui = userInfoService.selectByMerchantId(merchant.getId());
        log.info("商户号[{}], 交易点单号[{}]支付完成，开始通知商户", merchant.getId(), order.getOrderNo());
        this.sendMsgService.sendMessage(order.getTradeAmount().toPlainString(), order.getOrderNo(), paymentSdkPayCallbackResponse.getSn(),
                merchant.getMerchantName(),  merchant.getMerchantName(), ui.get().getOpenId());

        //商户提现(发消息)
        final JSONObject requestJsonObject = new JSONObject();
        requestJsonObject.put("merchantId", merchant.getId());
        requestJsonObject.put("payOrderId", order.getId());
        requestJsonObject.put("balanceAccountType", EnumBalanceTimeType.D0.getType());
        MqProducer.produce(requestJsonObject, MqConfig.MERCHANT_WITHDRAW, 100);
//        this.withdrawService.merchantWithdraw(merchant.getId(), order.getId(), EnumBalanceTimeType.D0.getType());
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
        order.setStatus(EnumOrderStatus.PAY_FAIL.getId());
        order.setRemark(paymentSdkPayCallbackResponse.getMessage());
        this.orderService.update(order);
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkPayCallbackResponse
     * @param order
     */
    @Override
    public void markPayHandling(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, final Order order) {
        order.setRemark(paymentSdkPayCallbackResponse.getMessage());
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
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        log.info("交易订单号[{}], 进行入账操作", order.getOrderNo());
        if (order.isPaySuccess() && order.isDueSettle()) {
            //商户账户
            final Account merchantAccount = this.accountService.getByIdWithLock(merchant.getAccountId()).get();
            //商户待结算增加
            this.accountService.increaseTotalAmount(merchantAccount.getId(), order.getTradeAmount().subtract(order.getPoundage()));
            this.accountService.increaseSettleAmount(merchantAccount.getId(), order.getTradeAmount().subtract(order.getPoundage()));
            this.settleAccountFlowService.addSettleAccountFlow(merchantAccount.getId(), order.getOrderNo(),
                    order.getTradeAmount().subtract(order.getPoundage()), "支付", EnumAccountFlowType.INCREASE);

            //手续费账户
            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDATE_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
            this.accountService.increaseSettleAmount(poundageAccount.getId(), order.getPoundage());
            this.settleAccountFlowService.addSettleAccountFlow(poundageAccount.getId(), order.getOrderNo(),
                    order.getPoundage(), "支付分润", EnumAccountFlowType.INCREASE);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     * @param merchantId
     */
    @Override
    @Transactional
    public void merchantSettle(final Order order, final long merchantId) {
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        //商户结算
        final Optional<SettleAccountFlow> optional = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(order.getOrderNo(),
                merchant.getAccountId(), EnumAccountFlowType.DECREASE.getId());
        if (!optional.isPresent() && !order.isSettled()) {
            final Account merchantAccount = this.accountService.getByIdWithLock(merchant.getAccountId()).get();
            final SettleAccountFlow merchantIncreaseSettleAccountFlow =
                    this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(order.getOrderNo(), merchant.getAccountId(),
                            EnumAccountFlowType.INCREASE.getId()).get();
            //待结算金额减少
            Preconditions.checkState(order.getTradeAmount().subtract(order.getPoundage()).compareTo(merchantAccount.getDueSettleAmount()) <= 0);
            Preconditions.checkState(order.getTradeAmount().subtract(order.getPoundage()).compareTo(merchantIncreaseSettleAccountFlow.getIncomeAmount()) == 0);
            this.accountService.increaseAvailableAmount(merchantAccount.getId(), merchantIncreaseSettleAccountFlow.getIncomeAmount());
            this.accountService.decreaseSettleAmount(merchantAccount.getId(), merchantIncreaseSettleAccountFlow.getIncomeAmount());
            this.settleAccountFlowService.addSettleAccountFlow(merchantAccount.getId(), order.getOrderNo(), merchantIncreaseSettleAccountFlow.getIncomeAmount(),
                    "支付", EnumAccountFlowType.DECREASE);
            //可用余额流水增加
            this.accountFlowService.addAccountFlow(merchantAccount.getId(), order.getOrderNo(), merchantIncreaseSettleAccountFlow.getIncomeAmount(),
                    "支付", EnumAccountFlowType.INCREASE);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     * @param merchantId
     */
    @Override
    @Transactional
    public void poundageSettle(final Order order, final long merchantId) {
        final Map<String, Triple<Long, BigDecimal, BigDecimal>> shallProfitMap = this.dealerService.shallProfit(order.getOrderNo(),
                order.getTradeAmount(), order.getPayChannelSign(), merchantId);
        final Triple<Long, BigDecimal, BigDecimal> channelMoneyTriple = shallProfitMap.get("channelMoney");
        final Triple<Long, BigDecimal, BigDecimal> productMoneyTriple = shallProfitMap.get("productMoney");
        final Triple<Long, BigDecimal, BigDecimal> firstMoneyTriple = shallProfitMap.get("firstMoney");
        final Triple<Long, BigDecimal, BigDecimal> secondMoneyTriple = shallProfitMap.get("secondMoney");
        final BigDecimal channelMoney = null == channelMoneyTriple ? new BigDecimal("0") : channelMoneyTriple.getMiddle();
        final BigDecimal productMoney = null == productMoneyTriple ? new BigDecimal("0") : productMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0") : secondMoneyTriple.getMiddle();
        Preconditions.checkState(order.getPoundage().compareTo(channelMoney.add(productMoney).add(firstMoney).add(secondMoney)) >= 0);
        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDATE_ACCOUNT_ID).get();
        Preconditions.checkState(order.getPoundage().compareTo(poundageAccount.getDueSettleAmount()) <= 0);
        //待结算--可用余额
        this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseSettleAmount(poundageAccount.getId(), order.getPoundage());
        this.settleAccountFlowService.addSettleAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "支付分润", EnumAccountFlowType.DECREASE);
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "支付分润", EnumAccountFlowType.INCREASE);

        //分账
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), order.getPoundage());
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "支付分润", EnumAccountFlowType.DECREASE);
        //增加分账记录
        //通道利润--到结算--到可用余额
        if (null != channelMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), channelMoneyTriple, "通道账户", EnumTradeType.PAY.getValue());
            final Account account = this.accountService.getById(channelMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE);
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
        }
        //产品利润--到结算--可用余额
        if (null != productMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), productMoneyTriple, "产品账户", EnumTradeType.PAY.getValue());
            final Account account = this.accountService.getById(productMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), productMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), productMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE);
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
        }
        //一级代理商利润--到结算--可用余额
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), firstMoneyTriple, dealer.getProxyName(), EnumTradeType.PAY.getValue());
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE);
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
        }
        //二级代理商利润--到结算--可用余额
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), secondMoneyTriple, dealer.getProxyName(), EnumTradeType.PAY.getValue());
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE);
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
        }
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
        placeOrderRequest.setReturnUrl(PaymentSdkConstants.SDK_PAY_RETURN_URL + order.getTradeAmount() + "/" + order.getId());
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

        final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_PLACE_ORDER, SdkSerializeUtil.convertObjToMap(placeOrderRequest));
        return JSON.parseObject(content, PaymentSdkPlaceOrderResponse.class);
    }
}
