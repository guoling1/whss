package com.jkm.hss.product.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2017-04-06.
 */
public enum EnumGatewayType {

    PRODUCT("product", "产品网关"),

    DEALER("dealer", "代理商网关");

    @Getter
    private String id;
    @Getter
    private String msg;

    EnumGatewayType(String id, String msg){

        this.id = id;
        this.msg = msg;
    }
}
