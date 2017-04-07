package com.jkm.hss.dealer.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/3/24.
 */
@Data
public class QueryMerchantResponse {

    /**
     * 商户编号
     */
    private String markCode;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 所属一级
     */
    private String proxyName;

    /**
     * 所属二
     * 级
     */
    private String proxyName1;

    /**
     * 注册时间
     * datetime
     */
    private Date createTime;

    /**
     * 注册时间
     * datetime
     */
    private String createTimes;

    /**
     * 注册手机号
     */
    private String mobile;

    /**
     * 注册方式标识
     */
    private int source;

    /**
     * 注册方式
     */
    private String registered;

    /**
     * 认证时间
     */
    private Date authenticationTime;

    /**
     * 认证时间
     */
    private String authenticationTimes;

    /**
     * 商户状态
     */
    private int status;

    /**
     * 商户状态
     */
    private String statusValue;


}
