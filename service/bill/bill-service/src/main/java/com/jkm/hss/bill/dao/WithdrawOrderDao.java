package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.WithdrawOrder;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2017-07-24.
 */
@Repository
public interface WithdrawOrderDao {

    void insert(WithdrawOrder withdrawOrder);

    void update(WithdrawOrder withdrawOrder);

    WithdrawOrder selectByIdWithlock(long id);

    WithdrawOrder getByOrderNo(String orderNo);

    List<Order> selectWithdrawingOrderByBefore(Date date);
}
