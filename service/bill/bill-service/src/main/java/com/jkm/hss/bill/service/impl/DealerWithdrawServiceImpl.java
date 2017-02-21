package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.entity.UnFrozenRecord;
import com.jkm.hss.account.enums.EnumUnfrozenType;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.account.sevice.FrozenRecordService;
import com.jkm.hss.account.sevice.UnfrozenRecordService;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.PaymentSdkDaiFuRequest;
import com.jkm.hss.bill.entity.PaymentSdkDaiFuResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.enums.EnumBasicStatus;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.product.enums.EnumUpperChannel;
import com.jkm.hss.bill.enums.EnumTradeType;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.helper.SdkSerializeUtil;
import com.jkm.hss.bill.service.DealerWithdrawService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yuxiang on 2017-02-15.
 */
@Slf4j
@Service
public class DealerWithdrawServiceImpl implements DealerWithdrawService {

    @Autowired
    private DealerService dealerService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private FrozenRecordService frozenRecordService;
    @Autowired
    private UnfrozenRecordService unfrozenRecordService;

    /**
     * {@inheritDoc}
     * @param accountId
     * @param totalAmount
     * @param channel
     * @param appId
     * @return
     */
    @Transactional
    @Override
    public Pair<Integer, String> withdraw(long accountId, String totalAmount, int channel, String appId) {
        Preconditions.checkState(EnumPayChannelSign.YG_WEIXIN.getId() == channel
                || EnumPayChannelSign.YG_ZHIFUBAO.getId() == channel
                || EnumPayChannelSign.YG_UNIONPAY.getId() == channel, "渠道选择错误[{}]", channel);

        final Optional<Dealer> dealerOptional = this.dealerService.getByAccountId(accountId);
        Preconditions.checkArgument(dealerOptional.isPresent(), "代理商不存在");
        final Dealer dealer = dealerOptional.get();
        final long playMoneyOrderId = this.orderService.createDealerPlayMoneyOrder(dealer, new BigDecimal(totalAmount),
                appId, channel, EnumBalanceTimeType.D0.getType());
        final Order playMoneyOrder = this.orderService.getByIdWithLock(playMoneyOrderId).get();
        if (playMoneyOrder.isWithDrawing()) {
            final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
            paymentSdkDaiFuRequest.setAppId(playMoneyOrder.getAppId());
            paymentSdkDaiFuRequest.setOrderNo(playMoneyOrder.getOrderNo());
            paymentSdkDaiFuRequest.setTotalAmount(playMoneyOrder.getTradeAmount().subtract(playMoneyOrder.getPoundage()).toPlainString());
            // TODO
            paymentSdkDaiFuRequest.setTradeType("D0");
            paymentSdkDaiFuRequest.setIsCompany("0");
            paymentSdkDaiFuRequest.setMobile(DealerSupport.decryptMobile(dealer.getId() , dealer.getBankReserveMobile()));
            paymentSdkDaiFuRequest.setBankName(dealer.getBankName());
            paymentSdkDaiFuRequest.setAccountName(dealer.getBankAccountName());
            paymentSdkDaiFuRequest.setAccountNumber(DealerSupport.decryptBankCard(dealer.getId(),dealer.getSettleBankCard()));
            paymentSdkDaiFuRequest.setIdCard(DealerSupport.decryptIdentity(dealer.getId(), dealer.getIdCard()));
            paymentSdkDaiFuRequest.setPlayMoneyChannel(EnumUpperChannel.SAOMI.getId());
            paymentSdkDaiFuRequest.setNote(dealer.getProxyName());
            paymentSdkDaiFuRequest.setSystemCode(playMoneyOrder.getAppId());
            paymentSdkDaiFuRequest.setNotifyUrl(PaymentSdkConstants.SDK_PAY_WITHDRAW_NOTIFY_URL);
            paymentSdkDaiFuRequest.setPayOrderSn("");
            //请求网关
            PaymentSdkDaiFuResponse response = null;
            try {
                final String content = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_WITHDRAW,
                        SdkSerializeUtil.convertObjToMap(paymentSdkDaiFuRequest));
                response = JSON.parseObject(content, PaymentSdkDaiFuResponse.class);
            } catch (final Throwable e) {
                log.error("交易订单[" + playMoneyOrder.getOrderNo() + "], 请求网关支付异常", e);
            }
            return this.handleWithdrawResult(playMoneyOrderId, 0, response);
        }
        return Pair.of(-1, "提现失败");
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
                    log.info("交易订单[{}]，提现成功", order.getOrderNo());
                    this.markWithdrawSuccess(orderId, accountId, response);
                    return Pair.of(0, response.getMessage());
                case FAIL:
                    log.info("交易订单[{}]，提现失败", order.getOrderNo());
                    this.markWithdrawFail(orderId, accountId, response);
                    return Pair.of(-1, response.getMessage());
                case HANDLING:
                    log.info("交易订单[{}]，提现处理中", order.getOrderNo());
                    this.orderService.updateRemark(order.getId(), response.getMessage());
                    return Pair.of(0,  response.getMessage());
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
            //提现手续费入账
            final Account dealerPoundageAccount1 = this.accountService.getById(AccountConstants.DEALER_POUNDAGE_ACCOUNT_ID).get();
            dealerPoundageAccount1.setAvailable(dealerPoundageAccount1.getAvailable().add(order.getPoundage()));
            dealerPoundageAccount1.setTotalAmount(dealerPoundageAccount1.getTotalAmount().add(order.getPoundage()));
            this.accountService.update(dealerPoundageAccount1);

            //入账到手续费账户
//            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDAGE_ACCOUNT_ID).get();
//            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
//            this.accountService.increaseAvailableAmount(poundageAccount.getId(), order.getPoundage());
//            this.accountFlowService.addAccountFlow(poundageAccount.getId(), order.getOrderNo(), order.getPoundage(),
//                    "提现分润", EnumAccountFlowType.INCREASE);
//            this.withdrawSplitAccount(this.orderService.getByIdWithLock(orderId).get(), shop);
            //推送
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
        log.error("###########【Impossible】#########提现单[{}]提现失败####################", orderId);
    }
    /**
     * {@inheritDoc}
     * @param paymentSdkWithdrawCallbackResponse
     */
    @Transactional
    @Override
    public void handleDealerWithdrawCallbackMsg(PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse) {

        final Order order = this.orderService.getByOrderNoAndTradeType(paymentSdkWithdrawCallbackResponse.getOrderNo(), EnumTradeType.WITHDRAW.getId()).get();
        if (order.isWithDrawing()) {
            this.handleWithdrawResult(order.getId(), order.getPayer(), paymentSdkWithdrawCallbackResponse);
        }
    }
}
