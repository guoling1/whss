package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.UnfrozenRecord;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/9/23.
 */
public interface UnfrozenRecordService {

    /**
     * 插入解冻记录
     *
     * @param unfrozenRecord
     */
     void add(UnfrozenRecord unfrozenRecord);

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
    Optional<UnfrozenRecord> getById(long id);


    /**
     * 按frozenRecordId查询解冻记录
     *
     * @param frozenRecordId
     * @return
     */
    Optional<UnfrozenRecord> getByFrozenRecordId(long frozenRecordId);

    /**
     * 按businessSn查询解冻记录
     *
     * @param businessNo
     * @return
     */
    Optional<UnfrozenRecord> getByBusinessNo(long businessNo);

    /**
     * 按账户id插叙解冻记录
     *
     * @param accountId
     * @return
     */
    List<UnfrozenRecord> getByAccountId(long accountId);
}
