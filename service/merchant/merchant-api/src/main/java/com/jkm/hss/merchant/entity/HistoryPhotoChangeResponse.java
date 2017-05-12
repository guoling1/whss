package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/4/24.
 */
@Data
public class HistoryPhotoChangeResponse {

    /**
     * 身份证正面
     */
    private String identityFacePic;

    /**
     * 身份证反面
     */
    private String identityOppositePic;

    /**
     * 手持身份证
     */
    private String identityHandPic;

    /**
     * 手持银行卡
     */
    private String bankHandPic;
    /**
     * 银行卡照片
     */
    private String bankPic;

    /**
     * 商户id
     */
    private long merchantId;

    /**
     * 照片
     */
    private String pohto;

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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建时间
     */
    private String createTimes;
}
