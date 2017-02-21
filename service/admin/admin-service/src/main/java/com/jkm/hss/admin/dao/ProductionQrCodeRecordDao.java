package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.ProductionQrCodeRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/2/20.
 */
@Repository
public interface ProductionQrCodeRecordDao {
    /**
     * 插入
     *
     * @param productionQrCodeRecord
     */
    void insert(ProductionQrCodeRecord productionQrCodeRecord);
}
