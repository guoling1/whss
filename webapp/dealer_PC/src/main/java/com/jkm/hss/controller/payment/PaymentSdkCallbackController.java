package com.jkm.hss.controller.payment;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.callback.PaymentSdkPayCallbackResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkRefundCallbackResponse;
import com.jkm.hss.bill.entity.callback.PaymentSdkWithdrawCallbackResponse;
import com.jkm.hss.bill.service.DealerWithdrawService;
import com.jkm.hss.bill.service.HSYTradeService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xiang.yu on 2016/12/23.
 */
@Slf4j
@Controller
@RequestMapping(value = "/callback")
public class PaymentSdkCallbackController extends BaseController {

    @Autowired
    private DealerWithdrawService dealerWithdrawService;

    /**
     * 提现回调
     *
     * @param paymentSdkWithdrawCallbackResponse
     */
    @ResponseBody
    @RequestMapping(value = "withdraw", method = RequestMethod.POST)
    public Object handleWithdrawCallbackMsg(@RequestBody final PaymentSdkWithdrawCallbackResponse paymentSdkWithdrawCallbackResponse) {
        log.info("收到支付中心的提现回调请求，订单号[{}], 参数[{}]", paymentSdkWithdrawCallbackResponse.getOrderNo(), JSON.toJSONString(paymentSdkWithdrawCallbackResponse));

        this.dealerWithdrawService.handleDealerWithdrawCallbackMsg(paymentSdkWithdrawCallbackResponse);


        return "success";
    }



}
