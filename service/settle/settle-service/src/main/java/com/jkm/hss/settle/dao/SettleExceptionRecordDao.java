package com.jkm.hss.settle.dao;

import com.jkm.hss.settle.entity.SettleExceptionRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by yuxiang on 2017-07-21.
 */
@Repository
public interface SettleExceptionRecordDao {

    void insert(SettleExceptionRecord settleExceptionRecord);

    void update(SettleExceptionRecord settleExceptionRecord);
}
