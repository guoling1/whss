package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by zhangbin on 2017/4/21.
 */
@Data
public class HistoricalPhotoChange extends BaseEntity {

    /**
     * 商户id
     */
    private long merchantId;

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
