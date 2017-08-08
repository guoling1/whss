package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.AgentApplicationRecord;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/8/8.
 */
@Repository
public interface AgentApplicationRecordDao {
    void insert(AgentApplicationRecord agentApplicationRecord);


}
