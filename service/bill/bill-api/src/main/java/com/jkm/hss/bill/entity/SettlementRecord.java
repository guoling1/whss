package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.bill.enums.EnumSettlementRecordStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/2/22.
 *
 * 结算单
 *
 * tb_settlement_record
 *
 * {@link com.jkm.hss.bill.enums.EnumSettlementRecordStatus}
 */
@Data
public class SettlementRecord extends BaseEntity {

    /**
     * 结算单号
     */
    private String settleNo;

    /**
     * 结算审核记录id
     */
    private long settleAuditRecordId;

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 商户编号
     */
    private String merchantNo;

    /**
     * 商户名
     */
    private String merchantName;

    /**
     * 业务线（hss,hsy）
     */
    private String appId;

    /**
     * 代理商编号
     */
    private String dealerNo;

    /**
     * 代理商名
     */
    private String dealerName;

    /**
     * 结算日期
     */
    private Date settleDate;

    /**
     * 结算笔数
     */
    private int tradeNumber;

    /**
     * 结算金额
     */
    private BigDecimal settleAmount;

    /**
     * 结算目的地(结算类型)
     *
     * {@link com.jkm.hss.bill.enums.EnumSettleDestinationType}
     */
    private int settleDestination;

    /**
     * 银行账户号
     */
    private String bankNo;

    /**
     * 银行开户名
     */
    private String bankName;

    /**
     * 结算状态
     *
     * {@link com.jkm.hss.bill.enums.EnumSettleStatus}
     */
    private int settleStatus;

    /**
     * 是否待提现
     *
     * @return
     */
    public boolean isWaitWithdraw() {
        return EnumSettlementRecordStatus.WAIT_WITHDRAW.getId() == this.status;
    }

    /**
     * 是否已经提现
     *
     * @return
     */
    public boolean isWithdrawing() {
        return EnumSettlementRecordStatus.WITHDRAWING.getId() == this.status;
    }
}
