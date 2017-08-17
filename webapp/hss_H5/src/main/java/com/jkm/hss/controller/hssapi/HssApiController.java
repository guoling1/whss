package com.jkm.hss.controller.hssapi;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.jkm.api.enums.EnumApiOrderSettleStatus;
import com.jkm.api.enums.EnumApiOrderStatus;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.requestparam.MerchantRequest;
import com.jkm.api.helper.requestparam.PreQuickPayRequest;
import com.jkm.api.helper.responseparam.MctApplyResponse;
import com.jkm.api.helper.responseparam.PreQuickPayResponse;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.service.MerchantService;
import com.jkm.api.service.QuickPayService;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * Created by yulong.zhang on 2017/8/15.
 */
@Slf4j
@Controller
@RequestMapping(value = "/api/hss")
public class HssApiController extends BaseController {

    @Autowired
    private DealerService dealerService;
    @Autowired
    private QuickPayService quickPayService;
    @Autowired
    private MerchantService merchantService;

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
        PreQuickPayRequest request;
        try {
            readParam = super.read(httpServletRequest);
            request = JSON.parseObject(readParam, PreQuickPayRequest.class);
        } catch (final IOException e) {
            log.error("商户号[{}]-商户订单号[{}]-预下单读取数据流异常", e);
            preQuickPayResponse.setReturnCode(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR.getErrorCode());
            preQuickPayResponse.setReturnCode(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
        }
        log.info("商户号[{}]-商户订单号[{}]-预下单-参数[{}]", request.getMerchantNo(), request.getOrderNo(), request);
        preQuickPayResponse.setDealerMarkCode(request.getDealerMarkCode());
        preQuickPayResponse.setMerchantNo(request.getMerchantNo());
        preQuickPayResponse.setOrderNo(request.getOrderNo());
        preQuickPayResponse.setMerchantReqTime(request.getMerchantReqTime());
        preQuickPayResponse.setOrderAmount(request.getOrderAmount());
        preQuickPayResponse.setCardNo(request.getCardNo());
        try {
            final Optional<Dealer> dealerOptional = this.dealerService.getDealerByMarkCode(request.getDealerMarkCode());
            if (!dealerOptional.isPresent()) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.DEALER_NOT_EXIST);
            }
            final Dealer dealer = dealerOptional.get();
            //取秘钥
            //参数校验
//            request.validateParam();
            if (request.verifySign("")) {
                log.error("商户号[{}]-商户订单号[{}]-预下单签名错误", 1, 2);
                preQuickPayResponse.setReturnCode(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorCode());
                preQuickPayResponse.setReturnMsg(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorMessage());
                preQuickPayResponse.setSign(preQuickPayResponse.createSign(""));
                return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
            }
            //请求
            final String tradeOrderNo = this.quickPayService.preQuickPay(request);
            preQuickPayResponse.setTradeOrderNo(tradeOrderNo);
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

    /**
     * 商户入网
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "mctApply", method = RequestMethod.POST)
    public Object mctApply(final HttpServletRequest httpServletRequest) {
        final MctApplyResponse mctApplyResponse = new MctApplyResponse();
        String readParam;
        MerchantRequest request;
        try {
            readParam = super.read(httpServletRequest);
            request = JSON.parseObject(readParam, MerchantRequest.class);
        } catch (final IOException e) {
            log.error("商户入网读取数据流异常", e);
            mctApplyResponse.setReturnCode(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR.getErrorCode());
            mctApplyResponse.setReturnCode(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(mctApplyResponse);
        }
        log.info("商户入网参数[{}]", JSON.toJSON(request).toString());
        mctApplyResponse.setDealerMarkCode(request.getDealerMarkCode());
        try {
            final Optional<Dealer> dealerOptional = this.dealerService.getDealerByMarkCode(request.getDealerMarkCode());
            if (!dealerOptional.isPresent()) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.DEALER_NOT_EXIST);
            }
            final Dealer dealer = dealerOptional.get();
            //取秘钥
            //参数校验
            if (request.verifySign("")) {
                log.error("商户入网签名错误");
                mctApplyResponse.setReturnCode(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorCode());
                mctApplyResponse.setReturnMsg(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorMessage());
                mctApplyResponse.setSign(mctApplyResponse.createSign(""));
                return SdkSerializeUtil.convertObjToMap(mctApplyResponse);
            }
            //请求
            Map map = merchantService.merchantIn(request);
            mctApplyResponse.setMerchantNo(map.get("merchantNo").toString());
            mctApplyResponse.setMerchantStatus(map.get("merchantStatus").toString());
            mctApplyResponse.setReturnCode(JKMTradeErrorCode.SUCCESS.getErrorCode());
            mctApplyResponse.setReturnMsg("入网成功");
        } catch (final JKMTradeServiceException e) {
            log.error("商户入网异常", e);
            mctApplyResponse.setReturnCode(e.getErrorCode());
            mctApplyResponse.setReturnMsg(e.getErrorMessage());
        } catch (final Throwable e) {
            log.error("商户入网异常", e);
            mctApplyResponse.setReturnCode(JKMTradeErrorCode.SYS_ERROR.getErrorCode());
            mctApplyResponse.setReturnMsg(JKMTradeErrorCode.SYS_ERROR.getErrorMessage());
        }
        mctApplyResponse.setSign(mctApplyResponse.createSign(""));
        return SdkSerializeUtil.convertObjToMap(mctApplyResponse);
    }

}
