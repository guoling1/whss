package com.jkm.hss.notifier.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by konglingxin on 3/11/15.
 * 通知类型
 */
@Getter
public enum EnumNoticeType {

    //########## 公共消息 ##########

//    LOGIN_ADMIN(101, "boss后台登录"),
//    LOGIN_MERCHANT(102, "商户登录"),
    LOGIN_DEALER(103, "代理商登录"),

//    MODIFY_PASSWORD_ADMIN(201, "boss后台修改密码"),
//    MODIFY_PASSWORD_MERCHANT(202, "商户修改密码"),
//    MODIFY_PASSWORD_DEALER(203, "经销商修改密码"),

//    RETRIEVE_PASSWORD_ADMIN(301, "boss后台找回密码"),
//    RETRIEVE_PASSWORD_MERCHANT(302, "商户找回密码"),
//    RETRIEVE_PASSWORD_DEALER(303, "经销商找回密码"),


    REGISTER_MERCHANT(401, "商户注册"),
    LOGIN_MERCHANT(402, "商户登录"),
    //########## 绑定银行卡消息 ##########
    BIND_CARD_MERCHANT(501, "商户绑定银行卡"),
    BIND_CARD_DEALER(502, "代理商绑定银行卡"),


    //########## 支付消息 ##########
    PAYMENT_CODE(601, "支付验证码"),

    //########## 提现消息 ##########
    WITHDRAW_CODE(701, "提现验证码"),

    /**
     * 代理商提现
     */
    WITHDRAW_CODE_DEALER(702, "代理商提现验证码"),

    /**
     * 商户提现
     */
    WITHDRAW_CODE_MERCHANT(703, "商户提现验证码"),

    //#############退款消息################

    DUPLICATE_PAY_REFUND(801, "重复支付退款"),

    /**
     * 员工密码
     */

    EMPLOYEE_PASSWORD(901, " 员工密码"),

    /**
     * 商户资料审核通过
     */

    AUDIT_PASS(902, " 商户资料审核通过"),

    /**
     * 商户资料审核未通过
     */

    AUDIT_NOPASS(903, " 商户资料审核未通过"),


    /**
     * 店铺收银
     */

    CASH(904, " 店铺收银"),

    /**
     * 店铺收银
     */

    CASH_OUT(905, " 提现");


    private static final Map<Integer, EnumNoticeType> ENUM_MAP = new HashMap<>();

    static {
        for (final EnumNoticeType enumNoticeType : EnumNoticeType.values()) {
            ENUM_MAP.put(enumNoticeType.getId(), enumNoticeType);
        }
    }

    private int id;
    private String desc;

    /**
     * 构造函数
     *
     * @param id
     * @param desc
     */
    EnumNoticeType(final int id, final String desc) {
        this.id = id;
        this.desc = desc;
    }

    /**
     * convertor
     *
     * @param id
     * @return
     */
    public static EnumNoticeType integer2Enum(final int id) {
        return ENUM_MAP.get(id);
    }
}
