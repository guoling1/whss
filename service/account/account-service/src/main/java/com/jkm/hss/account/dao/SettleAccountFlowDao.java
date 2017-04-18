package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.helper.selectresponse.SettleAccountFlowStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface SettleAccountFlowDao {

    /**
     * 插入
     *
     * @param settleAccountFlow
     */
    void insert(SettleAccountFlow settleAccountFlow);

    /**
     * 保存结算单id
     *
     * @param id
     * @return
     */
    int updateSettlementRecordIdById(@Param("id") long id, @Param("settlementRecordId") long settlementRecordId);

    /**
     * 保存结算单id
     *
     * @param settleAuditRecordId
     * @param settlementRecordId
     * @return
     */
    int updateSettlementRecordIdBySettleAuditRecordId(@Param("settleAuditRecordId") long settleAuditRecordId, @Param("settlementRecordId") long settlementRecordId);

    /**
     * 保存结算审核记录
     *
     * @param settleDate
     * @param accountId
     * @param settleAuditRecordId
     * @return
     */
    int updateSettleAuditRecordIdBySettleDateAndAccountId(@Param("settleDate") Date settleDate, @Param("accountId") long accountId, @Param("settleAuditRecordId") long settleAuditRecordId);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    SettleAccountFlow selectById(@Param("id") long id);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @return
     */
    SettleAccountFlow selectByOrderNoAndAccountIdAndType(@Param("orderNo") String orderNo, @Param("accountId") long accountId, @Param("type") int type);

    /**
     * 统计今日待结算的结算流水
     *
     * @param settleDate
     * @return
     */
    List<SettleAccountFlowStatistics> statisticsYesterdayFlow(@Param("settleDate") Date settleDate);

    /**
     * 按审核记录id查询
     *
     * @param recordId
     * @return
     */
    List<SettleAccountFlow> selectByAuditRecordId(@Param("recordId") long recordId);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @return
     */
    List<SettleAccountFlow> selectByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 按结算单id查询
     *
     * @param settlementRecordId
     * @return
     */
    List<SettleAccountFlow> selectBySettlementRecordId(@Param("settlementRecordId") long settlementRecordId);

    /**
     * 查询昨日出账流水个数
     *
     * @param settleDate
     * @return
     */
    int selectYesterdayDecreaseFlowCount(@Param("settleDate") Date settleDate);

    /**
     * 按流水号查询个数
     *
     * @param flowNo
     * @return
     */
    int selectCountByFlowNo(@Param("flowNo") String flowNo);
}
