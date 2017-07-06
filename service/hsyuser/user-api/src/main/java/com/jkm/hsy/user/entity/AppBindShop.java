package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by Thinkpad on 2017/1/17.
 */
@Data
public class AppBindShop {
    /**
     * 二维码
     */
    private String code;
    /**
     * 店铺编码
     */
    private Long shopId;
    /**
     * 登录用户编码
     */
    private Long userId;
}
