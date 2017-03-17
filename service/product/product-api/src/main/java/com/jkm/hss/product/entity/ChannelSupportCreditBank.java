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
 *
 * {@link com.jkm.base.common.enums.EnumBoolean}
 */
@Data
public class ChannelSupportCreditBank extends BaseEntity {

    /**
     *  渠道
     *
     *  {@link com.jkm.hss.product.enums.EnumUpperChannel}
     */
    private int upperChannel;
    /**
     * 银行编号
     */
    private String bankCode;
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
