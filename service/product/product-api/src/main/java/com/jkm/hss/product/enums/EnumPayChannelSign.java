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
     * 阳光微信
     */
    YG_WECHAT(101, "sm_wechat", "微信", "阳光微信", EnumUpperChannel.SAOMI, false),

    /**
     * 阳光支付宝
     */
    YG_ALIPAY(102, "sm_alipay", "支付宝", "阳光支付宝", EnumUpperChannel.SAOMI, false),

    /**
     * 快捷支付
     */
    YG_UNIONPAY(103, "sm_unionpay", "快捷", "阳光快捷", EnumUpperChannel.SAOMI, false),

    /**
     * 微信 扫码
     */
   // YG_WECHAT_CODE(104, "sm_wechat_code", "微信", "阳光微信扫码", EnumUpperChannel.SAOMI, false),

    /**
     * 阳光支付宝扫码
     */
    //YG_ALIPAY_CODE(105, "sm_alipay_code", "支付宝", "阳光支付宝扫码", EnumUpperChannel.SAOMI, false),

    //#################################卡盟############################################

    /**
     * 卡盟微信
     */
    KM_WECHAT_CODE(201, "km_wechat", "微信", "卡盟微信", EnumUpperChannel.KAMENG, true),

    /**
     * 卡盟支付宝
     */
    KM_ALIPAY_CODE(202, "km_alipay", "支付宝", "卡盟支付宝", EnumUpperChannel.KAMENG, true),

    /**
     * 卡盟微信公众号
     */
   // KM_WECHAT_PUBLIC(203, "km_wechat_public", "微信", "卡盟微信公众号", EnumUpperChannel.KAMENG, true),

    /**
     * 卡盟支付宝公众号
     */
  //  KM_ALIPAY_PUBLIC(204, "km_alipay_public", "支付宝", "卡盟支付宝公众号", EnumUpperChannel.KAMENG, true),

    //#################################摩宝#############################################
    /**
     * 摩宝快捷
     */
    MB_UNIONPAY(301, "mb_unionpay", "快捷", "摩宝快捷", EnumUpperChannel.MOBAO, false),

    //#################################合众易宝#########################################
    /**
     * 合众易宝微信
     */
//    HZYB_WECHAT(401, "hzyb_wechat", "微信", "合众易宝微信", EnumUpperChannel.HEZONG_YIBAO, false),

    /**
     * 合众易宝支付宝
     */
//    HZYB_ALIPAY(402, "hzyb_alipay", "支付宝", "合众易宝支付宝", EnumUpperChannel.HEZONG_YIBAO, false),

    //#################################溢+#############################################
    /**
     * 溢+微信
     */
//    YIJIA_WECHAT(501, "yijia_wechat", "微信", "溢+微信", EnumUpperChannel.YIJIA, false),

    /**
     * 溢+支付宝
     */
//    YIJIA_ALIPAY(502, "yijia_alipay", "支付宝", "溢+支付宝", EnumUpperChannel.YIJIA, false)

    ;

    private static final ImmutableMap<String, EnumPayChannelSign> STATUS_IMMUTABLE_MAP;

    private static final ImmutableMap<String, EnumPayChannelSign> CODE_INIT_MAP;

    private static final ImmutableMap<Integer, EnumPayChannelSign> ID_INIT_MAP;

    static {
        final ImmutableMap.Builder<String, EnumPayChannelSign> builder = new ImmutableMap.Builder<>();
        final ImmutableMap.Builder<String, EnumPayChannelSign> builder2 = new ImmutableMap.Builder<>();
        final ImmutableMap.Builder<Integer, EnumPayChannelSign> builder3 = new ImmutableMap.Builder<>();
        for (final EnumPayChannelSign status : EnumPayChannelSign.values()) {
            builder.put(status.getName(), status);
            builder2.put(status.getCode(), status);
            builder3.put(status.getId(), status);
        }
        STATUS_IMMUTABLE_MAP = builder.build();
        CODE_INIT_MAP = builder2.build();
        ID_INIT_MAP = builder3.build();
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
    @Getter
    private Boolean autoSettle;

    EnumPayChannelSign(final int id, final String code, final String channelName,
                       final String name, final EnumUpperChannel upperChannel, final boolean autoSettle) {
        this.id = id;
        this.code = code;
        this.channelName = channelName;
        this.name = name;
        this.upperChannel = upperChannel;
        this.autoSettle = autoSettle;
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
        return CODE_INIT_MAP.get(code);
    }


    public static EnumPayChannelSign idOf(final int id) {
        return ID_INIT_MAP.get(id);
    }


    public static boolean isExistByCode(final String code) {
        return CODE_INIT_MAP.containsKey(code);
    }

    public static boolean isExistById(final int id) {
        return ID_INIT_MAP.containsKey(id);
    }
}
