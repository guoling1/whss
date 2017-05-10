package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/5/10.
 */
@Data
public class ToUpgradeResponse {
    /**
     * 当前级别
     */
    private int currentLevel;
    /**
     * 商户编码
     */
    private long merchantId;
    /**
     * 分享地址
     */
    private String shareUrl;
    /**
     * 规则列表
     */
    private List<CurrentRulesResponse> upgradeRules;
}
