package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.dao.HsyOrderDao;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.helper.requestparam.TradeListRequestParam;
import com.jkm.hss.bill.helper.responseparam.HsyTradeListResponse;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.entity.AppParam;
import lombok.extern.slf4j.Slf4j;
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
}
