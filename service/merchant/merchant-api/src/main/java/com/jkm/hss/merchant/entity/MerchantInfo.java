package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

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



}
