package com.jkm.hss.controller.trade;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.bill.service.PayService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.DynamicCodeReceiptRequest;
import com.jkm.hss.helper.request.StaticCodeReceiptRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by yulong.zhang on 2017/1/16.
 */
@Slf4j
@Controller
@RequestMapping(value = "/trade")
public class TradeController extends BaseController {
    @Autowired
    private PayService payService;



    /**
     * 动态码支付
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "dcReceipt", method = RequestMethod.POST)
    public CommonResponse dynamicCodeReceipt(@RequestBody final DynamicCodeReceiptRequest dynamicCodeReceiptRequest) throws UnsupportedEncodingException {



        final Pair<Integer, String> resultPair = this.payService.codeReceipt(dynamicCodeReceiptRequest.getTotalFee(),
                dynamicCodeReceiptRequest.getPayChannel(), 0, EnumAppType.HSY.getId());
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "收款成功")
                    .addParam("payUrl", URLDecoder.decode(resultPair.getRight(), "UTF-8")).addParam("subMerName", "")
                    .addParam("amount", dynamicCodeReceiptRequest.getTotalFee()).build();
        }
        return CommonResponse.simpleResponse(-1, resultPair.getRight());

    }



    /**
     * 静态码收款
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "scReceipt", method = RequestMethod.POST)
    public CommonResponse staticCodeReceipt(@RequestBody final StaticCodeReceiptRequest staticCodeReceiptRequest) throws UnsupportedEncodingException {


        final Pair<Integer, String> resultPair = this.payService.codeReceipt(staticCodeReceiptRequest.getTotalFee(),
                staticCodeReceiptRequest.getPayChannel(), 0, EnumAppType.HSY.getId());
        if (0 == resultPair.getLeft()) {
            return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "收款成功")
                    .addParam("payUrl", URLDecoder.decode(resultPair.getRight(), "UTF-8")).addParam("subMerName", "")
                    .addParam("amount", staticCodeReceiptRequest.getTotalFee()).build();
        }
        return CommonResponse.simpleResponse(-1, resultPair.getRight());
    }
}
