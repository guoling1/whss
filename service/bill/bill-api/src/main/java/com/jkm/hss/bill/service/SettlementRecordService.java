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

    /**
     * 按结算审核记录id查找结算单
     *
     * @param settleAuditRecordId
     * @return
     */
    Optional<SettlementRecord> getBySettleAuditRecordId(long settleAuditRecordId);

    /**
     * 校验结算单号
     *
     * @param settleNo
     * @return
     */
    boolean checkExistBySettleNo(String settleNo);

    /**
     * 获取结算单号
     *
     * @param settleObject  结算单号
     * @param settleDestination  结算目的地
     * @return
     */
    public String getSettleNo(int settleObject, int settleDestination);
}
