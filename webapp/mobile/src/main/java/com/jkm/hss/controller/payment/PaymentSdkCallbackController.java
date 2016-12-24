package com.jkm.hss.controller.payment;

import com.alibaba.fastjson.JSON;
import com.jkm.hss.bill.entity.callback.PayCallbackResponse;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by yulong.zhang on 2016/12/23.
 */
@Slf4j
@Controller
@RequestMapping(value = "/callback")
public class PaymentSdkCallbackController extends BaseController {

    @Autowired
    private PayService payService;

    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public void handlePayCallbackMsg(@RequestBody final PayCallbackResponse payCallbackResponse) {
        log.info("收到支付中心的支付回调请求，订单号[{]], 参数[{}]", payCallbackResponse.getOrderNo(), JSON.toJSONString(payCallbackResponse));
        this.payService.handlePayCallbackMsg(payCallbackResponse);
    }
}
