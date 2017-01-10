package com.jkm.hss.merchant.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class MerchantInfoResponse {

    /**
     * 商户编号（正确的）
     */
    private String markCode;

    /**
     * 条数
     */
    private Integer offset;


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
     * 认证时间
     */
    private String authenticationTime;

    /**
     * 代理商级别
     */
    private long firstLevelDealerId;



    /**
     * 经销商级别
     *
     * {@link com.jkm.hss.merchant.enums.EnumDealerLevel}
     */
    private int level;

    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 每页显示页数
     */
    private int pageSize;

    /**
     * 注册手机号
     */
    private String mobile;

    /**
     * 商户id
     */
    private long id;

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
    private long dealerId;
    /**
     * 账户id
     */
    private long accountId;
    /**
     *审核通过时间
     */
    private String checkedTime;

    /**
     * 所属1代理商名称
     */
    private String proxyName;

    /**
     * 所属2代理商名称
     */
    private String proxyName1;

    /**
     * 注册时间
     * datetime
     */
    private Date createTime;

    /**
     * 商户状态
     */
    private Integer status;

    /**
     * code
     */
    private String code;



}
