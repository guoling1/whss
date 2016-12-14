package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.PastRecordDao;
import com.jkm.hss.merchant.entity.PastRecord;
import com.jkm.hss.merchant.service.PastRecordService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-28 15:46
 */
@Slf4j
@Service
public class PastRecordServiceImpl implements PastRecordService{
    @Autowired
    private PastRecordDao pastRecordDao;
    @Override
    public PastRecord selectByPrimaryKey(Long id) {
        return pastRecordDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(PastRecord record) {
        return pastRecordDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKey(PastRecord record) {
        return pastRecordDao.updateByPrimaryKey(record);
    }

    @Override
    public JSONObject settleMoney(long accountId) {
        double settleMoney = pastRecordDao.selectSettleMoney(accountId);
        double unSettleMoney = pastRecordDao.selectUnSettleMoney(accountId);
        JSONObject jo = new JSONObject();
        jo.put("settleMoney",settleMoney);
        jo.put("unSettleMoney",unSettleMoney);
        return jo;
    }


}
