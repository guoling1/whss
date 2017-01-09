package com.jkm.hss.merchant.enums;

import lombok.Getter;

/**
 * Created by Thinkpad on 2017/1/5.
 */
public enum EnumUpgradeResult {
    /**
     * 初始化
     */
    INIT(0, "等待结果"),
    /**
     * 初始化
     */
    SUCCESS(1, "升级成功"),
    /**
     * 初始化
     */
    FAIL(2, "升级失败");



    @Getter
    private int id;
    @Getter
    private String name;

    EnumUpgradeResult(final int id, final String name) {
        this.id = id;
        this.name = name;
    }
}
