package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.Account;

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
}
