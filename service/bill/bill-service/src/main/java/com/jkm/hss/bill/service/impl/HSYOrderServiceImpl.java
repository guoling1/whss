package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.service.HSYOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by wayne on 17/5/17.
 */
@Slf4j
@Service("HsyOrderService")
public class HSYOrderServiceImpl implements HSYOrderService {
    @Override
    @Transactional
    public void insert(HsyOrder hsyOrder) {

    }

    @Override
    public int update(HsyOrder hsyOrder) {
        return 0;
    }
}
