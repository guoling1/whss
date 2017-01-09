package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.UpgradeRecord;

import java.util.List;

/**
 * Created by Thinkpad on 2017/1/5.
 */
public interface UpgradeRecordService {
    /**
     * 初始化
     * @param upgradeRecord
     */
    int insert(UpgradeRecord upgradeRecord);

    /**
     * 根据编码查询
     * @param id
     * @return
     */
    UpgradeRecord selectById(long id);

    /**
     * 查询所有记录
     * @return
     */
    List<UpgradeRecord> selectAll();
}
