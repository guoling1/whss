package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/11/22.
 */
@Data
public class AccountInfo extends BaseEntity {

    /**
     *总金额
     */
    private BigDecimal amount;

    /**
     *可用金额
     */
    private BigDecimal available;

    /**
     *冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 待结算
     */
    private BigDecimal unsettled;



}
