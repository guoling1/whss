package com.jkm.hss.settle.service;

import com.jkm.hss.settle.entity.SettleExceptionRecord;

/**
 * Created by yuxiang on 2017-07-21.
 */
public interface SettleExceptionRecordService {

    void insert(SettleExceptionRecord settleExceptionRecord);

    void update(SettleExceptionRecord settleExceptionRecord);
}
