package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by Thinkpad on 2016/12/29.
 */
@Data
public class RecommendShort {
    /**
     * 好友姓名
     */
    private String name;
    /**
     * 好友类型
     * {@link com.jkm.hss.merchant.enums.EnumRecommendType}
     */
    private int type;
    /**
     * 审核状态
     * {@link com.jkm.hss.merchant.enums.EnumMerchantStatus}
     */
    private int status;

    /**
     * 审核状态
     */
    private String statusName;
    /**
     * 手机号
     */
    private String mobile;
}
