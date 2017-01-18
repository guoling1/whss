package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by Administrator on 2017/1/17.
 */
@Data
public class ReferralResponse {
    /**
     * 推荐所属1代理商名称
     */
    private String proxyNameYq;

    /**
     * 推荐所属2代理商名称
     */
    private String proxyNameYq1;
}
