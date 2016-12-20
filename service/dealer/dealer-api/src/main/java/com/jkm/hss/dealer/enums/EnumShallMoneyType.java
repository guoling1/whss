package com.jkm.hss.dealer.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2016-11-28.
 */
public enum EnumShallMoneyType {


    TOMERCHANT(1, "商户分润统计-分给二级"),
    TOSECOND(2, "二级代理分润统计-分给一级"),
    TOFIRST(3,"一级分润统计-一级分润"),
    TOCOMPANY(4, "公司分润统计"),
    TOSECONDSELF(5,"二级代理分润统计-二级自己");

    @Getter
    private int id;
    @Getter
    private String name;
    EnumShallMoneyType(int id, String name) {
        this.name = name;
        this.id = id;
    }
}
