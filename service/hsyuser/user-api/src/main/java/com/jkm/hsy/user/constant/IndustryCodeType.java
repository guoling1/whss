package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/1/10.
 */
public enum IndustryCodeType {
    CATERING(1000,"餐饮"),
    SUPERMARKET(1001,"商超"),
    LIFE_SERVICES(1002,"生活服务"),
    SHOPPING(1003,"购物"),
    BEAUTY(1004,"丽人"),
    EXERCISE(1005,"健身"),
    HOTEL(1006,"酒店");
    public int industryCodeKey;
    public String industryCodeValue;

    IndustryCodeType(int industryCodeKey, String industryCodeValue) {
        this.industryCodeKey = industryCodeKey;
        this.industryCodeValue = industryCodeValue;
    }
}
