package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AgentApplicationRecord;
import com.jkm.hss.merchant.helper.request.AppliationListRequest;
import com.jkm.hss.merchant.helper.response.AgentApplicationRecordResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/8/8.
 */
@Repository
public interface AgentApplicationRecordDao {
    void insert(AgentApplicationRecord agentApplicationRecord);
    Long getTotal(AppliationListRequest appliationListRequest);
    List<AgentApplicationRecordResponse> getList(AppliationListRequest appliationListRequest);
    AgentApplicationRecord getById(@Param("id") Long id);
    void updateById(@Param("id") Long id);
}
