
package com.jkm.hss.bill.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hss.product.enums.EnumUpperChannel;
import lombok.Getter;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 支付方式
 */
public enum EnumPayType {

    //#################################阳光万维#############################################################
    /**
     * 阳光微信公众号
     */
    YG_WECHAT_JSAPI("H", "sm_wechat_jsapi", "阳光微信公众号", EnumPaymentChannel.WECHAT_PAY, EnumUpperChannel.SAOMI),

    /**
     * 阳光支付宝公众号
     */
    YG_ALIPAY_JSAPI("Z", "sm_alipay_jsapi", "阳光支付宝公众号", EnumPaymentChannel.ALIPAY, EnumUpperChannel.SAOMI),

    /**
     * 阳光微信扫码
     */
    YG_WECHAT_CODE("N", "sm_wechat_code", "阳光微信扫码", EnumPaymentChannel.WECHAT_PAY, EnumUpperChannel.SAOMI),

    /**
     * 阳光支付宝扫码(原来是Z，为了区分加了ZZ)
     */
    YG_ALIPAY_CODE("ZZ", "sm_alipay_code", "阳光支付宝扫码", EnumPaymentChannel.ALIPAY, EnumUpperChannel.SAOMI),

    /**
     * 阳光银联支付
     */
    YG_UNIONPAY("B", "sm_unionpay", "阳光快捷", EnumPaymentChannel.UNIONPAY, EnumUpperChannel.SAOMI),

    //#################################卡盟##################################################################
    /**
     * 卡盟微信公众号
     */
    KM_WECHAT_JSAPI("WX_SCANCODE_JSAPI", "km_wechat_jsapi", "卡盟微信公众号", EnumPaymentChannel.WECHAT_PAY, EnumUpperChannel.KAMENG),

    /**
     * 卡盟支付宝公众号
     */
    KM_ALIPAY_JSAPI("Alipay_SCANCODE_JSAPI", "km_alipay_jsapi", "卡盟支付宝公众号", EnumPaymentChannel.ALIPAY, EnumUpperChannel.KAMENG),

    /**
     * 卡盟微信扫码
     */
    KM_WECHAT_CODE("WX_SCANCODE", "km_wechat_code", "卡盟微信扫码", EnumPaymentChannel.WECHAT_PAY, EnumUpperChannel.KAMENG),

    /**
     * 卡盟支付宝扫码
     */
    KM_ALIPAY_CODE("Alipay_SCANCODE", "km_alipay_code", "卡盟支付宝扫码", EnumPaymentChannel.ALIPAY, EnumUpperChannel.KAMENG),

    //#################################摩宝###################################################################
    /**
     * 摩宝快捷
     */
    MB_UNIONPAY("mb_unionpay", "mb_unionpay", "摩宝快捷", EnumPaymentChannel.WECHAT_PAY, EnumUpperChannel.MOBAO),

    //#################################合众易宝###############################################################
    /**
     * 合众易宝微信
     */
    HZYB_WECHAT("hzyb_wechat", "hzyb_wechat", "合众易宝微信", EnumPaymentChannel.ALIPAY, EnumUpperChannel.HEZONG_YIBAO),

    /**
     * 合众易宝支付宝
     */
    HZYB_ALIPAY("hzyb_alipay", "hzyb_alipay", "合众易宝支付宝", EnumPaymentChannel.ALIPAY, EnumUpperChannel.HEZONG_YIBAO),

    //#################################溢+####################################################################
    /**
     * 溢+微信
     */
    YIJIA_WECHAT("yijia_wechat", "yijia_wechat", "溢+微信", EnumPaymentChannel.WECHAT_PAY, EnumUpperChannel.YIJIA),

    /**
     * 溢+支付宝
     */
    YIJIA_ALIPAY("yijia_alipay", "yijia_alipay", "溢+支付宝", EnumPaymentChannel.WECHAT_PAY, EnumUpperChannel.YIJIA),

    /**
     * 易联快捷支付
     */
    EASY_LINK_UNIONPAY("easy_link_unionpay", "el_unionpay", "易联快捷", EnumPaymentChannel.UNIONPAY, EnumUpperChannel.EASY_LINK)
    ;



    @Getter
    private String id;
    @Getter
    private String code;
    @Getter
    private String value;
    @Getter
    private EnumPaymentChannel paymentChannel;
    @Getter
    private EnumUpperChannel upperChannel;

    EnumPayType(final String id, final String code, final String value,
                    final EnumPaymentChannel paymentChannel, final EnumUpperChannel upperChannel) {
        this.id = id;
        this.code = code;
        this.value = value;
        this.paymentChannel = paymentChannel;
        this.upperChannel = upperChannel;
    }


    private static final ImmutableMap<String, EnumPayType> INIT_MAP;

    private static final ImmutableMap<String, EnumPayType> INIT_MAP2;

    static {
        final ImmutableMap.Builder<String, EnumPayType> builder = new ImmutableMap.Builder<>();
        final ImmutableMap.Builder<String, EnumPayType> builder2 = new ImmutableMap.Builder<>();
        for (EnumPayType status : EnumPayType.values()) {
            builder.put(status.getId(), status);
            builder2.put(status.getCode(), status);
        }
        INIT_MAP = builder.build();
        INIT_MAP2 = builder2.build();
    }

    public static EnumPayType of (final String type) {
        Preconditions.checkState(INIT_MAP.containsKey(type), "unknown status[{}]", type);
        return INIT_MAP.get(type);
    }

    public static EnumPayType codeOf (final String code) {
        Preconditions.checkState(INIT_MAP2.containsKey(code), "unknown status[{}]", code);
        return INIT_MAP2.get(code);
    }
}

