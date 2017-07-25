package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by Allen on 2017/2/8.
 */
@Data
public class AppAuUserToken {
    private Long uid;
    private Long tid;
    private Integer status;//登录状态1登录 2未登录
    private Date loginTime;
    private Date outTime;
}
