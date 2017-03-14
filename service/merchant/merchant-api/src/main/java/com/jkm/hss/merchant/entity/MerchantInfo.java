package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class MerchantInfo extends BaseEntity{
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
     * 预留手机号
     */
    private String reserveMobile;

    /**
     * 商户名称
     */
    private String merchantName;

    /**
     * 地址
     */
    private String address;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String identity;

    /**
     * 卡bin
     */
    private String bankBin;

    /**
     * 银行卡名称
     */
    private String bankName;

    /**
     * 结算卡
     */
    private String bankNo;

    /**
     * 代理商id
     */
    private long dealerId=0;
    /**
     * 账户id
     */
    private long accountId;
    /**
     *审核通过时间
     */
    private Date checkedTime;

    /**
     * 认证时间
     */
    private Date authenticationTime;

    /**
     * code
     */
    private String code;
    /**
     * 注册手机号
     */
    private String mobile;
    /**
     * 加密手机号
     */
    private String mdMobile;
    /**
     * 银行卡简写
     */
    private String bankNoShort;

    /**
     * 一级代理商编码
     */
    private long firstDealerId;
    /**
     * 二级代理商编码
     */
    private long secondDealerId;
    /**
     * 直接商户 上级
     */
    private long firstMerchantId;
    /**
     * 间接商户 上上级
     */
    private long secondMerchantId;
    /**
     * 商户标示码
     */
    private String markCode;

    /**
     * 商户更改商户名
     */
    private String merchantChangeName;

    /**
     * 合伙人级别
     *
     */
    private Integer level;
    /**
     * 微信费率
     */
    private BigDecimal weixinRate;
    /**
     * 支付宝费率
     */
    private BigDecimal alipayRate;
    /**
     * 快捷费率
     */
    private BigDecimal fastRate;
    /**
     * 当前商户层级
     */
    private Integer hierarchy;
    /**
     * 产品编码
     */
    private Long productId;
    /**
     * 商户来源
     */
    private int source;

    /**
     * 是否升级
     * {@link com.jkm.hss.merchant.enums.EnumIsUpgrade}
     */
    private int isUpgrade;

    /**
     * 支行编码
     */
    private String branchCode;
    /**
     * 支行名称
     */
    private String branchName;
    /**
     * 所在省份编码
     */
    private String provinceCode;
    /**
     * 所在省份名称
     */
    private String provinceName;
    /**
     * 所在城市编码
     */
    private String cityCode;
    /**
     * 所在城市名称
     */
    private String cityName;
    /**
     * 所在县编码
     */
    private String countyCode;
    /**
     * 所在县名称
     */
    private String countyName;
    /**
     * 信用卡号
     */
    private String creditCard;
    /**
     * 信用卡银行名称
     */
    private String creditCardName;
    /**
     * 银行卡后四位
     */
    private String creditCardShort;

    /**
     * 是否认证 1认证 其他未认证
     */
    private String isAuthen;

    public String getPlainBankMobile(String phone){
        return phone.substring(0,3) + "****" + phone.substring(7,11);
    }

}
