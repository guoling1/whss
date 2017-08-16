package com.jkm.api.service.impl;

import com.google.common.base.Optional;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.requestparam.PreQuickPayRequest;
import com.jkm.api.service.QuickPayService;
import com.jkm.base.common.util.SnGenerator;
import com.jkm.hss.account.enums.EnumBankType;
import com.jkm.hss.bill.entity.BusinessOrder;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.enums.*;
import com.jkm.hss.bill.service.BusinessOrderService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.BankCardBin;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.BankCardBinService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.BasicChannelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/8/15.
 */
@Slf4j
@Service
public class QuickPayServiceImpl implements QuickPayService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private AccountBankService accountBankService;
    @Autowired
    private BankCardBinService bankCardBinService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private BusinessOrderService businessOrderService;

    /**
     * {@inheritDoc}
     *
     * @param request
     */
    @Override
    public void preQuickPay(final PreQuickPayRequest request) {
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.getByMarkCode(request.getMerchantNo());
        if (!merchantInfoOptional.isPresent()) {
            log.error("商户编号[{}]-商户订单号[{}]，预下单，商户不存在", request.getMerchantNo(), request.getOrderNo());
            throw new JKMTradeServiceException(JKMTradeErrorCode.MERCHANT_NOT_EXIST);
        }
        final Optional<BusinessOrder> businessOrderOptional = this.businessOrderService.getByOrderNo(request.getOrderNo());
        if (businessOrderOptional.isPresent()) {
            log.error("商户编号[{}]-商户订单号[{}]，预下单，商户订单号重复", request.getMerchantNo(), request.getOrderNo());
            throw new JKMTradeServiceException(JKMTradeErrorCode.MERCHANT_TRADE_NO_EXIST);
        }
        final EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.codeOf(request.getChannelCode());
        final MerchantInfo merchant = merchantInfoOptional.get();
        final Optional<BankCardBin> bankCardBinOptional = this.bankCardBinService.analyseCardNo(request.getCardNo());
        if (!bankCardBinOptional.isPresent()) {
            log.error("商户编号[{}]-商户订单号[{}]，预下单，商户银行卡号错误", request.getMerchantNo(), request.getOrderNo());
            throw new JKMTradeServiceException(JKMTradeErrorCode.NOT_SUPORT_CARD);
        }
        final BankCardBin bankCardBin = bankCardBinOptional.get();
        AccountBank accountBank;
        if (!EnumPayChannelSign.isNotNeedSaveBankCard(enumPayChannelSign.getId())) {
            final long creditBankCardId = this.accountBankService.initCreditBankCard(merchant.getAccountId(), request.getCardNo(),
                    bankCardBin.getBankName(), request.getMobile(), bankCardBin.getShorthand(), request.getExpireDate(), request.getCvv());
            accountBank = this.accountBankService.selectStatelessById(creditBankCardId).get();
        } else {
            final Optional<AccountBank> accountBankOptional = this.accountBankService.selectCreditCardByBankNo(merchant.getAccountId(), request.getCardNo());
            if (!accountBankOptional.isPresent()) {
                log.error("商户编号[{}]-商户订单号[{}]，预下单-银联报备后，数据库商户银行卡号不存在", request.getMerchantNo(), request.getOrderNo());
                throw new JKMTradeServiceException(JKMTradeErrorCode.NOT_SUPORT_CARD);
            }
            accountBank = accountBankOptional.get();
        }
        final BusinessOrder businessOrder = new BusinessOrder();
        businessOrder.setAppId("hss");
        businessOrder.setOrderNo(request.getOrderNo());
        businessOrder.setTradeAmount(new BigDecimal(request.getOrderAmount()));
        businessOrder.setMerchantId(merchant.getId());
        businessOrder.setGoodsName(StringUtils.isEmpty(request.getSubject()) ? merchant.getMerchantName() : request.getSubject());
        businessOrder.setGoodsDescribe(merchant.getMerchantName());
        businessOrder.setRemark("创建订单成功");
        businessOrder.setStatus(EnumBusinessOrderStatus.DUE_PAY.getId());
        businessOrder.setTradeOrderNo("");
        businessOrder.setPayChannelSign(enumPayChannelSign.getId());

        this.businessOrderService.add(businessOrder);

        final Order order = new Order();
        order.setBusinessOrderNo(businessOrder.getOrderNo());
        order.setOrderNo(SnGenerator.generateSn(EnumTradeType.PAY.getId()));
        order.setTradeAmount(businessOrder.getTradeAmount());
        order.setRealPayAmount(businessOrder.getTradeAmount());
        order.setAppId("hss");
        order.setTradeType(EnumTradeType.PAY.getId());
        order.setServiceType(EnumServiceType.RECEIVE_MONEY.getId());
        order.setPayer(0);
        order.setPayee(merchant.getAccountId());
        order.setGoodsName(merchant.getMerchantName());
        order.setGoodsDescribe(merchant.getMerchantName());
        order.setPayType(enumPayChannelSign.getCode());
        order.setPayChannelSign(enumPayChannelSign.getId());
        order.setChildChannelSign(enumPayChannelSign.getId());
        order.setPayBankCard(accountBank.getBankNo());
        order.setSettleStatus(EnumSettleStatus.DUE_SETTLE.getId());
        order.setSettleType(enumPayChannelSign.getSettleType().getType());
        order.setStatus(EnumOrderStatus.DUE_PAY.getId());
        order.setPayAccount(merchant.getName());
        order.setTradeCardNo(accountBank.getBankNo());
        order.setTradeCardType(EnumBankType.CREDIT_CARD.getId());
        order.setBankName(accountBank.getBankName());
        this.orderService.add(order);
        final BusinessOrder updateBusinessOrder = new BusinessOrder();
//        updateBusinessOrder.setId(businessOrderId);
        updateBusinessOrder.setTradeOrderNo(order.getOrderNo());
//        updateBusinessOrder.setPayChannelSign(parentChannelSign);
        updateBusinessOrder.setRemark("请求交易成功");
        updateBusinessOrder.setTradeCardNo(accountBank.getBankNo());
        updateBusinessOrder.setTradeCardType(EnumBankType.CREDIT_CARD.getId());
        updateBusinessOrder.setPayAccount(merchant.getName());
        updateBusinessOrder.setBankName(accountBank.getBankName());
        this.businessOrderService.update(updateBusinessOrder);


    }
}
