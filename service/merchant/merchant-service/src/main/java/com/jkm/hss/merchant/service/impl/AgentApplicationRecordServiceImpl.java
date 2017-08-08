package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.AgentApplicationRecordDao;
import com.jkm.hss.merchant.entity.AgentApplicationRecord;
import com.jkm.hss.merchant.service.AgentApplicationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xingliujie on 2017/8/8.
 */
@Slf4j
@Service
public class AgentApplicationRecordServiceImpl implements AgentApplicationRecordService {
    @Autowired
    private AgentApplicationRecordDao agentApplicationRecordDao;
    @Override
    public void insert(AgentApplicationRecord agentApplicationRecord) {
        agentApplicationRecordDao.insert(agentApplicationRecord);
    }
}
