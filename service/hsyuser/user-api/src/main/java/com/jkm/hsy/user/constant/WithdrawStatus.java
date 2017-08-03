package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/7/31.
 */
public enum WithdrawStatus {
    NORMAL(1,"正常"),
    REGISTING(2,"注册中"),
    COMMIT_CHECKING(3,"提交结算审核"),
    COMMIT_NOT_PASS(4,"审核驳回"),
    REJECT(5,"审核拒绝"),
   ;

    public int key;
    public String value;

    WithdrawStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
