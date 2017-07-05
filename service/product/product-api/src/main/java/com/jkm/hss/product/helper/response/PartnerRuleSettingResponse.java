package com.jkm.hss.product.helper.response;

import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/5/8.
 */
@Data
public class PartnerRuleSettingResponse {
    /**
     * 通道编码
     */
    private long id;
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
    /**
     * 通道名称
     */
    private String channelName;
}
