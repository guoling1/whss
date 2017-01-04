package com.jkm.hss.account.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 解冻记录
 *
 * tb_unfrozen_record
 */
@Data
public class UnFrozenRecord extends BaseEntity {

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 冻结记录id
     */
    private long frozenRecordId;

    /**
     * 业务流水号
     */
    private String businessNo;

    /**
     * 解冻类型
     *
     * {@link com.jkm.hss.account.enums.EnumUnfrozenType}
     */
    private int unfrozenType;

    /**
     * 解冻额度
     */
    private BigDecimal unfrozenAmount;

    /**
     * 解冻时间
     */
    private Date unfrozenTime;

    /**
     * 备注
     */
    private String remark;
}
