package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.TradeFeeRecordDao;
import com.jkm.hss.merchant.entity.TradeFeeRecord;
import com.jkm.hss.merchant.service.TradeFeeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-28 15:54
 */
@Slf4j
@Service
public class TradeFeeRecordServiceImpl implements TradeFeeRecordService {
    @Autowired
    private TradeFeeRecordDao tradeFeeRecordDao;
    @Override
    public TradeFeeRecord selectByPrimaryKey(Long id) {
        return tradeFeeRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(TradeFeeRecord record) {
        return tradeFeeRecordDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKey(TradeFeeRecord record) {
        return tradeFeeRecordDao.updateByPrimaryKey(record);
    }
}
