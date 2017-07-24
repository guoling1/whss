package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.dao.HsyOrderDao;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.entity.QueryHsyOrderRequest;
import com.jkm.hss.bill.entity.QueryHsyOrderResponse;
import com.jkm.hss.bill.enums.EnumHsyOrderStatus;
import com.jkm.hss.bill.enums.EnumHsySourceType;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.helper.AppStatisticsOrder;
import com.jkm.hss.bill.helper.requestparam.TradeListRequestParam;
import com.jkm.hss.bill.helper.responseparam.HsyTradeListResponse;
import com.jkm.hss.bill.helper.responseparam.PcStatisticsOrder;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.AppParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wayne on 17/5/17.
 */
@Slf4j
@Service("HsyOrderService")
public class HSYOrderServiceImpl implements HSYOrderService {

    @Autowired
    private HsyOrderDao hsyOrderDao;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HsyUserDao hsyUserDao;

    @Override
    @Transactional
    public void insert(HsyOrder hsyOrder) {
        hsyOrder.setOrdernumber("");
        hsyOrderDao.insert(hsyOrder);
        //开始生成订单号：
        String idStr=String.valueOf(hsyOrder.getId());
        if(idStr.length()<8){
            int len=8-idStr.length();
            if(len==7){
                idStr=getFixLenthString(6)+"0"+idStr;
            }
            else {
                idStr=getFixLenthString(len)+idStr;
            }
        }
        else {
            idStr=idStr.substring(idStr.length()-8);
        }
        hsyOrder.setOrdernumber(DateFormatUtil.format(new Date(),"yyyyMMdd")+idStr);
//        hsyOrder.setValidationcode(hsyOrder.getOrdernumber().substring(hsyOrder.getOrdernumber().length()-4));
        hsyOrderDao.updateOrderNumber(hsyOrder.getId(), hsyOrder.getOrdernumber());
    }
    private String getFixLenthString(int strLength) {

        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    @Override
    public int update(HsyOrder hsyOrder) {
        return hsyOrderDao.update(hsyOrder);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param amount
     * @param status
     */
    @Override
    @Transactional
    public void updateAmountAndStatus(final long id, final BigDecimal amount, final int status) {
        this.hsyOrderDao.updateAmountAndStatus(id, amount, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param hsyOrderId
     * @return
     */
    @Override
    public Optional<HsyOrder> getById(final long hsyOrderId) {
        return Optional.fromNullable(this.hsyOrderDao.selectById(hsyOrderId));
    }

    /**
     * {@inheritDoc}
     *
     * @param hsyOrderId
     * @return
     */
    @Override
    public Optional<HsyOrder> getByIdWithLock(final long hsyOrderId) {
        return Optional.fromNullable(this.hsyOrderDao.selectByIdWithLock(hsyOrderId));
    }

    @Override
    public Optional<HsyOrder> selectByOrderNo(String orderNo) {
        return Optional.fromNullable(hsyOrderDao.selectByOrderNo(orderNo));
    }

    @Override
    public Optional<HsyOrder> selectByOrderId(long orderId) {
        return Optional.fromNullable(null);
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @param merchantNo
     * @param selectAll
     * @param paymentChannels
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public long getOrderCountByParam(final long shopId, final String merchantNo, final String tradeOrderNo,
                                     final int selectAll, final List<Integer> paymentChannels, final Date startTime, final Date endTime) {
        return this.hsyOrderDao.selectOrderCountByParam(shopId, merchantNo, tradeOrderNo, selectAll, paymentChannels, startTime, endTime);
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @param merchantNo
     * @param selectAll
     * @param paymentChannels
     * @param startTime
     * @param endTime
     * @param offset
     * @param count
     * @return
     */
    @Override
    public List<HsyOrder> getOrdersByParam(final long shopId, final String merchantNo, final String tradeOrderNo, final int selectAll, final List<Integer> paymentChannels,
                                              final Date startTime, final Date endTime, final int offset, final int count) {
        return this.hsyOrderDao.selectOrdersByParam(shopId, merchantNo, tradeOrderNo, selectAll, paymentChannels,
                startTime, endTime, offset, count);
    }

    /**
     * {@inheritDoc}
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    @Override
    public String orderList(final String dataParam, final AppParam appParam) {
        final Gson gson=new GsonBuilder().setDateFormat(DateFormat.LONG).create();
        final TradeListRequestParam requestParam = gson.fromJson(dataParam, TradeListRequestParam.class);
        final String[] paymentChannelArray = requestParam.getChannel().split(",");
        final Map<String,Object> resultMap=new HashMap<>();
        final PageModel<HsyTradeListResponse> pageModel = new PageModel<>(requestParam.getPageNo(),
                requestParam.getPageSize());
        if (paymentChannelArray.length == 0) {
            pageModel.setCount(0);
            pageModel.setRecords(Collections.<HsyTradeListResponse>emptyList());
            resultMap.put("amount", 0);
            resultMap.put("number", 0);
            resultMap.put("pageModel",pageModel);
            return JSON.toJSONString(pageModel);
        }
        final List<Integer> paymentChannels = new ArrayList<>();
        for (int i = 0; i < paymentChannelArray.length; i++) {
            paymentChannels.add(Integer.valueOf(paymentChannelArray[i]));
        }
        requestParam.setPaymentChannels(paymentChannels);
        Date startTime = null;
        Date endTime = null;
        if (!StringUtils.isEmpty(requestParam.getStartTime())) {
            startTime = DateFormatUtil.parse(requestParam.getStartTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        if (!StringUtils.isEmpty(requestParam.getEndTime())) {
            endTime = DateFormatUtil.parse(requestParam.getEndTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        String merchantNo = null;
        if (requestParam.getAll() == 1) {
            final AppBizShop appBizShop = this.hsyShopDao.findAppBizShopByID(requestParam.getShopId()).get(0);
            final AppAuUser appAuUser = this.hsyShopDao.findAuUserByAccountID(appBizShop.getAccountID()).get(0);
            merchantNo = appAuUser.getGlobalID();
        }
        final long count = this.hsyOrderDao.selectOrderCountByParam(requestParam.getShopId(), merchantNo, "", requestParam.getAll(), paymentChannels, startTime, endTime);
        final List<HsyOrder> hsyOrders = this.hsyOrderDao.selectOrdersByParam(requestParam.getShopId(), merchantNo, "", requestParam.getAll(), paymentChannels,
                startTime, endTime, pageModel.getFirstIndex(), requestParam.getPageSize());
        if (CollectionUtils.isEmpty(hsyOrders)) {
            resultMap.put("amount", 0);
            resultMap.put("number", 0);
            pageModel.setCount(0);
            pageModel.setRecords(Collections.<HsyTradeListResponse>emptyList());
            resultMap.put("pageModel", pageModel);
            return JSON.toJSONString(pageModel);
        }

        final HashSet<Date> dateHashSet = new HashSet<>();
        for (HsyOrder order: hsyOrders) {
            dateHashSet.add(DateFormatUtil.parse(DateFormatUtil.format(order.getPaysuccesstime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd));
        }
        final Iterator<Date> iterator = dateHashSet.iterator();
        final HashMap<Date, AppStatisticsOrder> statisticsOrderHashMap = new HashMap<>();
        while (iterator.hasNext()) {
            final Date nextDate = iterator.next();
            final String formatDate = DateFormatUtil.format(nextDate, DateFormatUtil.yyyy_MM_dd);
            final Date sDate = DateFormatUtil.parse(formatDate + " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            final Date eDate = DateFormatUtil.parse(formatDate + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            final AppStatisticsOrder statisticsOrder = this.hsyOrderDao.statisticsOrdersByParam(requestParam.getShopId(), merchantNo, requestParam.getAll(), paymentChannels,
                    sDate, eDate);
            statisticsOrderHashMap.put(nextDate, statisticsOrder);
        }
        final List<HsyTradeListResponse> hsyTradeListResponseList=new ArrayList<>();
        for(HsyOrder hsyOrder : hsyOrders){
            Date payDate = null;
            if(hsyOrder.getPaysuccesstime()!=null){
                payDate=DateFormatUtil.parse(DateFormatUtil.format(hsyOrder.getPaysuccesstime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
            }
            final Date refundDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
            final Date curD = DateFormatUtil.parse(DateFormatUtil.format(hsyOrder.getPaysuccesstime(), DateFormatUtil.yyyy_MM_dd),  DateFormatUtil.yyyy_MM_dd);
            final HsyTradeListResponse hsyTradeListResponse = new HsyTradeListResponse();
            if(hsyOrder.isRefund() && refundDate.compareTo(payDate) == 0){
                hsyTradeListResponse.setCanRefund(1);
            } else {
                hsyTradeListResponse.setCanRefund(0);
            }
            AppStatisticsOrder curstorder = statisticsOrderHashMap.get(curD);
            if (curstorder==null) {curstorder = new AppStatisticsOrder();}
            hsyTradeListResponse.setTotalAmount(curstorder.getAmount());
            hsyTradeListResponse.setNumber(curstorder.getNumber());
            hsyTradeListResponse.setAmount(hsyOrder.getAmount());
            hsyTradeListResponse.setValidationCode(hsyOrder.getValidationcode());
            hsyTradeListResponse.setOrderstatus(hsyOrder.getOrderstatus());
            hsyTradeListResponse.setOrderstatusName(EnumHsyOrderStatus.of(hsyOrder.getOrderstatus()).getValue());
            hsyTradeListResponse.setRefundAmount(hsyOrder.getRefundamount());
            hsyTradeListResponse.setChannel(hsyOrder.getPaymentChannel());
            hsyTradeListResponse.setTime(hsyOrder.getCreateTime());
            hsyTradeListResponse.setOrderNumber(hsyOrder.getOrdernumber());
            hsyTradeListResponse.setValidationCode(hsyOrder.getValidationcode());
            hsyTradeListResponse.setOrderId(hsyOrder.getOrderid());
            hsyTradeListResponse.setId(hsyOrder.getId());
            hsyTradeListResponse.setOrderNo(hsyOrder.getOrderno());
            hsyTradeListResponse.setShopName(hsyOrder.getShopname());
            hsyTradeListResponse.setMerchantName(hsyOrder.getMerchantname());
            hsyTradeListResponse.setSourceType(hsyOrder.getSourcetype());
            hsyTradeListResponse.setSourceTypeName(EnumHsySourceType.of(hsyOrder.getSourcetype()).getValue());
            hsyTradeListResponseList.add(hsyTradeListResponse);
        }
        pageModel.setCount(count);
        pageModel.setRecords(hsyTradeListResponseList);
        resultMap.put("pageModel", pageModel);
        if(requestParam.getStType() == 1){
            final Date nowDate = new Date();
            final Date now = DateFormatUtil.parse(DateFormatUtil.format(nowDate, DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
            final AppStatisticsOrder statisticsOrder = statisticsOrderHashMap.get(now);
            if (null != statisticsOrder) {
                resultMap.put("amount", statisticsOrder.getAmount());
                resultMap.put("number", statisticsOrder.getNumber());
                return JSON.toJSONString(resultMap);
            }
            startTime = DateFormatUtil.parse(DateFormatUtil.format(nowDate, DateFormatUtil.yyyy_MM_dd) + " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            endTime = DateFormatUtil.parse(DateFormatUtil.format(nowDate, DateFormatUtil.yyyy_MM_dd) + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        final AppStatisticsOrder statisticsOrder = this.hsyOrderDao.statisticsOrdersByParam(requestParam.getShopId(), merchantNo, requestParam.getAll(), paymentChannels,
                startTime, endTime);
        resultMap.put("amount", statisticsOrder.getAmount());
        resultMap.put("number", statisticsOrder.getNumber());
        return JSON.toJSONString(resultMap);
    }

    @Override
    public String appOrderDetail(String dataParam, AppParam appParam) {
        Gson gson=new GsonBuilder().setDateFormat(DateFormat.LONG).create();
        final JSONObject paramJo = JSONObject.parseObject(dataParam);
        final long payOrderId = paramJo.getLongValue("payOrderId");
        final HsyOrder hsyOrder=hsyOrderDao.selectById(payOrderId);

        final Date payDate = DateFormatUtil.parse(DateFormatUtil.format(hsyOrder.getPaysuccesstime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
        final Date refundDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
        HsyTradeListResponse hsyTradeListResponse=new HsyTradeListResponse();
        if(hsyOrder.isRefund()&&payDate.compareTo(refundDate) == 0){
            hsyTradeListResponse.setCanRefund(1);
        }
        else{
            hsyTradeListResponse.setCanRefund(0);
        }
        hsyTradeListResponse.setAmount(hsyOrder.getAmount());
        hsyTradeListResponse.setAmount(hsyOrder.getAmount());
        hsyTradeListResponse.setValidationCode(hsyOrder.getValidationcode());
        hsyTradeListResponse.setOrderstatus(EnumHsyOrderStatus.of(hsyOrder.getOrderstatus()).getId());
        hsyTradeListResponse.setOrderstatusName(EnumHsyOrderStatus.of(hsyOrder.getOrderstatus()).getValue());
        hsyTradeListResponse.setRefundAmount(hsyOrder.getRefundamount());
        hsyTradeListResponse.setChannel(EnumPayChannelSign.idOf(hsyOrder.getPaychannelsign()).getPaymentChannel().getId());
        hsyTradeListResponse.setTime(hsyOrder.getCreateTime());
        hsyTradeListResponse.setOrderNumber(hsyOrder.getOrdernumber());
        hsyTradeListResponse.setValidationCode(hsyOrder.getValidationcode());
        hsyTradeListResponse.setOrderId(hsyOrder.getOrderid());
        hsyTradeListResponse.setId(hsyOrder.getId());
        hsyTradeListResponse.setOrderNo(hsyOrder.getOrderno());
        hsyTradeListResponse.setShopName(hsyOrder.getShopname());
        hsyTradeListResponse.setMerchantName(hsyOrder.getMerchantname());
        hsyTradeListResponse.setSourceType(hsyOrder.getSourcetype());
        hsyTradeListResponse.setSourceTypeName(EnumHsySourceType.of(hsyOrder.getSourcetype()).getValue());
        //return gson.toJson(hsyTradeListResponse);
        return JSON.toJSONString(hsyTradeListResponse);
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantNo
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<HsyOrder> getByMerchantNoAndTime(final String merchantNo, final Date startTime, final Date endTime) {
        return this.hsyOrderDao.selectByMerchantNoAndTime(merchantNo, startTime, endTime);
    }

    /**
     * {@inheritDoc}
     *
     * @param shopId
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<PcStatisticsOrder> pcStatisticsOrder(final long shopId, final Date startTime, final Date endTime) {
        return this.hsyOrderDao.pcStatisticsOrder(shopId, startTime, endTime);
    }

    /**
     * hsy订单
     * @param req
     * @return
     */
    @Override
    public List<QueryHsyOrderResponse> queryHsyOrderList(QueryHsyOrderRequest req) {
        List<QueryHsyOrderResponse> list = this.hsyOrderDao.queryHsyOrderList(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                list.get(i).setPaysuccesstimes(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(list.get(i).getPaysuccesstime()));
                list.get(i).setPaymentChannels(EnumPaymentChannel.of(list.get(i).getPaymentChannel()).getValue());
                list.get(i).setOrderstatuss(EnumOrderStatus.of(list.get(i).getOrderstatus()).getValue());
            }
        }
        return list;
    }

    /**
     * hsy订单总数
     * @param req
     * @return
     */
    @Override
    public int queryHsyOrderListCount(QueryHsyOrderRequest req) {
        return this.hsyOrderDao.queryHsyOrderListCount(req);
    }
}
