package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.dao.HsyOrderPrintTicketRecordDao;
import com.jkm.hss.bill.entity.HsyOrderPrintTicketRecord;
import com.jkm.hss.bill.service.HsyOrderPrintTicketRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2017/7/27.
 */
@Service
public class HsyOrderPrintTicketRecordServiceImpl implements HsyOrderPrintTicketRecordService {

    @Autowired
    private HsyOrderPrintTicketRecordDao hsyOrderPrintTicketRecordDao;

    /**
     * {@inheritDoc}
     *
     * @param record
     */
    @Override
    public void add(final HsyOrderPrintTicketRecord record) {
        this.hsyOrderPrintTicketRecordDao.insert(record);
    }
}
