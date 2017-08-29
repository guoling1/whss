package com.jkm.api.enums;

import lombok.Getter;

/**
 * Created by xingliujie on 2017/8/17.
 */
public enum EnumOpenCardStatus {
    SUBMIT(0,"已提交"),
    SUCCESS(1,"开通成功"),
    FAIL(2,"开通失败");
    @Getter
    private int id;
    @Getter
    private String value;

    EnumOpenCardStatus(int id, String value){
        this.id = id;
        this.value = value;
    }
}
