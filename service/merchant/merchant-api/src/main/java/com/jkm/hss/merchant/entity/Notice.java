package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by zhangbin on 2017/3/2.
 */
@Data
public class Notice extends BaseEntity {

    /**
     * 公告类型
     */
    private String type;

    /**
     * 发布人
     */
    private String publisher;

    /**
     * 产品id
     */
    private int productId;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品类型
     */
    private String productType;

    /**
     * 发布标题
     */
    private String title;

    /**
     * 正文
     */
    private String text;

    /**
     * 过期时间
     */
    private Data expirationTime;
}
