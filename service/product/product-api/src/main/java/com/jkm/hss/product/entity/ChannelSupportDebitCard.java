package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;



/**
 * Created by yuxiang on 2017-05-16.
 * tb_channel_support_debit_card
 *
 * 通道支持借记卡银行
 *
 * {@link com.jkm.base.common.enums.EnumBoolean}
 */
@Data
public class ChannelSupportDebitCard extends BaseEntity {

    /**
     *  渠道
     *
     *  {@link com.jkm.hss.product.enums.EnumUpperChannel}
     */
    private int upperChannel;
    /**
     * 渠道名字
     */
    private String upperChannelName;
    /**
     * 渠道编码
     */
    private String upperChannelCode;
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
}
