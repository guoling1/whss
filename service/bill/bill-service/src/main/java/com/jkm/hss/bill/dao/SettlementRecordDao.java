package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.helper.requestparam.QuerySettlementRecordParams;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/2/22.
 */
@Repository
public interface SettlementRecordDao {

    /**
     * 插入
     *
     * @param settlementRecord
     */
    void insert(SettlementRecord settlementRecord);

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
     * @param id
     * @param status
     * @return
     */
    int updateStatus(@Param("id") long id, @Param("status") int status);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    SettlementRecord selectById(@Param("id") long id);

    /**
     * 按id查询加锁
     *
     * @param id
     * @return
     */
    SettlementRecord selectByIdWithLock(@Param("id") long id);

    /**
     * 按结算流水号查询
     *
     * @param settleNo
     * @return
     */
    SettlementRecord selectBySettleNo(@Param("settleNo") String settleNo);

    /**
     * 更新结算状态
     *
     * @param id
     * @param settleStatus
     * @return
     */
    int updateSettleStatus(@Param("id") long id, @Param("settleStatus") int settleStatus);

    /**
     * 按结算审核记录id查找结算单
     *
     * @param settleAuditRecordId
     * @return
     */
    SettlementRecord selectBySettleAuditRecordId(@Param("settleAuditRecordId") long settleAuditRecordId);

    /**
     * 按结算单号查询个数
     *
     * @param settleNo
     * @return
     */
    int selectCountBySettleNo(@Param("settleNo") String settleNo);

    /**
     * 列表-查询个数
     *
     * @param querySettlementRecordParams
     * @return
     */
    int selectCountByParam(QuerySettlementRecordParams querySettlementRecordParams);

    /**
     * 列表-查询记录
     *
     * @param querySettlementRecordParams
     * @return
     */
    List<SettlementRecord> selectByParam(QuerySettlementRecordParams querySettlementRecordParams);
}
