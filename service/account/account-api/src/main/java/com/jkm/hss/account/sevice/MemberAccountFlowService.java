package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.MemberAccountFlow;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
public interface MemberAccountFlowService {

    /**
     * 插入
     *
     * @param memberAccountFlow
     */
    void add(MemberAccountFlow memberAccountFlow);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<MemberAccountFlow> selectById(long id);
}
