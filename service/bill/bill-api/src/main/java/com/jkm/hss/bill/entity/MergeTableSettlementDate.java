package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/3/16.
 *
 * 并表结算结算日期
 *
 * tb_merge_table_settlement_date
 */
@Data
public class MergeTableSettlementDate extends BaseEntity {

    /**
     * 并表开始日期
     */
    private Date beganDate;
    /**
     * 并表结束日期
     */
    private Date endDate;
    /**
     * 结算日期
     */
    private Date settleDate;
    /**
     * 并表理由
     */
    private Date reason;
    /**
     * 上游渠道
     *
     * {@link com.jkm.hss.product.enums.EnumUpperChannel}
     */
    private int upperChannel;
}
