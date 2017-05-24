package com.jkm.hss.bill.service.impl;

import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.dao.HsyRefundOrderDao;
import com.jkm.hss.bill.entity.HsyRefundOrder;
import com.jkm.hss.bill.service.HSYRefundOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

/**
 * Created by wayne on 17/5/20.
 */
@Slf4j
@Service("HsyRefundOrderService")
public class HSYRefundOrderServiceImlp implements HSYRefundOrderService {

    @Autowired
    private HsyRefundOrderDao hsyRefundOrderDao;

    @Override
    public void insert(HsyRefundOrder hsyRefundOrder) {
        hsyRefundOrderDao.insert(hsyRefundOrder);
        //开始生成订单号：
        String idStr=String.valueOf(hsyRefundOrder.getId());
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
        hsyRefundOrder.setRefundno("T"+DateFormatUtil.format(new Date(),"yyyyMMdd")+idStr);
    }

    @Override
    public int update(HsyRefundOrder hsyRefundOrder) {
        return hsyRefundOrderDao.update(hsyRefundOrder);
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
}
