package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.ExcelSheetVO;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.DateTimeUtil;
import com.jkm.base.common.util.ExcelUtil;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.FrozenRecordService;
import com.jkm.hss.account.sevice.SettleAccountFlowService;
import com.jkm.hss.bill.dao.OrderDao;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.requestparam.QueryMerchantPayOrdersRequestParam;
import com.jkm.hss.bill.service.*;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.mq.config.MqConfig;
import com.jkm.hss.mq.producer.MqProducer;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hsy.user.entity.AppBizShop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    private FrozenRecordService frozenRecordService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Autowired
    private WithdrawService withdrawService;
    @Autowired
    private PayService payService;

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
     * @param settleType
     */
    @Override
    @Transactional
    public long createPlayMoneyOrderByPayOrder(final long payOrderId, final long merchantId, final String settleType) {
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
        playMoneyOrder.setAppId(payOrder.getAppId());
        final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSS, merchantId, payOrder.getPayChannelSign());
        playMoneyOrder.setPoundage(merchantWithdrawPoundage);
        playMoneyOrder.setGoodsName(merchant.getMerchantName());
        playMoneyOrder.setGoodsDescribe(merchant.getMerchantName());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(new Date());
        playMoneyOrder.setSettleType(settleType);
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        return playMoneyOrder.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param shop
     * @param amount
     * @param appId
     * @param channel
     * @param settleType
     * @return
     */
    @Override
    @Transactional
    public long createPlayMoneyOrder(final AppBizShop shop, final BigDecimal amount,
                                     final String appId, final int channel, final String settleType) {
        final Account account = this.accountService.getByIdWithLock(shop.getAccountID()).get();
        Preconditions.checkState(account.getAvailable().compareTo(amount) >= 0, "余额不足");
        final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(0);
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(amount);
        playMoneyOrder.setRealPayAmount(amount);
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(channel);
        playMoneyOrder.setPayer(account.getId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setAppId(appId);
        final BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(EnumProductType.HSY, shop.getId(), channel);
        Preconditions.checkState(amount.compareTo(merchantWithdrawPoundage) > 0, "提现金额必须大于提现手续费");
        playMoneyOrder.setPoundage(merchantWithdrawPoundage);
        playMoneyOrder.setGoodsName(shop.getName());
        playMoneyOrder.setGoodsDescribe(shop.getName());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(DateTimeUtil.generateT1SettleDate(new Date()));
        playMoneyOrder.setSettleType(settleType);
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        this.accountService.decreaseAvailableAmount(account.getId(), amount);
        this.accountService.increaseFrozenAmount(account.getId(), amount);
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setFrozenAmount(amount);
        frozenRecord.setBusinessNo(playMoneyOrder.getOrderNo());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("手动提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水--减少
        this.accountFlowService.addAccountFlow(account.getId(), playMoneyOrder.getOrderNo(), amount,
                "手动提现", EnumAccountFlowType.DECREASE);
        return playMoneyOrder.getId();
    }

    /**
     * {@inheritDoc}
     * @param dealer
     * @param amount
     * @param appId
     * @param channel
     * @param settleType
     * @return
     */
    @Override
    public long createDealerPlayMoneyOrder(Dealer dealer, BigDecimal amount, String appId, int channel, String settleType, BigDecimal withdrawFee) {
        final Account account = this.accountService.getByIdWithLock(dealer.getAccountId()).get();
        Preconditions.checkState(account.getAvailable().compareTo(amount) >= 0, "余额不足");
        final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(0);
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(amount);
        playMoneyOrder.setRealPayAmount(amount);
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(channel);
        playMoneyOrder.setPayer(account.getId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setAppId(appId);
        //手续费
        playMoneyOrder.setPoundage(withdrawFee);
        playMoneyOrder.setGoodsName(dealer.getProxyName());
        playMoneyOrder.setGoodsDescribe(dealer.getProxyName());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(new Date());
        playMoneyOrder.setSettleType(settleType);
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        this.accountService.decreaseAvailableAmount(account.getId(), amount);
        this.accountService.increaseFrozenAmount(account.getId(), amount);
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setFrozenAmount(amount);
        frozenRecord.setBusinessNo(playMoneyOrder.getOrderNo());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("手动提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水--减少
        this.accountFlowService.addAccountFlow(account.getId(), playMoneyOrder.getOrderNo(), amount,
                "手动提现", EnumAccountFlowType.DECREASE);
        return playMoneyOrder.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantInfo
     * @param amount
     * @param appId
     * @param channel
     * @param settleType
     * @return
     */
    @Override
    public long createMerchantPlayMoneyOrder(MerchantInfo merchantInfo, BigDecimal amount, String appId, int channel, String settleType, BigDecimal withdrawFee) {
        final Account account = this.accountService.getByIdWithLock(merchantInfo.getAccountId()).get();
        Preconditions.checkState(account.getAvailable().compareTo(amount) >= 0, "余额不足");
        final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(0);
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(amount);
        playMoneyOrder.setRealPayAmount(amount);
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(channel);
        playMoneyOrder.setPayer(account.getId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setAppId(appId);
        //手续费
        playMoneyOrder.setPoundage(withdrawFee);
        playMoneyOrder.setGoodsName(merchantInfo.getMerchantName());
        playMoneyOrder.setGoodsDescribe(merchantInfo.getMerchantName());
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(new Date());
        playMoneyOrder.setSettleType(settleType);
        playMoneyOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.add(playMoneyOrder);
        this.accountService.decreaseAvailableAmount(account.getId(), amount);
        this.accountService.increaseFrozenAmount(account.getId(), amount);
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setFrozenAmount(amount);
        frozenRecord.setBusinessNo(playMoneyOrder.getOrderNo());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("手动提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水--减少
        this.accountFlowService.addAccountFlow(account.getId(), playMoneyOrder.getOrderNo(), amount,
                "提现", EnumAccountFlowType.DECREASE);
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

    @Override
    public List<MerchantTradeResponse> selectOrderListByPage(OrderTradeRequest req) {
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
        map.put("lessTotalFee",req.getLessTotalFee());
        map.put("moreTotalFee",req.getMoreTotalFee());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        map.put("sn",req.getSn());
        map.put("proxyName",req.getProxyName());
        map.put("proxyName1",req.getProxyName1());
        map.put("businessOrderNo",req.getBusinessOrderNo());
        List<MerchantTradeResponse> list = orderDao.selectOrderList(map);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
                if (list.get(i).getPayChannelSign()==101){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==102){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==103){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_UNIONPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==201){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==202){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==301){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.MB_UNIONPAY.getName());
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")){
                    if (list.get(i).getPayType().equals("sm_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_wechat_code")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_code")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_unionpay")){
                        list.get(i).setPayType(EnumPayType.YG_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_code")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_code")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("mb_unionpay")){
                        list.get(i).setPayType(EnumPayType.MB_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_wechat")){
                        list.get(i).setPayType(EnumPayType.HZYB_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_alipay")){
                        list.get(i).setPayType(EnumPayType.HZYB_ALIPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_wechat")){
                        list.get(i).setPayType(EnumPayType.YIJIA_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_alipay")){
                        list.get(i).setPayType(EnumPayType.YIJIA_ALIPAY.getValue());
                    }
                }

            }
        }
        return list;
    }



    public List<MerchantTradeResponse> selectOrderList(OrderTradeRequest req) {
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
        map.put("lessTotalFee",req.getLessTotalFee());
        map.put("moreTotalFee",req.getMoreTotalFee());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        map.put("sn",req.getSn());
        map.put("proxyName",req.getProxyName());
        map.put("proxyName1",req.getProxyName1());
        map.put("businessOrderNo",req.getBusinessOrderNo());
        List<MerchantTradeResponse> list = orderDao.downloadOrderList(map);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
                if (list.get(i).getPayChannelSign()==101){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==102){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==103){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_UNIONPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==201){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==202){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==301){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.MB_UNIONPAY.getName());
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")){
                    if (list.get(i).getPayType().equals("sm_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_wechat_code")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_code")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_unionpay")){
                        list.get(i).setPayType(EnumPayType.YG_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_code")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_code")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("mb_unionpay")){
                        list.get(i).setPayType(EnumPayType.MB_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_wechat")){
                        list.get(i).setPayType(EnumPayType.HZYB_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_alipay")){
                        list.get(i).setPayType(EnumPayType.HZYB_ALIPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_wechat")){
                        list.get(i).setPayType(EnumPayType.YIJIA_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_alipay")){
                        list.get(i).setPayType(EnumPayType.YIJIA_ALIPAY.getValue());
                    }
                }

            }
        }
        return list;
    }

    /**
     * 获取临时路径
     *
     * @return
     */
    public static String getTempDir() {
        final String dir = System.getProperty("java.io.tmpdir") + "hss" + File.separator + "trade" + File.separator + "record";
        final File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    /**
     * 下载Excele
     * @param
     * @param baseUrl
     * @return
     */
    @Override
    @Transactional
    public String downloadExcel(OrderTradeRequest req, String baseUrl) {
        final String tempDir = this.getTempDir();
        final File excelFile = new File(tempDir + File.separator + ".xls");
        final ExcelSheetVO excelSheet = generateCodeExcelSheet(req,baseUrl);
        final List<ExcelSheetVO> excelSheets = new ArrayList<>();
        excelSheets.add(excelSheet);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(excelFile);
            ExcelUtil.exportExcel(excelSheets, fileOutputStream);
            return excelFile.getAbsolutePath();
        } catch (final Exception e) {
            log.error("download trade record error", e);
            e.printStackTrace();
        }  finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (final IOException e) {
                    log.error("close fileOutputStream error", e);
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    @Override
    public MerchantTradeResponse selectOrderListByPageAll(OrderTradeRequest req) {
        MerchantTradeResponse list = orderDao.selectOrderListByPageAll(req.getOrderNo());
        if (list!=null){

                if (list.getAppId().equals("hss")){
                    String hss="好收收";
                    list.setAppId(hss);
                }
                if (list.getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.setAppId(hsy);
                }

                if (list.getMobile()!=null&&!"".equals(list.getMobile())){
                    list.setMobile(MerchantSupport.decryptMobile(list.getMobile()));
                }
                if (list.getBankNo()!=null&&!"".equals(list.getBankNo())){
                    list.setBankNo(MerchantSupport.decryptBankCard(list.getBankNo()));
                }
                if (list.getReserveMobile()!=null&&!"".equals(list.getReserveMobile())){
                    list.setReserveMobile(MerchantSupport.decryptMobile(list.getReserveMobile()));
                }
                if (list.getIdentity()!=null&&!"".equals(list.getIdentity())){
                    list.setIdentity(MerchantSupport.decryptIdentity(list.getIdentity()));
                }

                if (list.getPayChannelSign()==101){
                    list.setPayChannelSigns(EnumPayChannelSign.YG_WECHAT.getName());
                }
                if (list.getPayChannelSign()==102){
                    list.setPayChannelSigns(EnumPayChannelSign.YG_ALIPAY.getName());
                }
                if (list.getPayChannelSign()==103){
                    list.setPayChannelSigns(EnumPayChannelSign.YG_UNIONPAY.getName());
                }
                if (list.getPayChannelSign()==201){
                    list.setPayChannelSigns(EnumPayChannelSign.KM_WECHAT.getName());
                }
                if (list.getPayChannelSign()==202){
                    list.setPayChannelSigns(EnumPayChannelSign.KM_ALIPAY.getName());
                }
                if (list.getPayChannelSign()==301){
                    list.setPayChannelSigns(EnumPayChannelSign.MB_UNIONPAY.getName());
                }
                if (list.getPayType()!=null&&!list.getPayType().equals("")){
                    if (list.getPayType().equals("sm_wechat_jsapi")){
                        list.setPayType(EnumPayType.YG_WECHAT_JSAPI.getValue());
                    }
                    if (list.getPayType().equals("sm_alipay_jsapi")){
                        list.setPayType(EnumPayType.YG_ALIPAY_JSAPI.getValue());
                    }
                    if (list.getPayType().equals("sm_wechat_code")){
                        list.setPayType(EnumPayType.YG_WECHAT_CODE.getValue());
                    }
                    if (list.getPayType().equals("sm_alipay_code")){
                        list.setPayType(EnumPayType.YG_ALIPAY_CODE.getValue());
                    }
                    if (list.getPayType().equals("sm_unionpay")){
                        list.setPayType(EnumPayType.YG_UNIONPAY.getValue());
                    }
                    if (list.getPayType().equals("km_wechat_jsapi")){
                        list.setPayType(EnumPayType.KM_WECHAT_JSAPI.getValue());
                    }
                    if (list.getPayType().equals("km_alipay_jsapi")){
                        list.setPayType(EnumPayType.KM_ALIPAY_JSAPI.getValue());
                    }
                    if (list.getPayType().equals("km_wechat_code")){
                        list.setPayType(EnumPayType.KM_WECHAT_CODE.getValue());
                    }
                    if (list.getPayType().equals("km_alipay_code")){
                        list.setPayType(EnumPayType.KM_ALIPAY_CODE.getValue());
                    }
                    if (list.getPayType().equals("mb_unionpay")){
                        list.setPayType(EnumPayType.MB_UNIONPAY.getValue());
                    }
                    if (list.getPayType().equals("hzyb_wechat")){
                        list.setPayType(EnumPayType.HZYB_WECHAT.getValue());
                    }
                    if (list.getPayType().equals("hzyb_alipay")){
                        list.setPayType(EnumPayType.HZYB_ALIPAY.getValue());
                    }
                    if (list.getPayType().equals("yijia_wechat")){
                        list.setPayType(EnumPayType.YIJIA_WECHAT.getValue());
                    }
                    if (list.getPayType().equals("yijia_alipay")){
                        list.setPayType(EnumPayType.YIJIA_ALIPAY.getValue());
                    }
                }
                if (list.getLevel()==1){
                    list.setProxyName(list.getProxyName());
                }
                if (list.getLevel()==2){
                    list.setProxyName1(list.getProxyName());
                    String proxyName = dealerService.selectProxyName(list.getFirstLevelDealerId());
                    list.setProxyName(proxyName);
                }

            }
        return list;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @return
     */
    @Override
    public Optional<Order> getByOrderNo(final String orderNo) {
        return Optional.fromNullable(this.orderDao.selectByOrderNo(orderNo));
    }

    /**
     * {@inheritDoc}
     *
     * @param requestParam
     * @return
     */
    @Override
    public PageModel<Order> queryMerchantPayOrders(final QueryMerchantPayOrdersRequestParam requestParam) {
        final PageModel<Order> pageModel = new PageModel<>(requestParam.getPageNo(), requestParam.getPageSize());
        requestParam.setOffset(pageModel.getFirstIndex());
        requestParam.setCount(pageModel.getPageSize());
        final long count = this.orderDao.selectCountMerchantPayOrders(requestParam);
        final List<Order> orders = this.orderDao.selectMerchantPayOrders(requestParam);
        pageModel.setCount(count);
        pageModel.setRecords(orders);
        return pageModel;
    }

    /**
     * {@inheritDoc}
     *
     * @param businessOrderNo
     * @return
     */
    @Override
    public Optional<Order> getByBusinessOrderNo(final String businessOrderNo) {
        return Optional.fromNullable(this.orderDao.selectByBusinessOrderNo(businessOrderNo));
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param appId
     * @param serviceType
     * @return
     */
    @Override
    public BigDecimal getTotalTradeAmountByAccountId(final long accountId, final String appId, final int serviceType) {
        final BigDecimal totalAmount = this.orderDao.selectTotalTradeAmountByAccountId(accountId, appId, serviceType);
        return null == totalAmount ? new BigDecimal("0.00") : totalAmount;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNos
     * @return
     */
    @Override
    public List<String> getCheckedOrderNosByOrderNos(final List<String> orderNos) {
        if (CollectionUtils.isEmpty(orderNos)) {
            return Collections.emptyList();
        }
        return this.orderDao.selectCheckedOrderNosByOrderNos(orderNos);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param appId
     * @return
     */
    @Override
    public long getPageOrdersCountByAccountId(final long accountId, final String appId, final Date date) {
        return this.orderDao.selectPageOrdersCountByAccountId(accountId, appId, date);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @param appId
     * @param offset
     * @param count
     * @return
     */
    @Override
    public List<Order> getPageOrdersByAccountId(final long accountId, final String appId, final int offset,
                                                final int count, final Date date) {
        return this.orderDao.selectPageOrdersByAccountId(accountId, appId, offset, count, date);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNos
     * @return
     */
    @Override
    public List<Order> getByOrderNos(List<String> orderNos) {
        if(CollectionUtils.isEmpty(orderNos)){
            return Collections.EMPTY_LIST;
        }
        return this.orderDao.getByOrderNos(orderNos);
    }

    @Override
    public List<MerchantTradeResponse> getOrderList(OrderTradeRequest req) {
        List<MerchantTradeResponse> list = orderDao.getOrderList(req);
//        List<MerchantTradeResponse> list2 = orderService.getOrderList(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
                if (list.get(i).getPayChannelSign()==101){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==102){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==103){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_UNIONPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==201){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==202){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==301){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.MB_UNIONPAY.getName());
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")){
                    if (list.get(i).getPayType().equals("sm_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_wechat_code")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_code")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_unionpay")){
                        list.get(i).setPayType(EnumPayType.YG_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_code")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_code")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("mb_unionpay")){
                        list.get(i).setPayType(EnumPayType.MB_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_wechat")){
                        list.get(i).setPayType(EnumPayType.HZYB_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_alipay")){
                        list.get(i).setPayType(EnumPayType.HZYB_ALIPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_wechat")){
                        list.get(i).setPayType(EnumPayType.YIJIA_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_alipay")){
                        list.get(i).setPayType(EnumPayType.YIJIA_ALIPAY.getValue());
                    }
                }
//                if (list.get(i).getLevel()==1){
//                    list.get(i).setProxyName(list.get(i).getProxyName());
//                }
//                if (list.get(i).getLevel()==2){
//                    list.get(i).setProxyName1(list.get(i).getProxyName());
//                    String proxyName = dealerService.selectProxyName(list.get(i).getFirstLevelDealerId());
//                    list.get(i).setProxyName(proxyName);
//                }

            }
        }


        return list;
    }

    @Override
    public String  amountCount(OrderTradeRequest req) {
        String res = this.orderDao.amountCount(req);
        return res;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handleT1UnSettlePayOrder() {
        final String format = DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd);
        final List<Long> orderIds = this.getT1PaySuccessAndUnSettleOrderIds(DateFormatUtil.parse(format, DateFormatUtil.yyyy_MM_dd), EnumProductType.HSS.getId());
        log.info("hss-T1-定时处理提现, 订单[{}]", orderIds);
        if (!CollectionUtils.isEmpty(orderIds)) {
            for (int i = 0; i < orderIds.size(); i++) {
                final long orderId = orderIds.get(i);
                final JSONObject requestParam = new JSONObject();
                requestParam.put("orderId", orderId);
                MqProducer.produce(requestParam, MqConfig.MERCHANT_WITHDRAW_T1, 10 * i);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param settleDate
     * @param appId
     * @return
     */
    @Override
    public List<Long> getT1PaySuccessAndUnSettleOrderIds(Date settleDate, String appId) {
        return this.orderDao.selectT1PaySuccessAndUnSettleOrderIds(settleDate, appId);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderId
     */
    @Override
    @Transactional
    public void t1WithdrawByOrderId(final long orderId) {
        log.info("订单[{}], T1发起结算提现", orderId);
        final Order order = this.getByIdWithLock(orderId).get();
        if (order.isPaySuccess() && order.isDueSettle()) {
            final Optional<SettleAccountFlow> decreaseSettleAccountFlowOptional = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(order.getOrderNo(),
                    order.getPayee(), EnumAccountFlowType.DECREASE.getId());
            Preconditions.checkState(!decreaseSettleAccountFlowOptional.isPresent(), "订单[{}], 在T1发起结算提现时,出现已结算的记录!!", orderId);
            final SettleAccountFlow increaseSettleAccountFlow = this.settleAccountFlowService.getByOrderNoAndAccountIdAndType(order.getOrderNo(),
                    order.getPayee(), EnumAccountFlowType.INCREASE.getId()).get();
            final SettlementRecord settlementRecord = this.settlementRecordService.getByIdWithLock(increaseSettleAccountFlow.getSettlementRecordId()).get();
            if (settlementRecord.isWaitWithdraw()) {
                final MerchantInfo merchant = this.merchantInfoService.getByAccountId(order.getPayee()).get();
                final Pair<Integer, String> withdrawPair = this.withdrawService.merchantWithdrawBySettlementRecord(merchant.getId(),
                        settlementRecord.getId(), order.getSn(), order.getPayChannelSign());
                if (withdrawPair.getLeft() != -1) {
                    log.info("订单[{}], T1发起结算提现, 手续费-入账可用余额", orderId);
                    this.dealerAndMerchantPoundageSettleImpl(order, increaseSettleAccountFlow.getId());
                    return;
                }
                log.error("订单[{}],在T1发起结算提现时， 请求网关异常", orderId, order.getStatus(), order.getSettleStatus());
                return;
            }
        }
        log.error("订单[{}],在T1发起结算提现时，状态错误!!!，订单状态[{}], 结算状态[{}]", orderId, order.getStatus(), order.getSettleStatus());
    }

    @Override
    public List<WithdrawResponse> withdrawList(WithdrawRequest req) {
        List<WithdrawResponse> list = this.orderDao.withdrawList(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getMerchantName()!=null&&!("").equals(list.get(i).getMerchantName())){
                    list.get(i).setUserType("商户");
                }
                if (list.get(i).getProxyName()!=null&&!("").equals(list.get(i).getProxyName())){
                    list.get(i).setUserType("代理商");
                }
                if (list.get(i).getCreateTime()!=null&&!list.get(i).getCreateTime().equals("")){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
                if (list.get(i).getSuccessSettleTime()!=null&&!list.get(i).getSuccessSettleTime().equals("")){
                    String dates = sdf.format(list.get(i).getSuccessSettleTime());
                    list.get(i).setSuccessTime(dates);
                }
                if (list.get(i).getStatus()==5){
                    list.get(i).setWithdrawStatus(EnumOrderStatus.WITHDRAWING.getValue());
                }
                if (list.get(i).getStatus()==6){
                    list.get(i).setWithdrawStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getValue());
                }
                if (list.get(i).getPayChannelSign()==101){
                    list.get(i).setPayChannelName(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                if (list.get(i).getPayChannelSign()==102){
                    list.get(i).setPayChannelName(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                if (list.get(i).getPayChannelSign()==103){
                    list.get(i).setPayChannelName(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                if (list.get(i).getPayChannelSign()==201){
                    list.get(i).setPayChannelName(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                if (list.get(i).getPayChannelSign()==202){
                    list.get(i).setPayChannelName(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
                if (list.get(i).getPayChannelSign()==301){
                    list.get(i).setPayChannelName(EnumPayChannelSign.idOf(list.get(i).getPayChannelSign()).getName());
                }
            }
        }
        return list;
    }

    @Override
    public int getNo(WithdrawRequest req) {
        return this.orderDao.getNo(req);
    }

    @Override
    public WithdrawResponse withdrawAmount(WithdrawRequest req) {
        WithdrawResponse response = this.orderDao.withdrawAmount(req);
        return response;
    }

    @Override
    public WithdrawResponse withdrawDetail(long idd,String createTimes) {
        WithdrawResponse response = orderDao.withdrawDetail(idd,createTimes);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (response!=null){
            response.setLocation(response.getBelongProvinceName()+response.getBelongCityName());
            if (response.getLevel()==2){
                MerchantInfoResponse res = dealerService.getProxyName(response.getFirstLevelDealerId());
                response.setProxyNames(res.getProxyName());
                response.setMarkCode(res.getMarkCode());
            }
            if (response.getCreateTime()!=null&&!response.getCreateTime().equals("")){
                String dates = sdf.format(response.getCreateTime());
                response.setCreateTimes(dates);
            }
            if (response.getSuccessSettleTime()!=null&&!response.getSuccessSettleTime().equals("")){
                String dates = sdf.format(response.getSuccessSettleTime());
                response.setSuccessTime(dates);
            }
            if (response.getStatus()==5){
                response.setWithdrawStatus(EnumOrderStatus.WITHDRAWING.getValue());
            }
            if (response.getStatus()==6){
                response.setWithdrawStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getValue());
            }
            if (response.getPayChannelSign()==101){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==102){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==103){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==201){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==202){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==301){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
        }
        return response;
    }

    @Override
    public WithdrawResponse withdrawDetails(long idm,String createTimes) {
        WithdrawResponse response = this.orderDao.withdrawDetails(idm,createTimes);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (response!=null){
            response.setMobile(MerchantSupport.decryptMobile(response.getMobile()));
            if (response.getProvinceCode()!=null&&!("").equals(response.getProvinceCode())){
                if (response.getProvinceCode().equals("110000")||response.getProvinceCode().equals("120000")||response.getProvinceCode().equals("310000")||response.getProvinceCode().equals("500000")){
                    response.setLocationM(response.getProvinceName()+response.getCountyName());
                }else {
                    response.setLocationM(response.getProvinceName()+response.getCityName()+response.getCountyName());
                }
            }
            if (response.getFirstDealerId()>0 ){
                MerchantInfoResponse res = dealerService.getInfo(response.getFirstDealerId());
                response.setProxyNames(res.getProxyName());
                response.setMarkCode(res.getMarkCode());
            }
            if (response.getSecondDealerId()>0){
                MerchantInfoResponse res1 = dealerService.getInfo1(response.getSecondDealerId());
                response.setProxyName1(res1.getProxyName());
                response.setMarkCode1(res1.getMarkCode());
            }
            if (response.getCreateTime()!=null&&!response.getCreateTime().equals("")){
                String dates = sdf.format(response.getCreateTime());
                response.setCreateTimes(dates);
            }
            if (response.getSuccessSettleTime()!=null&&!response.getSuccessSettleTime().equals("")){
                String dates = sdf.format(response.getSuccessSettleTime());
                response.setSuccessTime(dates);
            }
            if (response.getStatus()==5){
                response.setWithdrawStatus(EnumOrderStatus.WITHDRAWING.getValue());
            }
            if (response.getStatus()==6){
                response.setWithdrawStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getValue());
            }
            if (response.getPayChannelSign()==101){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==102){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==103){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==201){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==202){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
            if (response.getPayChannelSign()==301){
                response.setPayChannelName(EnumPayChannelSign.idOf(response.getPayChannelSign()).getName());
            }
        }
        return response;
    }

    @Override
    public List<PlayResponse> getPlayMoney(String orderNo) {
        List<PlayResponse> list = this.orderDao.getPlayMoney(orderNo);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getRequestTime()!=null){
                    String dates = sdf.format(list.get(i).getRequestTime());
                    list.get(i).setRequestTimes(dates);
                }
                if (list.get(i).getFinishTime()!=null){
                    String dates = sdf.format(list.get(i).getFinishTime());
                    list.get(i).setFinishTimes(dates);
                }
                if (list.get(i).getStatus()==1){
                    list.get(i).setStatusValue(EnumPlayStatus.of(list.get(i).getStatus()).getValue());
                }
                if (list.get(i).getStatus()==2){
                    list.get(i).setStatusValue(EnumPlayStatus.of(list.get(i).getStatus()).getValue());
                }
                if (list.get(i).getStatus()==3){
                    list.get(i).setStatusValue(EnumPlayStatus.of(list.get(i).getStatus()).getValue());
                }
                if (list.get(i).getStatus()==4){
                    list.get(i).setStatusValue(EnumPlayStatus.of(list.get(i).getStatus()).getValue());
                }
                if (list.get(i).getStatus()==5){
                    list.get(i).setStatusValue(EnumPlayStatus.of(list.get(i).getStatus()).getValue());
                }

                if (list.get(i).getPlayMoneyChannel()==1){
                    list.get(i).setPlayMoneyChannels(EnumChannel.of(list.get(i).getPlayMoneyChannel()).getValue());
                }
                if (list.get(i).getPlayMoneyChannel()==2){
                    list.get(i).setPlayMoneyChannels(EnumChannel.of(list.get(i).getPlayMoneyChannel()).getValue());
                }
                if (list.get(i).getPlayMoneyChannel()==3){
                    list.get(i).setPlayMoneyChannels(EnumChannel.of(list.get(i).getPlayMoneyChannel()).getValue());
                }
                if (list.get(i).getPlayMoneyChannel()==4){
                    list.get(i).setPlayMoneyChannels(EnumChannel.of(list.get(i).getPlayMoneyChannel()).getValue());
                }
                if (list.get(i).getPlayMoneyChannel()==5){
                    list.get(i).setPlayMoneyChannels(EnumChannel.of(list.get(i).getPlayMoneyChannel()).getValue());
                }

            }
        }
        return list;
    }

    @Override
    public List<MerchantTradeResponse> getTrade(OrderTradeRequest req) {
        List<MerchantTradeResponse> list = this.orderDao.getTrade(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
                if (list.get(i).getPayChannelSign()==101){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==102){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==103){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_UNIONPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==201){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==202){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==301){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.MB_UNIONPAY.getName());
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")){
                    if (list.get(i).getPayType().equals("sm_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_wechat_code")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_code")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_unionpay")){
                        list.get(i).setPayType(EnumPayType.YG_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_code")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_code")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("mb_unionpay")){
                        list.get(i).setPayType(EnumPayType.MB_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_wechat")){
                        list.get(i).setPayType(EnumPayType.HZYB_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_alipay")){
                        list.get(i).setPayType(EnumPayType.HZYB_ALIPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_wechat")){
                        list.get(i).setPayType(EnumPayType.YIJIA_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_alipay")){
                        list.get(i).setPayType(EnumPayType.YIJIA_ALIPAY.getValue());
                    }
                }

            }
        }
        return list;
    }

    @Override
    public List<MerchantTradeResponse> getTradeFirst(OrderTradeRequest req) {
        List<MerchantTradeResponse> list = this.orderDao.getTradeFirst(req);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getAppId().equals("hss")){
                    String hss="好收收";
                    list.get(i).setAppId(hss);
                }
                if (list.get(i).getAppId().equals("hsy")){
                    String hsy="好收银";
                    list.get(i).setAppId(hsy);
                }
                if (list.get(i).getPayChannelSign()==101){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==102){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==103){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.YG_UNIONPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==201){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==202){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.KM_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==301){
                    list.get(i).setPayChannelSigns(EnumPayChannelSign.MB_UNIONPAY.getName());
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")){
                    if (list.get(i).getPayType().equals("sm_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_wechat_code")){
                        list.get(i).setPayType(EnumPayType.YG_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_alipay_code")){
                        list.get(i).setPayType(EnumPayType.YG_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("sm_unionpay")){
                        list.get(i).setPayType(EnumPayType.YG_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_jsapi")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_wechat_code")){
                        list.get(i).setPayType(EnumPayType.KM_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("km_alipay_code")){
                        list.get(i).setPayType(EnumPayType.KM_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals("mb_unionpay")){
                        list.get(i).setPayType(EnumPayType.MB_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_wechat")){
                        list.get(i).setPayType(EnumPayType.HZYB_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("hzyb_alipay")){
                        list.get(i).setPayType(EnumPayType.HZYB_ALIPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_wechat")){
                        list.get(i).setPayType(EnumPayType.YIJIA_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals("yijia_alipay")){
                        list.get(i).setPayType(EnumPayType.YIJIA_ALIPAY.getValue());
                    }
                }

            }
        }
        return list;
    }

    @Override
    public int listCount(OrderTradeRequest req) {
        return this.orderDao.listCount(req);
    }

    @Override
    public int listFirstCount(OrderTradeRequest req) {
        return this.orderDao.listFirstCount(req);
    }


    /**
     * 手续费由待结算入余额
     *
     * @param order
     * @param settleFlowId   收钱商户的待结算流水ID
     */
    private void dealerAndMerchantPoundageSettleImpl(final Order order, final long settleFlowId) {
        final List<SettleAccountFlow> flows = this.settleAccountFlowService.getByOrderNo(order.getOrderNo());
        if (!CollectionUtils.isEmpty(flows)) {
            for (SettleAccountFlow settleAccountFlow : flows) {
                if (settleAccountFlow.getId() == settleFlowId
                        || EnumAccountUserType.COMPANY.getId() == settleAccountFlow.getAccountUserType()) {
                    continue;
                }
                Preconditions.checkState(EnumAccountFlowType.INCREASE.getId() == settleAccountFlow.getType(),
                        "订单[{}],在T1发起结算提现时, 手续费入账到余额，出现已结算的待结算流水", order.getId());
                if (EnumAccountUserType.MERCHANT.getId() == settleAccountFlow.getAccountUserType()) {
                    final MerchantInfo merchant = this.merchantInfoService.getByAccountId(settleAccountFlow.getAccountId()).get();
                    final long settlementRecordId = this.payService.generateMerchantSettlementRecord(settleAccountFlow.getAccountId(),
                            merchant, order.getSettleTime(), settleAccountFlow.getIncomeAmount());
                    this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlow.getId(), settlementRecordId);
                    //待结算--可用余额
                    this.payService.merchantRecordedAccount(settleAccountFlow.getAccountId(), settleAccountFlow.getIncomeAmount(), order, settlementRecordId);
                }
                if (EnumAccountUserType.DEALER.getId() == settleAccountFlow.getAccountUserType()) {
                    final Dealer dealer = this.dealerService.getByAccountId(settleAccountFlow.getAccountId()).get();
                    final long settlementRecordId = this.payService.generateDealerSettlementRecord(settleAccountFlow.getAccountId(),
                            dealer, order.getSettleTime(), settleAccountFlow.getIncomeAmount());
                    this.settleAccountFlowService.updateSettlementRecordIdById(settleAccountFlow.getId(), settlementRecordId);
                    //待结算--可用余额
                    this.payService.dealerRecordedAccount(settleAccountFlow.getAccountId(), settleAccountFlow.getIncomeAmount(), order, settlementRecordId);
                }
            }
        }
    }

    /**
     * 生成ExcelVo
     * @param
     * @param baseUrl
     * @return
     */
    private ExcelSheetVO generateCodeExcelSheet(OrderTradeRequest req,String baseUrl) {
        List<MerchantTradeResponse> list = selectOrderList(req);
        final ExcelSheetVO excelSheetVO = new ExcelSheetVO();
        final List<List<String>> datas = new ArrayList<List<String>>();
        final ArrayList<String> heads = new ArrayList<>();
        excelSheetVO.setName("trade");
        heads.add("业务方");
        heads.add("业务订单号");
        heads.add("交易订单号");
        heads.add("支付流水号");
        heads.add("交易日期");
        heads.add("收款商户名称");
        heads.add("所属一级");
        heads.add("所属二级");
        heads.add("支付金额");
        heads.add("手续费率");
//        heads.add("手续费");
        heads.add("订单状态");
        heads.add("结算状态");
        heads.add("支付方式");
        heads.add("支付渠道");
        heads.add("渠道信息");
        datas.add(heads);
        if(list.size()>0){
            for(int i=0;i<list.size();i++){
                ArrayList<String> columns = new ArrayList<>();
                columns.add(list.get(i).getAppId());
                columns.add(list.get(i).getBusinessOrderNo());
                columns.add(list.get(i).getOrderNo());
                columns.add(list.get(i).getSn());
                if (list.get(i).getCreateTime()!= null && !"".equals(list.get(i).getCreateTime())){
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String st = df.format(list.get(i).getCreateTime());
                    columns.add(st);

                }else {
                    columns.add("");
                }
                columns.add(list.get(i).getMerchantName());
                columns.add(list.get(i).getProxyName());
                columns.add(list.get(i).getProxyName1());
                columns.add(String.valueOf(list.get(i).getTradeAmount()));
                columns.add(String.valueOf(list.get(i).getPayRate()));
//                if (list.get(i).getPayRate()==null){
//                    String x = "0";
//                    columns.add(x);
//                }else {
//                    columns.add(String.valueOf(list.get(i).getPayRate()));
//                }
//                if (list.get(i).getPoundage()==null){
//                    String x = " ";
//                    columns.add(x);
//                }else {
//                    columns.add(String.valueOf(list.get(i).getPoundage()));
//                }
                if (list.get(i).getStatus()==1){
                    columns.add("待支付");
                }
                if (list.get(i).getStatus()==3){
                    columns.add("支付失败");
                }
                if (list.get(i).getStatus()==4){
                    columns.add("支付成功");
                }
                if (list.get(i).getStatus()==5){
                    columns.add("提现中");
                }
                if (list.get(i).getStatus()==6){
                    columns.add("提现成功");
                }
                if (list.get(i).getStatus()==7){
                    columns.add("充值成功");
                }
                if (list.get(i).getStatus()==8){
                    columns.add("充值失败");
                }

                if (list.get(i).getSettleStatus()==1){
                    columns.add("未结算");
                }
                if (list.get(i).getSettleStatus()==2){
                    columns.add("结算中");
                }
                if (list.get(i).getSettleStatus()==3){
                    columns.add("已结算");
                }
                if (list.get(i).getPayType()!=null&&!list.get(i).getPayType().equals("")){
                    if (list.get(i).getPayType().equals(EnumPayType.YG_WECHAT_JSAPI.getValue())){
                        columns.add(EnumPayType.YG_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.YG_ALIPAY_JSAPI.getValue())){
                        columns.add(EnumPayType.YG_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.YG_WECHAT_CODE.getValue())){
                        columns.add(EnumPayType.YG_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.YG_ALIPAY_CODE.getValue())){
                        columns.add(EnumPayType.YG_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.YG_UNIONPAY.getValue())){
                        columns.add(EnumPayType.YG_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.KM_WECHAT_JSAPI.getValue())){
                        columns.add(EnumPayType.KM_WECHAT_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.KM_ALIPAY_JSAPI.getValue())){
                        columns.add(EnumPayType.KM_ALIPAY_JSAPI.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.KM_WECHAT_CODE.getValue())){
                        columns.add(EnumPayType.KM_WECHAT_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.KM_ALIPAY_CODE.getValue())){
                        columns.add(EnumPayType.KM_ALIPAY_CODE.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.MB_UNIONPAY.getValue())){
                        columns.add(EnumPayType.MB_UNIONPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.HZYB_WECHAT.getValue())){
                        columns.add(EnumPayType.HZYB_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.HZYB_ALIPAY.getValue())){
                        columns.add(EnumPayType.HZYB_ALIPAY.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.YIJIA_WECHAT.getValue())){
                        columns.add(EnumPayType.YIJIA_WECHAT.getValue());
                    }
                    if (list.get(i).getPayType().equals(EnumPayType.YIJIA_ALIPAY.getValue())){
                        columns.add(EnumPayType.YIJIA_ALIPAY.getValue());
                    }
                }else {
                    columns.add("");
                }
                if (list.get(i).getPayChannelSign()==101){
                    columns.add(EnumPayChannelSign.YG_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==102){
                    columns.add(EnumPayChannelSign.YG_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==103){
                    columns.add(EnumPayChannelSign.YG_UNIONPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==201){
                    columns.add(EnumPayChannelSign.KM_WECHAT.getName());
                }
                if (list.get(i).getPayChannelSign()==202){
                    columns.add(EnumPayChannelSign.KM_ALIPAY.getName());
                }
                if (list.get(i).getPayChannelSign()==301){
                    columns.add(EnumPayChannelSign.MB_UNIONPAY.getName());
                }
                columns.add(list.get(i).getRemark());
                datas.add(columns);
            }
        }
        excelSheetVO.setDatas(datas);
        return excelSheetVO;
    }

    @Override
    public int selectOrderListCount(OrderTradeRequest req) {
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
        map.put("lessTotalFee",req.getLessTotalFee());
        map.put("moreTotalFee",req.getMoreTotalFee());
        map.put("offset",req.getOffset());
        map.put("size",req.getSize());
        map.put("sn",req.getSn());
        map.put("proxyName",req.getProxyName());
        map.put("proxyName1",req.getProxyName1());
        map.put("businessOrderNo",req.getBusinessOrderNo());
        return orderDao.selectOrderListCount(map);
    }



}
