package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by zhangbin on 2016/11/8.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class BankCardBin extends BaseEntity {

    /**
     * 卡bin编码
     */
    private String backCode;

    /**
     * 英文缩写
     */
    private String shorthand;

    /**
     * 银行标准名称
     */
    private String bankName;

    /**
     * 银行全称
     */
    private String fullName;

    /**
     * 卡名
     */
    private String cardName;

    /**
     * 卡长度
     */
    private String cardLength;

    /**
     * 银行卡主账号
     */
    private String cardNo;

    /**
     * 卡bin长度
     */
    private String binLength;

    /**
     * 卡bin号
     */
    private String binNo;

    /**
     * 卡类型
     */
    private String cardType;

    /**
     * 卡类型编码
     */
    private String cardTypeCode;


}
