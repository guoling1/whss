package com.jkm.hss.account.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangbin on 2017/5/22.
 */
@Data
public class ProfitCountRespons {

    /**
     * 代理名称
     */
    private String proxyName;

    /**
     * 代理编号
     */
    private String markCode;

    /**
     * yu
     * 业务类型
     * {@link com.jkm.hss.account.enums.EnumSplitBusinessType}
     */
    private String businessType;

    /**
     * 分账所得金额
     */
    private BigDecimal splitAmount;

    /**
     * 分账日期
     */
    private Date splitDate;

    /**
     * 分账日期
     */
    private String splitDates;

}
