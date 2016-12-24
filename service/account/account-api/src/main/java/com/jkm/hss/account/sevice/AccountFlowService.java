package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.AccountFlow;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface AccountFlowService {
    /**
     * 插入
     *
     * @param accountFlow
     */
    void add(AccountFlow accountFlow);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    Optional<AccountFlow> getById(long id);
}
