package com.jkm.hss.admin.service.impl;

import com.jkm.hss.admin.dao.RevokeQrCodeRecordDao;
import com.jkm.hss.admin.entity.RevokeQrCodeRecord;
import com.jkm.hss.admin.service.RevokeQrCodeRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xingliujie on 2017/4/26.
 */
@Slf4j
@Service
public class RevokeQrCodeRecordServiceImpl implements RevokeQrCodeRecordService {
    @Autowired
    private RevokeQrCodeRecordDao revokeQrCodeRecordDao;
    /**
     * 新增记录
     *
     * @param revokeQrCodeRecord
     */
    @Override
    public void add(RevokeQrCodeRecord revokeQrCodeRecord) {
        revokeQrCodeRecordDao.add(revokeQrCodeRecord);
    }
}
