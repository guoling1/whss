package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.util.Date;


@Data
public class MerchantInfoRequest {

    /**
     * 条数
     */
    private Integer offset;



    /**
     * 认证时间
     */
    private String authenticationTime;


    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 每页显示页数
     */
    private int pageSize;


    /**
     * 商户id
     */
    private long id;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     *审核通过时间
     */
    private String checkedTime;

    /**
     * 注册时间
     * datetime
     */
    private Date createTime;

    /**
     * 商户状态
     */
    private Integer status;




}
