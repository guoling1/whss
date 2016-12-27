package com.jkm.hss.controller.wx;

import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.service.OrderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-14 15:37
 */
@Slf4j
@Controller
@RequestMapping(value = "/call")
public class CallBackController extends BaseController {
    //
    @Autowired
    private OrderRecordService orderRecordService;
    /**
     * 定时查询退款状态
     */
    @RequestMapping(value = "otherPayBack", method = RequestMethod.POST)
    public void otherPayBack(final HttpServletRequest request, final HttpServletResponse response) {
        String outTradeNo = request.getParameter("outTradeNo");
        String tradeResult = request.getParameter("tradeResult");
        String payNum = request.getParameter("orderId");
        String bankStatus = request.getParameter("bankStatus");
        orderRecordService.otherPayResult(outTradeNo,tradeResult,bankStatus,payNum);
    }
}
