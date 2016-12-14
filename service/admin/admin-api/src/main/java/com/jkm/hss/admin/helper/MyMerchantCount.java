package com.jkm.hss.admin.helper;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/30.
 *
 * 代理商发展的店铺数量统计
 */
@Data
public class MyMerchantCount {

    /**
     * 本周激活的店铺数
     */
    private int currentWeekActivateMerchantCount;

    /**
     * 已激活店铺数
     */
    private int activateMerchantCount;
}
