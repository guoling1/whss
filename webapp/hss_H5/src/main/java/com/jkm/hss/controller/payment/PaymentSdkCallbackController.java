package com.jkm.hss.controller.payment;

import com.alibaba.fastjson.JSON;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.bill.service.WithdrawService;
import com.jkm.hss.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
@Slf4j
@Controller
@RequestMapping(value = "/callback")
public class PaymentSdkCallbackController extends BaseController {

    @Autowired
    private PayService payService;

    @Autowired
    private WithdrawService withdrawService;

    /**
     * 支付回调
     *
     * @param paymentSdkPayCallbackResponse
     */
    @ResponseBody
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public Object handlePayCallbackMsg(@RequestBody final PaymentSdkPayCallbackResponse paymentSdkPayCallbackResponse) {
        log.info("收到支付中心的支付回调请求，订单号[{]], 参数[{}]", paymentSdkPayCallbackResponse.getOrderNo(), paymentSdkPayCallbackResponse);
        this.payService.handlePayCallbackMsg(paymentSdkPayCallbackResponse);
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
        log.info("收到支付中心的提现回调请求，订单号[{]], 参数[{}]", paymentSdkWithdrawCallbackResponse.getOrderNo(), JSON.toJSONString(paymentSdkWithdrawCallbackResponse));
        this.withdrawService.handleWithdrawCallbackMsg(paymentSdkWithdrawCallbackResponse);
        return "success";
    }

}
