package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.UpgradeRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Thinkpad on 2017/1/5.
 */
@Repository
public interface UpgradeRecordDao {
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
    UpgradeRecord selectById(@Param("id") long id);

    /**
     * 查询所有记录
     * @return
     */
    List<UpgradeRecord> selectAll();

}
