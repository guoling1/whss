package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/8/16.
 */
@Data
public class ApiMerchantRequest {
    private String merchantName;//商户名称
    private String mobile;//商户注册手机号
    private String provinceCode;//省份编码
    private String provinceName;//省份名称
    private String cityCode;//市编码
    private String cityName;//市名称
    private String countyCode;//县编码
    private String countyName;//县名称
    private String address;//地址
    private String branchCode;//联行号
    private String branchName;//支行名称
    private String districtCode;//支行所在地区编码
    private String dealerMarkCode;//代理商编号
    private String bankNo;//银行卡号
    private String name;//姓名
    private String identity;//身份证号
    private String reserveMobile;//预留手机号
    private String sign;//签
}
