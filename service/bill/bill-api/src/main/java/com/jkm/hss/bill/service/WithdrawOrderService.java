package com.jkm.hss.bill.service;

import com.alibaba.fastjson.JSONObject;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.WithdrawOrder;
import com.jkm.hsy.user.entity.AppBizCard;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2017-07-24.
 */
public interface WithdrawOrderService {

    void insert(WithdrawOrder withdrawOrder);

    void update(WithdrawOrder withdrawOrder);

    /**
     * D0提现
     * @param account
     * @return
     */
    JSONObject d0WithDrawImpl(Account account, long userId, String merchantNo, AppBizCard appBizCard);

    /**
     * 确认提现
     * @param withDrawOrderId
     * @return
     */
    Pair<Integer, String> confirmWithdraw(long withDrawOrderId);

    WithdrawOrder selectByIdWithlock(long id);

    WithdrawOrder getByOrderNo(String orderNo);

    List<Order> selectWithdrawingOrderByBefore(Date date);
}
