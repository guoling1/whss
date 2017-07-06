package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.UpgradeRecordDao;
import com.jkm.hss.merchant.entity.UpgradeRecord;
import com.jkm.hss.merchant.service.UpgradeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Thinkpad on 2017/1/5.
 */
@Slf4j
@Service
public class UpgradeRecordServiceImpl implements UpgradeRecordService{
    @Autowired
    private UpgradeRecordDao upgradeRecordDao;
    /**
     * 初始化
     *
     * @param upgradeRecord
     */
    @Override
    public int insert(UpgradeRecord upgradeRecord) {
        return upgradeRecordDao.insert(upgradeRecord);
    }

    /**
     * 根据编码查询
     *
     * @param id
     * @return
     */
    @Override
    public UpgradeRecord selectById(long id) {
        return upgradeRecordDao.selectById(id);
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<UpgradeRecord> selectAll() {
        return upgradeRecordDao.selectAll();
    }
}
