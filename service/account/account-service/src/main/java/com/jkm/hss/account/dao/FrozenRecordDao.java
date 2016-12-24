package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.FrozenRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/9/23.
 */
@Repository
public interface FrozenRecordDao {

    /**
     * 插入冻结记录
     *
     * @param frozenRecord
     */
    void insert(FrozenRecord frozenRecord);

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
    FrozenRecord selectById(@Param("id") long id);

    /**
     * 按ids查询冻结记录
     *
     * @param ids
     * @return
     */
    List<FrozenRecord> selectByIds(@Param("ids") List<Long> ids);

    /**
     * 按accountId查询冻结记录
     *
     * @param accountId
     * @return
     */
    List<FrozenRecord> selectByAccountId(@Param("accountId") long accountId);

    /**
     * 按businessSn查询冻结记录
     *
     * @param businessNo
     * @return
     */
    FrozenRecord selectByBusinessNo(@Param("businessNo") String businessNo);

    /**
     * 更新状态
     * @param businessNo
     */
    void updateStatusByBusinessNo(@Param("businessNo") String businessNo, @Param("status") int status);
}
