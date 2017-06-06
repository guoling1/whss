package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/4/24.
 */
@Data
public class HsyHistoryPhotoChangeResponse {

    private String idcardf;//身份证正面照

    private String idcardb;//身份证背面照

    private String idcardc;//银行卡照片

    private String licenceID;//营业执照id

    private String storefrontID;//店面照片id

    private String counterID;//收银台ID

    private String indoorID;//室内ID

    /**
     * hsy店铺id
     */
    private long sid;

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
     * hsy类型
     *
     */
    private int hsyType;

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
     * 更新时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private String createTimes;
}
