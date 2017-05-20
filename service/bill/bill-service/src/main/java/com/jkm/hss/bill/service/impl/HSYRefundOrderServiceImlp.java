package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.dao.HsyRefundOrderDao;
import com.jkm.hss.bill.entity.HsyRefundOrder;
import com.jkm.hss.bill.service.HSYRefundOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wayne on 17/5/20.
 */
@Slf4j
@Service("HsyRefundOrderService")
public class HSYRefundOrderServiceImlp implements HSYRefundOrderService {

    @Autowired
    private HsyRefundOrderDao hsyRefundOrderDao;

    @Override
    public void insert(HsyRefundOrder hsyRefundOrder) {
        hsyRefundOrderDao.insert(hsyRefundOrder);
    }

    @Override
    public int update(HsyRefundOrder hsyRefundOrder) {
        return hsyRefundOrderDao.update(hsyRefundOrder);
    }
}
