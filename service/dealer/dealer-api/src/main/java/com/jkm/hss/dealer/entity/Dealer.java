package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumLoginStatus;
import lombok.Data;

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
     * 银行开户名称
     */
    private String bankAccountName;

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


    public void setLogin () {
        this.isLogin = true;
    }

    public String getPlainMobile(){
        return this.mobile.substring(0,3) + "****" + this.mobile.substring(7,11);
    }
}
