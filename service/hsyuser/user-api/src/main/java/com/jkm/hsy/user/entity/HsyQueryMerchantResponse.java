package com.jkm.hsy.user.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/3/29.
 */
@Data
public class HsyQueryMerchantResponse {

    private String globalID;//商户编号

    private String shortName;//商户名称

    private String proxyName; //所属代理商

    private Date createTime;//注册时间

    private String createTimes;//注册时间

    private String cellphone;//注册手机号

    private String province;//省市

    private String aName;

    private String parentCode;

    private String industryCode;//行业代码

    private String industry;//行业

    private int status;//状态

    private String statusValue;//状态
}
