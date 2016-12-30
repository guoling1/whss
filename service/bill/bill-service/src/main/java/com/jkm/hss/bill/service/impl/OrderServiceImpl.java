package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.bill.dao.OrderDao;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumSettleStatus;
import com.jkm.hss.bill.enums.EnumTradeType;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private CalculateService calculateService;

    @Autowired
    private MerchantInfoService merchantInfoService;
    /**
     * {@inheritDoc}
     *
     * @param order
     */
    @Override
    @Transactional
    public void add(final Order order) {
        this.orderDao.insert(order);
    }

    /**
     * {@inheritDoc}
     *
     * @param payOrderId
     * @param merchantId
     * @param tradePeriod
     */
    @Override
    @Transactional
    public long createPlayMoneyOrderByPayOrder(final long payOrderId, final long merchantId, final String tradePeriod) {
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        // 查询支付单对应的提现单是否存在
        final Optional<Order> playMoneyOrderOptional = this.getByPayOrderId(payOrderId);
        Preconditions.checkState(!playMoneyOrderOptional.isPresent(), "支付单[{}]，对应的打款单已经存在，不可以重复生成",
                payOrderId);
        final Order payOrder = this.getByIdWithLock(payOrderId).get();
        Preconditions.checkState(payOrder.isPaySuccess());
        final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(payOrderId);
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(payOrder.getTradeAmount().subtract(payOrder.getPoundage()));
        playMoneyOrder.setRealPayAmount(playMoneyOrder.getTradeAmount());
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(payOrder.getPayChannelSign());
        playMoneyOrder.setPayer(merchant.getAccountId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setPayAccount(tradePeriod);
        BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(merchantId, payOrder.getPayChannelSign());
        playMoneyOrder.setPoundage(merchantWithdrawPoundage);
        playMoneyOrder.setGoodsName(merchant.getMerchantName());
        playMoneyOrder.setGoodsDescribe(merchant.getMerchantName());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(new Date());
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        return playMoneyOrder.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param order
     * @return
     */
    @Override
    public int update(final Order order) {
        return this.orderDao.update(order);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param remark
     * @return
     */
    @Override
    public int updateRemark(final long id, final String remark) {
        return this.orderDao.updateRemark(id, remark);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    public int updateStatus(final long id, final int status, final String remark) {
        return this.orderDao.updateStatus(id, status, remark);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    @Transactional
    public int updateSettleStatus(final long id, final int status) {
        return this.orderDao.updateSettleStatus(id, status);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @param status
     * @param successSettleTime
     * @return
     */
    @Override
    @Transactional
    public int markSettleSuccess(final long id, final int status, final Date successSettleTime) {
        return this.orderDao.markSettleSuccess(id, status, successSettleTime);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Order> getById(final long id) {
        return Optional.fromNullable(this.orderDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Order> getByIdWithLock(final long id) {
        return Optional.fromNullable(this.orderDao.selectByIdWithLock(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @param tradeType
     * @return
     */
    @Override
    public Optional<Order> getByOrderNoAndTradeType(final String orderNo, int tradeType) {
        return Optional.fromNullable(this.orderDao.selectByOrderNoAndTradeType(orderNo, tradeType));
    }

    /**
     * {@inheritDoc}
     *
     * @param payOrderId
     * @return
     */
    @Override
    public Optional<Order> getByPayOrderId(final long payOrderId) {
        return Optional.fromNullable(this.orderDao.selectByPayOrderId(payOrderId));
    }
}
