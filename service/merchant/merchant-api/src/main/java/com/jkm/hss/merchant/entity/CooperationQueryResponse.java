package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/4/5.
 */
@Data
public class CooperationQueryResponse {


    /**
     * ip地址
     */
    private String ip;

    /**
     * 类型（商户还是代理商）
     */
    private String type;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 申请时间
     */
    private Date createTime;

    /**
     * 申请时间
     */
    private String createTimes;

}
