package com.jkm.hss.dealer.service.impl;

import com.jkm.hss.dealer.dao.ShallProfitExceptionRecordDao;
import com.jkm.hss.dealer.entity.ShallProfitExceptionRecord;
import com.jkm.hss.dealer.service.ShallProfitExceptionRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yuxiang on 2016-12-21.
 */
@Service
public class ShallProfitExceptionRecordServiceImpl implements ShallProfitExceptionRecordService {

    @Autowired
    private ShallProfitExceptionRecordDao shallProfitExceptionRecordDao;


    /**
     * {@inheritDoc}
     * @param record
     */
    @Override
    public void add(ShallProfitExceptionRecord record) {

        this.shallProfitExceptionRecordDao.add(record);
    }
}
