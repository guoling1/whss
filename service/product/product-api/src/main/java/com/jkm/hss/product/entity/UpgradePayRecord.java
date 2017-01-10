package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by Thinkpad on 2017/1/9.
 * 升级充值记录
 * tb_upgrade_pay_record
 */
@Data
public class UpgradePayRecord extends BaseEntity {
    /**
     *商户编码
     */
    private long merchantId;

    /**
     *达到级别
     */
    private int level;

    /**
     *升级规则编码
     */
    private long upgradeRulesId;

    /**
     *支付流水
     */
    private String reqSn;

    /**
     *支付金额
     */
    private BigDecimal amount;

    /**
     *备注
     */
    private String note;

    /**
     * 支付结果
     *
     */
    private String payResult;
}
