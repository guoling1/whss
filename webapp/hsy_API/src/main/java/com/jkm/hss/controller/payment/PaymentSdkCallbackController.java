package com.jkm.hss.controller.payment;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkRefundCallbackResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.service.*;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.notifier.enums.EnumNoticeType;
import com.jkm.hss.notifier.enums.EnumUserType;
import com.jkm.hss.notifier.helper.SendMessageParams;
import com.jkm.hss.notifier.service.SendMessageService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
@Slf4j
@Controller
@RequestMapping(value = "/callback")
public class PaymentSdkCallbackController extends BaseController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private HSYTradeService hsyTradeService;
    @Autowired
    private DealerWithdrawService dealerWithdrawService;
    @Autowired
    private TradeService tradeService;

    /**
     * 支付回调
     *
     * @param paymentSdkPayCallbackResponse
     */
    @ResponseBody
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public Object handlePayCallbackMsg(@RequestBody final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse) {
        log.info("收到支付中心的支付回调请求，订单号[{}], 参数[{}]", paymentSdkPayCallbackResponse.getOrderNo(), paymentSdkPayCallbackResponse);
        this.tradeService.handlePayOrRechargeCallbackMsg(paymentSdkPayCallbackResponse);
//        this.hsyTradeService.handleHsyPayCallbackMsg(paymentSdkPayCallbackResponse);
        return "success";
    }

    /**
     * 提现回调
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    @ResponseBody
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public Object handleWithdrawCallbackMsg(@RequestBody final PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse) {
        log.info("收到支付中心的提现回调请求，订单号[{}], 参数[{}]", paymentSdkWithdrawCallbackResponse.getOrderNo(), JSON.toJSONString(paymentSdkWithdrawCallbackResponse));
        final Order order = this.orderService.getByOrderNo(paymentSdkWithdrawCallbackResponse.getOrderNo()).get();
        if (order.getAppId().equals(EnumAppType.HSY.getId())){
            this.hsyTradeService.handleHsyWithdrawCallbackMsg(paymentSdkWithdrawCallbackResponse);
        }else{
            this.dealerWithdrawService.handleDealerWithdrawCallbackMsg(paymentSdkWithdrawCallbackResponse);
        }

        return "success";
    }


    /**
     * 退款回调
     *
     * @param paymentSdkRefundCallbackResponse
     */
    @ResponseBody
    @RequestMapping(value = "refund", method = RequestMethod.POST)
    public Object handleRefundCallbackMsg(@RequestBody final PaymentSdkRefundCallbackResponse paymentSdkRefundCallbackResponse) {
        log.info("收到支付中心的重复支付退款回调请求，订单号[{}], 流水号[{}], 参数[{}]", paymentSdkRefundCallbackResponse.getOrderNo(),
                paymentSdkRefundCallbackResponse.getSn(), paymentSdkRefundCallbackResponse);
        final Optional<Order> orderOptional = this.orderService.getByOrderNo(paymentSdkRefundCallbackResponse.getOrderNo());

        return "success";
    }

}
