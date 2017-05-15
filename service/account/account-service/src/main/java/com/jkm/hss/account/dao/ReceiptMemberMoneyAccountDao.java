package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.ReceiptMemberMoneyAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
@Repository
public interface ReceiptMemberMoneyAccountDao {

    /**
     * 插入
     *
     * @param receiptMemberMoneyAccount
     */
    void insert(ReceiptMemberMoneyAccount receiptMemberMoneyAccount);

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
    int increaseIncomeAmount(@Param("id") long id, @Param("income") BigDecimal income);

    /**
     * 充值金额增加
     *
     * @param id
     * @param recharge
     * @return
     */
    int increaseRechargeAmount(@Param("id") long id, @Param("recharge") BigDecimal recharge);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    ReceiptMemberMoneyAccount selectById(@Param("id") long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    ReceiptMemberMoneyAccount selectByIdWithLock(@Param("id") long id);
}
