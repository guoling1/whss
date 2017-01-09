package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by Thinkpad on 2017/1/5.
 */
@Data
public class UpgradeRecord extends BaseEntity {
    /**
     * 商户编码
     */
    private long merchantId;
    /**
     * 升级类型
     * 1.交钱
     * 2.推广
     */
    private int type;


    /**
     *合伙人级别
     */
    private int level;

    /**
     * 备注
     */
    private String note;
}
