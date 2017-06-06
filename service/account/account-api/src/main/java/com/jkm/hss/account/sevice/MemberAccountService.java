package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.MemberAccount;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
public interface MemberAccountService {
    /**
     * 插入
     *
     * @param memberAccount
     */
    void add(MemberAccount memberAccount);

    /**
     * 初始化账户
     *
     * @return
     */
    long init();

    /**
     * 更新
     *
     * @param memberAccount
     * @return
     */
    int update(MemberAccount memberAccount);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<MemberAccount> getById(long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Optional<MemberAccount> getByIdWithLock(long id);
}
