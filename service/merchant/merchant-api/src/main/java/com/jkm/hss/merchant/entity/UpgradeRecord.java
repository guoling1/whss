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
     *升级结果 0 等待结果 1.升级成功 2升级失败
     */
    /**
     * 升级结果
     * 0.等待结果
     * 1.升级成功
     * 2.升级失败
     */
    private int upgradeResult;

    /**
     * 备注
     */
    private String note;
}
