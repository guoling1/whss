package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.SettleAccountFlow;
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
     * 保存结算审核记录
     *
     * @param orderNos
     * @param settleAuditRecordId
     * @return
     */
    int updateSettleAuditRecordIdByOrderNos(@Param("orderNos") List<String> orderNos, @Param("settleAuditRecordId") long settleAuditRecordId);

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
     * 查询上一个工作日的结算流水（未结算）
     *
     * 如果今日是周一查询的是（周五至周日）的；
     *
     * @param tradeDateList
     * @return
     */
    List<SettleAccountFlow> selectMerchantLastWordDayRecord(@Param("tradeDateList") List<Date> tradeDateList);
}
