package com.jkm.hss.account.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.account.dao.SettleAccountFlowDao;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class SettleAccountFlowServiceImpl implements SettleAccountFlowService {

    private SettleAccountFlowDao settleAccountFlowDao;

    /**
     * {@inheritDoc}
     *
     * @param accountFlow
     */
    @Override
    public void add(final SettleAccountFlow accountFlow) {
        this.settleAccountFlowDao.insert(accountFlow);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<SettleAccountFlow> getById(final long id) {
        return Optional.fromNullable(this.settleAccountFlowDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    public Optional<SettleAccountFlow> getByOrderNo(final String orderNo) {
        return Optional.fromNullable(this.settleAccountFlowDao.selectByOrderNo(orderNo));
    }
}
