package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.Account;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;


/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface AccountDao {

    /**
     * 初始化手续费账户（其他不可调用）
     *
     * @param account
     */
    void initPoundageAccount(Account account);

    /**
     * 插入
     *
     * @param account
     */
    void insert(Account account);

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
    int increaseTotalAmount(@Param("id") long id, @Param("amount") BigDecimal amount);

    /**
     * 总金额减少
     *
     * @param id
     * @param amount
     * @return
     */
    int decreaseTotalAmount(@Param("id") long id, @Param("amount") BigDecimal amount);

    /**
     * 可用余额增加
     *
     * @param id
     * @param amount
     * @return
     */
    int increaseAvailableAmount(@Param("id") long id, @Param("amount") BigDecimal amount);

    /**
     * 可用余额减少
     *
     * @param id
     * @param amount
     * @return
     */
    int decreaseAvailableAmount(@Param("id") long id, @Param("amount") BigDecimal amount);

    /**
     * 冻结余额增加
     *
     * @param id
     * @param amount
     * @return
     */
    int increaseFrozenAmount(@Param("id") long id, @Param("amount") BigDecimal amount);

    /**
     * 冻结余额减少
     *
     * @param id
     * @param amount
     * @return
     */
    int decreaseFrozenAmount(@Param("id") long id, @Param("amount") BigDecimal amount);

    /**
     * 待结算余额增加
     *
     * @param id
     * @param amount
     * @return
     */
    int increaseSettleAmount(@Param("id") long id, @Param("amount") BigDecimal amount);

    /**
     * 待结算余额减少
     *
     * @param id
     * @param amount
     * @return
     */
    int decreaseSettleAmount(@Param("id") long id, @Param("amount") BigDecimal amount);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Account selectById(@Param("id") long id);


    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Account selectByIdWithLock(@Param("id") long id);

    /**
     *
     * @param accountID
     * @return
     */
    Account getAccId(@Param("accountID") Long accountID);

    /**
     * 删除账户
     * @param accountID
     */
    void delAcct(@Param("accountID") Long accountID);
}