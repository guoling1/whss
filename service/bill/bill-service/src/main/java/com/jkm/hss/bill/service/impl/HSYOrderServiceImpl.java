package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.bill.dao.HsyOrderDao;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.service.HSYOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wayne on 17/5/17.
 */
@Slf4j
@Service("HsyOrderService")
public class HSYOrderServiceImpl implements HSYOrderService {

    @Autowired
    private HsyOrderDao hsyOrderDao;

    @Override
    @Transactional
    public void insert(HsyOrder hsyOrder) {
        hsyOrderDao.insert(hsyOrder);
    }

    @Override
    public int update(HsyOrder hsyOrder) {
        return hsyOrderDao.update(hsyOrder);
    }

    @Override
    public Optional<HsyOrder> selectByOrderNo(String orderNo) {
        return Optional.fromNullable(hsyOrderDao.selectByOrderNo(orderNo));
    }
}
