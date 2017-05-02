package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/4/24.
 */
@Data
public class HistoryPhotoChangeRequest {

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
     * 商户id
     */
    private long merchantId;

    /**
     * 照片
     */
    private String photo;

    /**
     * 照片地址
     */
//    private String photoName;

    /**
     * 类型（1：结算卡2：手持结算卡：3手持身份证4：身份证正面5：身份证反面）
     *
     */
    private int type;

    /**
     * 原因描述
     */
    private String reasonDescription;

    /**
     * 名称（结算卡、手持银行卡）
     */
    private String cardName;

    /**
     * 操作人
     */
    private String operator;
}
