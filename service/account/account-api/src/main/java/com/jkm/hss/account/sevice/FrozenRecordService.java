package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.enums.EnumFrozenStatus;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/9/23.
 */
public interface FrozenRecordService {

    /**
     * 插入冻结记录
     *
     * @param frozenRecord
     */
    void add(FrozenRecord frozenRecord);

    /**
     * 更新冻结记录
     *
     * @param frozenRecord
     * @return
     */
    int update(FrozenRecord frozenRecord);

    /**
     * 按id查询冻结记录
     *
     * @param id
     * @return
     */
    Optional<FrozenRecord> getById(long id);

    /**
     * 按ids查询冻结记录
     *
     * @param ids
     * @return
     */
    List<FrozenRecord> getByIds(List<Long> ids);

    /**
     * 按accountId查询冻结记录
     *
     * @param accountId
     * @return
     */
    List<FrozenRecord> getByAccountId(long accountId);

    /**
     * 按businessSn查询冻结记录
     *
     * @param businessNo
     * @return
     */
    Optional<FrozenRecord> getByBusinessNo(String businessNo);

    /**
     * 更新冻结记录状态
     * @param businessNo
     */
    void updateStatusByBusinessNo(String businessNo, EnumFrozenStatus status);
}
