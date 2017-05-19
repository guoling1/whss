package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumBoolean;
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
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.AppStatisticsOrder;
import com.jkm.hss.bill.helper.HolidaySettlementConstants;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.helper.requestparam.TradeListRequestParam;
import com.jkm.hss.bill.helper.responseparam.HsyTradeListResponse;
import com.jkm.hss.bill.service.*;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.merchant.service.SendMsgService;
import com.jkm.hss.notifier.enums.EnumVerificationCodeType;
import com.jkm.hss.notifier.service.SmsAuthService;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.push.sevice.PushService;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.sun.tools.javac.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.List;

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
    private ShallProfitDetailService shallProfitDetailService;
    @Autowired
    private PushService pushService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private HttpClientFacade httpClientFacade;
    @Autowired
    private MergeTableSettlementDateService mergeTableSettlementDateService;
    @Autowired
    private RefundOrderService refundOrderService;
    @Autowired
    private SplitAccountRecordService splitAccountRecordService;
    @Autowired
    private SplitAccountRefundRecordService splitAccountRefundRecordService;
    @Autowired
    private SendMsgService sendMsgService;
    @Autowired
    private HSYOrderService hsyOrderService;

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
        final BigDecimal merchantWithdrawPoundage = new BigDecimal("2");//this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSY, shop.getId(), channel);
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
                jo.put("channel", EnumPayChannelSign.idOf(order.getPayChannelSign()).getPaymentChannel().getValue());
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
     * @param dataParam
     * @param appParam
     * @return
     */
    @Override
    public String tradeList1(final String dataParam, final AppParam appParam) {
        final JSONObject dataJo = JSONObject.parseObject(dataParam);
        final long accountId = dataJo.getLongValue("accountId");
        final int pageNo = dataJo.getIntValue("pageNo");
        final int pageSize = dataJo.getIntValue("pageSize");
        final String channel = dataJo.getString("channel");
        final String[] paymentChannels = channel.split(",");
        final String startTimeStr = dataJo.getString("startTime");
        final String endTimeStr = dataJo.getString("endTime");
        final PageModel<JSONObject> pageModel = new PageModel<>(pageNo, pageSize);
        if (paymentChannels.length == 0) {
            pageModel.setCount(0);
            pageModel.setRecords(Collections.<JSONObject>emptyList());
            return JSON.toJSONString(pageModel);
        }
        final ArrayList<Integer> payChannelSigns = new ArrayList<>();
        for (int i = 0; i < paymentChannels.length; i++) {
            payChannelSigns.addAll(EnumPayChannelSign.getIdListByPaymentChannel(Integer.valueOf(paymentChannels[i])));
        }
        Date startTime = null;
        Date endTime = null;
        if (!StringUtils.isEmpty(startTimeStr) && !StringUtils.isEmpty(startTimeStr)) {
            startTime = DateFormatUtil.parse(startTimeStr, DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        if (!StringUtils.isEmpty(endTimeStr) && !StringUtils.isEmpty(endTimeStr)) {
            endTime = DateFormatUtil.parse(endTimeStr, DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        final long count = this.orderService.getOrderCountByParam(accountId, EnumAppType.HSY.getId(), payChannelSigns, startTime, endTime);
        final List<Order> orders= this.orderService.getOrdersByParam(accountId, EnumAppType.HSY.getId(),
                pageModel.getFirstIndex(), pageSize, payChannelSigns, startTime, endTime);
        pageModel.setCount(count);
        if (!CollectionUtils.isEmpty(orders)) {
            final List<JSONObject> recordList = new ArrayList<>();
            pageModel.setRecords(recordList);
            final HashSet<Date> dateHashSet = new HashSet<>();
            for (Order order: orders) {
                dateHashSet.add(DateFormatUtil.parse(DateFormatUtil.format(order.getCreateTime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd));
            }
            final Iterator<Date> iterator = dateHashSet.iterator();
            final HashMap<Date, AppStatisticsOrder> statisticsOrderHashMap = new HashMap<>();
            while (iterator.hasNext()) {
                final Date nextDate = iterator.next();
                final String sDate = DateFormatUtil.format(nextDate, DateFormatUtil.yyyy_MM_dd) + " 00:00:00";
                final String eDate = DateFormatUtil.format(nextDate, DateFormatUtil.yyyy_MM_dd) + " 23:59:59";
                final AppStatisticsOrder statisticsOrder = this.orderService.statisticsByParam(accountId, EnumAppType.HSY.getId(), payChannelSigns, sDate, eDate);
                statisticsOrderHashMap.put(nextDate, statisticsOrder);
            }
            for (Order order : orders) {
                final JSONObject jo = new JSONObject();
                recordList.add(jo);
                final Date payDate = DateFormatUtil.parse(DateFormatUtil.format(order.getPaySuccessTime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
                final Date refundDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
                if (order.isRefundSuccess() || order.isSettled() || payDate.compareTo(refundDate) != 0) {
                    jo.put("canRefund", 0);
                } else {
                    jo.put("canRefund", 1);
                }
                final Date parseDate = DateFormatUtil.parse(DateFormatUtil.format(order.getCreateTime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
                final AppStatisticsOrder statisticsOrder = statisticsOrderHashMap.get(parseDate);
                jo.put("number", statisticsOrder.getNumber());
                jo.put("totalAmount", null != statisticsOrder.getAmount() ? statisticsOrder.getAmount().toPlainString() : "0.00");
                jo.put("refundStatus", order.getRefundStatus());
                jo.put("refundStatusValue", EnumOrderRefundStatus.of(order.getRefundStatus()).getValue());
                if (EnumTradeType.PAY.getId() == order.getTradeType()) {
                    jo.put("tradeType", "1");
                } else if (EnumTradeType.WITHDRAW.getId() == order.getTradeType()) {
                    jo.put("tradeType", "2");
                }
                jo.put("channel", EnumPayChannelSign.idOf(order.getPayChannelSign()).getPaymentChannel().getId());
                if (EnumOrderStatus.PAY_SUCCESS.getId() == order.getStatus()) {
                    jo.put("msg", "收款成功");
                } else if (EnumOrderStatus.WITHDRAW_SUCCESS.getId() == order.getStatus()) {
                    jo.put("msg", "提现成功");
                }
                jo.put("amount", order.getTradeAmount().toPlainString());
                jo.put("time", order.getCreateTime());
                jo.put("code", order.getOrderNo().substring(order.getOrderNo().length() - 4));
                jo.put("orderId", order.getId());
            }
        } else {
            pageModel.setRecords(Collections.<JSONObject>emptyList());
        }

        return JSON.toJSONString(pageModel);
    }

    @Override
    public String tradeListhsy(final String dataParam, final AppParam appParam) {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();

        /**参数转化*/
        TradeListRequestParam tradeListRequestParam=null;
            tradeListRequestParam=gson.fromJson(dataParam, TradeListRequestParam.class);
        final PageModel<HsyTradeListResponse> pageModel = new PageModel<>(tradeListRequestParam.getPageNo(), tradeListRequestParam.getPageSize());
        pageModel.setCount(1);

        List<HsyTradeListResponse> hsyTradeListResponseList=new ArrayList<HsyTradeListResponse>();
        HsyTradeListResponse hsyTradeListResponse=new HsyTradeListResponse();
        hsyTradeListResponse.setAmount(new BigDecimal(10));
        hsyTradeListResponse.setNumber(1);
        hsyTradeListResponse.setValidationCode("1234");
        hsyTradeListResponse.setOrderstatusName("收款成功");
        hsyTradeListResponseList.add(hsyTradeListResponse);
        pageModel.setRecords(hsyTradeListResponseList);
        Map<String,Object> map=new HashMap<>();
        map.put("amount",10);
        map.put("number",1);
        map.put("pageModel",pageModel);
        return gson.toJson(map);
    }

    public String appOrderDetailhsy(String dataParam, AppParam appParam){
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        HsyTradeListResponse hsyTradeListResponse=new HsyTradeListResponse();
        return gson.toJson(hsyTradeListResponse);
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
        final String code=paramJo.getString("code");//add by wayne 2017/05/17
        final Pair<Integer, String> receiptResult = this.receipt(totalAmount, channel, shopId, appId, "",code);
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
     * @param paramData
     * @param appParam
     * @return
     */
    @Override
    public String appOrderDetail(final String paramData, final AppParam appParam) {
        final JSONObject result = new JSONObject();
        final JSONObject paramJo = JSONObject.parseObject(paramData);
        final long payOrderId = paramJo.getLongValue("payOrderId");
        final Order order = this.orderService.getById(payOrderId).get();
        final AppBizShop appBizShop = this.hsyShopDao.findAppBizShopByAccountID(order.getPayee()).get(0);
        result.put("amount", order.getTradeAmount().toPlainString());
        result.put("code", order.getOrderNo().substring(order.getOrderNo().length() - 4));
        result.put("merchantName", appBizShop.getName());
        result.put("time", order.getCreateTime());
        result.put("status", EnumOrderStatus.of(order.getStatus()).getValue());
        result.put("refundStatus", EnumOrderRefundStatus.of(order.getRefundStatus()).getValue());
        result.put("channel", EnumPayChannelSign.idOf(order.getPayChannelSign()).getPaymentChannel().getId());
        result.put("orderNo", order.getOrderNo());
        result.put("settleStatus", EnumSettleStatus.of(order.getSettleStatus()).getValue());
        return result.toJSONString();
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
    synchronized public String appRefund(final String paramData, final AppParam appParam) {
        final JSONObject result = new JSONObject();
        final JSONObject paramJo = JSONObject.parseObject(paramData);
        final long payOrderId = paramJo.getLongValue("payOrderId");
        final String password = paramJo.getString("password");

//        final BigDecimal refundAmount = new BigDecimal(paramJo.getString("refundAmount"));
        final Order payOrder = this.orderService.getByIdWithLock(payOrderId).get();
        final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(payOrder.getPayee()).get(0);
        if (StringUtils.isEmpty(password) || !password.equals(appAuUser.getPassword())) {
            result.put("code", -2);
            result.put("msg", "密码错误");
            return result.toJSONString();
        }
        if (payOrder.isRefundSuccess()) {
            result.put("code", -1);
            result.put("msg", "已退款");
            return result.toJSONString();
        }
//        final BigDecimal refundedAmount = this.refundOrderService.getRefundedAmount(payOrderId);
//        if (payOrder.getRealPayAmount().subtract(refundedAmount).compareTo(refundAmount) < 0) {
//            result.put("code", -1);
//            result.put("msg", "可退金额不足");
//            return result.toJSONString();
//        }
        final Date payDate = DateFormatUtil.parse(DateFormatUtil.format(payOrder.getPaySuccessTime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
        final Date refundDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
        if (payOrder.isSettled() || payOrder.isRefundSuccess() || payDate.compareTo(refundDate) != 0) {
            result.put("code", -1);
            result.put("msg", "只可以退当日订单");
            return result.toJSONString();
        }
        final List<RefundOrder> refundOrders = this.refundOrderService.getByPayOrderId(payOrderId);
        if (!CollectionUtils.isEmpty(refundOrders)) {
            result.put("code", -1);
            result.put("msg", "退款异常.");
            return result.toJSONString();
        }
        final RefundOrder refundOrder = new RefundOrder();
        refundOrder.setBatchNo("");
        refundOrder.setAppId(payOrder.getAppId());
        refundOrder.setOrderNo(SnGenerator.generateRefundSn());
        refundOrder.setPayOrderId(payOrder.getId());
        refundOrder.setPayOrderNo(payOrder.getOrderNo());
        refundOrder.setSn("");
        refundOrder.setRefundAccountId(payOrder.getPayee());
        refundOrder.setRefundAmount(payOrder.getRealPayAmount());
        refundOrder.setMerchantRefundAmount(payOrder.getRealPayAmount().subtract(payOrder.getPoundage()));
        refundOrder.setPoundageRefundAmount(payOrder.getPoundage());
        refundOrder.setUpperChannel(EnumPayChannelSign.idOf(payOrder.getPayChannelSign()).getUpperChannel().getId());
        refundOrder.setStatus(EnumRefundOrderStatus.REFUNDING.getId());
        this.refundOrderService.add(refundOrder);
        final Pair<Integer, String> resultPair = this.refundImpl(refundOrder, payOrder);
        if (0 == resultPair.getLeft()) {
            result.put("code", 0);
            result.put("msg", "退款成功");
            result.put("refundTime", this.refundOrderService.getById(refundOrder.getId()).get().getFinishTime());
        } else {
            result.put("code", -1);
            result.put("msg", "退款失败");
        }
        return result.toJSONString();
    }

    /**
     * {@inheritDoc}
     *
     * @param refundOrder 退款单
     * @param payOrder 交易单
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> refundImpl(final RefundOrder refundOrder, final Order payOrder) {
        //退款到手续费账户
        this.refund2Poundage(refundOrder, payOrder);
        //退商户款
        this.refundMerchant(refundOrder, payOrder);
        //手续费出款
        this.refundPoundageOutAccount(refundOrder, payOrder);
        //请求退款
        final PaymentSdkRefundRequest paymentSdkRefundRequest = new PaymentSdkRefundRequest();
        paymentSdkRefundRequest.setAppId(refundOrder.getAppId());
        paymentSdkRefundRequest.setOrderNo(payOrder.getOrderNo());
        paymentSdkRefundRequest.setRefundOrderNo(refundOrder.getOrderNo());
        paymentSdkRefundRequest.setAmount(refundOrder.getRefundAmount().toPlainString());
        final String content = this.httpClientFacade.jsonPost(PaymentSdkConstants.SDK_PAY_REFUND, SdkSerializeUtil.convertObjToMap(paymentSdkRefundRequest));
        final PaymentSdkRefundResponse paymentSdkRefundResponse = JSON.parseObject(content, PaymentSdkRefundResponse.class);
        final EnumBasicStatus status = EnumBasicStatus.of(paymentSdkRefundResponse.getCode());
        switch (status) {
            case FAIL:
                log.error("退款[{}], 失败", refundOrder.getId());
                final RefundOrder refundOrder1 = new RefundOrder();
                refundOrder1.setId(refundOrder.getId());
                refundOrder1.setStatus(EnumRefundOrderStatus.REFUND_FAIL.getId());
                refundOrder1.setRemark("退款失败");
                refundOrder1.setMessage(paymentSdkRefundResponse.getMessage());
                this.refundOrderService.update(refundOrder1);
                return Pair.of(-1, paymentSdkRefundResponse.getMessage());
            case SUCCESS:
                this.orderService.updateRefundInfo(payOrder.getId(), refundOrder.getRefundAmount(), EnumOrderRefundStatus.REFUND_SUCCESS);
                this.orderService.updateRemark(payOrder.getId(), paymentSdkRefundResponse.getMessage());
                final RefundOrder refundOrder2 = new RefundOrder();
                refundOrder2.setId(refundOrder.getId());
                refundOrder2.setStatus(EnumRefundOrderStatus.REFUND_SUCCESS.getId());
                refundOrder2.setRemark("退款成功");
                refundOrder2.setMessage(paymentSdkRefundResponse.getMessage());
                refundOrder2.setFinishTime(DateFormatUtil.parse(paymentSdkRefundResponse.getSuccessTime(), DateFormatUtil.yyyyMMddHHmmss));
                this.refundOrderService.update(refundOrder2);
                if (EnumPaymentChannel.WECHAT_PAY.getId() == EnumPayChannelSign.idOf(payOrder.getPayChannelSign()).getPaymentChannel().getId()) {
                    try {
                        this.sendMsgService.refundSendMessage(payOrder.getOrderNo(), refundOrder.getRefundAmount(), payOrder.getPayAccount());
                    } catch (final Throwable e) {
                        log.error("推送失败");
                    }
                }
                return Pair.of(0, "退款成功");
            default:
                log.error("退款[{}]， 网关返回状态异常", refundOrder.getId());
                return Pair.of(-1, "退款网关异常");
        }
    }


    /**
     * {@inheritDoc}
     *
     * @param refundOrder  退款单
     * @param payOrder  交易单
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> refund2Poundage(final RefundOrder refundOrder, final Order payOrder) {
        final List<SplitAccountRecord> splitAccountRecords = this.splitAccountRecordService.getByOrderNo(payOrder.getOrderNo());
        if (!CollectionUtils.isEmpty(splitAccountRecords)) {
            for (SplitAccountRecord splitAccountRecord : splitAccountRecords) {
                final SplitAccountRefundRecord splitAccountRefundRecord = new SplitAccountRefundRecord();
                splitAccountRefundRecord.setPayOrderNo(payOrder.getOrderNo());
                splitAccountRefundRecord.setBatchNo(refundOrder.getBatchNo());
                splitAccountRefundRecord.setRefundOrderNo(refundOrder.getOrderNo());
                splitAccountRefundRecord.setAccountUserType(splitAccountRecord.getAccountUserType());
                splitAccountRefundRecord.setOriginalSplitSn(splitAccountRecord.getSplitSn());
                splitAccountRefundRecord.setRefundAmount(splitAccountRecord.getSplitAmount());
                splitAccountRefundRecord.setSplitAmount(splitAccountRecord.getSplitAmount());
                splitAccountRefundRecord.setSplitTotalAmount(splitAccountRecord.getSplitTotalAmount());
                splitAccountRefundRecord.setOutMoneyAccountId(splitAccountRecord.getReceiptMoneyAccountId());
                splitAccountRefundRecord.setOutMoneyUserName(splitAccountRecord.getReceiptMoneyUserName());
                splitAccountRefundRecord.setReceiptMoneyAccountId(splitAccountRecord.getOutMoneyAccountId());
                splitAccountRefundRecord.setRemark("收单分润退款");
                splitAccountRefundRecord.setRefundTime(new Date());
                this.splitAccountRefundRecordService.add(splitAccountRefundRecord);
            }
        }
        final List<SettleAccountFlow> settleAccountFlows = this.settleAccountFlowService.getByOrderNo(payOrder.getOrderNo());
        if (!CollectionUtils.isEmpty(settleAccountFlows)) {
            for (SettleAccountFlow settleAccountFlow: settleAccountFlows) {
                if (settleAccountFlow.getAccountId() == payOrder.getPayee()) {
                    continue;
                }
                //TODO
                Preconditions.checkState(EnumAccountFlowType.INCREASE.getId() == settleAccountFlow.getType(),
                        "订单[{}], 全额退款，出现已结算的待结算流水", payOrder.getId());
                final Account account = this.accountService.getByIdWithLock(settleAccountFlow.getAccountId()).get();
                Preconditions.checkState(account.getDueSettleAmount().compareTo(settleAccountFlow.getIncomeAmount()) >= 0, "账户[{}]余额不足，退分润失败", account.getId());
                this.accountService.decreaseTotalAmount(account.getId(), settleAccountFlow.getIncomeAmount());
                this.accountService.decreaseSettleAmount(account.getId(), settleAccountFlow.getIncomeAmount());
                final SettleAccountFlow decreaseSettleAccountFlow = new SettleAccountFlow();
                decreaseSettleAccountFlow.setFlowNo("");
                decreaseSettleAccountFlow.setAccountId(settleAccountFlow.getAccountId());
                decreaseSettleAccountFlow.setAccountUserType(settleAccountFlow.getAccountUserType());
                decreaseSettleAccountFlow.setOrderNo(payOrder.getOrderNo());
                decreaseSettleAccountFlow.setRefundOrderNo(refundOrder.getOrderNo());
                decreaseSettleAccountFlow.setBeforeAmount(account.getDueSettleAmount());
                decreaseSettleAccountFlow.setAfterAmount(account.getDueSettleAmount().subtract(settleAccountFlow.getIncomeAmount()));
                decreaseSettleAccountFlow.setOutAmount(settleAccountFlow.getIncomeAmount());
                decreaseSettleAccountFlow.setIncomeAmount(new BigDecimal("0.00"));
                decreaseSettleAccountFlow.setAppId(settleAccountFlow.getAppId());
                decreaseSettleAccountFlow.setTradeDate(settleAccountFlow.getTradeDate());
                decreaseSettleAccountFlow.setSettleDate(settleAccountFlow.getSettleDate());
                decreaseSettleAccountFlow.setChangeTime(new Date());
                decreaseSettleAccountFlow.setType(EnumAccountFlowType.DECREASE.getId());
                decreaseSettleAccountFlow.setRemark("收单分润退款");
                this.settleAccountFlowService.add(decreaseSettleAccountFlow);
                this.settleAccountFlowService.updateStatus(settleAccountFlow.getId(), EnumBoolean.TRUE.getCode());
            }
        }
        final Account account = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        this.accountService.increaseTotalAmount(account.getId(), refundOrder.getPoundageRefundAmount());
        this.accountService.increaseAvailableAmount(account.getId(), refundOrder.getPoundageRefundAmount());
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(account.getId());
        accountFlow.setOrderNo(payOrder.getOrderNo());
        accountFlow.setRefundOrderNo(refundOrder.getOrderNo());
        accountFlow.setBeforeAmount(account.getAvailable());
        accountFlow.setAfterAmount(account.getAvailable().add(payOrder.getPoundage()));
        accountFlow.setIncomeAmount(refundOrder.getPoundageRefundAmount());
        accountFlow.setOutAmount(new BigDecimal("0.00"));
        accountFlow.setChangeTime(new Date());
        accountFlow.setType(EnumAccountFlowType.INCREASE.getId());
        accountFlow.setRemark("收单分润退款");
        this.accountFlowService.add(accountFlow);
        return Pair.of(0, "success");
    }

    /**
     * {@inheritDoc}
     *
     * @param refundOrder 退款单
     * @param payOrder 交易单
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> refundMerchant(final RefundOrder refundOrder, final Order payOrder) {
        //TODO
        final Optional<SettleAccountFlow> decreaseSettleAccountFlowOptional = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(payOrder.getOrderNo(),
                payOrder.getPayee(), EnumAccountFlowType.DECREASE.getId());
        if (decreaseSettleAccountFlowOptional.isPresent()) {
            log.error("商户账户[{}],待结算[{}]流水，已结算", payOrder.getPayee(), decreaseSettleAccountFlowOptional.get().getId());
            return Pair.of(-1, "商户流水已结算");
        }
        final Account account = this.accountService.getByIdWithLock(payOrder.getPayee()).get();
        Preconditions.checkState(account.getDueSettleAmount().compareTo(refundOrder.getMerchantRefundAmount()) >= 0,
                "退款单[{}]，商户账户[{}]待结算金额不足", refundOrder.getOrderNo(), account.getId());
        this.accountService.decreaseTotalAmount(account.getId(), refundOrder.getMerchantRefundAmount());
        this.accountService.decreaseSettleAmount(account.getId(), refundOrder.getMerchantRefundAmount());
        final SettleAccountFlow increaseSettleAccountFlow = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(payOrder.getOrderNo(),
                account.getId(), EnumAccountFlowType.INCREASE.getId()).get();
        final SettleAccountFlow decreaseSettleAccountFlow = new SettleAccountFlow();
        decreaseSettleAccountFlow.setFlowNo("");
        decreaseSettleAccountFlow.setAccountId(increaseSettleAccountFlow.getAccountId());
        decreaseSettleAccountFlow.setAccountUserType(increaseSettleAccountFlow.getAccountUserType());
        decreaseSettleAccountFlow.setOrderNo(payOrder.getOrderNo());
        decreaseSettleAccountFlow.setRefundOrderNo(refundOrder.getOrderNo());
        decreaseSettleAccountFlow.setBeforeAmount(account.getDueSettleAmount());
        decreaseSettleAccountFlow.setAfterAmount(account.getDueSettleAmount().subtract(increaseSettleAccountFlow.getIncomeAmount()));
        decreaseSettleAccountFlow.setOutAmount(increaseSettleAccountFlow.getIncomeAmount());
        decreaseSettleAccountFlow.setIncomeAmount(new BigDecimal("0.00"));
        decreaseSettleAccountFlow.setAppId(increaseSettleAccountFlow.getAppId());
        decreaseSettleAccountFlow.setTradeDate(increaseSettleAccountFlow.getTradeDate());
        decreaseSettleAccountFlow.setSettleDate(increaseSettleAccountFlow.getSettleDate());
        decreaseSettleAccountFlow.setChangeTime(new Date());
        decreaseSettleAccountFlow.setType(EnumAccountFlowType.DECREASE.getId());
        decreaseSettleAccountFlow.setRemark("支付退款");
        this.settleAccountFlowService.add(decreaseSettleAccountFlow);
        this.settleAccountFlowService.updateStatus(increaseSettleAccountFlow.getId(), EnumBoolean.TRUE.getCode());
        return Pair.of(0, "success");
    }

    /**
     * {@inheritDoc}
     *
     * @param refundOrder
     * @param payOrder
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> refundPoundageOutAccount(final RefundOrder refundOrder, final Order payOrder) {
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(poundageAccount.getAvailable().compareTo(refundOrder.getPoundageRefundAmount()) >= 0);
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), refundOrder.getPoundageRefundAmount());
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), refundOrder.getPoundageRefundAmount());
        final AccountFlow accountFlow = new AccountFlow();
        accountFlow.setAccountId(poundageAccount.getId());
        accountFlow.setOrderNo(payOrder.getOrderNo());
        accountFlow.setRefundOrderNo(refundOrder.getOrderNo());
        accountFlow.setBeforeAmount(poundageAccount.getAvailable());
        accountFlow.setAfterAmount(poundageAccount.getAvailable().subtract(refundOrder.getPoundageRefundAmount()));
        accountFlow.setIncomeAmount(new BigDecimal("0.00"));
        accountFlow.setOutAmount(refundOrder.getPoundageRefundAmount());
        accountFlow.setChangeTime(new Date());
        accountFlow.setType(EnumAccountFlowType.DECREASE.getId());
        accountFlow.setRemark("收单分润退款");
        this.accountFlowService.add(accountFlow);
        return Pair.of(0, "success");
    }

    /**
     * {@inheritDoc}
     *
     * @param totalAmount
     * @param channel
     * @param shopId  店铺id
     * @param appId
     * @param memberId 会员id
     * @return
     */
    @Override
    public Pair<Integer, String> receipt(final String totalAmount, final int channel, final long shopId, final String appId, final String memberId,final String code) {
        log.info("店铺[{}] 通过动态扫码， 支付一笔资金[{}]", shopId, totalAmount);
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByID(shopId).get(0);
        final String channelCode = this.basicChannelService.selectCodeByChannelSign(channel, EnumMerchantPayType.MERCHANT_JSAPI);
//        if (EnumPayChannelSign.SYJ_ALIPAY.getId() == channel) {
//            channelCode = this.basicChannelService.selectCodeByChannelSign(channel, EnumMerchantPayType.MERCHANT_JSAPI);
//        } else {
//            channelCode = this.basicChannelService.selectCodeByChannelSign(channel, EnumMerchantPayType.MERCHANT_CODE);
//        }

        //add by wayne 2017/05/17===========================================插入好收银订单
        final HsyOrder hsyOrder=new HsyOrder();
        hsyOrder.setAmount(new BigDecimal(totalAmount));
        hsyOrder.setOrdernumber("");
        hsyOrder.setShopid(shopId);
        hsyOrder.setShopname(shop.getShortName());
        hsyOrder.setMerchantname(shop.getName());
        hsyOrder.setOrderstatus(EnumHsyOrderStatus.DUE_PAY.getId());
        hsyOrder.setSourcetype(EnumHsySourceType.QRCODE.getId());
        hsyOrder.setValidationcode("");
        hsyOrder.setQrcode(code);
        hsyOrder.setPaychannelsign(channel);
        hsyOrder.setPaytype(channelCode);
        hsyOrderService.insert(hsyOrder);
        log.info("好收银订单[{}]-下单成功【{}】", hsyOrder.getOrdernumber(), hsyOrder);

        //====================================================^^^^^^^^^^===============================================

        final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(channel);
        final Order order = new Order();
        order.setBusinessOrderNo(hsyOrder.getOrdernumber());
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
        order.setPayAccountType(payChannelSign.getPaymentChannel().getId());
        order.setPayAccount(memberId);
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleType(payChannelSign.getSettleType().getType());
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        this.orderService.add(order);

        //add by wayne 2017/05/18===========================================更新好收银订单
        hsyOrder.setOrderno(order.getOrderNo());
        hsyOrderService.update(hsyOrder);
        //===============================================================================

        return this.handlePlaceOrder(shop, channelCode, order);
    }



    /**
     * {@inheritDoc}
     *
     * @param shop
     * @param channelCode
     * @param order
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> handlePlaceOrder(final AppBizShop shop, final String channelCode, final Order order) {
        //请求支付中心下单
        final PaymentSdkPlaceOrderResponse placeOrderResponse = this.requestPlaceOrder(order, channelCode, shop,
                PaymentSdkConstants.SDK_PAY_RETURN_URL + order.getTradeAmount() + "/" + order.getId());
        final EnumBasicStatus enumBasicStatus = EnumBasicStatus.of(placeOrderResponse.getCode());
        final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
        Preconditions.checkState(orderOptional.get().isDuePay());
        final EnumPayChannelSign payChannelSign = EnumPayChannelSign.idOf(order.getPayChannelSign());
        switch (enumBasicStatus) {
            case SUCCESS:
                log.info("订单[{}]-下单成功【{}】", order.getId(), placeOrderResponse);
                order.setRemark(placeOrderResponse.getMessage());
                order.setPayInfo(placeOrderResponse.getPayInfo());
                if (payChannelSign.getNeedPackage()) {
                    order.setPaySalt(RandomStringUtils.randomAlphanumeric(16));
                    order.setPaySign(order.getSignCode());
                    this.orderService.update(order);
                    final String url = "/trade/pay?" + "o_n=" + order.getOrderNo() + "&sign=" + order.getPaySign();
                    return Pair.of(0, url);
                }
                this.orderService.update(order);
                return Pair.of(0, placeOrderResponse.getPayUrl());
            case FAIL:
                log.info("订单[{}]-下单失败【{}】", order.getId(), placeOrderResponse.getMessage());
                this.orderService.updateRemark(order.getId(), placeOrderResponse.getMessage());
                return Pair.of(-1, "下单失败");
        }
        return Pair.of(-1, "下单网关返回状态异常");
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
                log.info("hsy订单[{}], 支付成功回调处理", paymentSdkPayCallbackResponse.getOrderNo());
                this.markPaySuccess(paymentSdkPayCallbackResponse, order);
                break;
            case FAIL:
                log.info("hsy订单[{}], 支付失败回调处理", paymentSdkPayCallbackResponse.getOrderNo());
                this.markPayFail(paymentSdkPayCallbackResponse, order);
                break;
            case HANDLING:
                log.info("hsy订单[{}], 支付处理中回调处理", paymentSdkPayCallbackResponse.getOrderNo());
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
        log.info("hsy业务返回的通道是[{}]", order.getPayType());
        log.info("交易订单[{}]，处理hsy支付回调业务", order.getOrderNo());
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByAccountID(order.getPayee()).get(0);
        final BigDecimal merchantPayPoundageRate = this.calculateService.getMerchantPayPoundageRate(EnumProductType.HSY, shop.getId(), enumPayChannelSign.getId());
        final BigDecimal merchantPayPoundage = this.calculateService.getMerchantPayPoundage(order.getTradeAmount(), merchantPayPoundageRate, order.getPayChannelSign());
        order.setPoundage(merchantPayPoundage);
        order.setPayRate(merchantPayPoundageRate);
        order.setBankTradeNo(paymentSdkPayCallbackResponse.getBankTradeNo());
        order.setTradeCardType(paymentSdkPayCallbackResponse.getTradeCardType());
        order.setTradeCardNo(paymentSdkPayCallbackResponse.getTradeCardNo());
        order.setWechatOrAlipayOrderNo(paymentSdkPayCallbackResponse.getWechatOrAlipayOrderNo());
        order.setSettleTime(this.getHsySettleDate(order, enumPayChannelSign));
        this.orderService.update(order);
        //好收银订单处理  add by wayne 2017/05/18
        final Optional<HsyOrder> hsyOrderOptional=hsyOrderService.selectByOrderNo(order.getOrderNo());
        if(hsyOrderOptional.isPresent()){
            HsyOrder hsyOrder=hsyOrderOptional.get();
            hsyOrder.setOrderstatus(EnumHsyOrderStatus.PAY_SUCCESS.getId());
            hsyOrder.setPoundage(merchantPayPoundage);
            hsyOrder.setPaysn(paymentSdkPayCallbackResponse.getSn());
            hsyOrder.setPaysuccesstime(order.getPaySuccessTime());
            hsyOrderService.update(hsyOrder);
        }
        //=============================================================================
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
        //好收银订单处理  add by wayne 2017/05/18
        final Optional<HsyOrder> hsyOrderOptional=hsyOrderService.selectByOrderNo(order.getOrderNo());
        if(hsyOrderOptional.isPresent()){
            HsyOrder hsyOrder=hsyOrderOptional.get();
            hsyOrder.setOrderstatus(EnumHsyOrderStatus.PAY_FAIL.getId());
            hsyOrderService.update(hsyOrder);
        }
        //=============================================================================
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
                    EnumAppType.HSY.getId(), order.getPaySuccessTime(), order.getSettleTime(), EnumAccountUserType.MERCHANT.getId());

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
        final Triple<Long, BigDecimal, BigDecimal> basicMoneyTriple = shallProfitMap.get("basicMoney");
        final Triple<Long, BigDecimal, BigDecimal> channelMoneyTriple = shallProfitMap.get("channelMoney");
        final Triple<Long, BigDecimal, BigDecimal> productMoneyTriple = shallProfitMap.get("productMoney");
        final Triple<Long, BigDecimal, BigDecimal> firstMoneyTriple = shallProfitMap.get("firstMoney");
        final Triple<Long, BigDecimal, BigDecimal> secondMoneyTriple = shallProfitMap.get("secondMoney");

        final BigDecimal basicMoney = null == basicMoneyTriple ? new BigDecimal("0.00") : basicMoneyTriple.getMiddle();
        final BigDecimal channelMoney = null == channelMoneyTriple ? new BigDecimal("0.00") : channelMoneyTriple.getMiddle();
        final BigDecimal productMoney = null == productMoneyTriple ? new BigDecimal("0.00") : productMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0.00") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0.00") : secondMoneyTriple.getMiddle();
        Preconditions.checkState(order.getPoundage().compareTo(basicMoney.add(channelMoney).add(productMoney).add(firstMoney).add(secondMoney)) >= 0, "手续费不可以小于分润总和");
        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(order.getPoundage().compareTo(poundageAccount.getAvailable()) <= 0, "该笔订单的分账手续费不可以大于手续费账户的可用余额总和");
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), order.getPoundage());
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "收单分润", EnumAccountFlowType.DECREASE);

        //判断业务类型
        final String splitBusinessType = EnumSplitBusinessType.HSYPAY.getId();
        //通道利润--到结算
        if (null != channelMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), channelMoneyTriple, "通道账户",
                    "收单分润", EnumSplitAccountUserType.JKM.getId(), order.getSettleType());
            final Account account = this.accountService.getById(channelMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "收单分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), order.getSettleTime(), EnumAccountUserType.COMPANY.getId());
        }
        //产品利润--到结算
        if (null != productMoneyTriple) {
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), productMoneyTriple, "产品账户",
                    "收单分润", EnumSplitAccountUserType.JKM.getId(), order.getSettleType());
            final Account account = this.accountService.getById(productMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), productMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "收单分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), order.getSettleTime(), EnumAccountUserType.COMPANY.getId());
        }
        //一级代理商利润--到结算
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), firstMoneyTriple, dealer.getProxyName(),
                    "收单分润", EnumSplitAccountUserType.FIRST_DEALER.getId(), order.getSettleType());
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "收单分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), order.getSettleTime(), EnumAccountUserType.DEALER.getId());
        }
        //二级代理商利润--到结算
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addPaySplitAccountRecord(splitBusinessType, order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), secondMoneyTriple, dealer.getProxyName(),
                    "收单分润", EnumSplitAccountUserType.SECOND_DEALER.getId(), order.getSettleType());
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "收单分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), order.getSettleTime(), EnumAccountUserType.DEALER.getId());
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
        final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(shop.getAccountID()).get(0);
        final PaymentSdkPlaceOrderRequest placeOrderRequest = new PaymentSdkPlaceOrderRequest();
        placeOrderRequest.setAppId(EnumAppType.HSY.getId());
        placeOrderRequest.setOrderNo(order.getOrderNo());
        placeOrderRequest.setGoodsDescribe(order.getGoodsDescribe());
        placeOrderRequest.setReturnUrl(returnUrl);
        placeOrderRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_NOTIFY_URL);
        placeOrderRequest.setMerName(shop.getShortName());
        placeOrderRequest.setMerNo(appAuUser.getGlobalID());
        placeOrderRequest.setTotalAmount(order.getTradeAmount().toPlainString());
        placeOrderRequest.setChannel(channel);
        placeOrderRequest.setWxAppId(WxConstants.APP_HSY_ID);
        placeOrderRequest.setMemberId(order.getPayAccount());
        try {
            final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_PLACE_ORDER, SdkSerializeUtil.convertObjToMap(placeOrderRequest));
            return JSON.parseObject(content, PaymentSdkPlaceOrderResponse.class);
        } catch (final Throwable e){
            log.error("hsy订单[{}]，下单失败-请求网关下单异常", order.getId());
            final PaymentSdkPlaceOrderResponse paymentSdkPlaceOrderResponse = new PaymentSdkPlaceOrderResponse();
            paymentSdkPlaceOrderResponse.setCode(EnumBasicStatus.FAIL.getId());
            paymentSdkPlaceOrderResponse.setMessage("请求网关下单异常");
            return paymentSdkPlaceOrderResponse;
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
//        final AppBizShop shop = this.hsyShopDao.findAppBizShopByAccountID(accountId).get(0);
        final BigDecimal merchantWithdrawPoundage = new BigDecimal("2");//this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSY, shop.getId(), channel);
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
                    "提现分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), order.getSettleTime(), EnumAccountUserType.COMPANY.getId());
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
                    "提现分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), order.getSettleTime(), EnumAccountUserType.COMPANY.getId());
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
                    "提现分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), order.getSettleTime(), EnumAccountUserType.DEALER.getId());
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
                    "提现分润", EnumAccountFlowType.INCREASE, EnumAppType.HSY.getId(), order.getPaySuccessTime(), order.getSettleTime(), EnumAccountUserType.DEALER.getId());
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

    /**
     * 获取收银家结算时间
     *
     * @param tradeDate
     * @return
     */
    private Date getShouYinJiaPaySettleDate(Date tradeDate) {
        if (HolidaySettlementConstants.HOLIDAY_OPEN) {
            final Date settlementDate = this.mergeTableSettlementDateService.getSettlementDate(tradeDate, EnumUpperChannel.SYJ.getId());
            if (null != settlementDate) {
                return settlementDate;
            }
        }
        return DateTimeUtil.generateT1SettleDate(tradeDate);
    }

    /**
     *  获取结算日期
     *
     * @param order
     * @param payChannelSign
     * @return
     */
    private Date getHsySettleDate(final Order order, final EnumPayChannelSign payChannelSign) {
        final EnumUpperChannel upperChannel = payChannelSign.getUpperChannel();
        switch (upperChannel) {
            case SYJ:
                return this.getShouYinJiaPaySettleDate(order.getPaySuccessTime());
        }
        log.error("can not be here");
        return new Date();
    }
}
