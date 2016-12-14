package com.jkm.hss.notifier.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konglingxin on 3/11/15.
 */
@Getter
public enum EnumVerificationCodeType {


    /**
     * 商户注册验证码
     */
    REGISTER_MERCHANT(1, EnumNoticeType.REGISTER_MERCHANT, 100),

    /**
     * 代理商登录验证码
     */
    LOGIN_DEALER(2, EnumNoticeType.LOGIN_DEALER, 100),

    /**
     * 商户绑定银行卡验证码
     */
    BIND_CARD_MERCHANT(3, EnumNoticeType.BIND_CARD_MERCHANT, 100),

    /**
     * 代理商绑定银行卡验证码
     */
    BIND_CARD_DEALER(4, EnumNoticeType.BIND_CARD_DEALER, 5),

    /**
     * 商户提现
     */
    WITH_DRAW(5, EnumNoticeType.WITHDRAW_CODE, 100);



    private static final Map<Integer, EnumVerificationCodeType> TYPE_MAP = new HashMap<>();

    static {
        for (final EnumVerificationCodeType enumVerificationCodeType : EnumVerificationCodeType.values()) {
            TYPE_MAP.put(enumVerificationCodeType.getId(), enumVerificationCodeType);
        }
    }

    private int id;

    /**
     * 短信类型
     */
    private EnumNoticeType smsType;

    /**
     * 每日发送次数
     */
    private int dailyCount;

    EnumVerificationCodeType(final int id,
                             final EnumNoticeType smsType,
                             final int dailyCount) {
        this.id = id;
        this.smsType = smsType;
        this.dailyCount = dailyCount;
    }

    /**
     * 根据id获取enum
     *
     * @param id
     * @return
     */
    public static final EnumVerificationCodeType integer2enum(final int id) {
        return TYPE_MAP.get(id);
    }
}
