package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.DateUtil;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.account.sevice.AccountFlowService;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.FrozenRecordService;
import com.jkm.hss.bill.dao.OrderDao;
import com.jkm.hss.bill.dao.WithdrawOrderDao;
import com.jkm.hss.bill.entity.*;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.SettlementRecordService;
import com.jkm.hss.bill.service.WithdrawOrderService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumUpperChannel;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.immutables.value.internal.$processor$.meta.$TreesMirrors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2017-07-24.
 */
@Service
@Slf4j
public class WithdrawOrderServiceImpl implements WithdrawOrderService {

    @Autowired
    private WithdrawOrderDao withdrawOrderDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Override
    public void insert(WithdrawOrder withdrawOrder) {
        this.withdrawOrderDao.insert(withdrawOrder);
    }
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FrozenRecordService frozenRecordService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private SettlementRecordService settlementRecordService;
    @Override
    public void update(WithdrawOrder withdrawOrder) {
        this.withdrawOrderDao.update(withdrawOrder);
    }

    /**
     * {@inheritDoc}
     * @param account
     * @return
     */
    @Transactional
    @Override
    public JSONObject d0WithDrawImpl(Account account, long userId, String merchantNo, AppBizCard appBizCard) {
        //查询商户当天的可以提现的订单 每次限制75个
        final List<Order> orderList =
                this.orderService.selectOrderListByCount(account.getId(), 75, EnumOrderStatus.PAY_SUCCESS, DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
        //判断交易时间 9:00 -22:00
        int isInDate = EnumBoolean.TRUE.getCode();
        if(!DateUtil.isInDate(new Date(),"09:00:00","22:00:00")){
            isInDate = EnumBoolean.FALSE.getCode();
        }
        //判断当天是否已经提现
        List<Order> withdrawOrders =  this.orderService.selectWithdrawingOrderByAccountId(account.getId(), DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
        int isFirst = EnumBoolean.TRUE.getCode();
        if (withdrawOrders.size() > 10){
            isFirst = EnumBoolean.FALSE.getCode();
        }
        //判断提现
        if(CollectionUtils.isEmpty(orderList)){
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("withDrawOrderId", 0);
            jsonObject.put("avaWithdraw",new BigDecimal("0"));
            jsonObject.put("fee",new BigDecimal("0"));
            jsonObject.put("isFirst",isFirst);
            jsonObject.put("isInDate",isInDate);
            return jsonObject;
        }
  /*      final BigDecimal wechatTradeRate =
                this.userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.WECHAT.getId()).get().getTradeRateD0();
        final BigDecimal alipayTradeRate =
                this.userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.ALIPAY.getId()).get().getTradeRateD0();
        final UserWithdrawRate userWithdrawRate = this.userWithdrawRateService.selectByUserId(userId).get();*/
        //统计待结算金额
        BigDecimal sumAmount = new BigDecimal("0");
        //统计T1手续费
        BigDecimal sumT1Fee = new BigDecimal("0");
        //统计DO手续费
        BigDecimal sumD0Fee = new BigDecimal("0");
        //统计交易金额
        BigDecimal tradeAmount = new BigDecimal("0");
        StringBuffer sns = new StringBuffer();
        for (Order order : orderList){
            tradeAmount = tradeAmount.add(order.getTradeAmount());
            sns = sns.append(order.getSn() + ",");
            sumAmount= sumAmount.add(order.getTradeAmount().subtract(order.getPoundage()));
            sumT1Fee = sumT1Fee.add(order.getPoundage());
            /*BigDecimal d0PayRate = null;
            if (EnumPayChannelSign.idOf(order.getPayChannelSign()).getPaymentChannel().getId() == EnumPaymentChannel.WECHAT_PAY.getId()){
                d0PayRate = wechatTradeRate;
            }else if (EnumPayChannelSign.idOf(order.getPayChannelSign()).getPaymentChannel().getId() == EnumPaymentChannel.ALIPAY.getId()){
                d0PayRate = alipayTradeRate;
            }
            sumD0Fee = sumD0Fee.add(this.calculateService.getMerchantPayPoundage(order.getTradeAmount(), d0PayRate,order.getPayChannelSign()));*/
        }
        final BigDecimal avaWithdraw = sumAmount;
        final BigDecimal fee = sumD0Fee;

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("avaWithdraw",avaWithdraw);
        jsonObject.put("fee",fee);
        jsonObject.put("tradeAmount", tradeAmount);
        jsonObject.put("merchantNo", merchantNo);
        //初始化提现单
        final long withDrawOrderId = this.initD0WithDrawOrder(jsonObject, sns.toString(), account, appBizCard);
        jsonObject.put("withDrawOrderId",withDrawOrderId);
        jsonObject.put("isFirst",isFirst);
        jsonObject.put("isInDate",isInDate);
        return jsonObject;
    }

    /**
     * {@inheritDoc}
     * @param withDrawOrderId
     * @return
     */
    @Transactional
    @Override
    public Pair<Integer, String> confirmWithdraw(long withDrawOrderId) {

        final WithdrawOrder withdrawOrder = this.selectByIdWithlock(withDrawOrderId);
        final AppBizShop appBizShop =
                this.hsyShopDao.findPrimaryAppBizShopByAccountID(withdrawOrder.getWithdrawUserAccountId()).get(0);
        final AppBizCard appCard = new AppBizCard();
        appCard.setSid(appBizShop.getId());
        final AppBizCard appBizCard = this.hsyShopDao.findAppBizCardByParam(appCard).get(0);
        //请求提现
        if (withdrawOrder.getStatus() == EnumOrderStatus.WAIT_WITHDRAW.getId()) {
            final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
            paymentSdkDaiFuRequest.setAppId(withdrawOrder.getAppId());
            paymentSdkDaiFuRequest.setOrderNo(withdrawOrder.getOrderNo());
            paymentSdkDaiFuRequest.setTotalAmount(withdrawOrder.getWithdrawAmount().subtract(withdrawOrder.getPoundage()).toPlainString());
            paymentSdkDaiFuRequest.setTradeType("D0");
            paymentSdkDaiFuRequest.setIsCompany("0");
            paymentSdkDaiFuRequest.setMobile("");
            paymentSdkDaiFuRequest.setBankName(appBizCard.getCardBank());
            paymentSdkDaiFuRequest.setAccountName(appBizCard.getCardAccountName());
            paymentSdkDaiFuRequest.setAccountNumber(appBizCard.getCardNO());
            paymentSdkDaiFuRequest.setIdCard(appBizCard.getIdcardNO());
            paymentSdkDaiFuRequest.setPlayMoneyChannel(EnumUpperChannel.SYJ.getId());
            paymentSdkDaiFuRequest.setNote("");
            paymentSdkDaiFuRequest.setSystemCode(withdrawOrder.getAppId());
            paymentSdkDaiFuRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL);
            paymentSdkDaiFuRequest.setPayOrderSn("");
            paymentSdkDaiFuRequest.setOrders(withdrawOrder.getTradeOrders());
            paymentSdkDaiFuRequest.setMerchantNo(withdrawOrder.getWithdrawUserNo());
            //请求网关
            PaymentSdkDaiFuResponse response = null;
            try {
                final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_WITHDRAW,
                        SdkSerializeUtil.convertObjToMap(paymentSdkDaiFuRequest));
                response = JSON.parseObject(content, PaymentSdkDaiFuResponse.class);
            } catch (final Throwable e) {
                log.error("交易订单[" + withdrawOrder.getOrderNo() + "], 请求网关支付异常", e);
                withdrawOrder.setRemarks("请求网关支付异常");
                withdrawOrder.setStatus(EnumOrderStatus.WITHDRAW_FAIL.getId());
                this.update(withdrawOrder);
            }
            return this.handleHsyD0WithdrawResult(withdrawOrder, response);
        }
        withdrawOrder.setStatus(EnumOrderStatus.WITHDRAW_FAIL.getId());
        this.update(withdrawOrder);
        return Pair.of(-1, "提现失败");
    }

    @Override
    public WithdrawOrder selectByIdWithlock(long id) {
        return this.withdrawOrderDao.selectByIdWithlock(id);
    }

    @Override
    public WithdrawOrder getByOrderNo(String orderNo) {
        return this.withdrawOrderDao.getByOrderNo(orderNo);
    }

    @Override
    public List<WithdrawOrder> selectWithdrawingOrderByBefore(Date date) {
        return this.withdrawOrderDao.selectWithdrawingOrderByBefore(date);
    }

    private Pair<Integer,String> handleHsyD0WithdrawResult(WithdrawOrder withdrawOrder, PaymentSdkDaiFuResponse response) {

        //判断代付是否受理成功
        final int status = response.getStatus();
        if (status == EnumBasicStatus.SUCCESS.getId()){
            //代付受理成功

            final String orders = withdrawOrder.getTradeOrders();
            final String[] split = orders.split(",");
            final List<String> sns = Arrays.asList(split);

            final Order firstOrder = this.orderDao.selectOrderBySn(split[0]);
            final Order lastOrder = this.orderDao.selectOrderBySn(split[split.length -1]);
            final Account account = this.accountService.getByIdWithLock(withdrawOrder.getWithdrawUserAccountId()).get();
            final BigDecimal withdrawAmount = withdrawOrder.getWithdrawAmount();
            //结算 ，  冻结
            this.accountService.decreaseSettleAmount(account.getId(), withdrawAmount);
            this.accountService.increaseFrozenAmount(account.getId(), withdrawAmount);
            final FrozenRecord frozenRecord = new FrozenRecord();
            frozenRecord.setAccountId(account.getId());
            frozenRecord.setFrozenAmount(withdrawAmount);
            frozenRecord.setBusinessNo(withdrawOrder.getOrderNo());
            frozenRecord.setFrozenTime(new Date());
            frozenRecord.setRemark("Hsy商户提现");
            this.frozenRecordService.add(frozenRecord);
            //添加账户流水--减少
            this.accountFlowService.addAccountFlowToSettle(account.getId(), withdrawOrder.getOrderNo(), withdrawAmount,
                    "提现", EnumAccountFlowType.DECREASE);
            //创建结算单
            //生成结算单
            final AppBizShop shop = this.hsyShopDao.findAppBizShopByAccountID(account.getId()).get(0);
            final SettlementRecord settlementRecord = new SettlementRecord();
            settlementRecord.setAccountUserType(EnumAccountUserType.MERCHANT.getId());
            settlementRecord.setSettleDestination(EnumSettleDestinationType.TO_CARD.getId());
            settlementRecord.setSettleNo(this.settlementRecordService.getSettleNo(settlementRecord.getAccountUserType(), settlementRecord.getSettleDestination()));
            settlementRecord.setSettleAuditRecordId(0);
            settlementRecord.setAccountId(withdrawOrder.getWithdrawUserAccountId());
            settlementRecord.setUserNo(shop.getGlobalID());
            settlementRecord.setUserName(shop.getShortName());
            settlementRecord.setAppId(EnumAppType.HSY.getId());
            settlementRecord.setSettleDate(withdrawOrder.getApplyTime());
            settlementRecord.setTradeNumber(split.length);
            settlementRecord.setTradeAmount(withdrawOrder.getTradeAmount());
            settlementRecord.setSettleAmount(withdrawOrder.getWithdrawAmount());
            settlementRecord.setSettlePoundage(withdrawOrder.getPoundage());
            settlementRecord.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
            settlementRecord.setSettleMode(EnumSettleModeType.CHANNEL_SETTLE.getId());
            settlementRecord.setStatus(EnumSettlementRecordStatus.WAIT_WITHDRAW.getId());
            settlementRecord.setUpperChannel(EnumUpperChannel.SYJ.getId());
            settlementRecord.setBalanceStartTime(firstOrder.getPaySuccessTime());
            settlementRecord.setBalanceEndTime(lastOrder.getPaySuccessTime());
            settlementRecord.setSettleChannel(EnumSettleChannel.ALL.getId());
            this.settlementRecordService.add(settlementRecord);
            //更新交易订单为提现中， 结算中
            this.orderDao.updateOrdersBySns(sns, EnumOrderStatus.WITHDRAWING.getId(), EnumSettleStatus.SETTLE_ING.getId(), settlementRecord.getId(), null);
            //this.markOrder2SettlementIng(playMoneyOrder.getSettleTime(), playMoneyOrder.getPayer(), settlementRecordId, EnumSettleStatus.SETTLE_ING.getId(), playMoneyOrder.getUpperChannel());
            withdrawOrder.setSn(response.getSn());
            withdrawOrder.setStatus(EnumOrderStatus.WITHDRAWING.getId());
            withdrawOrder.setSettlementOrderId(settlementRecord.getId());
            withdrawOrder.setRemarks("提现受理成功");
            this.update(withdrawOrder);
            return Pair.of(1, "提现受理成功");

        }else if (status == EnumBasicStatus.FAIL.getId()){
            //代付失败
            withdrawOrder.setStatus(EnumOrderStatus.WITHDRAW_FAIL.getId());
            withdrawOrder.setRemarks("提现失败");
            this.update(withdrawOrder);
            return Pair.of(-1, "提现失败");
        }
        withdrawOrder.setStatus(EnumOrderStatus.WITHDRAW_FAIL.getId());
        withdrawOrder.setRemarks("提现失败");
        this.update(withdrawOrder);
        return Pair.of(-1, "提现失败");
    }

    private long initD0WithDrawOrder(JSONObject jsonObject, String sns, Account account, AppBizCard appBizCard) {
        final String orders = sns.substring(0, sns.lastIndexOf(","));
        final WithdrawOrder withdrawOrder = new WithdrawOrder();
        withdrawOrder.setAppId(EnumAppType.HSY.getId());
        withdrawOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        withdrawOrder.setSn("");
        withdrawOrder.setTradeAmount(new BigDecimal(jsonObject.getString("tradeAmount")));
        withdrawOrder.setWithdrawAmount(new BigDecimal(jsonObject.getString("avaWithdraw")));
        withdrawOrder.setWithdrawUserType(EnumWithdrawUserType.HSY_MERCHANT.getType());
        withdrawOrder.setWithdrawUserNo(jsonObject.getString("merchantNo"));
        withdrawOrder.setWithdrawUserName(account.getUserName());
        withdrawOrder.setWithdrawUserAccountId(account.getId());
        withdrawOrder.setUpperChannel(EnumUpperChannel.SYJ.getId());
        withdrawOrder.setApplyTime(new Date());
        withdrawOrder.setPoundage(new BigDecimal(jsonObject.getString("fee")));
        withdrawOrder.setTradeOrders(orders);
        withdrawOrder.setBankName(appBizCard.getCardBank());
        withdrawOrder.setBankCardNo(appBizCard.getCardNO());
        withdrawOrder.setRemarks("待提现");
        withdrawOrder.setStatus(EnumOrderStatus.WAIT_WITHDRAW.getId());
        /*final Order playMoneyOrder = new Order();
        playMoneyOrder.setPayOrderId(0);
        playMoneyOrder.setAppId(EnumAppType.HSY.getId());
        playMoneyOrder.setOrderNo(SnGenerator.generateSn(EnumTradeType.WITHDRAW.getId()));
        playMoneyOrder.setTradeAmount(new BigDecimal(jsonObject.getString("tradeAmount")));
        playMoneyOrder.setRealPayAmount(new BigDecimal(jsonObject.getString("avaWithdraw")));
        playMoneyOrder.setTradeType(EnumTradeType.WITHDRAW.getId());
        playMoneyOrder.setPayChannelSign(EnumPayChannelSign.SYJ_WECHAT.getId());
        playMoneyOrder.setPayer(account.getId());
        playMoneyOrder.setPayee(0);
        playMoneyOrder.setAppId(EnumAppType.HSY.getId());
        playMoneyOrder.setMerchantNo(jsonObject.getString("merchantNo"));
        playMoneyOrder.setBankName(appBizCard.getCardBank());
        playMoneyOrder.setTradeCardNo(appBizCard.getCardNO());
        //手续费
        playMoneyOrder.setPoundage(new BigDecimal(jsonObject.getString("fee")));
        playMoneyOrder.setGoodsName(account.getUserName());
        playMoneyOrder.setGoodsDescribe(orders);
        playMoneyOrder.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        playMoneyOrder.setSettleTime(new Date());
//        playMoneyOrder.setSettleType(EnumBalanceTimeType.D0.g'etType());
        playMoneyOrder.setStatus(EnumOrderStatus.WAIT_WITHDRAW.  getId());
        playMoneyOrder.setRemark("");
        this.add(playMoneyOrder);*/
        this.insert(withdrawOrder);
        return withdrawOrder.getId();
    }

}
