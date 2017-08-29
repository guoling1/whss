package com.jkm.api.helper.requestparam;

import com.google.common.base.Objects;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.api.helper.sdk.serialize.annotation.SdkSerializeAlias;
import lombok.Data;

/**
 * Created by xingliujie on 2017/8/16.
 */
@Data
public class MerchantRequest {
    /**
     * 代理商编号
     */
    @SdkSerializeAlias(signSort = 1, needSign = true)
    private String dealerMarkCode;
    /**
     * 商户名称
     */
    @SdkSerializeAlias(signSort = 2, needSign = true)
    private String merchantName;
    /**
     * 商户注册手机号
     */
    @SdkSerializeAlias(signSort = 3, needSign = true)
    private String mobile;
    /**
     * 省份编码
     */
    @SdkSerializeAlias(signSort = 4, needSign = true)
    private String provinceCode;
    /**
     * 市编码
     */
    @SdkSerializeAlias(signSort = 5, needSign = true)
    private String cityCode;
    /**
     * 县编码
     */
    @SdkSerializeAlias(signSort = 6, needSign = true)
    private String countyCode;
    /**
     * 地址
     */
    @SdkSerializeAlias(signSort = 7, needSign = true)
    private String address;
    /**
     * 联行号
     */
    @SdkSerializeAlias(signSort = 8, needSign = true)
    private String branchCode;
    /**
     * 支行名称
     */
    @SdkSerializeAlias(signSort = 9, needSign = true)
    private String branchName;
    /**
     * 支行所在地省份编码
     */
    @SdkSerializeAlias(signSort = 10, needSign = true)
    private String bankProvinceCode;
    /**
     * 支行所在地市编码
     */
    @SdkSerializeAlias(signSort = 11, needSign = true)
    private String bankCityCode;
    /**
     * 支行所在地区县编码
     */
    @SdkSerializeAlias(signSort = 12, needSign = true)
    private String bankCountryCode;
    /**
     * 银行卡号
     */
    @SdkSerializeAlias(signSort = 13, needSign = true)
    private String bankNo;
    /**
     * 姓名
     */
    @SdkSerializeAlias(signSort = 14, needSign = true)
    private String name;
    /**
     * 身份证号
     */
    @SdkSerializeAlias(signSort = 15, needSign = true)
    private String identity;
    /**
     * 预留手机号
     */
    @SdkSerializeAlias(signSort = 16, needSign = true)
    private String reserveMobile;

    private String sign;//签
    /**
     * 校验签名
     *
     * @param key
     */
    public boolean verifySign(final String key) {
        if ("JKM-SIGN-TEST".equals(this.sign)) {
            return true;
        }
        return Objects.equal(SdkSignUtil.sign2(this, key), this.sign);
    }
}
