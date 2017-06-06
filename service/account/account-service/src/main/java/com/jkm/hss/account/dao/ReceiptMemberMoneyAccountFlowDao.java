package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.ReceiptMemberMoneyAccountFlow;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
@Repository
public interface ReceiptMemberMoneyAccountFlowDao {
    /**
     * 插入
     *
     * @param receiptMemberMoneyAccountFlow
     */
    void insert(ReceiptMemberMoneyAccountFlow receiptMemberMoneyAccountFlow);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    ReceiptMemberMoneyAccountFlow selectById(@Param("id") long id);

    /**
     * 按流水号查询
     *
     * @param flowNo
     * @return
     */
    int selectCountByFlowNo(@Param("flowNo") String flowNo);
}
