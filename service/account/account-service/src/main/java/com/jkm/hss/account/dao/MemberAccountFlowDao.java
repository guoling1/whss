package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.MemberAccountFlow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
@Repository
public interface MemberAccountFlowDao {

    /**
     * 插入
     *
     * @param memberAccountFlow
     */
    void insert(MemberAccountFlow memberAccountFlow);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    MemberAccountFlow selectById(@Param("id") long id);
}
