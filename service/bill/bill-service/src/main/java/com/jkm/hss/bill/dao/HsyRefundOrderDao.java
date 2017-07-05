package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.HsyRefundOrder;
import org.springframework.stereotype.Repository;

/**
 * Created by shitou on 17/5/20.
 */
@Repository
public interface HsyRefundOrderDao {
    void insert(HsyRefundOrder hsyRefundOrder);
    int update(HsyRefundOrder hsyRefundOrder);
}
