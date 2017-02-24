package com.jkm.hss.product.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-24.
 *
 * 支付通道唯一标识
 */
public enum EnumPayChannelSign {

    /****供应商以YG区分 100*****/
    /**
     * 微信扫码
     */
    YG_WEIXIN(101, "sm_wechat", "微信", "阳光微信", EnumUpperChannel.SAOMI),

    /**
     * 支付宝扫码
     */
    YG_ZHIFUBAO(102, "sm_alipay", "支付宝", "阳光支付宝", EnumUpperChannel.SAOMI),

    /**
     * 银联支付
     */
    YG_YINLIAN(103, "sm_unionpay", "银联", "阳光银联", EnumUpperChannel.SAOMI),

    /**
     * 微信 公众号扫码
     */
    YG_WEIXIN_JSAPI(104, "sm_wechat_jsapi", "微信公众号", "阳光微信公众号", EnumUpperChannel.SAOMI),

    /**
     * 支付宝 公众号扫码
     */
    YG_ZHIFUBAO_JSAPI(105, "sm_alipay_jsapi", "支付宝公众号", "阳光支付宝公众号", EnumUpperChannel.SAOMI),


    ////////****卡盟*******/////////
    /**
     * 卡盟微信支付
     */
    KM_WECHAT(201, "km_wechat", "微信", "卡盟微信", EnumUpperChannel.KAMENG),

    /**
     * 卡盟支付宝
     */
    KM_ALIPAY(202, "km_alipay", "支付宝", "卡盟支付宝", EnumUpperChannel.KAMENG),

    /**
     * 卡盟微信支付
     */
    KM_WECHAT_JSAPI(203, "km_wechat_jsapi", "微信公众号", "卡盟微信公众号", EnumUpperChannel.KAMENG),

    /**
     * 卡盟支付宝
     */
    KM_ALIPAY_JSAPI(204, "km_alipay_jsapi", "支付宝公众号", "卡盟支付宝公众号", EnumUpperChannel.KAMENG)
    ;

    /**
     * id enum map
     */
    private static final ImmutableMap<String, EnumPayChannelSign> STATUS_IMMUTABLE_MAP;

    static {
        final ImmutableMap.Builder<String, EnumPayChannelSign> builder = new ImmutableMap.Builder<>();
        for (final EnumPayChannelSign status : EnumPayChannelSign.values()) {
            builder.put(status.getName(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
    }

    @Getter
    private int id;
    @Getter
    private String code;
    @Getter
    private String channelName;
    @Getter
    private String name;
    @Getter
    private EnumUpperChannel upperChannel;

    EnumPayChannelSign(final int id, final String code, final String channelName,
                       final String name, final EnumUpperChannel upperChannel) {
        this.id = id;
        this.code = code;
        this.channelName = channelName;
        this.name = name;
        this.upperChannel = upperChannel;
    }

    /**
     * int convert to enum
     *
     * @param name String
     * @return enum
     */
    public static EnumPayChannelSign of(final String name) {
        return STATUS_IMMUTABLE_MAP.get(name);
    }
}
