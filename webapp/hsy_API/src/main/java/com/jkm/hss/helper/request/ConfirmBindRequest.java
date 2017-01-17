package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by Thinkpad on 2017/1/17.
 */
@Data
public class ConfirmBindRequest {
    /**
     * 二维码
     */
    private String code;
    /**
     * 店铺编码
     */
    private long shopId;
    /**
     * 登录用户编码
     */
    private long userId;
}
