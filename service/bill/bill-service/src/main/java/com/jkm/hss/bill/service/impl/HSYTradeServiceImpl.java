package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.entity.UnFrozenRecord;
import com.jkm.hss.account.enums.*;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.bill.service.HSYTradeService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.push.sevice.PushService;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by yulong.zhang on 2017/1/17.
 */
@Slf4j
@Service("hsyTradeService")
public class HSYTradeServiceImpl implements HSYTradeService {

    @Autowired
    private SmsAuthService smsAuthService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private UnfrozenRecordService unfrozenRecordService;
    @Autowired
    private FrozenRecordService frozenRecordService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private SplitAccountRecordService splitAccountRecordService;
    @Autowired
    private ShallProfitDetailService shallProfitDetailService;
    @Autowired
    private PushService pushService;
    @Autowired
    private BasicChannelService basicChannelService;

    /**
     * {@inheritDoc}
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    @Override
    public String getWithdrawInfo(final String dataParam, final AppParam appParam) {
        final JSONObject dataJo = JSONObject.parseObject(dataParam);
        final JSONObject result = new JSONObject();
        final long accountId = dataJo.getLongValue("accountId");
        final int channel = dataJo.getIntValue("channel");
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByAccountID(accountId).get(0);
        final AppBizCard appBizCard = new AppBizCard();
        appBizCard.setSid(shop.getId());
        final AppBizCard appBizCard1 = this.hsyShopDao.findAppBizCardByParam(appBizCard).get(0);
        final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(accountId).get(0);
        final Account account = this.accountService.getById(accountId).get();
        result.put("bankName", appBizCard1.getCardBank());
        final String cardNO = appBizCard1.getCardNO();
        result.put("cardNo", cardNO.substring(cardNO.length() - 4));
        final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSY, shop.getId(), channel);
        result.put("poundage", merchantWithdrawPoundage);
        result.put("mobile", appAuUser.getCellphone());
        result.put("available", account.getAvailable());
        return result.toJSONString();
    }

    /**
     * {@inheritDoc}
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    @Override
    public String tradeList(final String dataParam, final AppParam appParam) {
        final JSONObject dataJo = JSONObject.parseObject(dataParam);
        final long accountId = dataJo.getLongValue("accountId");
        final int pageNo = dataJo.getIntValue("pageNo");
        final int pageSize = dataJo.getIntValue("pageSize");
        final String dateStr = dataJo.getString("date");
        Date date = null;
        if (!StringUtils.isEmpty(dateStr) && !StringUtils.isEmpty(dateStr)) {
            date = DateFormatUtil.parse(dateStr + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        final PageModel<JSONObject> pageModel = new PageModel<>(pageNo, pageSize);
        final long count = this.orderService.getPageOrdersCountByAccountId(accountId, EnumAppType.HSY.getId(), date);
        final List<Order> orders = this.orderService.getPageOrdersByAccountId(accountId, EnumAppType.HSY.getId(),
                pageModel.getFirstIndex(), pageSize, date);
        pageModel.setCount(count);
        if (!CollectionUtils.isEmpty(orders)) {
            final List<JSONObject> recordList = new ArrayList<>();
            pageModel.setRecords(recordList);
            for (Order order : orders) {
                final JSONObject jo = new JSONObject();
                recordList.add(jo);
                if (EnumTradeType.PAY.getId() == order.getTradeType()) {
                    jo.put("tradeType", "1");
                } else if (EnumTradeType.WITHDRAW.getId() == order.getTradeType()) {
                    jo.put("tradeType", "2");
                }

                if (EnumPayChannelSign.YG_WECHAT.getId() == order.getPayChannelSign()) {
                    jo.put("channel", "微信");
                } else if (EnumPayChannelSign.YG_ALIPAY.getId() == order.getPayChannelSign()) {
                    jo.put("channel", "支付宝");
                } else if (EnumPayChannelSign.YG_UNIONPAY.getId() == order.getPayChannelSign()) {
                    jo.put("channel", "快捷");
                }

                if (EnumOrderStatus.PAY_SUCCESS.getId() == order.getStatus()) {
                    jo.put("msg", "收款成功");
                } else if (EnumOrderStatus.WITHDRAW_SUCCESS.getId() == order.getStatus()) {
                    jo.put("msg", "提现成功");
                }

                jo.put("amount", order.getTradeAmount().toPlainString());
                jo.put("time", order.getCreateTime());
                jo.put("code", order.getOrderNo().substring(order.getOrderNo().length() - 4));
            }
        } else {
            pageModel.setRecords(Collections.<JSONObject>emptyList());
        }

        return JSON.toJSONString(pageModel);
    }


    /**
     * {@inheritDoc}
     *
     * @param paramData
     * @param appParam
     * @return
     */
    @Override
    public String appReceipt(final String paramData, final AppParam appParam) {
        final JSONObject result = new JSONObject();
        final JSONObject paramJo = JSONObject.parseObject(paramData);
        final String totalAmount = paramJo.getString("totalAmount");
        final int channel = paramJo.getIntValue("channel");
        final long shopId = paramJo.getLongValue("shopId");
        final String appId = paramJo.getString("appId");
        final Pair<Integer, String> receiptResult = this.receipt(totalAmount, channel, shopId, appId);
        if (0 != receiptResult.getLeft()) {
            result.put("code", -1);
            result.put("msg", "下单失败");
            result.put("url", receiptResult.getRight());
        } else {
            result.put("code", 0);
            result.put("msg", "下单成功");
            result.put("url", receiptResult.getRight());
        }
        return result.toJSONString();
    }

    /**
     * {@inheritDoc}
     *
     * @param totalAmount
     * @param channel
     * @param shopId
     * @param appId
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> receipt(final String totalAmount, final int channel, final long shopId, final String appId) {
        log.info("店铺[{}] 通过动态扫码， 支付一笔资金[{}]", shopId, totalAmount);
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByID(shopId).get(0);
        final String channelCode = this.basicChannelService.selectCodeByChannelSign(channel, EnumMerchantPayType.MERCHANT_JSAPI);
        final Order order = new Order();
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.PAY.getId()));
        order.setTradeAmount(new BigDecimal(totalAmount));
        order.setRealPayAmount(new BigDecimal(totalAmount));
        order.setAppId(appId);
        order.setTradeType(EnumTradeType.PAY.getId());
        order.setServiceType(EnumServiceType.RECEIVE_MONEY.getId());
        order.setPayer(0);
        order.setPayee(shop.getAccountID());
        order.setGoodsName(shop.getShortName());
        order.setGoodsDescribe(shop.getShortName());
        order.setPayType(channelCode);
        order.setPayChannelSign(channel);
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleType(EnumBalanceTimeType.T1.getType());
        order.setSettleTime(DateTimeUtil.generateT1SettleDate(new Date()));
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        this.orderService.add(order);
        //请求支付中心下单
        final PaymentSdkPlaceOrderResponse placeOrderResponse = this.requestPlaceOrder(order, channelCode, shop,
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
                return Pair.of(-1, "下单失败");
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
    public void handleHsyPayCallbackMsg(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse) {
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
                log.info("hsy订单[{}], 支付成功回调处理", paymentSdkPayCallbackResponse.getOrderNo());
                this.markPaySuccess(paymentSdkPayCallbackResponse, order);
                break;
            case FAIL:
                log.info("hsy订单[{}], 支付失败回调处理", paymentSdkPayCallbackResponse.getOrderNo());
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
    public void markPaySuccess(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order) {
        order.setPayType(paymentSdkPayCallbackResponse.getPayType());
        order.setPaySuccessTime(new DateTime(Long.valueOf(paymentSdkPayCallbackResponse.getPaySuccessTime())).toDate());
        order.setRemark(paymentSdkPayCallbackResponse.getMessage());
        order.setSn(paymentSdkPayCallbackResponse.getSn());
        order.setStatus(EnumOrderStatus.PAY_SUCCESS.getId());
        final EnumPayChannelSign enumPayChannelSign = this.basicChannelService.getEnumPayChannelSignByCode(paymentSdkPayCallbackResponse.getPayType());
        order.setPayChannelSign(enumPayChannelSign.getId());
        log.info("返回的通道是[{}]", order.getPayType());
        log.info("交易订单[{}]，处理hsy支付回调业务", order.getOrderNo());
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByAccountID(order.getPayee()).get(0);
        final BigDecimal merchantPayPoundageRate = this.calculateService.getMerchantPayPoundageRate(EnumProductType.HSY, shop.getId(), enumPayChannelSign.getId());
        final BigDecimal merchantPayPoundage = this.calculateService.getMerchantPayPoundage(order.getTradeAmount(), merchantPayPoundageRate, order.getPayChannelSign());
        order.setPoundage(merchantPayPoundage);
        order.setPayRate(merchantPayPoundageRate);
        this.orderService.update(order);
        //入账
        this.recorded(order.getId(), shop);
        //分账
        this.paySplitAccount(this.orderService.getByIdWithLock(order.getId()).get(), shop);
        //推送
        try {
            this.pushService.pushCashMsg(shop.getId(), enumPayChannelSign.getPaymentChannel().getValue(), order.getTradeAmount().doubleValue(), order.getOrderNo().substring(order.getOrderNo().length() - 4));
        } catch (final Throwable e) {
            log.error("订单[" + order.getOrderNo() + "]，支付成功，推送异常", e);
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
    public void markPayFail(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order) {
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
    @Transactional
    public void markPayHandling(final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse, Order order) {
        order.setRemark(paymentSdkPayCallbackResponse.getMessage());
        this.orderService.update(order);
    }


    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param shop
     */
    @Override
    @Transactional
    public void recorded(final long orderId, final AppBizShop shop) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        log.info("交易订单号[{}], 进行入账操作", order.getOrderNo());
        if (order.isPaySuccess() && order.isDueSettle()) {
            //商户账户
            final Account account = this.accountService.getByIdWithLock(shop.getAccountID()).get();
            //商户待结算增加
            this.accountService.increaseTotalAmount(account.getId(), order.getRealPayAmount().subtract(order.getPoundage()));
            this.accountService.increaseSettleAmount(account.getId(), order.getRealPayAmount().subtract(order.getPoundage()));
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(),
                    order.getRealPayAmount().subtract(order.getPoundage()), "支付", EnumAccountFlowType.INCREASE,
                    EnumAppType.HSY.getId(), order.getPaySuccessTime(), EnumAccountUserType.MERCHANT.getId());

            //手续费账户--可用余额
            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
            this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
            //可用余额流水增加
            this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                    "支付", EnumAccountFlowType.INCREASE);
        }


    }

    /**
     * {@inheritDoc}
     *
     * @param order
     * @param shop
     */
    @Override
    @Transactional
    public void paySplitAccount(final Order order, final AppBizShop shop) {
        final Map<String, Triple<Long, BigDecimal, BigDecimal>> shallProfitMap = this.dealerService.shallProfit(EnumProductType.HSY, order.getOrderNo(),
                order.getTradeAmount(), order.getPayChannelSign(), shop.getId());
        final Triple<Long, BigDecimal, BigDecimal> channelMoneyTriple = shallProfitMap.get("channelMoney");
        final Triple<Long, BigDecimal, BigDecimal> productMoneyTriple = shallProfitMap.get("productMoney");
        final Triple<Long, BigDecimal, BigDecimal> firstMoneyTriple = shallProfitMap.get("firstMoney");
        final Triple<Long, BigDecimal, BigDecimal> secondMoneyTriple = shallProfitMap.get("secondMoney");

        final BigDecimal channelMoney = null == channelMoneyTriple ? new BigDecimal("0.00") : channelMoneyTriple.getMiddle();
        final BigDecimal productMoney = null == productMoneyTriple ? new BigDecimal("0.00") : productMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0.00") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0.00") : secondMoneyTriple.getMiddle();
        Preconditions.checkState(order.getPoundage().compareTo(channelMoney.add(productMoney).add(firstMoney).add(secondMoney)) >= 0, "手续费不可以小于分润总和");
        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(order.getPoundage().compareTo(poundageAccount.getAvailable()) <= 0, "该笔订单的分账手续费不可以大于手续费账户的可用余额总和");
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), order.getPoundage());
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "支付分润", EnumAccountFlowType.DECREASE);

        //判断业务类型
        final String splitBusinessType = EnumSplitBusinessType.HSYPAY.getId();
        log.info(">>>orderNo:" + order.getOrderNo() + "该笔订单的业务类型：" + splitBusinessType);
        //通道利润--到结算
        if (null != channelMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), channelMoneyTriple, "通道账户",
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.JKM.getId());
            final Account account = this.accountService.getById(channelMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
        }
        //产品利润--到结算
        if (null != productMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), productMoneyTriple, "产品账户",
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.JKM.getId());
            final Account account = this.accountService.getById(productMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), productMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
        }
        //一级代理商利润--到结算
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), firstMoneyTriple, dealer.getProxyName(),
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.FIRST_DEALER.getId());
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
        }
        //二级代理商利润--到结算
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), secondMoneyTriple, dealer.getProxyName(),
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.SECOND_DEALER.getId());
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "支付分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
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
     * 请求支付中心下单
     *
     *  交易类型   JSAPI，NATIVE，APP，WAP,EPOS
     *
     * @param order
     * @param shop
     * @param returnUrl 前端回调地址
     */
    private PaymentSdkPlaceOrderResponse requestPlaceOrder(final Order order, final String channel,
                                                           final AppBizShop shop, final String returnUrl) {
        final PaymentSdkPlaceOrderRequest placeOrderRequest = new PaymentSdkPlaceOrderRequest();
        placeOrderRequest.setAppId(EnumAppType.HSY.getId());
        placeOrderRequest.setOrderNo(order.getOrderNo());
        placeOrderRequest.setGoodsDescribe(order.getGoodsDescribe());
        placeOrderRequest.setReturnUrl(returnUrl);
        placeOrderRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_NOTIFY_URL);
        placeOrderRequest.setMerName(shop.getShortName());
        placeOrderRequest.setMerNo(shop.getGlobalID());
        placeOrderRequest.setTotalAmount(order.getTradeAmount().toPlainString());
        placeOrderRequest.setChannel(channel);

        final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_PLACE_ORDER, SdkSerializeUtil.convertObjToMap(placeOrderRequest));
        return JSON.parseObject(content, PaymentSdkPlaceOrderResponse.class);
    }

    /**
     * {@inheritDoc}
     *
     * @param paramData
     * @param appParam
     * @return
     */
    @Override
    public String appWithdraw(final String paramData, final AppParam appParam) {
        final JSONObject result = new JSONObject();
        final JSONObject paramJo = JSONObject.parseObject(paramData);
        final String totalAmount = paramJo.getString("totalAmount");
        final int channel = paramJo.getIntValue("channel");
        final long accountId = paramJo.getLongValue("accountId");
        final String verifyCode = paramJo.getString("verifyCode");
        if (!this.hsyShopDao.isShopStatusCheckPass(accountId)) {
            result.put("code", -1);
            result.put("msg", "未审核通过");
            return result.toJSONString();
        }
        if (StringUtils.isEmpty(totalAmount)) {
            result.put("code", -1);
            result.put("msg", "提现金额错误");
            return result.toJSONString();
        }
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByAccountID(accountId).get(0);
        final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSY, shop.getId(), channel);
        if (new BigDecimal(totalAmount).compareTo(merchantWithdrawPoundage) <= 0) {
            result.put("code", -1);
            result.put("msg", "提现金额必须大于手续费");
            return result.toJSONString();
        }
        final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(accountId).get(0);
        final Pair<Integer, String> checkResult =
                this.smsAuthService.checkVerifyCode(appAuUser.getCellphone(), verifyCode, EnumVerificationCodeType.WITHDRAW_MERCHANT);
        if (1 == checkResult.getLeft()) {
            final Pair<Integer, String> withdrawResult = this.withdraw(accountId, totalAmount, channel, EnumProductType.HSY.getId());
            if (-1 == withdrawResult.getLeft()) {
                result.put("code", -1);
                result.put("msg", "提现失败--" + withdrawResult.getRight());
            } else {
                result.put("code", 0);
                result.put("msg", "提现受理成功");
            }
        } else {
            result.put("code", -1);
            result.put("msg", checkResult.getValue());
        }
        return result.toJSONString();
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param totalAmount
     * @param channel
     * @return
     */
    @Override
    public Pair<Integer, String> withdraw(final long accountId, final String totalAmount, final int channel, final String appId) {
        Preconditions.checkState(EnumPayChannelSign.YG_WECHAT.getId() == channel
                || EnumPayChannelSign.YG_ALIPAY.getId() == channel
                || EnumPayChannelSign.YG_UNIONPAY.getId() == channel, "渠道选择错误[{}]", channel);
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByAccountID(accountId).get(0);
        final long playMoneyOrderId = this.orderService.createPlayMoneyOrder(shop, new BigDecimal(totalAmount),
                appId, channel, EnumBalanceTimeType.T1.getType());
        return this.withdrawImpl(shop, playMoneyOrderId, EnumUpperChannel.SAOMI);
    }

    /**
     * {@inheritDoc}
     *
     * @param shop
     * @param playMoneyOrderId
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> withdrawImpl(final AppBizShop shop, final long playMoneyOrderId, final EnumUpperChannel playMoneyChannel) {
        final AppBizCard paramAppBizCard = new AppBizCard();
        paramAppBizCard.setSid(shop.getId());
        final AppBizCard appBizCard = this.hsyShopDao.findAppBizCardByParam(paramAppBizCard).get(0);
        final Order playMoneyOrder = this.orderService.getByIdWithLock(playMoneyOrderId).get();
        if (playMoneyOrder.isWithDrawing()) {
            final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
            paymentSdkDaiFuRequest.setAppId(playMoneyOrder.getAppId());
            paymentSdkDaiFuRequest.setOrderNo(playMoneyOrder.getOrderNo());
            paymentSdkDaiFuRequest.setTotalAmount(playMoneyOrder.getTradeAmount().subtract(playMoneyOrder.getPoundage()).toPlainString());
            // TODO
            paymentSdkDaiFuRequest.setTradeType("D0");
            paymentSdkDaiFuRequest.setIsCompany("0");
            paymentSdkDaiFuRequest.setMobile(appBizCard.getCardCellphone());
            paymentSdkDaiFuRequest.setBankName(appBizCard.getCardBank());
            paymentSdkDaiFuRequest.setAccountName(shop.getContactName());
            paymentSdkDaiFuRequest.setAccountNumber(appBizCard.getCardNO());
            paymentSdkDaiFuRequest.setIdCard(appBizCard.getIdcardNO());
            paymentSdkDaiFuRequest.setPlayMoneyChannel(playMoneyChannel.getId());
            paymentSdkDaiFuRequest.setNote(shop.getName());
            paymentSdkDaiFuRequest.setSystemCode(playMoneyOrder.getAppId());
            paymentSdkDaiFuRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL);
            paymentSdkDaiFuRequest.setPayOrderSn("");
            //请求网关
            PaymentSdkDaiFuResponse response = null;
            try {
                final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_WITHDRAW,
                        SdkSerializeUtil.convertObjToMap(paymentSdkDaiFuRequest));
                log.info("订单[{}]，向网关发起代付[{}]，返回[{}]", playMoneyOrder.getOrderNo(), paymentSdkDaiFuRequest, content);
                response = JSON.parseObject(content, PaymentSdkDaiFuResponse.class);
            } catch (final Throwable e) {
                log.error("交易订单[" + playMoneyOrder.getOrderNo() + "], 请求网关支付异常", e);
            }
            return this.handleWithdrawResult(playMoneyOrderId, 0, response);
        }
        return Pair.of(-1, "提现失败");
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    @Override
    @Transactional
    public void handleHsyWithdrawCallbackMsg(final PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse) {
        final Order order = this.orderService.getByOrderNoAndTradeType(paymentSdkWithdrawCallbackResponse.getOrderNo(), EnumTradeType.WITHDRAW.getId()).get();
        if (order.isWithDrawing()) {
            this.handleWithdrawResult(order.getId(), order.getPayer(), paymentSdkWithdrawCallbackResponse);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     * @param shop
     */
    @Override
    @Transactional
    public void withdrawSplitAccount(final Order order, final AppBizShop shop) {
        final Map<String, Triple<Long, BigDecimal, String>> shallProfitMap =
                this.shallProfitDetailService.withdrawProfitCount(EnumProductType.HSY, order.getOrderNo(), order.getTradeAmount(), order.getPayChannelSign(), shop.getId());
        final Triple<Long, BigDecimal, String> channelMoneyTriple = shallProfitMap.get("channelMoney");
        final Triple<Long, BigDecimal, String> productMoneyTriple = shallProfitMap.get("productMoney");
        final Triple<Long, BigDecimal, String> firstMoneyTriple = shallProfitMap.get("firstMoney");
        final Triple<Long, BigDecimal, String> secondMoneyTriple = shallProfitMap.get("secondMoney");
        final BigDecimal channelMoney = null == channelMoneyTriple ? new BigDecimal("0") : channelMoneyTriple.getMiddle();
        final BigDecimal productMoney = null == productMoneyTriple ? new BigDecimal("0") : productMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0") : secondMoneyTriple.getMiddle();
        Preconditions.checkState(order.getPoundage().compareTo(channelMoney.add(productMoney).add(firstMoney).add(secondMoney)) >= 0, "手续费不可以小于分润总和");
        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(order.getPoundage().compareTo(poundageAccount.getAvailable()) <= 0, "该笔订单的分账手续费不可以大于手续费账户的可用余额总和");
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), order.getPoundage());
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "提现分润", EnumAccountFlowType.DECREASE);
        //判断分账的业务类型
        final String splitBusinessType = this.getSplitBusinessType(order);

        //增加分账记录
        //通道利润--到结算
        if (null != channelMoneyTriple) {
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), channelMoneyTriple, "通道账户",
                    EnumTradeType.WITHDRAW.getValue(), EnumSplitAccountUserType.JKM.getId());
            final Account account = this.accountService.getById(channelMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
        }
        //产品利润--到结算
        if (null != productMoneyTriple) {
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), productMoneyTriple, "产品账户",
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.JKM.getId());
            final Account account = this.accountService.getById(productMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), productMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), EnumAccountUserType.COMPANY.getId());
        }
        //一级代理商利润--到结算
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), firstMoneyTriple, dealer.getProxyName(),
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.FIRST_DEALER.getId());
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
        }
        //二级代理商利润--到结算
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), secondMoneyTriple, dealer.getProxyName(),
                    EnumTradeType.PAY.getValue(), EnumSplitAccountUserType.SECOND_DEALER.getId());
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), EnumAccountUserType.DEALER.getId());
        }

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
            final EnumBasicStatus status = EnumBasicStatus.of(response.getStatus());
            switch (status) {
                case SUCCESS:
                    log.info("交易订单[{}]，提现成功", order.getOrderNo());
                    this.markWithdrawSuccess(orderId, accountId, response);
                    return Pair.of(0, response.getMessage());
                case FAIL:
                    log.info("交易订单[{}]，提现失败", order.getOrderNo());
                    this.markWithdrawFail(orderId, accountId, response);
                    return Pair.of(-1, response.getMessage());
                case HANDLING:
                    log.info("交易订单[{}]，提现处理中", order.getOrderNo());
                    this.orderService.updateRemark(order.getId(), response.getMessage());
                    return Pair.of(1,  response.getMessage());
                default:
                    log.error("#####交易订单[{}]，返回状态异常######", order.getOrderNo());
                    break;
            }
        }
        return Pair.of(-1, response.getMessage());
    }

    /**
     * 提现成功
     *
     * @param orderId
     * @param accountId
     * @param response
     */
    private void markWithdrawSuccess(final long orderId, final long accountId,
                                     final PaymentSdkDaiFuResponse response) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        if (order.isWithDrawing()) {
            final Account account = this.accountService.getByIdWithLock(accountId).get();
            order.setStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getId());
            order.setRemark(response.getMessage());
            order.setSn(response.getSn());
            this.orderService.update(order);
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
            this.accountService.decreaseFrozenAmount(accountId, frozenRecord.getFrozenAmount());
            this.accountService.decreaseTotalAmount(accountId, frozenRecord.getFrozenAmount());
            //入账到手续费账户
//            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
//            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
//            this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
//            this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
//                    "提现分润", EnumAccountFlowType.INCREASE);
//            this.withdrawSplitAccount(this.orderService.getByIdWithLock(orderId).get(), shop);
            //推送
            try {
                log.info("订单[]，提现成功，推送", order.getOrderNo());
                final AppBizShop shop = this.hsyShopDao.findAppBizShopByAccountID(accountId).get(0);
                final AppBizCard appBizCard = new AppBizCard();
                appBizCard.setSid(shop.getId());
                final AppBizCard appBizCard1 = this.hsyShopDao.findAppBizCardByParam(appBizCard).get(0);
                final String cardNO = appBizCard1.getCardNO();
                this.pushService.pushCashOutMsg(shop.getUid(), appBizCard1.getCardBank(), order.getTradeAmount().doubleValue(), cardNO.substring(cardNO.length() - 4));
            } catch (final Throwable e) {
                log.error("订单[" + order.getOrderNo() + "]，提现成功，推送异常", e);
            }
        }
    }
    /**
     * 提现失败
     *
     * @param orderId
     * @param accountId
     * @param response
     */
    private void  markWithdrawFail(final long orderId, final long accountId,
                                   final PaymentSdkDaiFuResponse response) {
        log.error("###########【Impossible】#########提现单[{}]提现失败####################", orderId);
    }
}
