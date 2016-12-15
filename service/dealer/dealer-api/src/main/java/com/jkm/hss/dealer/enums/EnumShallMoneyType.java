package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-28.
 */
public enum EnumShallMoneyType {


    TOMERCHANT(1, "商户分润统计"),
    TOSECOND(2, "二级代理分润统计"),
    TOFIRST(3,"一级分润统计"),
    TOCOMPANY(4, "公司分润统计");

    @Getter
    private int id;
    @Getter
    private String name;
    EnumShallMoneyType(int id, String name) {
        this.name = name;
        this.id = id;
    }
}
