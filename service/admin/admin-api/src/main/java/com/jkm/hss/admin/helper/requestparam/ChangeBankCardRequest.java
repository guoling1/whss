package com.jkm.hss.admin.helper.requestparam;

import lombok.Data;


@Data
public class ChangeBankCardRequest{
    /**
     * 账户编码
     */
    private long merchantId;
    /**
     * 银行卡号
     */
    private String bankNo;
    /**
     * 预留手机号
     */
    private String reserveMobile;

    /**
     * 支行名称
     */
    private String branchName;
    /**
     * 支行所在省份编码
     */
    private String branchProvinceCode;
    /**
     * 支行所属省份名称
     */
    private String branchProvinceName;
    /**
     * 支行所属城市编码
     */
    private String branchCityCode;
    /**
     * 支行所属城市名称
     */
    private String branchCityName;
    /**
     * 支行所属县编码
     */
    private String branchCountyCode;
    /**
     * 支行所属县名称
     */
    private String branchCountyName;
}
