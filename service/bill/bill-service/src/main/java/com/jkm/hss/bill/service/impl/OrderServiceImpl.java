package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.bill.dao.OrderDao;
import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumSettleStatus;
import com.jkm.hss.bill.enums.EnumTradeType;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    private AccountService accountService;
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
        final Account account = this.accountService.getByIdWithLock(merchant.getAccountId()).get();
        Preconditions.checkState(payOrder.getTradeAmount().subtract(payOrder.getPoundage()).compareTo(account.getAvailable()) <= 0);
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

    private List<String>  PayOf(String status) {
        List<String> payResults = new ArrayList<String>();
        if("N".equals(status)){
            payResults.add("N");
        }
        if("H".equals(status)){
            payResults.add("H");
            payResults.add("W");
            payResults.add("A");
            payResults.add("E");
        }
        if("S".equals(status)){
            payResults.add("S");
        }
        if("F".equals(status)){
            payResults.add("F");
        }
        return payResults;
    }

    private String  PayOfStatus(String status) {
        String message = "";
        if("N".equals(status)){
            message="待支付";
        }
        if("H".equals(status)||"W".equals(status)||"A".equals(status)||"E".equals(status)){
            message="支付中";
        }
        if("S".equals(status)){
            message="支付成功";
        }
        if("F".equals(status)){
            message="支付失败";
        }
        return message;
    }

    @Override
    public List<MerchantTradeResponse> selectOrderListByPage(OrderTradeRequest req) {
//        List<String> payResults = PayOf(req.getPayResult());
//        req.setPayResults(payResults);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("orderNo",req.getOrderNo());
        map.put("startTime",req.getStartTime());
        map.put("endTime",req.getEndTime());
        map.put("merchantId",req.getMerchantId());
        map.put("merchantName",req.getMerchantName());
        map.put("orderNo",req.getOrderNo());
        map.put("payType",req.getPayType());
        map.put("settleStatus",req.getSettleStatus());
        map.put("status",req.getStatus());
        map.put("tradeAmount",req.getTradeAmount());
//        map.put("settleStatus",req.getSettleStatus());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        List<MerchantTradeResponse> list = orderDao.selectOrderList(map);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                long payee = list.get(i).getPayee();
                long payer = list.get(i).getPayer();
                List<MerchantTradeResponse> lists = orderDao.getMerchant(payee,payer);
                if (lists.size()>0){
                    for(int j=0;j<lists.size();j++){
                        list.get(i).setMerchantName(lists.get(j).getMerchantName());
                        List<MerchantTradeResponse> result = orderDao.getDealer(lists.get(j).getDealerId());
                        if (result.size()>0){
                            for (int x=0;result.size()>x;x++){
                                if (result.get(x).getLevel()==1){
                                    list.get(i).setProxyName(result.get(x).getProxyName());
                                }
                                if (result.get(x).getLevel()==1){
                                    List<MerchantTradeResponse> res = orderDao.getProxyName(result.get(x).getFirstLevelDealerId());
                                    if (res.size()>0){
                                        for (int m=0;res.size()>m;m++){
                                            list.get(i).setProxyName1(res.get(m).getProxyName());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        }
        return list;
    }

    @Override
    public int selectOrderListCount(OrderTradeRequest req) {
//        List<String> payResults = PayOf(req.getPayResult());
//        req.setPayResults(payResults);
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("orderNo",req.getOrderNo());
        map.put("startTime",req.getStartTime());
        map.put("endTime",req.getEndTime());
        map.put("merchantId",req.getMerchantId());
        map.put("merchantName",req.getMerchantName());
        map.put("orderNo",req.getOrderNo());
        map.put("payType",req.getPayType());
        map.put("settleStatus",req.getSettleStatus());
        map.put("status",req.getStatus());
        map.put("tradeAmount",req.getTradeAmount());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        return orderDao.selectOrderListCount(map);
    }



//    @Override
//    public MerchantTradeResponse selectOrderListByPageAll(OrderListRequest req) {
//        List<String> payResults = PayOf(req.getPayResult());
//        req.setPayResults(payResults);
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("id",req.getId());
//        MerchantTradeResponse merchantTradeResponse = orderDao.selectOrderListCountAll(map);
//        if(merchantTradeResponse!=null){
//            merchantTradeResponse.setOrderMessage(PayOfStatus(merchantTradeResponse.getPayResult()));
//        }
//        return merchantTradeResponse;
//    }

}
