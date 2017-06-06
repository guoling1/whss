package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.MemberAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
@Repository
public interface MemberAccountDao {

    /**
     * 插入
     *
     * @param memberAccount
     */
    void insert(MemberAccount memberAccount);

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
    MemberAccount selectById(@Param("id") long id);

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    MemberAccount selectByIdWithLock(@Param("id") long id);
}
