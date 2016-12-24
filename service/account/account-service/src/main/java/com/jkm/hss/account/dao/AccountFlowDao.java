package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.AccountFlow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface AccountFlowDao {

    /**
     * 插入
     *
     * @param accountFlow
     */
    void insert(AccountFlow accountFlow);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    AccountFlow selectById(@Param("id") long id);
}
