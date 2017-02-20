package com.jkm.hss.admin.service.impl;

import com.jkm.hss.admin.dao.ProductionQrCodeRecordDao;
import com.jkm.hss.admin.entity.ProductionQrCodeRecord;
import com.jkm.hss.admin.service.ProductionQrCodeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xingliujie on 2017/2/20.
 */
@Slf4j
@Service
public class ProductionQrCodeRecordServiceImpl implements ProductionQrCodeRecordService {
    @Autowired
    private ProductionQrCodeRecordDao productionQrCodeRecordDao;
    /**
     * 插入
     *
     * @param productionQrCodeRecord
     */
    @Override
    public void add(ProductionQrCodeRecord productionQrCodeRecord) {
        productionQrCodeRecordDao.insert(productionQrCodeRecord);
    }
}
