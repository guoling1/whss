package com.jkm.hss.controller.hssapi;

import com.alibaba.fastjson.JSON;
import com.jkm.api.helper.requestparam.PreQuickPayRequest;
import com.jkm.api.helper.responseparam.PreQuickPayResponse;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.hss.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Created by yulong.zhang on 2017/8/15.
 */
@Slf4j
@Controller
@RequestMapping(value = "/api")
public class HssApiController extends BaseController {

    /**
     * 快捷预下单
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "pay", method = RequestMethod.POST)
    public Object preQuickPay(final HttpServletRequest httpServletRequest) {
        final PreQuickPayResponse preQuickPayResponse = new PreQuickPayResponse();
        String readParam;
        try {
            readParam = super.read(httpServletRequest);
        } catch (final IOException e) {
            log.error("商户号[{}]-商户订单号[{}]-预下单读取数据流异常", e);

            return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
        }
        try {
            final PreQuickPayRequest request = JSON.parseObject(readParam, PreQuickPayRequest.class);
            request.validateParam();


            if (request.verifySign("")) {
                log.error("商户号[{}]-商户订单号[{}]-预下单签名错误", 1, 2);
                return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
            }


        } catch (final Throwable e) {

        }


        return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
    }
}
