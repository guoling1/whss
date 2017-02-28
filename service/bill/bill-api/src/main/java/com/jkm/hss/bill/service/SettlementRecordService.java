package com.jkm.hss.bill.service;

import com.google.common.base.Optional;
import com.jkm.hss.bill.entity.SettlementRecord;

/**
 * Created by yulong.zhang on 2017/2/22.
 */
public interface SettlementRecordService {

    /**
     * 添加
     *
     * @param settlementRecord
     * @return
     */
    long add(SettlementRecord settlementRecord);

    /**
     * 更新
     *
     * @param settlementRecord
     * @return
     */
    int update(SettlementRecord settlementRecord);

    /**
     * 更新状态
     *
     * @param status
     * @param id
     */
    int updateStatus(long id, int status);

    /**
     * 更新结算状态
     *
     * @param id
     * @param settleStatus
     */
    int updateSettleStatus(long id, int settleStatus);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<SettlementRecord> getById(long id);

    /**
     * 按id查询加锁
     *
     * @param id
     * @return
     */
    Optional<SettlementRecord> getByIdWithLock(long id);

    /**
     * 按结算单号查询
     *
     * @param settleNo
     * @return
     */
    Optional<SettlementRecord> getBySettleNo(String settleNo);
}
