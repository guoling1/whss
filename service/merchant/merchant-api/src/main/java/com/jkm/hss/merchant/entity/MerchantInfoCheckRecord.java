package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by zhangbin on 2016/11/22.
 */
@Data
public class MerchantInfoCheckRecord extends BaseEntity {

    /**
     * 商户编码
     */
    private long merchantId;

    /**
     * 原因描述
     */
    private String descr;
}
