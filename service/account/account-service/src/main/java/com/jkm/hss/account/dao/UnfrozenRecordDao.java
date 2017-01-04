package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.UnFrozenRecord;
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
     * @param unFrozenRecord
     */
    void insert(UnFrozenRecord unFrozenRecord);

    /**
     * 更新解冻记录
     *
     * @param unFrozenRecord
     * @return
     */
    int update(UnFrozenRecord unFrozenRecord);

    /**
     * 按id查询解冻记录
     *
     * @param id
     * @return
     */
    UnFrozenRecord selectById(@Param("id") long id);


    /**
     * 按frozenRecordId查询解冻记录
     *
     * @param frozenRecordId
     * @return
     */
    UnFrozenRecord selectByFrozenRecordId(@Param("frozenRecordId") long frozenRecordId);

    /**
     * 按businessSn查询解冻记录
     *
     * @param businessNo
     * @return
     */
    UnFrozenRecord selectByBusinessNo(@Param("businessNo") long businessNo);

    /**
     * 按账户id插叙解冻记录
     *
     * @param accountId
     * @return
     */
    List<UnFrozenRecord> selectByAccountId(@Param("accountId") long accountId);
}
