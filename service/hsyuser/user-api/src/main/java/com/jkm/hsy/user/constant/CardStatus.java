package com.jkm.hsy.user.constant;

/**
 * Created by Allen on 2017/6/9.
 */
public enum CardStatus {
    USING(1,"正常"),
    HALT_USING(2,"停止使用"),
    ;
    public int key;
    public String value;

    CardStatus(int key, String value) {
        this.key = key;
        this.value = value;
    }
}
