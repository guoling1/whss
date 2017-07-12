package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.dealer.enums.EnumShallMoneyType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-24.
 *
 * 每日分润统计
 * tb_daily_profit_detail
 */
@Data
public class DailyProfitDetail extends BaseEntity{
    /**
     * 分润统计类型 {@link EnumShallMoneyType}
     * 1:商户  2:二级
     */
    private int shallMoneyType;
    /**
     * 所属商户id
     */
    private long merchantId;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 所属二级代理商
     */
    private long secondDealerId;
    /**
     * 所属二级代理商名称
     */
    private String dealerName;
    /**
     * 所属一级代理商
     */
    private long firstDealerId;
    /**
     * 所属一级代理商名称
     */
    private String firstDealerName;
    /**
     * 统计的是哪天的交易
     * 1999-09-09
     */
    private String statisticsDate;

    /**
     * 收单分润
     */
    private BigDecimal collectMoney;

    /**
     * 提现分润
     */
    private BigDecimal withdrawMoney;

    /**
     * 分润总额
     */
    private BigDecimal totalMoney;
}
