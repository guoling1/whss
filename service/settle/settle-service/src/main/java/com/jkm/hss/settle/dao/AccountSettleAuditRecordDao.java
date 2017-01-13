package com.jkm.hss.settle.dao;

import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
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
}
