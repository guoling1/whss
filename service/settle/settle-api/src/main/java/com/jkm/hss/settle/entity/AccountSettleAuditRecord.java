package com.jkm.hss.settle.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.settle.enums.EnumAccountCheckStatus;
import com.jkm.hss.settle.enums.EnumSettleStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/1/12.
 *
 * 结算审核记录
 *
 * tb_account_settle_audit_record
 */
@Data
public class AccountSettleAuditRecord extends BaseEntity {

    /**
     * 编号
     */
    private String userNo;

    /**
     * 名称
     */
    private String userName;

    /**
     * 账户类型
     *
     * {@link com.jkm.hss.account.enums.EnumAccountUserType}
     */
    private int accountUserType;

    /**
     * 账户id
     */
    private long accountId;

    /**
     * 交易日期
     */
    private Date tradeDate;

    /**
     * 交易笔数
     */
    private int tradeNumber;

    /**
     * 结算总金额
     */
    private BigDecimal settleAmount;

    /**
     * 对账状态
     *
     * {@link com.jkm.hss.settle.enums.EnumAccountCheckStatus}
     */
    private int accountCheckStatus;

    /**
     * 结算状态
     *
     * {@link com.jkm.hss.settle.enums.EnumSettleStatus}
     */
    private int settleStatus;

    /**
     * 结算日期
     */
    private Date settleDate;

    /**
     * 是否是待对账
     *
     * @return
     */
    public boolean isDueAccountCheck() {
        return EnumAccountCheckStatus.DUE_ACCOUNT_CHECK.getId() == this.accountCheckStatus;
    }

    /**
     * 是否是对账完成
     *
     * @return
     */
    public boolean isSuccessAccountCheck() {
        return EnumAccountCheckStatus.SUCCESS_ACCOUNT_CHECK.getId() == this.accountCheckStatus;
    }

    /**
     * 是否是单边
     *
     * @return
     */
    public boolean isSideException() {
        return EnumAccountCheckStatus.SIDE_EXCEPTION.getId() == this.accountCheckStatus;
    }

    /**
     * 是否是待结算
     *
     * @return
     */
    public boolean isDueSettle() {
        return EnumSettleStatus.DUE_SETTLE.getId() == this.settleStatus;
    }

    /**
     * 是否是结算成功(全部)
     *
     * @return
     */
    public boolean isSettleSuccess() {
        return EnumSettleStatus.SETTLED_ALL.getId() == this.settleStatus;
    }
}
