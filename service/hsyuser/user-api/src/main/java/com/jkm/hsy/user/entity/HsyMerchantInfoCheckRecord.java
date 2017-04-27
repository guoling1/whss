package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by zhangbin on 2016/11/22.
 */
@Data
public class HsyMerchantInfoCheckRecord extends BaseEntity {

    /**
     * 商户编码
     */
    private long merchantId;

    /**
     *  数据库shop中id
     */
    private long sid;

    /**
     * 审核人
     */
    private String name;

    /**
     * 原因描述
     */
    private String descr;

}
