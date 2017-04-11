package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.merchant.enums.EnumAccountBank;
import com.jkm.hss.merchant.enums.EnumBankDefault;
import com.jkm.hss.merchant.enums.EnumCleanType;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/6.
 */
@Data
public class AccountBank extends BaseEntity {
    /**
     * 账户编码
     */
    private long accountId;
    /**
     * 银行卡号
     */
    private String bankNo;
    /**
     * 银行卡名称
     */
    private String bankName;
    /**
     * 预留手机号
     */
    private String reserveMobile;
    /**
     * 支行编码
     */
    private String branchCode;
    /**
     * 支行名称
     */
    private String branchName;
    /**
     * 支行所在省份编码
     */
    private String branchProvinceCode;
    /**
     * 支行所属省份名称
     */
    private String branchProvinceName;
    /**
     * 支行所属城市编码
     */
    private String branchCityCode;
    /**
     * 支行所属城市名称
     */
    private String branchCityName;
    /**
     * 支行所属县编码
     */
    private String branchCountyCode;
    /**
     * 支行所属县名称
     */
    private String branchCountyName;
    /**
     * 银行卡类型（0借记卡 1借贷卡）
     * {@link EnumAccountBank}
     */
    private int cardType;
    /**
     * 是否认证通过
     */
    private String isAuthen;
    /**
     * 是否为默认银行卡
     * {@link EnumBankDefault}
     */
    private int isDefault;

    /**
     * 卡宾
     */
    private String bankBin;

    /**
     * 有效期
     */
    private String expiryTime;
    /**
     * cvv
     */
    private String cvv;

    /**
     * 更新类型
     * {@link EnumCleanType}
     */
    private int updateType;
}
