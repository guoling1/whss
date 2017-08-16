package com.jkm.hss.controller.hssapi;

import com.alibaba.fastjson.JSON;
import com.jkm.api.enums.EnumApiOrderSettleStatus;
import com.jkm.api.enums.EnumApiOrderStatus;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.requestparam.PreQuickPayRequest;
import com.jkm.api.helper.responseparam.PreQuickPayResponse;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.service.QuickPayService;
import com.jkm.hss.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private QuickPayService quickPayService;

    /**
     * 快捷预下单
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "preQuickPay", method = RequestMethod.POST)
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
            log.info("商户号[{}]-商户订单号[{}]-预下单-参数[{}]", request.getMerchantNo(), request.getOrderNo(), request);
            preQuickPayResponse.setMerchantNo(request.getMerchantNo());
            preQuickPayResponse.setOrderNo(request.getOrderNo());
            preQuickPayResponse.setMerchantReqTime(request.getMerchantReqTime());
            preQuickPayResponse.setOrderAmount(request.getOrderAmount());
            preQuickPayResponse.setCardNo(request.getCardNo());
            //参数校验
            request.validateParam();
            if (request.verifySign("")) {
                log.error("商户号[{}]-商户订单号[{}]-预下单签名错误", 1, 2);
                preQuickPayResponse.setReturnCode(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorCode());
                preQuickPayResponse.setReturnMsg(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorMessage());
                preQuickPayResponse.setSign(preQuickPayResponse.createSign(""));
                return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
            }
            //请求
            this.quickPayService.preQuickPay(request);

            preQuickPayResponse.setOrderStatus(EnumApiOrderStatus.INIT.getCode());
            preQuickPayResponse.setSettleStatus(EnumApiOrderSettleStatus.WAIT.getCode());
            preQuickPayResponse.setReturnCode(JKMTradeErrorCode.ACCEPT_SUCCESS.getErrorCode());
            preQuickPayResponse.setReturnMsg(JKMTradeErrorCode.ACCEPT_SUCCESS.getErrorMessage());
        } catch (final JKMTradeServiceException e) {
            log.error("商户号[{}]-商户订单号[{}]-预下单异常", e);
            preQuickPayResponse.setReturnCode(e.getErrorCode());
            preQuickPayResponse.setReturnCode(e.getErrorMessage());
        } catch (final Throwable e) {
            log.error("商户号[{}]-商户订单号[{}]-预下单异常", e);
            preQuickPayResponse.setReturnCode(JKMTradeErrorCode.SYS_ERROR.getErrorCode());
            preQuickPayResponse.setReturnCode(JKMTradeErrorCode.SYS_ERROR.getErrorMessage());
        }
        preQuickPayResponse.setSign(preQuickPayResponse.createSign(""));
        return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
    }
}
