package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.entity.UnFrozenRecord;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.enums.EnumUnfrozenType;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.*;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.PaymentSdkDaiFuRequest;
import com.jkm.hss.bill.entity.PaymentSdkDaiFuResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.bill.service.WithdrawService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.SendMsgService;
import com.jkm.hss.merchant.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/25.
 */
@Slf4j
@Service
public class WithdrawServiceImpl implements WithdrawService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountFlowService accountFlowService;
    @Autowired
    private SettleAccountFlowService settleAccountFlowService;
    @Autowired
    private FrozenRecordService frozenRecordService;
    @Autowired
    private UnfrozenRecordService unfrozenRecordService;
    @Autowired
    private CalculateService calculateService;
    @Autowired
    private ShallProfitDetailService shallProfitDetailService;
    @Autowired
    private SplitAccountRecordService splitAccountRecordService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private SendMsgService sendMsgService;
    @Autowired
    private UserInfoService userInfoService;

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param payOrderId 交易单（支付单）id
     * @param payOrderSn 支付中心支付流水号
     * @param tradePeriod 结算周期
     * @return
     */
    @Override
    public Pair<Integer, String> merchantWithdrawByOrder(final long merchantId, final long payOrderId, final String payOrderSn, final String tradePeriod) {
        log.info("商户[{}]，对支付交易订单[{}],进行提现", merchantId, payOrderId);
        final long playMoneyOrderId = this.orderService.createPlayMoneyOrderByPayOrder(payOrderId, merchantId, tradePeriod);
        return this.withdrawByOrder(merchantId, playMoneyOrderId, payOrderSn, tradePeriod);
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantId merchantId
     * @param playMoneyOrderId
     * @param tradePeriod
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> withdrawByOrder(final long merchantId, final long playMoneyOrderId,  final String payOrderSn, final String tradePeriod) {
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        final Account account = this.accountService.getByIdWithLock(merchant.getAccountId()).get();
        final Order playMoneyOrder = this.orderService.getByIdWithLock(playMoneyOrderId).get();
        this.accountService.decreaseAvailableAmount(account.getId(), playMoneyOrder.getTradeAmount());
        this.accountService.increaseFrozenAmount(account.getId(), playMoneyOrder.getTradeAmount());
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setBusinessNo(playMoneyOrder.getOrderNo());
        frozenRecord.setFrozenAmount(playMoneyOrder.getTradeAmount());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水--减少
        this.accountFlowService.addAccountFlow(account.getId(), playMoneyOrder.getOrderNo(), playMoneyOrder.getTradeAmount(),
                "提现", EnumAccountFlowType.DECREASE);

        final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
        paymentSdkDaiFuRequest.setAppId(PaymentSdkConstants.APP_ID);
        paymentSdkDaiFuRequest.setOrderNo(playMoneyOrder.getOrderNo());
        paymentSdkDaiFuRequest.setTotalAmount(playMoneyOrder.getTradeAmount().subtract(playMoneyOrder.getPoundage()).toPlainString());
        paymentSdkDaiFuRequest.setTradeType(tradePeriod);
        paymentSdkDaiFuRequest.setIsCompany("0");
        paymentSdkDaiFuRequest.setMobile(MerchantSupport.decryptMobile(merchant.getMobile()));
        paymentSdkDaiFuRequest.setBankName(merchant.getBankName());
        paymentSdkDaiFuRequest.setAccountName(merchant.getName());
        paymentSdkDaiFuRequest.setAccountNumber(MerchantSupport.decryptBankCard(merchant.getBankNo()));
        paymentSdkDaiFuRequest.setIdCard(MerchantSupport.decryptIdentity(merchant.getIdentity()));
        paymentSdkDaiFuRequest.setPlayMoneyChannel(EnumPlayMoneyChannel.SAOMI.getId());
        paymentSdkDaiFuRequest.setNote(merchant.getMerchantName());
        paymentSdkDaiFuRequest.setSystemCode(PaymentSdkConstants.APP_ID);
        paymentSdkDaiFuRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL);
        paymentSdkDaiFuRequest.setPayOrderSn(payOrderSn);
        //请求网关
        PaymentSdkDaiFuResponse response;
        try {
            final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_WITHDRAW,
                    SdkSerializeUtil.convertObjToMap(paymentSdkDaiFuRequest));
            response = JSON.parseObject(content, PaymentSdkDaiFuResponse.class);
        } catch (final Throwable e) {
            log.error("交易订单[" + playMoneyOrder.getOrderNo() + "], 请求网关支付异常", e);
            return Pair.of(-1, "请求网关异常， 提现失败");
        }
        return this.handleWithdrawResult(playMoneyOrder.getId(), merchant.getAccountId(), response);
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    @Override
    @Transactional
    public void handleWithdrawCallbackMsg(final PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse) {
        final Order order = this.orderService.getByOrderNoAndTradeType(paymentSdkWithdrawCallbackResponse.getOrderNo(), EnumTradeType.WITHDRAW.getId()).get();
//        final MerchantInfo merchant = this.merchantInfoService.getByAccountId().get();
        if (order.isWithDrawing()) {
            this.handleWithdrawResult(order.getId(), order.getPayer(), paymentSdkWithdrawCallbackResponse);
        }
    }


    /**
     * 处理提现结果
     *
     * @param orderId
     * @param accountId
     * @param response
     * @return
     */
    private Pair<Integer, String> handleWithdrawResult(final long orderId, final long accountId,
                                                       final PaymentSdkDaiFuResponse response) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        if (order.isWithDrawing()) {
            final EnumBasicStatus status = EnumBasicStatus.of(response.getStatus());
            switch (status) {
                case SUCCESS:
                    this.markWithdrawSuccess(orderId, accountId, response);
                    return Pair.of(0, response.getMessage());
                case FAIL:
                    log.info("交易订单[{}]，提现失败", order.getOrderNo());
                    this.markWithdrawFail(orderId, accountId, response);
                    return Pair.of(-1, response.getMessage());
                case HANDLING:
                    log.info("交易订单[{}]，提现处理中", order.getOrderNo());
                    this.orderService.updateRemark(order.getId(), response.getMessage());
                    return Pair.of(1,  response.getMessage());
                default:
                    log.error("#####交易订单[{}]，返回状态异常######", order.getOrderNo());
                    break;
            }
        }
        return Pair.of(-1, response.getMessage());
    }

    /**
     * 提现成功
     *
     * @param orderId
     * @param accountId
     * @param response
     */
    private void markWithdrawSuccess(final long orderId, final long accountId,
                                     final PaymentSdkDaiFuResponse response) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        if (order.isWithDrawing()) {
            final Account account = this.accountService.getByIdWithLock(accountId).get();
            order.setStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getId());
            order.setRemark(response.getMessage());
            order.setSn(response.getSn());
            this.orderService.update(order);
            final FrozenRecord frozenRecord = this.frozenRecordService.getByBusinessNo(response.getOrderNo()).get();
            //解冻金额
            final UnFrozenRecord unFrozenRecord = new UnFrozenRecord();
            unFrozenRecord.setAccountId(account.getId());
            unFrozenRecord.setFrozenRecordId(frozenRecord.getId());
            unFrozenRecord.setBusinessNo(order.getOrderNo());
            unFrozenRecord.setUnfrozenType(EnumUnfrozenType.CONSUME.getId());
            unFrozenRecord.setUnfrozenAmount(frozenRecord.getFrozenAmount());
            unFrozenRecord.setUnfrozenTime(new Date());
            unFrozenRecord.setRemark("提现成功");
            this.unfrozenRecordService.add(unFrozenRecord);
            //减少总金额,减少冻结金额
            Preconditions.checkState(account.getFrozenAmount().compareTo(frozenRecord.getFrozenAmount()) >= 0);
            Preconditions.checkState(account.getTotalAmount().compareTo(frozenRecord.getFrozenAmount()) >= 0);
            this.accountService.decreaseFrozenAmount(accountId, frozenRecord.getFrozenAmount());
            this.accountService.decreaseTotalAmount(accountId, frozenRecord.getFrozenAmount());
            //入账到手续费账户
            this.merchantPoundageRecorded(orderId);
            final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
            if (orderOptional.get().isWithdrawSuccess() && (orderOptional.get().isDueSettle() || orderOptional.get().isSettleing())) {
                //将交易单标记为结算中
                log.info("交易订单[{}], 提现分润--结算", order.getOrderNo());
                this.orderService.updateSettleStatus(orderOptional.get().getId(), EnumSettleStatus.SETTLE_ING.getId());
                this.merchantPoundageSettle(orderOptional.get(), order.getPayer());
                //结算完毕
                this.orderService.updateSettleStatus(orderOptional.get().getId(), EnumSettleStatus.SETTLED.getId());
            }
            final MerchantInfo merchant= this.merchantInfoService.getByAccountId(order.getPayer()).get();
            final UserInfo user = userInfoService.selectByMerchantId(merchant.getId()).get();
            log.info("商户[{}], 提现单[{}], 提现成功", merchant.getId(), order.getOrderNo());
            this.sendMsgService.sendPushMessage(order.getTradeAmount(), orderOptional.get().getCreateTime(),
                    orderOptional.get().getPoundage(), merchant.getBankNoShort(), user.getOpenId());
        }
    }

    /**
     * 提现失败
     *
     * @param orderId
     * @param accountId
     * @param response
     */
    private void  markWithdrawFail(final long orderId, final long accountId,
                                  final PaymentSdkDaiFuResponse response) {
        log.error("###########【Impossible】#########提现单[{}]出现--好收收暂时不会出现的一种情况：打款最终结果，上游返回失败，不可以再次补发提现####################", orderId);
    }

    /**
     * 提现入账到手续费账户
     *
     * @param orderId
     */
    private void merchantPoundageRecorded(final long orderId) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        log.info("提现单--交易订单号[{}], 进行入账操作", order.getOrderNo());
        if (order.isWithdrawSuccess() && order.isDueSettle()) {
            //手续费账户
            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
            this.accountService.increaseSettleAmount(poundageAccount.getId(), order.getPoundage());
            this.settleAccountFlowService.addSettleAccountFlow(poundageAccount.getId(), order.getOrderNo(),
                    order.getPoundage(), "提现分润", EnumAccountFlowType.INCREASE);
        }
    }

    /**
     * 商户提现结算
     *
     * @param order
     * @param accountId
     */
    @Override
    @Transactional
    public void merchantPoundageSettle(final Order order, final long accountId) {
        final MerchantInfo merchant = this.merchantInfoService.getByAccountId(accountId).get();
        final Map<String, Triple<Long, BigDecimal, String>> shallProfitMap =
                this.shallProfitDetailService.withdrawProfitCount(order.getOrderNo(), order.getTradeAmount(), order.getPayChannelSign(), merchant.getId());
        final Triple<Long, BigDecimal, String> channelMoneyTriple = shallProfitMap.get("channelMoney");
        final Triple<Long, BigDecimal, String> productMoneyTriple = shallProfitMap.get("productMoney");
        final Triple<Long, BigDecimal, String> firstMoneyTriple = shallProfitMap.get("firstMoney");
        final Triple<Long, BigDecimal, String> secondMoneyTriple = shallProfitMap.get("secondMoney");
        final BigDecimal channelMoney = null == channelMoneyTriple ? new BigDecimal("0") : channelMoneyTriple.getMiddle();
        final BigDecimal productMoney = null == productMoneyTriple ? new BigDecimal("0") : productMoneyTriple.getMiddle();
        final BigDecimal firstMoney = null == firstMoneyTriple ? new BigDecimal("0") : firstMoneyTriple.getMiddle();
        final BigDecimal secondMoney = null == secondMoneyTriple ? new BigDecimal("0") : secondMoneyTriple.getMiddle();
        Preconditions.checkState(order.getPoundage().compareTo(channelMoney.add(productMoney).add(firstMoney).add(secondMoney)) >= 0);
        //手续费账户结算
        final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(order.getPoundage().compareTo(poundageAccount.getDueSettleAmount()) <= 0);
        //待结算--可用余额
        this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseSettleAmount(poundageAccount.getId(), order.getPoundage());
        this.settleAccountFlowService.addSettleAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "提现分润", EnumAccountFlowType.DECREASE);
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "提现分润", EnumAccountFlowType.INCREASE);
        //分账
        final Account poundageAccount1 = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
        Preconditions.checkState(poundageAccount1.getAvailable().compareTo(channelMoney.add(productMoney).add(firstMoney).add(secondMoney)) >= 0);
        this.accountService.decreaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
        this.accountService.decreaseTotalAmount(poundageAccount.getId(), order.getPoundage());
        this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
                "提现分润", EnumAccountFlowType.DECREASE);

        //增加分账记录
        //通道利润--到结算--到可用余额
        if (null != channelMoneyTriple) {
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), channelMoneyTriple, "通道账户", EnumTradeType.WITHDRAW.getValue());
            final Account account = this.accountService.getById(channelMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), channelMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.DECREASE);
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), channelMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);
        }
        //产品利润--到结算--可用余额
        if (null != productMoneyTriple) {
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), productMoneyTriple, "产品账户", EnumTradeType.PAY.getValue());
            final Account account = this.accountService.getById(productMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), productMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), productMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), productMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.DECREASE);
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), productMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);
        }
        //一级代理商利润--到结算--可用余额
        if (null != firstMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(firstMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), firstMoneyTriple, dealer.getProxyName(), EnumTradeType.PAY.getValue());
            final Account account = this.accountService.getById(firstMoneyTriple.getLeft()).get();
            this.accountService.increaseTotalAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), firstMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.DECREASE);
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), firstMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);
        }
        //二级代理商利润--到结算--可用余额
        if (null != secondMoneyTriple) {
            final Dealer dealer = this.dealerService.getByAccountId(secondMoneyTriple.getLeft()).get();
            final Account account = this.accountService.getById(secondMoneyTriple.getLeft()).get();
            this.splitAccountRecordService.addWithdrawSplitAccountRecord(order.getOrderNo(), order.getOrderNo(),
                    order.getTradeAmount(), order.getPoundage(), secondMoneyTriple, dealer.getProxyName(), EnumTradeType.PAY.getValue());
            this.accountService.increaseTotalAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.increaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.INCREASE);

            //待结算--可用余额
            this.accountService.increaseAvailableAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.accountService.decreaseSettleAmount(account.getId(), secondMoneyTriple.getMiddle());
            this.settleAccountFlowService.addSettleAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "提现分润", EnumAccountFlowType.DECREASE);
            this.accountFlowService.addAccountFlow(account.getId(), order.getOrderNo(), secondMoneyTriple.getMiddle(),
                    "分润", EnumAccountFlowType.INCREASE);
        }
    }
}
