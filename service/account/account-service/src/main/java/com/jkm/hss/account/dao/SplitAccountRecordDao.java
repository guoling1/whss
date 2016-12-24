package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.SplitAccountRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface SplitAccountRecordDao {

    /**
     * 插入
     *
     * @param record
     */
    void insert(SplitAccountRecord record);

    /**
     * 更新
     *
     * @param record
     * @return
     */
    int update(SplitAccountRecord record);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    SplitAccountRecord selectById(@Param("id") long id);
}
