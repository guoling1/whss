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
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.enums.EnumSplitBusinessType;
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

import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.MerchantPromoteShallService;
import com.jkm.hss.merchant.service.SendMsgService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradeRecommendRulesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private CalculateService calculateService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UpgradeRecommendRulesService upgradeRecommendRulesService;
    @Autowired
    private MerchantPromoteShallService merchantPromoteShallService;
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
     * @param merchantId
     * @param businessOrderNo 业务订单号
     * @param amount
     * @param businessReturnUrl 业务方回调url
     * @return
     */
    @Override
    public Pair<Integer, String> generateMerchantUpgradeUrl(final long merchantId, final String businessOrderNo,
                                                            final BigDecimal amount, final String businessReturnUrl) {

        final Optional<Order> orderOptional = this.orderService.getByBusinessOrderNo(businessOrderNo);
        if (orderOptional.isPresent()) {
            final Order order = orderOptional.get();
            if (order.isPaySuccess()) {
                return Pair.of(-1, "该订单号已经支付成功");
            }
            return Pair.of(-1, "订单号重复");
        }
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();

        final Order order = new Order();
        order.setBusinessOrderNo(businessOrderNo);
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.PAY.getId()));
        order.setTradeAmount(amount);
        order.setRealPayAmount(amount);
        order.setAppId(EnumAppType.HSS.getId());
        order.setTradeType(EnumTradeType.PAY.getId());
        order.setServiceType(EnumServiceType.APPRECIATION_PAY.getId());
        order.setPayChannelSign(EnumPayChannelSign.YG_WECHAT.getId());
        order.setPayer(merchant.getAccountId());
        order.setPayee(AccountConstants.JKM_ACCOUNT_ID);
        order.setGoodsName(merchant.getMerchantName());
        order.setGoodsDescribe(merchant.getMerchantName());
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleTime(new Date());
        order.setSettleType(EnumBalanceTimeType.D0.getType());
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        this.orderService.add(order);
        //请求支付中心下单
        final PaymentSdkPlaceOrderResponse placeOrderResponse = this.requestPlaceOrder(order,
                EnumPayChannelSign.YG_WECHAT.getId(), merchant, businessReturnUrl);
        return this.handlePlaceOrder(placeOrderResponse, order);
    }

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
    public Pair<Integer, String> codeReceipt(final String totalAmount, final int channel, final long merchantId, final String appId) {
        log.info("商户[{}] 通过动态扫码， 支付一笔资金[{}]", merchantId, totalAmount);
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();

        final Order order = new Order();
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.PAY.getId()));
        order.setTradeAmount(new BigDecimal(totalAmount));
        order.setRealPayAmount(new BigDecimal(totalAmount));
        order.setAppId(appId);
        order.setTradeType(EnumTradeType.PAY.getId());
        order.setServiceType(EnumServiceType.RECEIVE_MONEY.getId());
        order.setPayer(0);
        order.setPayee(merchant.getAccountId());
        order.setGoodsName(merchant.getMerchantName());
        order.setGoodsDescribe(merchant.getMerchantName());
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleTime(new Date());
        order.setSettleType(EnumBalanceTimeType.D0.getType());
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        this.orderService.add(order);
        //请求支付中心下单
        final PaymentSdkPlaceOrderResponse placeOrderResponse = this.requestPlaceOrder(order, channel, merchant,
                PaymentSdkConstants.SDK_PAY_RETURN_URL + order.getTradeAmount() + "/" + order.getId());
        return this.handlePlaceOrder(placeOrderResponse, order);
    }

    private Pair<Integer, String> handlePlaceOrder(final PaymentSdkPlaceOrderResponse placeOrderResponse, final Order order) {
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
        order.setRemark(paymentSdkPayCallbackResponse.getMessage());
        order.setSn(paymentSdkPayCallbackResponse.getSn());
        order.setStatus(EnumOrderStatus.PAY_SUCCESS.getId());
        final EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.codeOf(order.getPayType());
        order.setPayChannelSign(enumPayChannelSign.getId());
        log.info("返回的通道是[{}]", order.getPayType());
        //处理商户升级的支付单(此时商户自己付款给金开门)
        if (order.getPayer() > 0 && order.getPayee() == AccountConstants.JKM_ACCOUNT_ID) {
            log.info("交易订单[{}]，处理商户升级支付回调业务", order.getOrderNo());
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(order.getPayer()).get();
            //手续费， 费率
            final BigDecimal merchantUpgradePoundage = this.calculateService.getMerchantUpgradePoundage(merchant.getId(),order.getOrderNo(), order.getTradeAmount(), order.getBusinessOrderNo());
            order.setPoundage(merchantUpgradePoundage);
            this.orderService.update(order);
            //公司利润账户，手续费入账
            this.companyRecorded(order.getId());
            //结算
            final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
            if (orderOptional.get().isPaySuccess() && (orderOptional.get().isDueSettle() || orderOptional.get().isSettleing())) {
                //将交易单标记为结算中
                this.orderService.updateSettleStatus(orderOptional.get().getId(), EnumSettleStatus.SETTLE_ING.getId());
                log.info("交易订单号[{}], 进行结算操作", order.getOrderNo());
                this.companySettle(orderOptional.get());
                //手续费结算到代理商等，再到 可用余额
                this.merchantUpgradePoundageSettle(orderOptional.get(), merchant.getId());
                //结算完毕
                this.orderService.updateSettleStatus(orderOptional.get().getId(), EnumSettleStatus.SETTLED.getId());
            }
            //回调商户升级业务
            try  {
                this.merchantInfoService.toUpgrade(order.getBusinessOrderNo(), "S");
            } catch (final Throwable e) {
                log.error("##############商户升级支付成功，回调商户升级业务异常##############", e);
            }
            return;
        }
        //普通支付单处理业务
        log.info("交易订单[{}]，处理普通支付回调业务", order.getOrderNo());
        final MerchantInfo merchant = this.merchantInfoService.getByAccountId(order.getPayee()).get();
        //手续费， 费率
        final BigDecimal merchantPayPoundageRate = this.calculateService.getMerchantPayPoundageRate(EnumProductType.HSS, merchant.getId(), enumPayChannelSign.getId());
        final BigDecimal merchantPayPoundage = this.calculateService.getMerchantPayPoundage(order.getTradeAmount(), merchantPayPoundageRate);
        order.setPoundage(merchantPayPoundage);
        order.setPayRate(merchantPayPoundageRate);
        this.orderService.update(order);
        //商户入账
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
        //判断商户交易金额--是否升级
        try  {
            final BigDecimal totalTradeAmount = this.orderService.getTotalTradeAmountByAccountId(merchant.getAccountId(),
                    EnumAppType.HSS.getId(), EnumServiceType.RECEIVE_MONEY.getId());
            final BigDecimal merchantUpgradeMinAmount = this.upgradeRecommendRulesService.selectInviteStandard();
            if (totalTradeAmount.compareTo(merchantUpgradeMinAmount) >= 0) {
                this.merchantInfoService.toUpgradeByRecommend(merchant.getId());
            }
        } catch (final Throwable e) {
            log.error("##############商户交易金额达到升级标准，调用商户升级业务异常##############", e);
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
        requestJsonObject.put("payOrderSn", paymentSdkPayCallbackResponse.getSn());
        requestJsonObject.put("balanceAccountType", EnumBalanceTimeType.D0.getType());
        MqProducer.produce(requestJsonObject, MqConfig.MERCHANT_WITHDRAW, 100);
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
        //回调商户升级业务
        try  {
            this.merchantInfoService.toUpgrade(order.getBusinessOrderNo(), "F");
        } catch (final Throwable e) {
            log.error("##############商户升级支付成功，回调商户升级业务异常##############");
        }
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
                    order.getTradeAmount().subtract(order.getPoundage()), "支付", EnumAccountFlowType.INCREASE,
                    EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            //手续费账户
            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
            this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());

            //可用余额流水增加
            this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                    "支付", EnumAccountFlowType.INCREASE);

//            this.settleAccountFlowService.addSettleAccountFlow(poundageAccount.getId(), order.getOrderNo(),
//                    order.getPoundage(), "支付分润", EnumAccountFlowType.INCREASE,
//                    EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     */
    @Override
    @Transactional
    public void companyRecorded(final long orderId) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        log.info("交易订单号[{}], 进行入账操作", order.getOrderNo());
        if (order.isPaySuccess() && order.isDueSettle()) {
            //公司账户待结算增加
            final Account jkmAccount = this.accountService.getByIdWithLock(AccountConstants.JKM_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(jkmAccount.getId(), order.getTradeAmount().subtract(order.getPoundage()));
            this.accountService.increaseSettleAmount(jkmAccount.getId(), order.getTradeAmount().subtract(order.getPoundage()));
            this.settleAccountFlowService.addSettleAccountFlow(jkmAccount.getId(), order.getOrderNo(),
                    order.getTradeAmount().subtract(order.getPoundage()), "商户升级", EnumAccountFlowType.INCREASE,
                    EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());

            //手续费账户
            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
            this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());

            //可用余额流水增加
            this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                    "商户升级", EnumAccountFlowType.INCREASE);
//            this.settleAccountFlowService.addSettleAccountFlow(poundageAccount.getId(), order.getOrderNo(),
//                    order.getPoundage(), "商户升级", EnumAccountFlowType.INCREASE,
//                    EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
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
                    "支付", EnumAccountFlowType.DECREASE,
                    EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());
            //可用余额流水增加
            this.accountFlowService.addAccountFlow(merchantAccount.getId(), order.getOrderNo(), merchantIncreaseSettleAccountFlow.getIncomeAmount(),
                    "支付", EnumAccountFlowType.INCREASE);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     */
    @Override
    @Transactional
    public void companySettle(Order order) {
        //公司账户结算
        final Optional<SettleAccountFlow> optional = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(order.getOrderNo(),
                AccountConstants.JKM_ACCOUNT_ID, EnumAccountFlowType.DECREASE.getId());
        if (!optional.isPresent() && !order.isSettled()) {
            final Account jkmAccount = this.accountService.getByIdWithLock(AccountConstants.JKM_ACCOUNT_ID).get();
            final SettleAccountFlow jkmIncreaseSettleAccountFlow =
                    this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(order.getOrderNo(), jkmAccount.getId(),
                            EnumAccountFlowType.INCREASE.getId()).get();
            //待结算金额减少
            Preconditions.checkState(order.getTradeAmount().subtract(order.getPoundage()).compareTo(jkmAccount.getDueSettleAmount()) <= 0);
            Preconditions.checkState(order.getTradeAmount().subtract(order.getPoundage()).compareTo(jkmIncreaseSettleAccountFlow.getIncomeAmount()) == 0);
            this.accountService.increaseAvailableAmount(jkmAccount.getId(), jkmIncreaseSettleAccountFlow.getIncomeAmount());
            this.accountService.decreaseSettleAmount(jkmAccount.getId(), jkmIncreaseSettleAccountFlow.getIncomeAmount());
            this.settleAccountFlowService.addSettleAccountFlow(jkmAccount.getId(), order.getOrderNo(), jkmIncreaseSettleAccountFlow.getIncomeAmount(),
                    "商户升级", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
            //可用余额流水增加
            this.accountFlowService.addAccountFlow(jkmAccount.getId(), order.getOrderNo(), jkmIncreaseSettleAccountFlow.getIncomeAmount(),
                    "商户升级", EnumAccountFlowType.INCREASE);
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
        final Map<String, Triple<Long, BigDecimal, BigDecimal>> shallProfitMap = this.dealerService.shallProfit(EnumProductType.HSS, order.getOrderNo(),
                order.getTradeAmount(), order.getPayChannelSign(), merchantId);
        final Triple<Long, BigDecimal, BigDecimal> channelMoneyTriple = shallProfitMap.get("channelMoney");
        final Triple<Long, BigDecimal, BigDecimal> productMoneyTriple = shallProfitMap.get("productMoney");
        final Triple<Long, BigDecimal, BigDecimal> firstMoneyTriple = shallProfitMap.get("firstMoney");
        final Triple<Long, BigDecimal, BigDecimal> secondMoneyTriple = shallProfitMap.get("secondMoney");
        final Triple<Long, BigDecimal, BigDecimal> firstMerchantMoneyTriple = shallProfitMap.get("firstMerchantMoney");
        final Triple<Long, BigDecimal, BigDecimal> secondMerchantMoneyTriple = shallProfitMap.get("secondMerchantMoney");

        final BigDecimal channelMoney = null == channelMoneyTriple ? new BigDecimal("0.00") : channelMoneyTriple.getMiddle();
        final BigDecimal productMoney = null == productMoneyTriple ? new BigDecimal("0.00") : productMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0.00") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0.00") : secondMoneyTriple.getMiddle();
        final BigDecimal firstMerchantMoney = null == firstMerchantMoneyTriple ? new BigDecimal("0.00") : firstMerchantMoneyTriple.getMiddle();
        final BigDecimal secondMerchantMoney = null == secondMerchantMoneyTriple ? new BigDecimal("0.00") : secondMerchantMoneyTriple.getMiddle();
        Preconditions.checkState(order.getPoundage().compareTo(channelMoney.add(productMoney).add(firstMoney).add(secondMoney).add(firstMerchantMoney).add(secondMerchantMoney)) >= 0, "收手续不可以小于分润总和");
        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(order.getPoundage().compareTo(poundageAccount.getAvailable()) <= 0, "该笔订单的分账手续费不可以大于手续费账户的可用余额总和");
        //待结算--可用余额
//        this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
//        this.accountService.decreaseSettleAmount(poundageAccount.getId(), order.getPoundage());
//        this.settleAccountFlowService.addSettleAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
//                "支付分润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
//        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
//                "支付分润", EnumAccountFlowType.INCREASE);

        //分账
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), order.getPoundage());
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "支付分润", EnumAccountFlowType.DECREASE);
        //判断分账的业务类型
        final String splitBusinessType = this.getSplitBusinessType(order);

        //增加分账记录
        //通道利润--到结算--到可用余额
        if (null != channelMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), channelMoneyTriple, "通道账户", EnumTradeType.PAY.getValue());
            final Account account = this.accountService.getById(channelMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
        }
        //产品利润--到结算--可用余额
        if (null != productMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), productMoneyTriple, "产品账户", EnumTradeType.PAY.getValue());
            final Account account = this.accountService.getById(productMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), productMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), productMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
        }
        //一级代理商利润--到结算--可用余额
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), firstMoneyTriple, dealer.getProxyName(), EnumTradeType.PAY.getValue());
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
        }
        //二级代理商利润--到结算--可用余额
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), secondMoneyTriple, dealer.getProxyName(), EnumTradeType.PAY.getValue());
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
        }
        //直推商户利润--到结算--可用余额
        if (null != firstMerchantMoneyTriple) {
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(firstMerchantMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(firstMerchantMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), firstMerchantMoneyTriple, merchant.getMerchantName(), EnumTradeType.PAY.getValue());
            this.accountService.increaseTotalAmount(account.getId(), firstMerchantMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMerchantMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMerchantMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), firstMerchantMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), firstMerchantMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMerchantMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), firstMerchantMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
        }
        //间推商户利润--到结算--可用余额
        if (null != secondMerchantMoneyTriple) {
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(secondMerchantMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), secondMerchantMoneyTriple, merchant.getMerchantName(), EnumTradeType.PAY.getValue());
            final Account account = this.accountService.getById(secondMerchantMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), secondMerchantMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMerchantMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMerchantMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), secondMerchantMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), secondMerchantMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMerchantMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), secondMerchantMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE);
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

    /**
     * {@inheritDoc}
     *
     * @param order
     * @param merchantId
     */
    @Override
    @Transactional
    public void merchantUpgradePoundageSettle(final Order order, final long merchantId) {
        final Map<String, Triple<Long, BigDecimal, String>> promoteShallMap =
                this.merchantPromoteShallService.merchantPromoteShall(merchantId, order.getOrderNo(), order.getBusinessOrderNo(), order.getTradeAmount());

        final Triple<Long, BigDecimal, String> jkmMoneyTriple = promoteShallMap.get("companyMoney");
        final Triple<Long, BigDecimal, String> firstMoneyTriple = promoteShallMap.get("firstMoney");
        final Triple<Long, BigDecimal, String> secondMoneyTriple = promoteShallMap.get("secondMoney");
        final Triple<Long, BigDecimal, String> directMoneyTriple = promoteShallMap.get("directMoney");
        final Triple<Long, BigDecimal, String> inDirectMoneyTriple = promoteShallMap.get("inDirectMoney");
        final BigDecimal jkmMoney = null == jkmMoneyTriple ? new BigDecimal("0") : jkmMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0") : secondMoneyTriple.getMiddle();
        final BigDecimal directMoney = null == directMoneyTriple ? new BigDecimal("0") : directMoneyTriple.getMiddle();
        final BigDecimal inDirectMoney = null == inDirectMoneyTriple ? new BigDecimal("0") : inDirectMoneyTriple.getMiddle();
        Preconditions.checkState(order.getPoundage().compareTo(firstMoney.add(secondMoney).add(directMoney).add(inDirectMoney)) >= 0);
        Preconditions.checkState(order.getTradeAmount().compareTo(jkmMoney.add(firstMoney).add(secondMoney).add(directMoney).add(inDirectMoney)) >= 0);
        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();

//        this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
//        this.accountService.decreaseSettleAmount(poundageAccount.getId(), order.getPoundage());
//        this.settleAccountFlowService.addSettleAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
//                "商户升级", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
//        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
//                "商户升级", EnumAccountFlowType.INCREASE);

        Preconditions.checkState(order.getPoundage().compareTo(poundageAccount.getAvailable()) <= 0, "该笔订单的分账手续费不可以大于手续费账户的可用余额总和");
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), order.getPoundage());
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "商户升级", EnumAccountFlowType.DECREASE);

        //增加分账记录
        //jkm利润--到结算--到可用余额
        if (null != jkmMoneyTriple) {
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), jkmMoneyTriple, "jkm利润账户", "商户升级");
            final Account account = this.accountService.getById(jkmMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), jkmMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), jkmMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), jkmMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), jkmMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), jkmMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), jkmMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), jkmMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE);
        }
        //一级代理商利润--到结算--可用余额
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), firstMoneyTriple, dealer.getProxyName(), "商户升级");
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE);
        }
        //二级代理商利润--到结算--可用余额
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), secondMoneyTriple, dealer.getProxyName(), "商户升级");
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE);
        }
        //直推商户--到结算--可用余额
        if (null != directMoneyTriple) {
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(directMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), directMoneyTriple, merchant.getMerchantName(), "商户升级");
            final Account account = this.accountService.getById(directMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), directMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), directMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), directMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), directMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), directMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), directMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), directMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE);
        }
        //间推商户--到结算--可用余额
        if (null != inDirectMoneyTriple) {
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(inDirectMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(inDirectMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), inDirectMoneyTriple, merchant.getMerchantName(), "商户升级");
            this.accountService.increaseTotalAmount(account.getId(), inDirectMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), inDirectMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), inDirectMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), inDirectMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), inDirectMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), inDirectMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), inDirectMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE);
        }
    }


    /**
     * 请求支付中心下单
     *
     *  交易类型   JSAPI，NATIVE，APP，WAP,EPOS
     *
     * @param order
     * @param merchant
     * @param returnUrl 前端回调地址
     */
    private PaymentSdkPlaceOrderResponse requestPlaceOrder(final Order order, final int channel,
                                                           final MerchantInfo merchant, final String returnUrl) {
        final PaymentSdkPlaceOrderRequest placeOrderRequest = new PaymentSdkPlaceOrderRequest();
        placeOrderRequest.setAppId(PaymentSdkConstants.APP_ID);
        placeOrderRequest.setOrderNo(order.getOrderNo());
        placeOrderRequest.setGoodsDescribe(order.getGoodsDescribe());
        placeOrderRequest.setReturnUrl(returnUrl);
        placeOrderRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_NOTIFY_URL);
        placeOrderRequest.setMerName(merchant.getMerchantName());
        placeOrderRequest.setMerNo(merchant.getMarkCode());
        placeOrderRequest.setTotalAmount(order.getTradeAmount().toPlainString());
        if (EnumPayChannelSign.YG_WECHAT.getId() == channel
                || EnumPayChannelSign.YG_ALIPAY.getId() == channel) {
            placeOrderRequest.setTradeType("JSAPI");
        } else if (EnumPayChannelSign.YG_UNIONPAY.getId() == channel) {
            placeOrderRequest.setTradeType("EPOS");
        }

        final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_PLACE_ORDER, SdkSerializeUtil.convertObjToMap(placeOrderRequest));
        return JSON.parseObject(content, PaymentSdkPlaceOrderResponse.class);
    }
}
