package com.jkm.hss.product.enums;

import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-24.
 *
 * 支付通道唯一标识
 */
public enum EnumPayChannelSign {

    //#################################阳光万维########################################
    /**
     * 微信扫码
     */
    YG_WEIXIN(101, "sm_wechat", "微信", "阳光微信", EnumUpperChannel.SAOMI),

    /**
     * 支付宝扫码
     */
    YG_ZHIFUBAO(102, "sm_alipay", "支付宝", "阳光支付宝", EnumUpperChannel.SAOMI),

    /**
     * 快捷支付
     */
    YG_UNIONPAY(103, "sm_unionpay", "快捷", "阳光快捷", EnumUpperChannel.SAOMI),

    //#################################卡盟############################################
    /**
     * 卡盟微信支付
     */
    KM_WECHAT(201, "km_wechat", "微信", "卡盟微信", EnumUpperChannel.KAMENG),

    /**
     * 卡盟支付宝
     */
    KM_ALIPAY(202, "km_alipay", "支付宝", "卡盟支付宝", EnumUpperChannel.KAMENG),

    //#################################摩宝#############################################
    /**
     * 摩宝快捷
     */
    MB_UNIONPAY(301, "mb_unionpay", "快捷", "摩宝快捷", EnumUpperChannel.MOBAO),

    //#################################合众易宝#########################################
    /**
     * 合众易宝微信
     */
    HZYB_WECHAT(401, "hzyb_wechat", "微信", "合众易宝微信", EnumUpperChannel.HEZONG_YIBAO),

    /**
     * 合众易宝支付宝
     */
    HZYB_ALIPAY(402, "hzyb_alipay", "支付宝", "合众易宝支付宝", EnumUpperChannel.HEZONG_YIBAO),

    //#################################溢+#############################################
    /**
     * 溢+微信
     */
    YIJIA_WECHAT(501, "yijia_wechat", "微信", "溢+微信", EnumUpperChannel.YIJIA),

    /**
     * 溢+支付宝
     */
    YIJIA_ALIPAY(502, "yijia_alipay", "支付宝", "溢+支付宝", EnumUpperChannel.YIJIA)

    ;

    /**
     * id enum map
     */
    private static final ImmutableMap<String, EnumPayChannelSign> STATUS_IMMUTABLE_MAP;

    /**
     * id enum map
     */
    private static final ImmutableMap<String, EnumPayChannelSign> INIT_MAP;


    static {
        final ImmutableMap.Builder<String, EnumPayChannelSign> builder = new ImmutableMap.Builder<>();
        final ImmutableMap.Builder<String, EnumPayChannelSign> builder2 = new ImmutableMap.Builder<>();
        for (final EnumPayChannelSign status : EnumPayChannelSign.values()) {
            builder.put(status.getName(), status);
            builder2.put(status.getCode(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
        INIT_MAP = builder2.build();
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


    /**
     * int convert to enum
     *
     * @param code
     * @return enum
     */
    public static EnumPayChannelSign codeOf(final String code) {
        return INIT_MAP.get(code);
    }


    public static boolean isExist(final String code) {
        return INIT_MAP.containsKey(code);
    }
}
