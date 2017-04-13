package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/4/5.
 */
@Data
public class WebsiteRequest{

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
}
