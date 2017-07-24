package com.jkm.hss.settle.service.impl;

import com.jkm.hss.settle.dao.SettleExceptionRecordDao;
import com.jkm.hss.settle.entity.SettleExceptionRecord;
import com.jkm.hss.settle.service.SettleExceptionRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2017-07-21.
 */
@Service
@Slf4j
public class SettleExceptionRecordServiceImpl implements SettleExceptionRecordService {

    @Autowired
    private SettleExceptionRecordDao settleExceptionRecordDao;

    @Override
    public void insert(SettleExceptionRecord settleExceptionRecord) {

        this.settleExceptionRecordDao.insert(settleExceptionRecord);
    }

    @Override
    public void update(SettleExceptionRecord settleExceptionRecord) {
        this.settleExceptionRecordDao.update(settleExceptionRecord);
    }
}
