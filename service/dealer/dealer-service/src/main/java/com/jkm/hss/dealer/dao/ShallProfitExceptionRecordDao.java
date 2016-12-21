package com.jkm.hss.dealer.dao;

import com.jkm.hss.dealer.entity.ShallProfitExceptionRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by yuxiang on 2016-12-21.
 */
@Repository
public interface ShallProfitExceptionRecordDao {

    /**
     * 添加
     */
    void add(ShallProfitExceptionRecord record);
}
