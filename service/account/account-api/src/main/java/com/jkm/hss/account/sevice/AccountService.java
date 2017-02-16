package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.Account;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface AccountService {
    /**
     * 插入
     *
     * @param account
     */
    void add(Account account);

    /**
     * 初始化账户
     *
     * @param userName
     * @return
     */
    long initAccount(String userName);

    /**
     * 更新
     *
     * @param account
     * @return
     */
    int update(Account account);

    /**
     * 总金额增加
     *
     * @param id
     * @param amount
     * @return
     */
    int increaseTotalAmount(long id, BigDecimal amount);

    /**
     * 总金额减少
     *
     * @param id
     * @param amount
     * @return
     */
    int decreaseTotalAmount(long id, BigDecimal amount);

    /**
     * 可用余额增加
     *
     * @param id
     * @param amount
     * @return
     */
    int increaseAvailableAmount(long id, BigDecimal amount);

    /**
     * 可用余额减少
     *
     * @param id
     * @param amount
     * @return
     */
    int decreaseAvailableAmount(long id, BigDecimal amount);

    /**
     * 冻结金额余额增加
     *
     * @param id
     * @param amount
     * @return
     */
    int increaseFrozenAmount(long id, BigDecimal amount);

    /**
     * 冻结余额减少
     *
     * @param id
     * @param amount
     * @return
     */
    int decreaseFrozenAmount(long id, BigDecimal amount);

    /**
     * 待结算余额增加
     *
     * @param id
     * @param amount
     * @return
     */
    int increaseSettleAmount(long id, BigDecimal amount);

    /**
     * 待结算余额减少
     *
     * @param id
     * @param amount
     * @return
     */
    int decreaseSettleAmount(long id, BigDecimal amount);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<Account> getById(long id);


    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Optional<Account> getByIdWithLock(long id);

    Account getAccId(Long accountID);

    /**
     * 删除账户
     * @param accountID
     */
    void delAcct(Long accountID);
}
