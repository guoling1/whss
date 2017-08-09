package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.merchant.dao.AgentApplicationRecordDao;
import com.jkm.hss.merchant.entity.AgentApplicationRecord;
import com.jkm.hss.merchant.helper.request.AppliationListRequest;
import com.jkm.hss.merchant.helper.response.AgentApplicationRecordResponse;
import com.jkm.hss.merchant.service.AgentApplicationRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public PageModel<AgentApplicationRecordResponse> agentList(AppliationListRequest appliationListRequest) {
        PageModel<AgentApplicationRecordResponse> pageModel = new PageModel<>(appliationListRequest.getPageNo(),appliationListRequest.getPageSize());
        appliationListRequest.setOffset(pageModel.getFirstIndex());
        appliationListRequest.setCount(pageModel.getPageSize());
        Long count = agentApplicationRecordDao.getTotal(appliationListRequest);
        List<AgentApplicationRecordResponse> agentApplicationRecords =  agentApplicationRecordDao.getList(appliationListRequest);
        pageModel.setCount(count);
        pageModel.setRecords(agentApplicationRecords);
        return pageModel;
    }

    @Override
    public Optional<AgentApplicationRecord> getById(Long id) {
        return Optional.fromNullable(agentApplicationRecordDao.getById(id));
    }

    @Override
    public void updateById(Long id) {
        agentApplicationRecordDao.updateById(id);
    }
}
