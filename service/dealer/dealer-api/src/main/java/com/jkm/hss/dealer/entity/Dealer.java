package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumLoginStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/11/22.
 *
 * 经销商(代理商) model
 *
 * tb_dealer
 *
 * {@link com.jkm.hss.dealer.enums.EnumDealerStatus}
 */
@Data
public class Dealer extends BaseEntity {

    /**
     * 代理手机号
     */
    private String mobile;

    /**
     * 代理名称(唯一)
     */
    private String proxyName;
    /**
     * 登录名
     */
    private String loginName;
    /**
     * 登录密码
     */
    private String loginPwd;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 所在省code
     */
    private String belongProvinceCode;
    /**
     * 所在省
     */
    private String belongProvinceName;
    /**
     * 所在省code
     */
    private String belongCityCode;
    /**
     * 所在市
     */
    private String belongCityName;

    /**
     * 银行开户名称
     */
    private String bankAccountName;
    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 合伙人推荐功能开关
     * {@link com.jkm.hss.dealer.enums.EnumRecommendBtn}
     */
    private int recommendBtn;

    /**
     * 收单总分润空间
     */
    private BigDecimal totalProfitSpace;

    /**
     * 所属区域
     */
    private String belongArea;

    /**
     * 经销商级别
     *
     * {@link com.jkm.hss.dealer.enums.EnumDealerLevel}
     */
    private int level;

    /**
     * 一级经销商id
     */
    private long firstLevelDealerId;

    /**
     * 二级经销商id
     */
    private long secondLevelDealerId;

    /**
     * 资金帐号id
     */
    private long accountId;

    /**
     * 结算卡
     */
    private String settleBankCard;

    /**
     * 银行预留手机号
     */
    private String bankReserveMobile;


    /**
     * 最后一次登录时间
     */
    private Date lastLoginDate;

    /**
     * 是否登录
     */
    private boolean isLogin;


    /**
     * 代理商标示
     */
    private String markCode;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 推广功能
     * {@link com.jkm.hss.dealer.enums.EnumInviteBtn}
     */
    private int inviteBtn;



    public void setLogin () {
        this.isLogin = true;
    }

    public String getPlainMobile(){
        return this.mobile.substring(0,3) + "****" + this.mobile.substring(7,11);
    }
    public String getPlainBankMobile(String phone){
        return phone.substring(0,3) + "****" + phone.substring(7,11);
    }
}
