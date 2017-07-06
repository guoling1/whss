package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.dao.MemberAccountFlowDao;
import com.jkm.hss.account.entity.MemberAccountFlow;
import com.jkm.hss.account.sevice.MemberAccountFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by yulong.zhang on 2017/5/10.
 */
@Slf4j
@Service
public class MemberAccountFlowServiceImpl implements MemberAccountFlowService {

    @Autowired
    private MemberAccountFlowDao memberAccountFlowDao;

    /**
     * {@inheritDoc}
     *
     * @param memberAccountFlow
     */
    @Override
    @Transactional
    public void add(final MemberAccountFlow memberAccountFlow) {
        this.memberAccountFlowDao.insert(memberAccountFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<MemberAccountFlow> selectById(final long id) {
        return Optional.fromNullable(this.memberAccountFlowDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String getFlowNo() {
        final String flowNo = SnGenerator.generateFlowSn();
        if (this.memberAccountFlowDao.selectCountByFlowNo(flowNo) == 0) {
            return flowNo;
        }
        return this.getFlowNo();
    }
}
