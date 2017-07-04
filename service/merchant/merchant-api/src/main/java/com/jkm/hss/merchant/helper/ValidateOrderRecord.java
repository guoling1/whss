package com.jkm.hss.merchant.helper;

import com.aliyun.openservices.shade.com.alibaba.rocketmq.shade.io.netty.util.internal.StringUtil;
import com.jkm.hss.merchant.entity.OrderRecordConditions;

/**
 * Created by Admin on 2016/12/13.
 */
public class ValidateOrderRecord {
    public static OrderRecordConditions validateOrderRecord(OrderRecordConditions orderRecordConditions){
        if (StringUtil.isNullOrEmpty(orderRecordConditions.getStartTime())){
            orderRecordConditions.setStartTime(null);
        }
        if (StringUtil.isNullOrEmpty(orderRecordConditions.getEndTime())){
            orderRecordConditions.setEndTime(null);
        }
        if (StringUtil.isNullOrEmpty(orderRecordConditions.getSubName())){
            orderRecordConditions.setSubName(null);
        }
        if (StringUtil.isNullOrEmpty(orderRecordConditions.getMdMobile())){
            orderRecordConditions.setMdMobile(null);
        }
        if (!StringUtil.isNullOrEmpty(orderRecordConditions.getMdMobile())){
            orderRecordConditions.setMdMobile(MerchantSupport.passwordDigest(orderRecordConditions.getMdMobile(),"JKM"));
        }
        if (StringUtil.isNullOrEmpty(orderRecordConditions.getOrderId())){
            orderRecordConditions.setOrderId(null);
        }
        if (StringUtil.isNullOrEmpty(orderRecordConditions.getPayResult())){
            orderRecordConditions.setPayResult(null);
        }

        return orderRecordConditions;
    }
}
