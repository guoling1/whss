package com.jkm.hss.settle.dao;

import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import com.jkm.hss.settle.helper.requestparam.ListSettleAuditRecordRequest;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/12.
 */
@Repository
public interface AccountSettleAuditRecordDao {

    /**
     * 插入
     *
     * @param record
     */
    void insert(AccountSettleAuditRecord record);

    /**
     * 更新对账状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateAccountCheckStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 更新结算状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateSettleStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 按id 查询
     *
     * @param id
     * @return
     */
    AccountSettleAuditRecord selectById(@Param("id") long id);

    /**
     * 加锁按id 查询
     *
     * @param id
     * @return
     */
    AccountSettleAuditRecord selectByIdWithLock(@Param("id") long id);

    /**
     * 按ids查询
     *
     * @param recordIds
     * @return
     */
    List<AccountSettleAuditRecord> selectByIds(@Param("recordIds") List<Long> recordIds);

    /**
     * 按accountId查询
     *
     * @param accountId
     * @return
     */
    long selectCountByAccountId(@Param("accountId") long accountId);

    /**
     * 按accountId查询
     *
     * @param accountId
     * @return
     */
    List<AccountSettleAuditRecord> selectByAccountId(@Param("accountId") long accountId, @Param("offset") int offset, @Param("count") int count);

    /**
     * 记录个数
     *
     * @param settleAuditRecordRequest
     * @return
     */
    long selectCountByParam(ListSettleAuditRecordRequest settleAuditRecordRequest);

    /**
     * 记录列表
     *
     * @param settleAuditRecordRequest
     * @return
     */
    List<AccountSettleAuditRecord> selectByParam(ListSettleAuditRecordRequest settleAuditRecordRequest);

    /**
     * 查询指定结算日期，指定结算状态的结算审核记录ids
     *
     * @param settleDate
     * @param settleStatus
     * @return
     */
    List<Long> selectPendingSettleAuditRecordIdsBySettleDateAndSettleStatus(@Param("settleDate") Date settleDate, @Param("settleStatus") int settleStatus);
}
