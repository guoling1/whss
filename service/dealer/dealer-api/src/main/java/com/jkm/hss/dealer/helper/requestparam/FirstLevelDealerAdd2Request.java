package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 增加代理商 入参
 */
@Data
public class FirstLevelDealerAdd2Request {
    //=============基本信息========================
    /**
     * 代理手机号
     */
    private String mobile;

    /**
     * 代理名称
     */
    private String name;

    /**
     * 所在省code
     */
    private long belongProvinceCode;

    /**
     * 所在省
     */
    private String belongProvinceName;

    /**
     * 所在市code
     */
    private long belongCityCode;

    /**
     * 所在市
     */
    private String belongCityName;
    /**
     * 详细地址
     */
    private String belongArea;
    //================结算信息设置=====================
    /**
     * 结算卡
     */
    private String bankCard;

    /**
     * 银行开户名称
     */
    private String bankAccountName;
    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行预留手机号
     */
    private String bankReserveMobile;
    /**
     * 身份证号
     */
    private String idCard;

}
