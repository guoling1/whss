package com.jkm.hss.settle.service;

import com.jkm.hss.settle.entity.AccountSettleAuditRecord;

/**
 * Created by yulong.zhang on 2017/1/12.
 */
public interface AccountSettleAuditRecordService {

    /**
     * 插入
     *
     * @param record
     */
    void add(AccountSettleAuditRecord record);

    /**
     * 更新对账状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateAccountCheckStatus(long id, int status);

    /**
     * 更新结算状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateSettleStatus(long id, int status);
}
