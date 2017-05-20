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
import com.jkm.hss.bill.enums.EnumHsyOrderStatus;
import com.jkm.hss.bill.helper.AppStatisticsOrder;
import com.jkm.hss.bill.helper.requestparam.TradeListRequestParam;
import com.jkm.hss.bill.helper.responseparam.HsyOrderSTResponse;
import com.jkm.hss.bill.helper.responseparam.HsyTradeListResponse;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.entity.AppParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wayne on 17/5/17.
 */
@Slf4j
@Service("HsyOrderService")
public class HSYOrderServiceImpl implements HSYOrderService {

    @Autowired
    private HsyOrderDao hsyOrderDao;

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
        hsyOrder.setValidationcode(hsyOrder.getOrdernumber().substring(hsyOrder.getOrdernumber().length()-4));
        hsyOrderDao.update(hsyOrder);
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

    @Override
    public Optional<HsyOrder> selectByOrderNo(String orderNo) {
        return Optional.fromNullable(hsyOrderDao.selectByOrderNo(orderNo));
    }

    @Override
    public String orderListst(final String dataParam, final AppParam appParam) {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        /**参数转化*/
        TradeListRequestParam requestParam=null;
        requestParam=gson.fromJson(dataParam, TradeListRequestParam.class);
        if(requestParam.getPaymentChannels()==null){
            requestParam.setPaymentChannels(requestParam.getChannel().split(","));
        }
        Map<String,Object> resultMap=new HashMap<>();

        final PageModel<HsyTradeListResponse> pageModel = new PageModel<>(requestParam.getPageNo(),
                requestParam.getPageSize());
        if (requestParam.getPaymentChannels().length == 0) {
            pageModel.setCount(0);
            pageModel.setRecords(Collections.<HsyTradeListResponse>emptyList());
            resultMap.put("amount",0);
            resultMap.put("number",0);
            resultMap.put("pageModel",pageModel);
            return JSON.toJSONString(pageModel);
        }
        final ArrayList<Integer> payChannelSigns = new ArrayList<>();
        for (int i = 0; i < requestParam.getPaymentChannels().length; i++) {
            payChannelSigns.addAll(EnumPayChannelSign.getIdListByPaymentChannel(Integer.valueOf(requestParam.getPaymentChannels()[i])));
        }
        Date startTime = null;
        Date endTime = null;
        if (!StringUtils.isEmpty(requestParam.getStartTime())) {
            startTime = DateFormatUtil.parse(requestParam.getStartTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        if (!StringUtils.isEmpty(requestParam.getEndTime())) {
            endTime = DateFormatUtil.parse(requestParam.getEndTime(), DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        final long count=hsyOrderDao.selectOrderCountByParam(requestParam.getShopId(),payChannelSigns,startTime,endTime);
        final List<HsyOrder> hsyOrders=hsyOrderDao.selectOrdersByParam( requestParam.getShopId(),
                                                                        pageModel.getFirstIndex(),
                                                                        requestParam.getPageSize(),
                                                                        payChannelSigns,
                                                                        startTime,endTime);
        pageModel.setCount(count);

        List<HsyTradeListResponse> hsyTradeListResponseList=new ArrayList<HsyTradeListResponse>();
        if (!CollectionUtils.isEmpty(hsyOrders)) {
            final HashMap<String, AppStatisticsOrder> statisticsOrderHashMap = new HashMap<>();
            AppStatisticsOrder appStatisticsOrder=null;
            String readedTime="";
            String readingTime="";
            for(HsyOrder hsyOrder:hsyOrders){
                readingTime=DateFormatUtil.format(hsyOrder.getCreateTime(), DateFormatUtil.yyyy_MM_dd);
                if(readedTime.equals(readingTime)){
                    appStatisticsOrder.setAmount(appStatisticsOrder.getAmount().add(hsyOrder.getAmount()));
                    appStatisticsOrder.setNumber(appStatisticsOrder.getNumber()+1);
                }
                else
                {
                    if(appStatisticsOrder!=null) {
                        statisticsOrderHashMap.put(readedTime, appStatisticsOrder);
                    }
                    readedTime=readingTime;
                    appStatisticsOrder=new AppStatisticsOrder();
                    appStatisticsOrder.setNumber(1);
                    appStatisticsOrder.setAmount(hsyOrder.getAmount());

                }
            }
            if(appStatisticsOrder!=null) {
                statisticsOrderHashMap.put(readedTime, appStatisticsOrder);
            }
            
            HsyTradeListResponse hsyTradeListResponse=null;
            for(HsyOrder hsyOrder:hsyOrders){
                final Date payDate = DateFormatUtil.parse(DateFormatUtil.format(hsyOrder.getPaysuccesstime(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
                final Date refundDate = DateFormatUtil.parse(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
                String curD=DateFormatUtil.format(hsyOrder.getCreateTime(), DateFormatUtil.yyyy_MM_dd);
                hsyTradeListResponse=new HsyTradeListResponse();
                if(hsyOrder.isRefund()&&payDate.compareTo(refundDate) == 0){
                        hsyTradeListResponse.setCanRefund(1);
                }
                else{
                    hsyTradeListResponse.setCanRefund(0);
                }
                hsyTradeListResponse.setTotalAmount(statisticsOrderHashMap.get(curD).getAmount());
                hsyTradeListResponse.setNumber(statisticsOrderHashMap.get(curD).getNumber());
                hsyTradeListResponse.setAmount(hsyOrder.getAmount());
                hsyTradeListResponse.setValidationCode(hsyOrder.getValidationcode());
                hsyTradeListResponse.setOrderstatus(EnumHsyOrderStatus.of(hsyOrder.getOrderstatus()).getId());
                hsyTradeListResponse.setOrderstatusName(EnumHsyOrderStatus.of(hsyOrder.getOrderstatus()).getValue());
                hsyTradeListResponse.setRefundAmount(hsyOrder.getRefundamount());
                hsyTradeListResponse.setChannel(EnumPayChannelSign.idOf(hsyOrder.getPaychannelsign()).getId());
                hsyTradeListResponse.setTime(hsyOrder.getCreateTime());
                hsyTradeListResponse.setOrderNumber(hsyOrder.getOrdernumber());
                hsyTradeListResponse.setValidationCode(hsyOrder.getValidationcode());
                hsyTradeListResponse.setOrderId(hsyOrder.getOrderid());
                hsyTradeListResponse.setId(hsyOrder.getId());
                hsyTradeListResponse.setShopName(hsyOrder.getShopname());
                hsyTradeListResponse.setMerchantName(hsyOrder.getMerchantname());
                hsyTradeListResponseList.add(hsyTradeListResponse);
            }
        }
        pageModel.setRecords(hsyTradeListResponseList);
        if(requestParam.getStType()==1){
            Date nowd=new Date();
            startTime=DateFormatUtil.parse(DateFormatUtil.format(nowd,DateFormatUtil.yyyy_MM_dd)+" 00:00:00",DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            endTime=DateFormatUtil.parse(DateFormatUtil.format(nowd,DateFormatUtil.yyyy_MM_dd)+" 23:59:59",DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        HsyOrderSTResponse hsyOrderSTResponse=hsyOrderDao.selectOrderStByParm(requestParam.getShopId(),payChannelSigns,startTime,endTime);

        resultMap.put("amount",hsyOrderSTResponse.getTotalAmount());
        resultMap.put("number",hsyOrderSTResponse.getNumber());
        resultMap.put("pageModel",pageModel);
        return gson.toJson(resultMap);
    }

    @Override
    public String appOrderDetail(String dataParam, AppParam appParam) {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
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
        hsyTradeListResponse.setValidationCode(hsyOrder.getValidationcode());
        hsyTradeListResponse.setOrderstatus(EnumHsyOrderStatus.of(hsyOrder.getOrderstatus()).getId());
        hsyTradeListResponse.setOrderstatusName(EnumHsyOrderStatus.of(hsyOrder.getOrderstatus()).getValue());
        hsyTradeListResponse.setRefundAmount(hsyOrder.getRefundamount());
        hsyTradeListResponse.setChannel(EnumPayChannelSign.idOf(hsyOrder.getPaychannelsign()).getId());
        hsyTradeListResponse.setTime(hsyOrder.getCreateTime());
        hsyTradeListResponse.setOrderNumber(hsyOrder.getOrdernumber());
        hsyTradeListResponse.setValidationCode(hsyOrder.getValidationcode());
        hsyTradeListResponse.setOrderId(hsyOrder.getOrderid());
        hsyTradeListResponse.setId(hsyOrder.getId());
        hsyTradeListResponse.setShopName(hsyOrder.getShopname());
        hsyTradeListResponse.setMerchantName(hsyOrder.getMerchantname());
        return gson.toJson(hsyTradeListResponse);
    }
}
