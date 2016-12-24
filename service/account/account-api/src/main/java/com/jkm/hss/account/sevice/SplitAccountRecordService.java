package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.SplitAccountRecord;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface SplitAccountRecordService {

    /**
     * 插入
     *
     * @param record
     */
    void add(SplitAccountRecord record);

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
    Optional<SplitAccountRecord> getById(long id);
}
