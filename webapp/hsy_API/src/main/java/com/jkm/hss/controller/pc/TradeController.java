package com.jkm.hss.controller.pc;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.bill.service.HSYTransactionService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.PcGenerateOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/7/4.
 */
@Slf4j
@Controller
@RequestMapping(value = "/pc/trade")
public class TradeController extends BaseController {

    @Autowired
    private HSYTransactionService  hsyTransactionService;

    /**
     * 下单
     *
     * @param generateOrderRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "generateOrder", method = RequestMethod.POST)
    public CommonResponse generateOrder(@RequestBody PcGenerateOrderRequest generateOrderRequest) {
        final long orderId = this.hsyTransactionService.createOrder2(generateOrderRequest.getShopId(), new BigDecimal(generateOrderRequest.getAmount()));
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("orderId", orderId)
                .addParam("amount", generateOrderRequest.getAmount())
                .build();
    }




}
