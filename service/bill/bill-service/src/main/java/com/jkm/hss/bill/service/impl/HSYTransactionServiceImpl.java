package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumSplitBusinessType;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.enums.EnumBasicStatus;
import com.jkm.hss.bill.enums.EnumHsyOrderStatus;
import com.jkm.hss.bill.enums.EnumHsySourceType;
import com.jkm.hss.bill.helper.CallbackResponse;
import com.jkm.hss.bill.helper.SplitProfitParams;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.HSYTransactionService;
import com.jkm.hss.bill.service.TradeService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.notifier.entity.ConsumeMsgSplitProfitRecord;
import com.jkm.hss.notifier.enums.EnumConsumeMsgSplitProfitRecordStatus;
import com.jkm.hss.notifier.service.SendMqMsgService;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.push.sevice.PushService;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
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
    private DealerService dealerService;
    @Autowired
    private HSYOrderService hsyOrderService;
    @Autowired
    private SendMqMsgService sendMqMsgService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private BaseHSYTransactionService baseHSYTransactionService;

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
    @Transactional
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
        hsyOrder.setGoodsname(shop.getShortName());
        hsyOrder.setGoodsdescribe(shop.getShortName());
        hsyOrder.setSettleType(EnumBalanceTimeType.T1.getType());
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
        if (hsyOrder.isPendingPay()) {
            final BigDecimal amount = new BigDecimal(totalAmount);
            this.hsyOrderService.updateAmount(hsyOrderId, amount);
            return this.baseHSYTransactionService.placeOrderImpl(hsyOrder,amount);
        }
        return Triple.of(-1, "订单状态异常", "");
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
        if (hsyOrder.isPendingPay()) {
            final HsyOrder hsyOrder1 = this.hsyOrderService.getByIdWithLock(hsyOrder.getId()).get();
            if (hsyOrder1.isPendingPay()) {
                final EnumBasicStatus status = EnumBasicStatus.of(callbackResponse.getCode());
                final HsyOrder updateOrder = new HsyOrder();
                switch (status) {
                    case SUCCESS:
                        //推送
                        try {
                            this.pushService.pushCashMsg(hsyOrder1.getShopid(), EnumPaymentChannel.of(hsyOrder1.getPaymentChannel()).getValue(),
                                    hsyOrder1.getAmount().doubleValue(), hsyOrder1.getValidationcode(), hsyOrder1.getOrderno());
                        } catch (final Throwable e) {
                            log.error("订单[" + hsyOrder1.getOrderno() + "]，支付成功，推送异常", e);
                        }

                        updateOrder.setId(hsyOrder1.getId());
                        updateOrder.setOrderstatus(EnumHsyOrderStatus.PAY_SUCCESS.getId());
                        updateOrder.setPoundage(callbackResponse.getPoundage());
                        updateOrder.setPaysn(callbackResponse.getSn());
                        updateOrder.setPaysuccesstime(callbackResponse.getSuccessTime());
                        updateOrder.setRemark(callbackResponse.getMessage());
                        this.hsyOrderService.update(hsyOrder);
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
                        hsyOrderService.update(hsyOrder);
                        break;
                    case HANDLING:
                        updateOrder.setRemark(callbackResponse.getMessage());
                        this.hsyOrderService.update(hsyOrder);
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
                    final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(firstMoneyTriple.getLeft()).get(0);
                    splitProfitDetails.add(splitProfitDetail);
                    splitProfitDetail.setAccountId(firstMoneyTriple.getLeft());
                    splitProfitDetail.setUserNo(appAuUser.getGlobalID());
                    splitProfitDetail.setUserName(appAuUser.getShopShortName());
                    splitProfitDetail.setProfit(firstMoneyTriple.getMiddle());
                    splitProfitDetail.setRate(firstMoneyTriple.getRight());
                    splitProfitDetail.setAccountUserType(EnumAccountUserType.DEALER.getId());
                    splitProfitDetail.setLevel(1);
                }
                if (null != secondMoneyTriple) {
                    final SplitProfitParams.SplitProfitDetail splitProfitDetail = splitProfitParams.new SplitProfitDetail();
                    final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(secondMoneyTriple.getLeft()).get(0);
                    splitProfitDetails.add(splitProfitDetail);
                    splitProfitDetail.setAccountId(secondMoneyTriple.getLeft());
                    splitProfitDetail.setUserNo(appAuUser.getGlobalID());
                    splitProfitDetail.setUserName(appAuUser.getShopShortName());
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
                log.info("记录[{}],开始分润,用时[{}]", consumeMsgSplitProfitRecordId, stopWatch.getTime());
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
}
