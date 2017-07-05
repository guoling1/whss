package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.UnFrozenRecord;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/9/23.
 */
public interface UnfrozenRecordService {

    /**
     * 插入解冻记录
     *
     * @param unFrozenRecord
     */
     void add(UnFrozenRecord unFrozenRecord);

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
    Optional<UnFrozenRecord> getById(long id);


    /**
     * 按frozenRecordId查询解冻记录
     *
     * @param frozenRecordId
     * @return
     */
    Optional<UnFrozenRecord> getByFrozenRecordId(long frozenRecordId);

    /**
     * 按businessSn查询解冻记录
     *
     * @param businessNo
     * @return
     */
    Optional<UnFrozenRecord> getByBusinessNo(long businessNo);

    /**
     * 按账户id插叙解冻记录
     *
     * @param accountId
     * @return
     */
    List<UnFrozenRecord> getByAccountId(long accountId);
}
