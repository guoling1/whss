package com.jkm.hss.controller.wx;

import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.service.OrderRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-12 13:40
 */
@Slf4j
@Controller
@RequestMapping(value = "/time")
public class WxTimeController extends BaseController {
    @Autowired
    private OrderRecordService orderRecordService;

    /**
     * 定时查询退款状态
     */
    @RequestMapping(value = "regularlyCheckOtherPayResult", method = RequestMethod.POST)
    public void regularlyCheckOtherPayResult() {
        log.info("开始查询退款状态。。。");
        orderRecordService.regularlyCheckOtherPayResult();
        log.info("结束查询退款状态。。。");
    }
    /**
     * 定时查询支付状态
     */
    @RequestMapping(value = "regularlyCheckPayResult", method = RequestMethod.POST)
    public void regularlyCheckPayResult() {
        log.info("开始查询退款状态。。。");
        orderRecordService.regularlyCheckPayResult();
        log.info("结束查询退款状态。。。");
    }
}
