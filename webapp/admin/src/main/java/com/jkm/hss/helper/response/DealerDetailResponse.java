package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/9.
 */
@Data
public class DealerDetailResponse {
    /**
     * 代理商编码
     */
    private long id;


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
     * 代理编码
     */
    private String markCode;

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
     * 上级代理名称
     */
    private String firstDealerName;

    /**
     * 上级代理编号
     */
    private String firstMarkCode;

    /**
     * 结算卡
     */
    private String bankCard;

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
