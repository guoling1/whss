package com.jkm.hss.product.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumBasicChannelStatus;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-23.
 *
 * 基础通道设置
 *{@link EnumBasicChannelStatus}
 * tb_basic_channel
 */
@Data
public class BasicChannel extends BaseEntity{

    //通道名称，通道类型，收单公司，渠道来源，原始交易费率，原始结算费率（按笔收，最低手续费
    /**
     * 一个通道供应商下的多个通道对应一个资金帐号id
     * 该通道对应的资金帐号id
     */
    private long accountId;

    /**
     * 通道简称(后台展示的)
     */
    private String channelShortName;

    /**
     * 通道名称(华友支付宝,卡盟微信)
     */
    private String channelName;

    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 通道唯一标识...
     * {@link EnumPayChannelSign}
     */
    private int channelTypeSign;

    /**
     * 支持的支付方式， 扫码，公众号， 或者两者都支持
     *
     * {@link com.jkm.hss.product.enums.EnumChannelSupportWay}
     */
    private int supportWay;

    /**
     * 鉴权类型
     *
     *三要素：银行卡号，姓名，身份证号
     *四要素：银行卡号，姓名，身份证号，手机号
     *五要素：银行卡号，姓名，身份证号，手机号，有效期（信用卡）
     *六要素：银行卡号，姓名，身份证号，手机号，有效期（信用卡），CVV（信用卡）
     *
     * {@link com.jkm.hss.product.enums.EnumCheckType}
     */
    private int checkType;

    /**
     * 收单公司(支付宝,微信)
     */
    private String thirdCompany;
    /**
     * 渠道来源(供应商名字:华友， 卡盟)
     */
    private String channelSource;
    /**
     * 支付费率
     */
    private BigDecimal basicTradeRate;
    /**
     * 提现成本费用
     */
    private BigDecimal basicWithdrawFee;
    /**
     * 结算时间
     * {@link EnumBalanceTimeType}
     */
    private String basicBalanceType;

    /**
     * 结算方式
     * {@link com.jkm.hss.product.enums.EnumBasicSettleType}
     */
    private String basicSettleType;

    /**
     * 预估额度(最小)
     */
    private BigDecimal limitMinAmount;

    /**
     * 预估额度(最大)
     */
    private BigDecimal limitAmount;

    /**
     * 是否需要商户入网
     *
     * {@link com.jkm.base.common.enums.EnumBoolean}
     */
    private int isNeed;

    /**
     * 公司
     */
    private String channelCompany;

    /**
     * 最低手续费
     */
    private BigDecimal lowestFee;

    /**
     *  是否维护
     *  {@link com.jkm.base.common.enums.EnumBoolean}
     *
     *  0维护 1使用
     */
    private int isUse;

    /**
     * 0 否 1是
     */
    private int isHsy;
    /**
     * 备注
     */
    private String remarks;
}
