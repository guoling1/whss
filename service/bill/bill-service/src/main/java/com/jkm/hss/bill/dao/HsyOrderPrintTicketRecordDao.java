package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.HsyOrderPrintTicketRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by yulong.zhang on 2017/7/27.
 */
@Repository
public interface HsyOrderPrintTicketRecordDao {

    /**
     * 插入
     *
     * @param record
     */
    void insert(HsyOrderPrintTicketRecord record);
}
