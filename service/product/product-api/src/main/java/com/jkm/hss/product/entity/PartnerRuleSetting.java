package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/5/4.
 *
 *  升级通道网关配置
 *
 *  1正常 2禁止
 */
@Data
public class PartnerRuleSetting extends BaseEntity {
    /**
     * 产品编码
     */
    private long productId;
    /**
     * 通道唯一标识...
     * {@link EnumPayChannelSign}
     */
    private Integer channelTypeSign;

    /**
     * 默认分润空间
     */
    private BigDecimal defaultProfitSpace;

    /**
     * 基准费率
     */
    private BigDecimal commonRate;
    /**
     * 店员费率
     */
    private BigDecimal clerkRate;
    /**
     * 店长费率
     */
    private BigDecimal shopownerRate;
    /**
     * 老板费率
     */
    private BigDecimal bossRate;
}
