package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.RevokeQrCodeRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/4/26.
 */
@Repository
public interface RevokeQrCodeRecordDao {
    /**
     * 新增记录
     * @param revokeQrCodeRecord
     */
    void add(RevokeQrCodeRecord revokeQrCodeRecord);
}
