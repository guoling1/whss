package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by Thinkpad on 2016/12/29.
 * 推广好友
 * tb_recommend
 */
@Data
public class Recommend extends BaseEntity{
    /**
     * 商户编码
     */
    private Long merchantId;

    /**
     * 推广人
     */
    private Long recommendMerchantId;

    /**
     * 推广类型
     *{@link com.jkm.hss.merchant.enums.EnumRecommendType}
     */
    private Integer type;

}
