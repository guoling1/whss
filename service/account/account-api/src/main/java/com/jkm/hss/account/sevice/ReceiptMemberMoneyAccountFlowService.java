package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.ReceiptMemberMoneyAccountFlow;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
public interface ReceiptMemberMoneyAccountFlowService {

    /**
     * 插入
     *
     * @param receiptMemberMoneyAccountFlow
     */
    void add(ReceiptMemberMoneyAccountFlow receiptMemberMoneyAccountFlow);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<ReceiptMemberMoneyAccountFlow> getById(long id);
}
