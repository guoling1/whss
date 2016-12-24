package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.UnfrozenRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/9/23.
 */
@Repository
public interface UnfrozenRecordDao {

    /**
     * 插入解冻记录
     *
     * @param unfrozenRecord
     */
    void insert(UnfrozenRecord unfrozenRecord);

    /**
     * 更新解冻记录
     *
     * @param unfrozenRecord
     * @return
     */
    int update(UnfrozenRecord unfrozenRecord);

    /**
     * 按id查询解冻记录
     *
     * @param id
     * @return
     */
    UnfrozenRecord selectById(@Param("id") long id);


    /**
     * 按frozenRecordId查询解冻记录
     *
     * @param frozenRecordId
     * @return
     */
    UnfrozenRecord selectByFrozenRecordId(@Param("frozenRecordId") long frozenRecordId);

    /**
     * 按businessSn查询解冻记录
     *
     * @param businessNo
     * @return
     */
    UnfrozenRecord selectByBusinessNo(@Param("businessNo") long businessNo);

    /**
     * 按账户id插叙解冻记录
     *
     * @param accountId
     * @return
     */
    List<UnfrozenRecord> selectByAccountId(@Param("accountId") long accountId);
}
