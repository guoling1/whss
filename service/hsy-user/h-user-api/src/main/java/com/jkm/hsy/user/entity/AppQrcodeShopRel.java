package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by Thinkpad on 2017/1/16.
 */
@Data
public class AppQrcodeShopRel extends BaseEntity{
    /**
     * 二维码
     */
    private String code;
    /**
     * 店铺编码
     */
    private long shopId;
}
