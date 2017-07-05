package com.jkm.hsy.user.entity;

import lombok.Data;

/**
 * Created by Thinkpad on 2017/1/17.
 */
@Data
public class ShopMsg {
    /**
     * 是否已经绑定
     * 1.已绑定
     * 2.未绑定
     */
    private String isBind;
    /**
     * 要绑定的二维码
     */
    private String currentShopName;
    /**
     * 已经绑定的店铺名
     */
    private String hasBindShopName;
}
