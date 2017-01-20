package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 冻结记录
 *
 * tb_frozen_record
 */
@Data
public class FrozenRecord extends BaseEntity{

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 交易订单号
     */
    private String businessNo;

    /**
     * 冻结额度
     */
    private BigDecimal frozenAmount;

    /**
     * 冻结时间
     */
    private Date frozenTime;

    /**
     * 备注
     */
    private String remark;
}
