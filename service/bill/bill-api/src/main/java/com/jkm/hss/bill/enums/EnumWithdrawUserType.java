package com.jkm.hss.bill.enums;

import lombok.Getter;

/**
 * Created by yuxiang on 2017-07-24.
 *
 * 提现用户类型
 */
public enum EnumWithdrawUserType {

    HSY_MERCHANT(1,"钱包++商户");

    @Getter
    private int type;
    @Getter
    private String msg;

     EnumWithdrawUserType(int type, String msg){
         this.type = type;
         this.msg = msg;
     }
}
