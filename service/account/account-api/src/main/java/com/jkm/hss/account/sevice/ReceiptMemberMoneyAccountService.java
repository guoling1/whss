package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.ReceiptMemberMoneyAccount;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
public interface ReceiptMemberMoneyAccountService {

    /**
     * 插入
     *
     * @param receiptMemberMoneyAccount
     */
    void add(ReceiptMemberMoneyAccount receiptMemberMoneyAccount);

    /**
     * 初始化账户
     *
     * @return
     */
    long init();

    /**
     * 更新
     *
     * @param receiptMemberMoneyAccount
     * @return
     */
    int update(ReceiptMemberMoneyAccount receiptMemberMoneyAccount);

    /**
     * 收入总金额增加
     *
     * @param id
     * @param income
     * @return
     */
    int increaseIncomeAmount(long id, BigDecimal income);

    /**
     * 充值总金额增加
     *
     * @param id
     * @param recharge
     * @return
     */
    int increaseRechargeAmount(long id, BigDecimal recharge);
    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<ReceiptMemberMoneyAccount> getById(long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Optional<ReceiptMemberMoneyAccount> getByIdWithLock(long id);
}
