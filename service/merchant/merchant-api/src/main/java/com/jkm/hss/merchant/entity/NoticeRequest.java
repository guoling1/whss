package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/3/2.
 */
@Data
public class NoticeRequest {

    /**
     * 公告id
     */
    private int id;

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;

    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 每页显示页数
     */
    private int pageSize;

    /**
     * 条数
     */
    private Integer offset;

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
     *发布人
     */
    private String publisher;
}
