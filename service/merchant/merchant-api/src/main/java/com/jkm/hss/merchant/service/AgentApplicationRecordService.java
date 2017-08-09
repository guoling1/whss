package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.merchant.entity.AgentApplicationRecord;
import com.jkm.hss.merchant.helper.request.AppliationListRequest;
import com.jkm.hss.merchant.helper.response.AgentApplicationRecordResponse;

/**
 * Created by xingliujie on 2017/8/8.
 */
public interface AgentApplicationRecordService {
    void insert(AgentApplicationRecord agentApplicationRecord);
    PageModel<AgentApplicationRecordResponse> agentList(AppliationListRequest appliationListRequest);
    Optional<AgentApplicationRecord> getById(Long id);
    void updateById(Long id);
}
