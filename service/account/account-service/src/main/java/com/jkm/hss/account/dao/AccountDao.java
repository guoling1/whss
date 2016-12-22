package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.Account;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface AccountDao {

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
}