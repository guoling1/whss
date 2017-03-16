package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.*;
import com.jkm.hss.account.enums.*;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.*;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;

import com.jkm.hss.merchant.enums.EnumSettlePeriodType;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumMerchantPayType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.UpgradeRecommendRulesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private HttpClientFacade httpClientFacade;

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
        final String channelCode = this.basicChannelService.selectCodeByChannelSign(EnumPayChannelSign.YG_WECHAT.getId(), EnumMerchantPayType.MERCHANT_JSAPI);
        final Order order = new Order();
        order.setBusinessOrderNo(businessOrderNo);
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.PAY.getId()));
        order.setTradeAmount(amount);
        order.setRealPayAmount(amount);
        order.setAppId(EnumAppType.HSS.getId());
        order.setTradeType(EnumTradeType.PAY.getId());
        order.setServiceType(EnumServiceType.APPRECIATION_PAY.getId());
        order.setPayType(channelCode);
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
        final AccountBank accountBank = this.accountBankService.getDefault(merchant.getAccountId());
        final PaymentSdkPlaceOrderResponse placeOrderResponse = this.requestPlaceOrder(order,
                channelCode, accountBank, merchant, businessReturnUrl);
        return this.handlePlaceOrder(placeOrderResponse, order);
    }

    /**
     * {@inheritDoc}
     *
     * @param totalAmount
     * @param channel  通道
     * @param merchantId
     * @param isDynamicCode
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> codeReceipt(final String totalAmount, final int channel, final long merchantId,
                                             final String appId, final boolean isDynamicCode) {
        log.info("商户[{}] 通过动态扫码， 支付一笔资金[{}]", merchantId, totalAmount);
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        final String channelCode = this.basicChannelService.selectCodeByChannelSign(channel, isDynamicCode ? EnumMerchantPayType.MERCHANT_CODE : EnumMerchantPayType.MERCHANT_JSAPI);
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
        order.setPayType(channelCode);
        order.setPayChannelSign(channel);
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleTime(new Date());
        order.setSettleType(EnumBalanceTimeType.D0.getType());
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        this.orderService.add(order);
        //请求支付中心下单
        final AccountBank accountBank = this.accountBankService.getDefault(merchant.getAccountId());
        final PaymentSdkPlaceOrderResponse placeOrderResponse = this.requestPlaceOrder(order, channelCode, accountBank, merchant,
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
        if (orderOptional.get().isDuePay() || orderOptional.get().isPaying()) {
            final Order order = this.orderService.getByIdWithLock(orderOptional.get().getId()).get();
            if (order.isDuePay() || order.isPaying()) {
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
        final EnumPayChannelSign enumPayChannelSign = this.basicChannelService.getEnumPayChannelSignByCode(paymentSdkPayCallbackResponse.getPayType());
        order.setPayChannelSign(enumPayChannelSign.getId());
        if (EnumPayChannelSign.MB_UNIONPAY.getId() == enumPayChannelSign.getId()) {
            order.setSettleTime(this.getMoBaoUnionPaySettleDate(order.getPaySuccessTime()));
        }
        log.info("返回的通道是[{}]", order.getPayType());
        //处理商户升级的支付单(此时商户自己付款给金开门)
        if (order.getPayer() > 0 && order.getPayee() == AccountConstants.JKM_ACCOUNT_ID) {
            log.info("交易订单[{}]，处理商户升级支付回调业务", order.getOrderNo());
            this.orderService.update(order);
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(order.getPayer()).get();
            final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
            if (orderOptional.get().isPaySuccess() && orderOptional.get().isDueSettle()) {
                this.orderService.updateSettleStatus(orderOptional.get().getId(), EnumSettleStatus.SETTLE_ING.getId());
                log.info("商户升级-交易订单号[{}], 进行手续费结算操作", order.getOrderNo());
                this.merchantUpgradePoundageSettle(orderOptional.get(), merchant.getId());
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
        //商户入账返回结算单id
        final long settlementRecordId = this.merchantRecorded(order.getId(), merchant);
        //手续费结算
        final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
        if (orderOptional.get().isPaySuccess() && orderOptional.get().isDueSettle()) {
            log.info("交易订单号[{}], 进行结算操作", order.getOrderNo());
            //手续费结算到代理商等，再到 可用余额
            this.poundageSettle(orderOptional.get(), merchant.getId());
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
        if (!enumPayChannelSign.getAutoSettle() && !EnumPayChannelSign.isT1Settlement(enumPayChannelSign.getId())) {
            final JSONObject requestJsonObject = new JSONObject();
            requestJsonObject.put("merchantId", merchant.getId());
            requestJsonObject.put("settlementRecordId", settlementRecordId);
            requestJsonObject.put("payOrderSn", paymentSdkPayCallbackResponse.getSn());
            requestJsonObject.put("payChannelSign", enumPayChannelSign.getId());
            MqProducer.produce(requestJsonObject, MqConfig.MERCHANT_WITHDRAW, 100);
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
    public long merchantRecorded(final long orderId, final MerchantInfo merchant) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        log.info("交易订单号[{}], 进行入账操作", order.getOrderNo());
        if (order.isPaySuccess() && order.isDueSettle()) {
            //手续费账户
            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
            this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
            //可用余额流水增加
            this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                    "支付入账", EnumAccountFlowType.INCREASE);

            //商户账户
            final Account merchantAccount = this.accountService.getByIdWithLock(merchant.getAccountId()).get();
            //商户待结算增加
            this.accountService.increaseTotalAmount(merchantAccount.getId(), order.getTradeAmount().subtract(order.getPoundage()));
            this.accountService.increaseSettleAmount(merchantAccount.getId(), order.getTradeAmount().subtract(order.getPoundage()));
            final long settleAccountFlowIncreaseId = this.settleAccountFlowService.addSettleAccountFlow(merchantAccount.getId(), order.getOrderNo(),
                    order.getTradeAmount().subtract(order.getPoundage()), "支付结算", EnumAccountFlowType.INCREASE,
                    EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());
            final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(order.getPayChannelSign());
            //生成结算单
            final SettlementRecord settlementRecord = new SettlementRecord();
            settlementRecord.setAccountId(merchantAccount.getId());
            settlementRecord.setUserNo(merchant.getMarkCode());
            settlementRecord.setUserName(merchant.getMerchantName());
            settlementRecord.setAccountUserType(EnumAccountUserType.MERCHANT.getId());
            settlementRecord.setAppId(EnumAppType.HSS.getId());
            settlementRecord.setSettleDate(order.getSettleTime());
            settlementRecord.setTradeNumber(1);
            settlementRecord.setSettleAmount(order.getTradeAmount().subtract(order.getPoundage()));
            settlementRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
            if (payChannelSign.getAutoSettle()) {
                settlementRecord.setSettleMode(EnumSettleModeType.CHANNEL_SETTLE.getId());
                settlementRecord.setSettleDestination(EnumSettleDestinationType.CHANNEL_SETTLE.getId());
                settlementRecord.setStatus(EnumSettlementRecordStatus.WITHDRAWING.getId());
                settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.MERCHANT.getId(), EnumSettleDestinationType.CHANNEL_SETTLE.getId()));
            } else {
                settlementRecord.setSettleMode(EnumSettleModeType.SELF_SETTLE.getId());
                settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_CARD.getId());
                settlementRecord.setStatus(EnumSettlementRecordStatus.WAIT_WITHDRAW.getId());
                settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.MERCHANT.getId(), EnumSettleDestinationType.TO_CARD.getId()));
            }
            final long settlementRecordId = this.settlementRecordService.add(settlementRecord);
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecordId);
            return settlementRecordId;
        }
        return 0;
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

        //分账
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), order.getPoundage());
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "支付分润", EnumAccountFlowType.DECREASE);
        //判断分账的业务类型
        final String splitBusinessType = EnumSplitBusinessType.HSSPAY.getId();

        //增加分账记录
        //通道利润--到可用余额
        if (null != channelMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), channelMoneyTriple, "通道账户",
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.JKM.getId());
            final Account account = this.accountService.getById(channelMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.increaseAvailableAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "收单反润", EnumAccountFlowType.INCREASE);
        }
        //产品利润--可用余额
        if (null != productMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), productMoneyTriple, "产品账户",
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.JKM.getId());
            final Account account = this.accountService.getById(productMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.increaseAvailableAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "收单反润", EnumAccountFlowType.INCREASE);
        }
        //一级代理商利润--到结算--可用余额
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), firstMoneyTriple, dealer.getProxyName(),
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.FIRST_DEALER.getId());
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            final long settleAccountFlowIncreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "收单反润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());

            if (EnumSettlePeriodType.D0.getId().equals(order.getSettleType())) {
                //生成结算单
                final long settlementRecordId = this.generateDealerSettlementRecord(account.getId(), dealer, order.getSettleTime(), firstMoneyTriple.getMiddle());
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecordId);

                //待结算--可用余额
                this.dealerRecordedAccount(account.getId(), firstMoneyTriple.getMiddle(), order, settlementRecordId);
            }
        }
        //二级代理商利润--到结算--可用余额
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), secondMoneyTriple, dealer.getProxyName(),
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.SECOND_DEALER.getId());
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            final long settleAccountFlowIncreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "收单反润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());

            if (EnumSettlePeriodType.D0.getId().equals(order.getSettleType())) {
                //生成结算单
                final long settlementRecordId = this.generateDealerSettlementRecord(account.getId(), dealer, order.getSettleTime(), secondMoneyTriple.getMiddle());
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecordId);

                //待结算--可用余额
                this.dealerRecordedAccount(account.getId(), secondMoneyTriple.getMiddle(), order, settlementRecordId);
            }
        }
        //直推商户利润--到结算--可用余额
        if (null != firstMerchantMoneyTriple) {
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(firstMerchantMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(firstMerchantMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), firstMerchantMoneyTriple, merchant.getMerchantName(),
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.MERCHANT.getId());
            this.accountService.increaseTotalAmount(account.getId(), firstMerchantMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMerchantMoneyTriple.getMiddle());
            final long settleAccountFlowIncreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMerchantMoneyTriple.getMiddle(),
                    "收单-直推", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            if (EnumSettlePeriodType.D0.getId().equals(order.getSettleType())) {
                //生成结算单
                final long settlementRecordId = this.generateMerchantSettlementRecord(account.getId(), merchant, order.getSettleTime(), firstMerchantMoneyTriple.getMiddle());
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecordId);

                //待结算--可用余额
                this.merchantRecordedAccount(account.getId(), firstMerchantMoneyTriple.getMiddle(), order, settlementRecordId);
            }
        }
        //间推商户利润--到结算--可用余额
        if (null != secondMerchantMoneyTriple) {
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(secondMerchantMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), secondMerchantMoneyTriple, merchant.getMerchantName(),
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.MERCHANT.getId());
            final Account account = this.accountService.getById(secondMerchantMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), secondMerchantMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMerchantMoneyTriple.getMiddle());
            final long settleAccountFlowIncreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMerchantMoneyTriple.getMiddle(),
                    "收单-间推", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            if (EnumSettlePeriodType.D0.getId().equals(order.getSettleType())) {
                //生成结算单
                final long settlementRecordId = this.generateMerchantSettlementRecord(account.getId(), merchant, order.getSettleTime(), secondMerchantMoneyTriple.getMiddle());
                this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecordId);

                //待结算--可用余额
                this.merchantRecordedAccount(account.getId(), secondMerchantMoneyTriple.getMiddle(), order, settlementRecordId);
            }
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param dealer
     * @param settleDate
     * @param settleAmount
     * @return
     */
    @Override
    @Transactional
    public long generateDealerSettlementRecord(final long accountId, final Dealer dealer, final Date settleDate, final BigDecimal settleAmount) {
        final SettlementRecord settlementRecord = new SettlementRecord();
        settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.DEALER.getId(), EnumSettleDestinationType.TO_ACCOUNT.getId()));
        settlementRecord.setAccountId(accountId);
        settlementRecord.setUserNo(dealer.getMarkCode());
        settlementRecord.setUserName(dealer.getProxyName());
        settlementRecord.setAccountUserType(EnumAccountUserType.DEALER.getId());
        settlementRecord.setAppId(EnumAppType.HSS.getId());
        settlementRecord.setSettleDate(settleDate);
        settlementRecord.setTradeNumber(1);
        settlementRecord.setSettleAmount(settleAmount);
        settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
        settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
        return this.settlementRecordService.add(settlementRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param merchant
     * @param settleDate
     * @param settleAmount
     * @return
     */
    @Override
    @Transactional
    public long generateMerchantSettlementRecord(final long accountId, final MerchantInfo merchant, final Date settleDate, final BigDecimal settleAmount) {
        final SettlementRecord settlementRecord = new SettlementRecord();
        settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.MERCHANT.getId(), EnumSettleDestinationType.TO_ACCOUNT.getId()));
        settlementRecord.setAccountId(accountId);
        settlementRecord.setUserNo(merchant.getMarkCode());
        settlementRecord.setUserName(merchant.getMerchantName());
        settlementRecord.setAccountUserType(EnumAccountUserType.MERCHANT.getId());
        settlementRecord.setAppId(EnumAppType.HSS.getId());
        settlementRecord.setSettleDate(settleDate);
        settlementRecord.setTradeNumber(1);
        settlementRecord.setSettleAmount(settleAmount);
        settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
        settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
        return this.settlementRecordService.add(settlementRecord);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param settleAmount
     * @param order
     * @param settlementRecordId
     */
    @Override
    @Transactional
    public void dealerRecordedAccount(long accountId, BigDecimal settleAmount, Order order, long settlementRecordId) {
        this.accountService.increaseAvailableAmount(accountId, settleAmount);
        this.accountService.decreaseSettleAmount(accountId, settleAmount);
        final long settleAccountFlowDecreaseId = this.settleAccountFlowService.addSettleAccountFlow(accountId, order.getOrderNo(), settleAmount,
                "收单反润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
        this.accountFlowService.addAccountFlow(accountId, order.getOrderNo(), settleAmount,
                "收单反润", EnumAccountFlowType.INCREASE);
        this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowDecreaseId, settlementRecordId);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param settleAmount
     * @param order
     * @param settlementRecordId
     */
    @Override
    @Transactional
    public void merchantRecordedAccount(final long accountId, final BigDecimal settleAmount, final Order order, final long settlementRecordId) {
        this.accountService.increaseAvailableAmount(accountId, settleAmount);
        this.accountService.decreaseSettleAmount(accountId, settleAmount);
        final long settleAccountFlowDecreaseId = this.settleAccountFlowService.addSettleAccountFlow(accountId, order.getOrderNo(), settleAmount,
                "收单-间推", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());
        this.accountFlowService.addAccountFlow(accountId, order.getOrderNo(), settleAmount,
                "收单-间推", EnumAccountFlowType.INCREASE);
        this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowDecreaseId, settlementRecordId);
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
        final Triple<Long, BigDecimal, String> companyMoneyTriple = promoteShallMap.get("companyMoney");
        final Triple<Long, BigDecimal, String> firstMoneyTriple = promoteShallMap.get("firstMoney");
        final Triple<Long, BigDecimal, String> secondMoneyTriple = promoteShallMap.get("secondMoney");
        final Triple<Long, BigDecimal, String> directMoneyTriple = promoteShallMap.get("directMoney");
        final Triple<Long, BigDecimal, String> inDirectMoneyTriple = promoteShallMap.get("inDirectMoney");
        final BigDecimal companyMoney = null == companyMoneyTriple ? new BigDecimal("0.00") : companyMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0.00") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0.00") : secondMoneyTriple.getMiddle();
        final BigDecimal directMoney = null == directMoneyTriple ? new BigDecimal("0.00") : directMoneyTriple.getMiddle();
        final BigDecimal inDirectMoney = null == inDirectMoneyTriple ? new BigDecimal("0.00") : inDirectMoneyTriple.getMiddle();
        Preconditions.checkState(order.getRealPayAmount().compareTo(companyMoney.add(firstMoney).add(secondMoney).add(directMoney).add(inDirectMoney)) >= 0);
        //手续费账户结算
        if (null != companyMoneyTriple) {
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getRealPayAmount(), companyMoneyTriple, "金开门利润账户",
                    "商户升级-反润", EnumSplitAccountUserType.JKM.getId());
            final Account jkmAccount = this.accountService.getByIdWithLock(AccountConstants.JKM_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(jkmAccount.getId(), companyMoneyTriple.getMiddle());
            this.accountService.increaseAvailableAmount(jkmAccount.getId(), companyMoneyTriple.getMiddle());
            this.accountFlowService.addAccountFlow(jkmAccount.getId(), order.getOrderNo(), companyMoneyTriple.getMiddle(),
                    "商户升级", EnumAccountFlowType.INCREASE);
        }
        //一级代理商利润--到结算--可用余额
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getRealPayAmount(), firstMoneyTriple, dealer.getProxyName(),
                    "商户升级-反润", EnumSplitAccountUserType.FIRST_DEALER.getId());
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            final long settleAccountFlowIncreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "商户升级-反润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());

            //生成结算单
            final SettlementRecord settlementRecord = new SettlementRecord();
            settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.DEALER.getId(), EnumSettleDestinationType.TO_ACCOUNT.getId()));
            settlementRecord.setAccountId(account.getId());
            settlementRecord.setUserNo(dealer.getMarkCode());
            settlementRecord.setUserName(dealer.getProxyName());
            settlementRecord.setAccountUserType(EnumAccountUserType.DEALER.getId());
            settlementRecord.setAppId(EnumAppType.HSS.getId());
            settlementRecord.setSettleDate(order.getSettleTime());
            settlementRecord.setTradeNumber(1);
            settlementRecord.setSettleAmount(firstMoneyTriple.getMiddle());
            settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
            settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
            final long settlementRecordId = this.settlementRecordService.add(settlementRecord);
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecordId);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            final long settleAccountFlowDecreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "商户升级-反润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "商户升级-反润", EnumAccountFlowType.INCREASE);
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowDecreaseId, settlementRecordId);
        }
        //二级代理商利润--到结算--可用余额
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getRealPayAmount(), secondMoneyTriple, dealer.getProxyName(),
                    "商户升级-反润", EnumSplitAccountUserType.SECOND_DEALER.getId());
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            final long settleAccountFlowIncreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "商户升级-反润", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());

            //生成结算单
            final SettlementRecord settlementRecord = new SettlementRecord();
            settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.DEALER.getId(), EnumSettleDestinationType.TO_ACCOUNT.getId()));
            settlementRecord.setAccountId(account.getId());
            settlementRecord.setUserNo(dealer.getMarkCode());
            settlementRecord.setUserName(dealer.getProxyName());
            settlementRecord.setAccountUserType(EnumAccountUserType.DEALER.getId());
            settlementRecord.setAppId(EnumAppType.HSS.getId());
            settlementRecord.setSettleDate(order.getSettleTime());
            settlementRecord.setTradeNumber(1);
            settlementRecord.setSettleAmount(secondMoneyTriple.getMiddle());
            settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
            settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
            final long settlementRecordId = this.settlementRecordService.add(settlementRecord);
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecordId);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            final long settleAccountFlowDecreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "商户升级-反润", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "商户升级-反润", EnumAccountFlowType.INCREASE);
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowDecreaseId, settlementRecordId);
        }
        //直推商户--到结算--可用余额
        if (null != directMoneyTriple) {
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(directMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getRealPayAmount(), directMoneyTriple, merchant.getMerchantName(),
                    "商户升级-直推", EnumSplitAccountUserType.MERCHANT.getId());
            final Account account = this.accountService.getById(directMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), directMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), directMoneyTriple.getMiddle());
            final long settleAccountFlowIncreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), directMoneyTriple.getMiddle(),
                    "商户升级-直推", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            //生成结算单
            final SettlementRecord settlementRecord = new SettlementRecord();
            settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.MERCHANT.getId(), EnumSettleDestinationType.TO_ACCOUNT.getId()));
            settlementRecord.setAccountId(account.getId());
            settlementRecord.setUserNo(merchant.getMarkCode());
            settlementRecord.setUserName(merchant.getMerchantName());
            settlementRecord.setAccountUserType(EnumAccountUserType.MERCHANT.getId());
            settlementRecord.setAppId(EnumAppType.HSS.getId());
            settlementRecord.setSettleDate(order.getSettleTime());
            settlementRecord.setTradeNumber(1);
            settlementRecord.setSettleAmount(directMoneyTriple.getMiddle());
            settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
            settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
            final long settlementRecordId = this.settlementRecordService.add(settlementRecord);
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecordId);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), directMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), directMoneyTriple.getMiddle());
            final long settleAccountFlowDecreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), directMoneyTriple.getMiddle(),
                    "商户升级-直推", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), directMoneyTriple.getMiddle(),
                    "商户升级-直推", EnumAccountFlowType.INCREASE);
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowDecreaseId, settlementRecordId);
        }
        //间推商户--到结算--可用余额
        if (null != inDirectMoneyTriple) {
            final MerchantInfo merchant = this.merchantInfoService.getByAccountId(inDirectMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(inDirectMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addMerchantUpgradePaySplitAccountRecord(EnumSplitBusinessType.HSSPROMOTE.getId(), order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getRealPayAmount(), inDirectMoneyTriple, merchant.getMerchantName(),
                    "商户升级-间推", EnumSplitAccountUserType.MERCHANT.getId());
            this.accountService.increaseTotalAmount(account.getId(), inDirectMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), inDirectMoneyTriple.getMiddle());
            final long settleAccountFlowIncreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), inDirectMoneyTriple.getMiddle(),
                    "商户升级-间推", EnumAccountFlowType.INCREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            //生成结算单
            final SettlementRecord settlementRecord = new SettlementRecord();
            settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(EnumAccountUserType.MERCHANT.getId(), EnumSettleDestinationType.TO_ACCOUNT.getId()));
            settlementRecord.setAccountId(account.getId());
            settlementRecord.setUserNo(merchant.getMarkCode());
            settlementRecord.setUserName(merchant.getMerchantName());
            settlementRecord.setAccountUserType(EnumAccountUserType.MERCHANT.getId());
            settlementRecord.setAppId(EnumAppType.HSS.getId());
            settlementRecord.setSettleDate(order.getSettleTime());
            settlementRecord.setTradeNumber(1);
            settlementRecord.setSettleAmount(inDirectMoneyTriple.getMiddle());
            settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_ACCOUNT.getId());
            settlementRecord.setSettleStatus(EnumSettleStatus.SETTLED.getId());
            final long settlementRecordId = this.settlementRecordService.add(settlementRecord);
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowIncreaseId, settlementRecordId);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), inDirectMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), inDirectMoneyTriple.getMiddle());
            final long settleAccountFlowDecreaseId = this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), inDirectMoneyTriple.getMiddle(),
                    "商户升级-间推", EnumAccountFlowType.DECREASE, EnumAppType.HSS.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), inDirectMoneyTriple.getMiddle(),
                    "商户升级-间推", EnumAccountFlowType.INCREASE);
            this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlowDecreaseId, settlementRecordId);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantId  商户ID
     * @param amount      金额
     * @param channel     渠道
     * @param creditBankCardId  信用卡ID
     * @param cvv2   cvv2
     * @param appId
     * @return
     */
    @Override
    public Pair<Integer, String> unionPay(final long merchantId, final String amount, final int channel,
                                          final long creditBankCardId, final String cvv2, final String appId) {
        log.info("商户[{}] 通过快捷， 支付一笔资金[{}]", merchantId, amount);
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        final EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.idOf(channel);
        final Order order = new Order();
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.PAY.getId()));
        order.setTradeAmount(new BigDecimal(amount));
        order.setRealPayAmount(new BigDecimal(amount));
        order.setAppId(appId);
        order.setTradeType(EnumTradeType.PAY.getId());
        order.setServiceType(EnumServiceType.RECEIVE_MONEY.getId());
        order.setPayer(0);
        order.setPayee(merchant.getAccountId());
        order.setGoodsName(merchant.getMerchantName());
        order.setGoodsDescribe(merchant.getMerchantName());
        order.setPayType(enumPayChannelSign.getCode());
        order.setPayChannelSign(channel);
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleType(EnumBalanceTimeType.T1.getType());
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        this.orderService.add(order);
        final AccountBank accountBank = this.accountBankService.selectById(creditBankCardId).get();
        final PaymentSdkUnionPayRequest paymentSdkUnionPayRequest = new PaymentSdkUnionPayRequest();
        paymentSdkUnionPayRequest.setAppId(appId);
        paymentSdkUnionPayRequest.setOrderNo(order.getOrderNo());
        paymentSdkUnionPayRequest.setChannel(order.getPayType());
        paymentSdkUnionPayRequest.setGoodsDescribe(merchant.getMerchantChangeName());
        paymentSdkUnionPayRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_NOTIFY_URL);
        paymentSdkUnionPayRequest.setMerName(merchant.getName());
        paymentSdkUnionPayRequest.setTotalAmount(order.getRealPayAmount().toPlainString());
        paymentSdkUnionPayRequest.setCardByName(merchant.getName());
        paymentSdkUnionPayRequest.setCardByNo(MerchantSupport.decryptBankCard(merchant.getAccountId(), accountBank.getBankNo()));
        paymentSdkUnionPayRequest.setExpireDate(accountBank.getExpiryTime());
        paymentSdkUnionPayRequest.setCardCvv(cvv2);
        paymentSdkUnionPayRequest.setCerNumber(MerchantSupport.decryptIdentity(merchant.getIdentity()));
        paymentSdkUnionPayRequest.setMobile(MerchantSupport.decryptMobile(merchant.getAccountId(), accountBank.getReserveMobile()));
        final String resultStr = this.httpClientFacade.jsonPost(PaymentSdkConstants.SDK_PAY_UNIONPAY_PREPARE, SdkSerializeUtil.convertObjToMap(paymentSdkUnionPayRequest));
        log.info("商户[{}], 订单号[{}],  快捷预下单结果[{}]", merchantId, order.getOrderNo(), resultStr);
        PaymentSdkUnionPayResponse paymentSdkUnionPayResponse;
        try {
            paymentSdkUnionPayResponse = JSONObject.parseObject(resultStr, PaymentSdkUnionPayResponse.class);
        } catch (final Throwable e) {
            log.error("商户[ " + merchantId +" ], 订单号[{ " + order.getOrderNo() + " ], 下单失败", e);
            this.orderService.updateRemark(order.getId(), "下单失败");
            return Pair.of(-1, "下单失败");
        }
        final EnumBasicStatus enumBasicStatus = EnumBasicStatus.of(paymentSdkUnionPayResponse.getCode());
        switch (enumBasicStatus) {
            case SUCCESS:
                order.setRemark(paymentSdkUnionPayResponse.getMessage());
                this.orderService.update(order);
                return Pair.of(0, order.getId() + "");
            case FAIL:
                order.setRemark(paymentSdkUnionPayResponse.getMessage());
                this.orderService.update(order);
                return Pair.of(-1, order.getRemark());
        }
        return Pair.of(-1, "下单失败");
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param code
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> confirmUnionPay(final long orderId, final String code) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        if (order.isDuePay()) {
            this.orderService.updateStatus(orderId, EnumOrderStatus.PAYING.getId(), StringUtils.isEmpty(order.getRemark()) ? "" : order.getRemark());
            final PaymentSdkConfirmUnionPayRequest paymentSdkConfirmUnionPayRequest = new PaymentSdkConfirmUnionPayRequest();
            paymentSdkConfirmUnionPayRequest.setAppId(order.getAppId());
            paymentSdkConfirmUnionPayRequest.setOrderNo(order.getOrderNo());
            paymentSdkConfirmUnionPayRequest.setCode(order.getPayType());
            paymentSdkConfirmUnionPayRequest.setYzm(code);
            final String resultStr = this.httpClientFacade.jsonPost(PaymentSdkConstants.SDK_PAY_UNIONPAY_CONFRIM, SdkSerializeUtil.convertObjToMap(paymentSdkConfirmUnionPayRequest));
            log.info("订单号[{}], 快捷确认下单结果[{}]", order.getOrderNo(), resultStr);
            final PaymentSdkConfirmUnionPayResponse paymentSdkConfirmUnionPayResponse = JSONObject.parseObject(resultStr, PaymentSdkConfirmUnionPayResponse.class);
            final EnumBasicStatus enumBasicStatus = EnumBasicStatus.of(paymentSdkConfirmUnionPayResponse.getCode());
            switch (enumBasicStatus) {
                case SUCCESS:
                    this.orderService.updateRemark(orderId, paymentSdkConfirmUnionPayResponse.getMessage());
                    return Pair.of(0, "");
                case FAIL:
                    this.orderService.updateRemark(orderId, paymentSdkConfirmUnionPayResponse.getMessage());
                    return Pair.of(-1, order.getRemark());
            }
        }
        return Pair.of(-1, "订单状态错误，确认支付失败");
    }


    /**
     * 请求支付中心下单
     *
     *
     * @param order
     * @param merchant
     * @param returnUrl 前端回调地址
     */
    private PaymentSdkPlaceOrderResponse requestPlaceOrder(final Order order, final String channel, final AccountBank accountBank,
                                                           final MerchantInfo merchant, final String returnUrl) {
        final PaymentSdkPlaceOrderRequest placeOrderRequest = new PaymentSdkPlaceOrderRequest();
        placeOrderRequest.setAppId(PaymentSdkConstants.APP_ID);
        placeOrderRequest.setOrderNo(order.getOrderNo());
        placeOrderRequest.setGoodsDescribe(order.getGoodsDescribe());
        placeOrderRequest.setReturnUrl(returnUrl);
        placeOrderRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_NOTIFY_URL);
        placeOrderRequest.setMerName(merchant.getMerchantChangeName());
        placeOrderRequest.setMerNo(merchant.getMarkCode());
        placeOrderRequest.setTotalAmount(order.getTradeAmount().toPlainString());
        placeOrderRequest.setChannel(channel);
        placeOrderRequest.setSettleNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL);
        placeOrderRequest.setBankCode(accountBank.getBranchCode());
        placeOrderRequest.setCardNo(MerchantSupport.encryptBankCard(accountBank.getBankNo()));
        placeOrderRequest.setPayerName(merchant.getName());
        placeOrderRequest.setIdCardNo(merchant.getIdentity());
        final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_PLACE_ORDER, SdkSerializeUtil.convertObjToMap(placeOrderRequest));
        return JSON.parseObject(content, PaymentSdkPlaceOrderResponse.class);
    }

    /**
     * 获取魔宝快捷结算时间
     *
     * 82560000表示22:56:00
     *
     * @param tradeDate
     * @return
     */
    private Date getMoBaoUnionPaySettleDate(Date tradeDate) {
        final int millisOfDay = new DateTime(tradeDate).getMillisOfDay();
        if (millisOfDay > 82560000) {//当作第二个工作日的交易
            tradeDate = new DateTime(tradeDate).plusDays(1).toDate();
        }
        return DateTimeUtil.generateT1SettleDate(tradeDate);
    }
}
