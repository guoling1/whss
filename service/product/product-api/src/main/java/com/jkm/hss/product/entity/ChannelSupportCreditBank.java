package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/3/10.
 *
 * tb_channel_support_credit_bank
 *
 * 通道支持信用卡银行
 */
@Data
public class ChannelSupportCreditBank extends BaseEntity {

    /**
     *  通道唯一标志
     *
     *  {@link com.jkm.hss.product.enums.EnumPayChannelSign}
     */
    private int channelSign;
    /**
     * 银行名字
     */
    private String bankName;
    /**
     * 卡类型（目前都是贷记卡）
     */
    private String bankCardType;
    /**
     * 代笔限额
     */
    private BigDecimal singleLimitAmount;
    /**
     * 日累计限额
     */
    private BigDecimal dayLimitAmount;
}
