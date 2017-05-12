package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Data
public class FirstLevelDealerUpdate2Request {

    /**
     * 一级代理商id
     */
    private long dealerId;

    /**
     * 代理手机号
     */
    private String mobile;

    /**
     * 代理名称
     */
    private String name;
    /**
     * 登录名
     */
    private String loginName;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 代理商归属
     */
    private String dealerBelong;

    /**
     * 所在省code
     */
    private String belongProvinceCode;
    /**
     * 所在省
     */
    private String belongProvinceName;
    /**
     * 所在省code
     */
    private String belongCityCode;
    /**
     * 所在市
     */
    private String belongCityName;

    /**
     * 所在地
     */
    private String belongArea;

    /**
     * 结算卡
     */
    private String bankCard;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行开户名称
     */
    private String bankAccountName;

    /**
     * 银行预留手机号
     */
    private String bankReserveMobile;
    /**
     * 身份证号
     */
    private String idCard;
}
