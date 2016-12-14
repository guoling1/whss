package com.jkm.hss.merchant.service.impl;



import com.jkm.hss.merchant.dao.PayExceptionRecordDao;
import com.jkm.hss.merchant.entity.PayExceptionRecord;
import com.jkm.hss.merchant.service.PayExceptionRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PayExceptionRecordServiceImpl implements PayExceptionRecordService {
    @Autowired
    private PayExceptionRecordDao payExceptionRecordDao;
    @Override
    public PayExceptionRecord selectByPrimaryKey(Long id) {
        return payExceptionRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(PayExceptionRecord record) {
        return payExceptionRecordDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(PayExceptionRecord record) {
        return payExceptionRecordDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insertByCondition(String orderId, String reqSn, String type) {
        PayExceptionRecord payExceptionRecord = new PayExceptionRecord();
        payExceptionRecord.setStatus(0);
        payExceptionRecord.setPayChannel("101");
        payExceptionRecord.setOrderId(orderId);
        payExceptionRecord.setReqSn(reqSn);
        payExceptionRecord.setType(type);
        return payExceptionRecordDao.insertSelective(payExceptionRecord);
    }
}
