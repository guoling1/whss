package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.spring.http.client.HttpClientFacade;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.entity.Account;
import com.jkm.hss.account.entity.AccountFlow;
import com.jkm.hss.account.entity.FrozenRecord;
import com.jkm.hss.account.entity.UnfrozenRecord;
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
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
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
    private HttpClientFacade httpClientFacade;
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
    private DealerService dealerService;

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param tradePeriod 结算周期
     * @return
     */
    @Override
    @Transactional
    public Pair<Integer, String> merchantWithdraw(final long merchantId, final String tradePeriod) {
        log.info("商户[{}]，进行提现", merchantId);
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        final Account account = this.accountService.getByIdWithLock(merchant.getId()).get();
        if (account.getAvailable().compareTo(new BigDecimal("2.00")) < 0) {
            log.info("商户[{}]账户余额不足2元", merchant.getId());
            return Pair.of(-1, "余额不足");
        }
        final Order order = new Order();
        order.setOrderNo(SnGenerator.generate());
        order.setTradeAmount(account.getAvailable());
        order.setTradeType(EnumTradeType.WITHDRAW.getId());
        order.setPayer(merchantId);
        order.setPayee(0);
        order.setPayAccount(tradePeriod);
        BigDecimal merchantWithdrawPoundage = this.calculateService.getMerchantWithdrawPoundage(merchantId, 1);
        order.setPoundage(merchantWithdrawPoundage);
        order.setGoodsName(merchant.getMerchantName());
        order.setGoodsDescribe(merchant.getMerchantName());
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleTime(new Date());
        order.setStatus(EnumOrderStatus.WITHDRAWING.getId());
        this.orderService.add(order);
        //冻结金额
        this.accountService.decreaseAvailableAmount(account.getId(), account.getAvailable());
        this.accountService.increaseFrozenAmount(account.getId(), account.getAvailable());
        final FrozenRecord frozenRecord = new FrozenRecord();
        frozenRecord.setAccountId(account.getId());
        frozenRecord.setBusinessNo(order.getOrderNo());
        frozenRecord.setFrozenAmount(account.getAvailable());
        frozenRecord.setFrozenTime(new Date());
        frozenRecord.setRemark("提现");
        this.frozenRecordService.add(frozenRecord);
        //添加账户流水--减少
        this.accountFlowService.addAccountFlow(account, order.getOrderNo(), order.getTradeAmount(),
                order.getGoodsName(), EnumAccountFlowType.DECREASE);

        final PaymentSdkDaiFuRequest paymentSdkDaiFuRequest = new PaymentSdkDaiFuRequest();
        paymentSdkDaiFuRequest.setAppId(PaymentSdkConstants.APP_ID);
        paymentSdkDaiFuRequest.setOrderNo(order.getOrderNo());
        paymentSdkDaiFuRequest.setTotalAmount(order.getTradeAmount().subtract(order.getPoundage()).toPlainString());
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

        //请求三方
        final String content = this.requestImpl(PaymentSdkConstants.SDK_PAY_WITHDRAW, SdkSerializeUtil.convertObjToMap(paymentSdkDaiFuRequest));
        final PaymentSdkDaiFuResponse response = JSON.parseObject(content, PaymentSdkDaiFuResponse.class);
        return this.handleWithdrawResult(order.getId(), merchant.getAccountId(), response);
    }

    /**
     * {@inheritDoc}
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    @Override
    @Transactional
    public void handleWithdrawCallbackMsg(final PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse) {
        final Order order = this.orderService.getByOrderNo(paymentSdkWithdrawCallbackResponse.getOrderNo()).get();
        final MerchantInfo merchant = this.merchantInfoService.selectById(order.getPayer()).get();
        if (order.isWithDrawing()) {
            this.handleWithdrawResult(order.getId(), merchant.getAccountId(), paymentSdkWithdrawCallbackResponse);
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
                    return Pair.of(0, "提现成功");
                case FAIL:
                    order.setRemark(response.getMessage());
                    this.orderService.update(order);
                    log.info("交易订单[{}]，提现暂未成功", order.getOrderNo());
                    break;
                default:
                    order.setRemark(response.getMessage());
                    this.orderService.update(order);
                    log.info("交易订单[{}]，提现暂未成功", order.getOrderNo());
                    break;
            }
        }
        return Pair.of(-1, "提现中");
    }

    private void markWithdrawSuccess(final long orderId, final long accountId,
                                     final PaymentSdkDaiFuResponse response) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        if (order.isWithDrawing()) {
            final Account account = this.accountService.getByIdWithLock(accountId).get();
            order.setStatus(EnumOrderStatus.WITHDRAW_SUCCESS.getId());
            order.setRemark(response.getMessage());
            this.orderService.update(order);
            final FrozenRecord frozenRecord = this.frozenRecordService.getByBusinessNo(response.getOrderNo()).get();
            //解冻金额
            final UnfrozenRecord unfrozenRecord = new UnfrozenRecord();
            unfrozenRecord.setAccountId(account.getId());
            unfrozenRecord.setFrozenRecordId(frozenRecord.getId());
            unfrozenRecord.setBusinessNo(order.getOrderNo());
            unfrozenRecord.setUnfrozenType(EnumUnfrozenType.CONSUME.getId());
            unfrozenRecord.setUnfrozenAmount(frozenRecord.getFrozenAmount());
            unfrozenRecord.setUnfrozenTime(new Date());
            unfrozenRecord.setRemark("提现成功");
            this.unfrozenRecordService.add(unfrozenRecord);
            //减少总金额,减少冻结金额
            Preconditions.checkState(account.getFrozenAmount().compareTo(frozenRecord.getFrozenAmount()) >= 0);
            Preconditions.checkState(account.getTotalAmount().compareTo(frozenRecord.getFrozenAmount()) >= 0);
            this.accountService.decreaseFrozenAmount(accountId, frozenRecord.getFrozenAmount());
            this.accountService.decreaseTotalAmount(accountId, frozenRecord.getFrozenAmount());
            //入账到手续费账户
            this.merchantPoundageRecorded(orderId);
            final Optional<Order> orderOptional = this.orderService.getByIdWithLock(order.getId());
            if (orderOptional.get().isPaySuccess() && (orderOptional.get().isDueSettle() || orderOptional.get().isSettleing())) {
                //将交易单标记为结算中
                this.orderService.updateSettleStatus(orderOptional.get().getId(), EnumSettleStatus.SETTLE_ING.getId());
                this.merchantPoundageSettle(orderOptional.get(), order.getPayer());
            }
            //结算成功？
        }
    }

    /**
     * 提现入账到手续费账户
     *
     * @param orderId
     */
    private void merchantPoundageRecorded(final long orderId) {
        final Order order = this.orderService.getByIdWithLock(orderId).get();
        log.info("交易订单号[{}], 进行入账操作", order.getOrderNo());
        if (order.isPaySuccess() && order.isDueSettle()) {
            //手续费账户
            final Account poundageAccount = this.accountService.getByIdWithLock(AccountConstants.POUNDATE_ACCOUNT_ID).get();
            this.settleAccountFlowService.addSettleAccountFlow(poundageAccount, order.getOrderNo(),
                    order.getPoundage(), order.getGoodsName(), EnumAccountFlowType.INCREASE);
            this.accountService.increaseTotalAmount(poundageAccount.getId(), order.getPoundage());
            this.accountService.increaseSettleAmount(poundageAccount.getId(), order.getPoundage());
        }
    }

    /**
     * 商户提现结算
     *
     * @param order
     * @param merchantId
     */
    @Override
    @Transactional
    public void merchantPoundageSettle(final Order order, final long merchantId) {
        if (order.isPaySuccess() && (order.isDueSettle() || order.isSettleing())) {

        }
    }


    /**
     * 发送http请求
     *
     * @param url
     * @param paramMap
     * @return
     */
    private String requestImpl(String url, Map<String, String> paramMap) {
        try {
            final String responseContent = this.httpClientFacade.post(url, paramMap);
            return responseContent;
        } catch (final Throwable e) {
            log.error("url[{}], param[{}]请求支付中心异常", url, paramMap);
            throw e;
        }
    }
}
