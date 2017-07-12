package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/3/2.
 */
@Data
public class NoticeResponse {

    /**
     * 公告类型
     */
    private String type;

    /**
     * 公告id
     */
    private int id;

    /**
     * 发布时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 状态
     * tinyint
     */
    private int status;

    /**
     *  发布状态
     */
    private String pushStatus;

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
    private Date expirationTime;

    /**
     * 发布时间
     */
    private String dates;
}
