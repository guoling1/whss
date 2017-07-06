package com.jkm.hss.admin.enums;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import lombok.Getter;

/**
 * Created by xingliujie on 2017/2/22.
 */
public enum EnumDataDictionaryType {
    /**
     * 公司
     */
    COMPANY("user_company", "公司"),

    /**
     * 部门
     */
    DEPT("user_dept", "部门");



    @Getter
    private String id;

    @Getter
    private String value;

    EnumDataDictionaryType(final String id, final String value) {
        this.id = id;
        this.value = value;
    }
}
