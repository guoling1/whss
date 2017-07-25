package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.CallbackResponse;
import com.jkm.hss.bill.helper.PayParams;
import com.jkm.hss.bill.helper.PayResponse;
import com.jkm.hss.bill.helper.SplitProfitParams;
import com.jkm.hss.bill.helper.util.VerifyAuthCodeUtil;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.HSYTransactionService;
import com.jkm.hss.bill.service.TradeService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.notifier.entity.ConsumeMsgSplitProfitRecord;
import com.jkm.hss.notifier.enums.EnumConsumeMsgSplitProfitRecordStatus;
import com.jkm.hss.notifier.service.SendMqMsgService;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.push.sevice.PushService;
import com.jkm.hsy.user.Enum.EnumPolicyType;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.service.UserChannelPolicyService;
import com.jkm.hsy.user.service.UserCurrentChannelPolicyService;
import com.jkm.hsy.user.service.impl.BaseShopSocketExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yulong.zhang on 2017/6/12.
 */
@Slf4j
@Service
public class HSYTransactionServiceImpl implements HSYTransactionService {

    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private PushService pushService;
    @Autowired
    private TradeService tradeService;
    @Autowired
    private HsyUserDao hsyUserDao;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private HSYOrderService hsyOrderService;
    @Autowired
    private SendMqMsgService sendMqMsgService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private UserChannelPolicyService userChannelPolicyService;
    @Autowired
    private BaseHSYTransactionService baseHSYTransactionService;
    @Autowired
    private UserCurrentChannelPolicyService userCurrentChannelPolicyService;
    @Autowired
    private BaseShopSocketExecutorService baseShopSocketExecutorService;

    /**
     * {@inheritDoc}
     *
     * @param channel
     * @param shopId
     * @param memberId
     * @param code
     * @return
     */
    @Override
    public long createOrder(final int channel, final long shopId, final String memberId, final String code) {
        log.info("用户[{}]在店铺[{}]扫静态码创建订单，通道[{}]", memberId, shopId, channel);
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByID(shopId).get(0);
        final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(shop.getAccountID()).get(0);
        final EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.idOf(channel);
        final String channelCode = this.basicChannelService.selectCodeByChannelSign(channel, EnumMerchantPayType.MERCHANT_JSAPI);
        final HsyOrder hsyOrder = new HsyOrder();
        hsyOrder.setShopid(shopId);
        hsyOrder.setShopname(shop.getShortName());
        hsyOrder.setMerchantNo(appAuUser.getGlobalID());
        hsyOrder.setMerchantname(shop.getName());
        hsyOrder.setOrderstatus(EnumHsyOrderStatus.DUE_PAY.getId());
        hsyOrder.setSourcetype(EnumHsySourceType.QRCODE.getId());
        hsyOrder.setValidationcode("");
        hsyOrder.setQrcode(code);
        hsyOrder.setPaychannelsign(channel);
        hsyOrder.setPaytype(channelCode);
        hsyOrder.setMemberId(memberId);
        hsyOrder.setPaymentChannel(enumPayChannelSign.getPaymentChannel().getId());
        hsyOrder.setUpperChannel(enumPayChannelSign.getUpperChannel().getId());
        hsyOrder.setGoodsname(shop.getShortName());
        hsyOrder.setGoodsdescribe(shop.getShortName());
        hsyOrder.setSettleType(EnumBalanceTimeType.T1.getType());
        hsyOrder.setUid(appAuUser.getId());
        hsyOrder.setAccountid(appAuUser.getAccountID());
        hsyOrder.setDealerid(appAuUser.getDealerID());
        this.hsyOrderService.insert(hsyOrder);
        return hsyOrder.getId();
    }

    /**
     * 创建订单
     *
     * @param shopId
     * @param amount
     * @return
     */
    @Override
    public long createOrder2(final long shopId, final long currentUid, final BigDecimal amount) {
        log.info("用户-在店铺[{}]通过被扫创建订单，金额[{}]", shopId, amount);
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByID(shopId).get(0);
        final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(shop.getAccountID()).get(0);
        final AppAuUser currentUser = this.hsyUserDao.findAppAuUserByID(currentUid).get(0);
        final HsyOrder hsyOrder = new HsyOrder();
        hsyOrder.setShopid(shopId);
        hsyOrder.setAmount(amount);
        hsyOrder.setRealAmount(amount);
        hsyOrder.setShopname(shop.getShortName());
        hsyOrder.setMerchantNo(appAuUser.getGlobalID());
        hsyOrder.setMerchantname(shop.getName());
        hsyOrder.setOrderstatus(EnumHsyOrderStatus.DUE_PAY.getId());
        hsyOrder.setSourcetype(EnumHsySourceType.SCAN.getId());
        hsyOrder.setValidationcode("");
        hsyOrder.setQrcode("");
        hsyOrder.setPaychannelsign(0);
        hsyOrder.setPaytype("");
        hsyOrder.setMemberId("");
        hsyOrder.setPaymentChannel(0);
        hsyOrder.setUpperChannel(0);
        hsyOrder.setCashierid(currentUser.getId());
        hsyOrder.setCashiername(currentUser.getRealname());
        hsyOrder.setGoodsname(shop.getShortName());
        hsyOrder.setGoodsdescribe(shop.getShortName());
        hsyOrder.setSettleType(EnumBalanceTimeType.T1.getType());
        hsyOrder.setUid(appAuUser.getId());
        hsyOrder.setAccountid(appAuUser.getAccountID());
        hsyOrder.setDealerid(appAuUser.getDealerID());
        this.hsyOrderService.insert(hsyOrder);
        return hsyOrder.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param totalAmount 订单金额
     * @param hsyOrderId 订单id
     * @return
     */
    @Override
    @Transactional
    public Triple<Integer, String, String> placeOrder(final String totalAmount, final long hsyOrderId) {
        log.info("订单[{}]发起支付请求，金额[{}]", hsyOrderId, totalAmount);
        final HsyOrder hsyOrder = this.hsyOrderService.getByIdWithLock(hsyOrderId).get();
        final long newOrderId = this.baseHSYTransactionService.isNeedCreateNewOrder(hsyOrder);
        HsyOrder tradeHsyOrder = hsyOrder;
        //说明已经请求过交易，用新订单再次请求
        if (newOrderId > 0) {
            tradeHsyOrder = this.hsyOrderService.getByIdWithLock(newOrderId).get();
        }
        if (tradeHsyOrder.isPendingPay()) {
            final BigDecimal amount = new BigDecimal(totalAmount);
            this.hsyOrderService.updateAmountAndStatus(tradeHsyOrder.getId(), amount, EnumHsyOrderStatus.HAVE_REQUESTED_TRADE.getId());
            return this.baseHSYTransactionService.placeOrderImpl(tradeHsyOrder, amount);
        }
        return Triple.of(-1, "订单异常", "");
    }

    /**
     * {@inheritDoc}
     *
     * @param callbackResponse
     */
    @Override
    @Transactional
    public void handlePayCallbackMsg(final CallbackResponse callbackResponse) {
        final HsyOrder hsyOrder = this.hsyOrderService.selectByOrderNo(callbackResponse.getTradeOrderNo()).get();
        if (hsyOrder.isPendingPay() || hsyOrder.isHaveRequestedTrade()) {
            final HsyOrder hsyOrder1 = this.hsyOrderService.getByIdWithLock(hsyOrder.getId()).get();
            if (hsyOrder1.isPendingPay() || hsyOrder.isHaveRequestedTrade()) {
                final EnumBasicStatus status = EnumBasicStatus.of(callbackResponse.getCode());
                final HsyOrder updateOrder = new HsyOrder();
                switch (status) {
                    case SUCCESS:
                        //推送
                        try {
                            log.info("hsy订单[{}],开始推送", callbackResponse.getTradeOrderNo());
                            this.pushService.pushCashMsg(hsyOrder1.getShopid(), EnumPaymentChannel.of(hsyOrder1.getPaymentChannel()).getValue(),
                                    hsyOrder1.getAmount().doubleValue(), hsyOrder1.getValidationcode(), hsyOrder1.getOrderno());
                            log.info("hsy订单[{}],推送调用结束", callbackResponse.getTradeOrderNo());
                        } catch (final Throwable e) {
                            log.error("订单[" + hsyOrder1.getOrderno() + "]，支付成功，推送异常", e);
                        }

                        updateOrder.setId(hsyOrder1.getId());
                        updateOrder.setOrderstatus(EnumHsyOrderStatus.PAY_SUCCESS.getId());
                        updateOrder.setPoundage(callbackResponse.getPoundage());
                        updateOrder.setPaysn(callbackResponse.getSn());
                        updateOrder.setPaysuccesstime(callbackResponse.getSuccessTime());
                        updateOrder.setRemark(callbackResponse.getMessage());

                        if (!StringUtils.isEmpty(hsyOrder1.getPaytype())
                                && hsyOrder1.getPaytype().contains(EnumMerchantPayType.MERCHANT_JSAPI.getId())) {
                            final JSONObject jo = new JSONObject();
                            jo.put("orderNo", hsyOrder1.getOrdernumber());
                            jo.put("tradeOrderNo", hsyOrder1.getOrderno());
                            jo.put("status", updateOrder.getOrderstatus());
                            jo.put("paySuccessTime", updateOrder.getPaysuccesstime());
                            jo.put("shopName", hsyOrder1.getShopname());
                            jo.put("tradeAmount", hsyOrder1.getAmount());
                            jo.put("discountAmount", "0.00");
                            jo.put("totalAmount", hsyOrder1.getRealAmount());
                            jo.put("payChannel", hsyOrder1.getPaymentChannel());
                            this.baseShopSocketExecutorService.runTask(hsyOrder1.getShopid(), jo.toJSONString());
                        }

                        this.hsyOrderService.update(updateOrder);
                        //生成分润消息记录
                        final ConsumeMsgSplitProfitRecord consumeMsgSplitProfitRecord = new ConsumeMsgSplitProfitRecord();
                        consumeMsgSplitProfitRecord.setHsyOrderId(hsyOrder1.getId());
                        consumeMsgSplitProfitRecord.setRequestParam("");
                        consumeMsgSplitProfitRecord.setTag(MqConfig.SPLIT_PROFIT);
                        consumeMsgSplitProfitRecord.setStatus(EnumConsumeMsgSplitProfitRecordStatus.PENDING_SEND.getId());
                        final long consumeMsgSplitProfitRecordId = this.sendMqMsgService.add(consumeMsgSplitProfitRecord);
                        //发消息分润
                        final JSONObject requestJsonObject = new JSONObject();
                        requestJsonObject.put("consumeMsgSplitProfitRecordId", consumeMsgSplitProfitRecordId);
                        MqProducer.produce(requestJsonObject, MqConfig.SPLIT_PROFIT, 20000);
                        break;
                    case FAIL:
                        updateOrder.setId(hsyOrder1.getId());
                        updateOrder.setOrderstatus(EnumHsyOrderStatus.PAY_FAIL.getId());
                        updateOrder.setPaysn(callbackResponse.getSn());
                        updateOrder.setRemark(callbackResponse.getMessage());
                        hsyOrderService.update(updateOrder);
                        break;
                    case HANDLING:
                        updateOrder.setRemark(callbackResponse.getMessage());
                        this.hsyOrderService.update(updateOrder);
                        break;
                    default:
                        log.error("交易回调业务，返回状态码[{}]异常", callbackResponse.getCode());
                        break;
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param consumeMsgSplitProfitRecordId
     */
    @Override
    @Transactional
    public void paySplitProfit(final long consumeMsgSplitProfitRecordId) {
        log.info("记录[{}],开始分润", consumeMsgSplitProfitRecordId);
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final ConsumeMsgSplitProfitRecord consumeMsgSplitProfitRecord = this.sendMqMsgService.getByIdWithLock(consumeMsgSplitProfitRecordId).get();
        if (consumeMsgSplitProfitRecord.isPendingSend()) {
            final HsyOrder hsyOrder = this.hsyOrderService.getById(consumeMsgSplitProfitRecord.getHsyOrderId()).get();
            final SplitProfitParams splitProfitParams = new SplitProfitParams();
            final Map<String, Triple<Long, BigDecimal, BigDecimal>> shallProfitMap = this.dealerService.shallProfit(EnumProductType.HSY, hsyOrder.getOrderno(),
                    hsyOrder.getAmount(), hsyOrder.getPaychannelsign(), hsyOrder.getShopid());
            if (!shallProfitMap.isEmpty()) {
                final Triple<Long, BigDecimal, BigDecimal> basicMoneyTriple = shallProfitMap.get("basicMoney");
                final Triple<Long, BigDecimal, BigDecimal> channelMoneyTriple = shallProfitMap.get("channelMoney");
                final Triple<Long, BigDecimal, BigDecimal> productMoneyTriple = shallProfitMap.get("productMoney");
                final Triple<Long, BigDecimal, BigDecimal> firstMoneyTriple = shallProfitMap.get("firstMoney");
                final Triple<Long, BigDecimal, BigDecimal> secondMoneyTriple = shallProfitMap.get("secondMoney");
                splitProfitParams.setOrderNo(hsyOrder.getOrderno());
                splitProfitParams.setSplitType(EnumSplitBusinessType.HSYPAY.getId());
                splitProfitParams.setCost(basicMoneyTriple.getMiddle());
                splitProfitParams.setSettleType(hsyOrder.getSettleType());
                final ArrayList<SplitProfitParams.SplitProfitDetail> splitProfitDetails = new ArrayList<>(5);
                splitProfitParams.setSplitProfitDetails(splitProfitDetails);
                if (null != channelMoneyTriple) {
                    final SplitProfitParams.SplitProfitDetail splitProfitDetail = splitProfitParams.new SplitProfitDetail();
                    splitProfitDetails.add(splitProfitDetail);
                    splitProfitDetail.setAccountId(channelMoneyTriple.getLeft());
                    splitProfitDetail.setUserNo("");
                    splitProfitDetail.setUserName("通道账户");
                    splitProfitDetail.setProfit(channelMoneyTriple.getMiddle());
                    splitProfitDetail.setRate(channelMoneyTriple.getRight());
                    splitProfitDetail.setAccountUserType(EnumAccountUserType.COMPANY.getId());
                    splitProfitDetail.setLevel(1);
                }
                if (null != productMoneyTriple) {
                    final SplitProfitParams.SplitProfitDetail splitProfitDetail = splitProfitParams.new SplitProfitDetail();
                    splitProfitDetails.add(splitProfitDetail);
                    splitProfitDetail.setAccountId(productMoneyTriple.getLeft());
                    splitProfitDetail.setUserNo("");
                    splitProfitDetail.setUserName("产品账户");
                    splitProfitDetail.setProfit(productMoneyTriple.getMiddle());
                    splitProfitDetail.setRate(productMoneyTriple.getRight());
                    splitProfitDetail.setAccountUserType(EnumAccountUserType.COMPANY.getId());
                    splitProfitDetail.setLevel(2);
                }
                if (null != firstMoneyTriple) {
                    final SplitProfitParams.SplitProfitDetail splitProfitDetail = splitProfitParams.new SplitProfitDetail();
//                    final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(firstMoneyTriple.getLeft()).get(0);
                    final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
                    splitProfitDetails.add(splitProfitDetail);
                    splitProfitDetail.setAccountId(firstMoneyTriple.getLeft());
                    splitProfitDetail.setUserNo(dealer.getMarkCode());
                    splitProfitDetail.setUserName(dealer.getProxyName());
                    splitProfitDetail.setProfit(firstMoneyTriple.getMiddle());
                    splitProfitDetail.setRate(firstMoneyTriple.getRight());
                    splitProfitDetail.setAccountUserType(EnumAccountUserType.DEALER.getId());
                    splitProfitDetail.setLevel(1);
                }
                if (null != secondMoneyTriple) {
                    final SplitProfitParams.SplitProfitDetail splitProfitDetail = splitProfitParams.new SplitProfitDetail();
//                    final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(secondMoneyTriple.getLeft()).get(0);
                    final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
                    splitProfitDetails.add(splitProfitDetail);
                    splitProfitDetail.setAccountId(secondMoneyTriple.getLeft());
                    splitProfitDetail.setUserNo(dealer.getMarkCode());
                    splitProfitDetail.setUserName(dealer.getProxyName());
                    splitProfitDetail.setProfit(secondMoneyTriple.getMiddle());
                    splitProfitDetail.setRate(secondMoneyTriple.getRight());
                    splitProfitDetail.setAccountUserType(EnumAccountUserType.DEALER.getId());
                    splitProfitDetail.setLevel(2);
                }
                final Pair<Integer, String> splitProfitResult = this.tradeService.splitProfitImpl(splitProfitParams);
                if (0 == splitProfitResult.getLeft()) {
                    this.sendMqMsgService.updateStatus(consumeMsgSplitProfitRecordId, EnumConsumeMsgSplitProfitRecordStatus.SUCCESS_SEND.getId());
                }
                stopWatch.stop();
                log.info("记录[{}],分润结束,用时[{}]", consumeMsgSplitProfitRecordId, stopWatch.getTime());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleRetrySplitProfitTask() {
        final List<ConsumeMsgSplitProfitRecord> records = this.sendMqMsgService.getPendingRecordsByTag(MqConfig.SPLIT_PROFIT);
        log.info("处理重发分润任务，个数[{}]", records.size());
        if (!CollectionUtils.isEmpty(records)) {
            for (int i = 0; i < records.size(); i++) {
                this.paySplitProfit(records.get(i).getId());

                try {
                    Thread.sleep(5000);
                } catch (final Throwable e) {
                    log.error("处理重发分润任务异常", e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param paramData
     * @param appParam
     * @return
     */
    @Override
    @Transactional
    public String refund(final String paramData, final AppParam appParam) {

        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param authCode
     * @return
     */
    @Override
    public Pair<Integer, String> authCodePay(final long orderId, final String authCode) {
        final Optional<HsyOrder> orderOptional = this.hsyOrderService.getById(orderId);
        if (!orderOptional.isPresent()) {
            return Pair.of(-1, "订单不存在");
        }
        if (orderOptional.get().isPendingPay()) {
            final EnumPolicyType policyType = VerifyAuthCodeUtil.verify(authCode);
            final HsyOrder updateHsyOrder = new HsyOrder();
            updateHsyOrder.setId(orderId);
            final UserCurrentChannelPolicy userCurrentChannelPolicy =
                    this.userCurrentChannelPolicyService.selectByUserId(orderOptional.get().getUid()).get();
            switch (policyType) {
                case ALIPAY:
                    updateHsyOrder.setPaychannelsign(userCurrentChannelPolicy.getAlipayChannelTypeSign());
                    break;
                case WECHAT:
                    updateHsyOrder.setPaychannelsign(userCurrentChannelPolicy.getWechatChannelTypeSign());
                    break;
            }
            final UserChannelPolicy userChannelPolicy =
                    this.userChannelPolicyService.selectByUserIdAndChannelTypeSign(orderOptional.get().getUid(), updateHsyOrder.getPaychannelsign()).get();
            updateHsyOrder.setSettleType(userChannelPolicy.getSettleType());
            updateHsyOrder.setPaytype(basicChannelService.selectCodeByChannelSign(updateHsyOrder.getPaychannelsign(), EnumMerchantPayType.MERCHANT_BAR));
            final EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.idOf(updateHsyOrder.getPaychannelsign());
            updateHsyOrder.setPaymentChannel(enumPayChannelSign.getPaymentChannel().getId());
            updateHsyOrder.setUpperChannel(enumPayChannelSign.getUpperChannel().getId());
            this.hsyOrderService.update(updateHsyOrder);

            final PayParams payParams=new PayParams();
            payParams.setBusinessOrderNo(orderOptional.get().getOrdernumber());
            payParams.setChannel(updateHsyOrder.getPaychannelsign());
            payParams.setMerchantPayType(EnumMerchantPayType.MERCHANT_BAR);
            payParams.setAppId(EnumAppType.HSY.getId());
            payParams.setTradeAmount(orderOptional.get().getAmount());
            payParams.setRealPayAmount(orderOptional.get().getRealAmount());
            payParams.setServiceType(EnumServiceType.RECEIVE_MONEY.getId());
            payParams.setPayeeAccountId(orderOptional.get().getAccountid());
            payParams.setGoodsName(orderOptional.get().getGoodsname());
            payParams.setGoodsDescribe(orderOptional.get().getGoodsdescribe());
            payParams.setWxAppId(WxConstants.APP_HSY_ID);
            payParams.setMerchantNo(orderOptional.get().getMerchantNo());
            payParams.setMerchantName(orderOptional.get().getMerchantname());
            payParams.setAuthCode(authCode);
            final PayResponse payResponse=tradeService.pay(payParams);
            final EnumBasicStatus status = EnumBasicStatus.of(payResponse.getCode());
            final HsyOrder updateOrder = new HsyOrder();
            switch (status) {
                case FAIL:
                    updateOrder.setId(orderOptional.get().getId());
                    updateOrder.setOrderno(payResponse.getTradeOrderNo());
                    updateOrder.setOrderid(payResponse.getTradeOrderId());
                    updateOrder.setOrderstatus(EnumOrderStatus.PAY_FAIL.getId());
                    updateOrder.setRemark(payResponse.getMessage());
                    this.hsyOrderService.update(updateOrder);
                    return Pair.of(-1, payResponse.getMessage());
                case SUCCESS:
                    updateOrder.setId(orderOptional.get().getId());
                    updateOrder.setOrderno(payResponse.getTradeOrderNo());
                    updateOrder.setValidationcode(payResponse.getTradeOrderNo().substring(payResponse.getTradeOrderNo().length() - 4));
                    updateOrder.setOrderid(payResponse.getTradeOrderId());
                    updateOrder.setRemark(payResponse.getMessage());
                    this.hsyOrderService.update(updateOrder);
                    return Pair.of(0, payResponse.getMessage());
            }
        }
        return Pair.of(-1, "订单状态错误");
    }
}
