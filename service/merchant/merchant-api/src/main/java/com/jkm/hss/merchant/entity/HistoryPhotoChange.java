package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by zhangbin on 2017/4/21.
 */
@Data
public class HistoryPhotoChange extends BaseEntity {

    /**
     * 商户id
     */
    private long merchantId;

    /**
     * hsy店铺id
     */
    private long sid;

    /**
     * 照片
     */
    private String photo;

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
}
