package com.jkm.hss.controller.hssapi;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.jkm.api.enums.EnumApiOrderSettleStatus;
import com.jkm.api.enums.EnumApiOrderStatus;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.requestparam.*;
import com.jkm.api.helper.responseparam.*;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.service.MerchantService;
import com.jkm.api.service.OpenCardService;
import com.jkm.api.service.QuickPayService;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoService;
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
    private OrderService orderService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private QuickPayService quickPayService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private OpenCardService openCardService;
    @Autowired
    private MerchantInfoService merchantInfoService;

    /**
     * 快捷预下单
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "kuaiPayPreOrder", method = RequestMethod.POST)
    public Object preQuickPay(final HttpServletRequest httpServletRequest) {
        final PreQuickPayResponse preQuickPayResponse = new PreQuickPayResponse();
        String readParam;
        PreQuickPayRequest request;
        try {
            readParam = super.read(httpServletRequest);
            request = JSON.parseObject(readParam, PreQuickPayRequest.class);
        } catch (final IOException e) {
            log.error("预下单读取数据流异常", e);
            preQuickPayResponse.setReturnCode(JKMTradeErrorCode.READ_PARAM_ERROR.getErrorCode());
            preQuickPayResponse.setReturnMsg(JKMTradeErrorCode.READ_PARAM_ERROR.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
        }
        log.info("商户号[{}]-商户订单号[{}]-预下单-参数[{}]", request.getMerchantNo(), request.getOrderNo(), request);
        preQuickPayResponse.setDealerMarkCode(request.getDealerMarkCode());
        preQuickPayResponse.setMerchantNo(request.getMerchantNo());
        preQuickPayResponse.setOrderNo(request.getOrderNo());
        preQuickPayResponse.setMerchantReqTime(request.getMerchantReqTime());
        preQuickPayResponse.setOrderAmount(request.getOrderAmount());
        preQuickPayResponse.setCardNo(request.getCardNo());
        final Optional<Dealer> dealerOptional = this.dealerService.getDealerByMarkCode(request.getDealerMarkCode());
        if (!dealerOptional.isPresent()) {
            preQuickPayResponse.setReturnCode(JKMTradeErrorCode.DEALER_NOT_EXIST.getErrorCode());
            preQuickPayResponse.setReturnMsg(JKMTradeErrorCode.DEALER_NOT_EXIST.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
        }
        final Dealer dealer = dealerOptional.get();
        try {
            //参数校验
//            request.validateParam();
            if (request.verifySign(dealer.getApiKey())) {
                log.error("商户号[{}]-商户订单号[{}]-预下单签名错误", request.getMerchantNo(), request.getOrderNo());
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
            preQuickPayResponse.setReturnCode(JKMTradeErrorCode.SUCCESS.getErrorCode());
            preQuickPayResponse.setReturnMsg("预下单成功");
        } catch (final JKMTradeServiceException e) {
            log.error("商户号[{}]-商户订单号[{}]-预下单异常", e);
            preQuickPayResponse.setReturnCode(e.getErrorCode());
            preQuickPayResponse.setReturnMsg(e.getErrorMessage());
        } catch (final Throwable e) {
            log.error("商户号[{}]-商户订单号[{}]-预下单异常", e);
            preQuickPayResponse.setReturnCode(JKMTradeErrorCode.SYS_ERROR.getErrorCode());
            preQuickPayResponse.setReturnMsg(JKMTradeErrorCode.SYS_ERROR.getErrorMessage());
        }
        preQuickPayResponse.setSign(preQuickPayResponse.createSign(dealer.getApiKey()));
        return SdkSerializeUtil.convertObjToMap(preQuickPayResponse);
    }

    /**
     * 快捷去人支付
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "kuaiPayConfirmOrder", method = RequestMethod.POST)
    public Object confirmQuickPay(final HttpServletRequest httpServletRequest) {
        final ConfirmQuickPayResponse response = new ConfirmQuickPayResponse();
        String readParam;
        ConfirmQuickPayRequest request;
        try {
            readParam = super.read(httpServletRequest);
            request = JSON.parseObject(readParam, ConfirmQuickPayRequest.class);
        } catch (final IOException e) {
            log.error("确认支付读取数据流异常", e);
            response.setReturnCode(JKMTradeErrorCode.READ_PARAM_ERROR.getErrorCode());
            response.setReturnMsg(JKMTradeErrorCode.READ_PARAM_ERROR.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(response);
        }
        log.info("商户号[{}]-商户订单号[{}]-确认支付-参数[{}]", request.getMerchantNo(), request.getOrderNo(), request);
        response.setDealerMarkCode(request.getDealerMarkCode());
        response.setMerchantNo(request.getMerchantNo());
        response.setOrderNo(request.getOrderNo());
        response.setOrderAmount(request.getOrderAmount());
        final Optional<Dealer> dealerOptional = this.dealerService.getDealerByMarkCode(request.getDealerMarkCode());
        if (!dealerOptional.isPresent()) {
            response.setReturnCode(JKMTradeErrorCode.DEALER_NOT_EXIST.getErrorCode());
            response.setReturnMsg(JKMTradeErrorCode.DEALER_NOT_EXIST.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(response);
        }
        final Dealer dealer = dealerOptional.get();
        try {
            //参数校验
//            request.validateParam();
            if (request.verifySign(dealer.getApiKey())) {
                log.error("商户号[{}]-商户订单号[{}]-确认支付签名错误", request.getMerchantNo(), request.getOrderNo());
                response.setReturnCode(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorCode());
                response.setReturnMsg(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorMessage());
                response.setSign(response.createSign(dealer.getApiKey()));
                return SdkSerializeUtil.convertObjToMap(response);
            }
            //请求
            this.quickPayService.confirmQuickPay(request, response);
            response.setOrderStatus(EnumApiOrderStatus.PROCESSING.getCode());
            response.setSettleStatus(EnumApiOrderSettleStatus.WAIT.getCode());
            response.setReturnCode(JKMTradeErrorCode.SUCCESS.getErrorCode());
            response.setReturnMsg("确认支付成功");
        } catch (final JKMTradeServiceException e) {
            log.error("商户号[{}]-商户订单号[{}]-确认支付异常", e);
            response.setReturnCode(e.getErrorCode());
            response.setReturnMsg(e.getErrorMessage());
        } catch (final Throwable e) {
            log.error("商户号[{}]-商户订单号[{}]-确认支付异常", e);
            response.setReturnCode(JKMTradeErrorCode.SYS_ERROR.getErrorCode());
            response.setReturnMsg(JKMTradeErrorCode.SYS_ERROR.getErrorMessage());
        }
        response.setSign(response.createSign(dealer.getApiKey()));
        return SdkSerializeUtil.convertObjToMap(response);
    }

    /**
     * 快捷去人支付
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "orderQuery", method = RequestMethod.POST)
    public Object orderQuery(final HttpServletRequest httpServletRequest) {
        final OrderQueryResponse response = new OrderQueryResponse();
        String readParam;
        OrderQueryRequest request;
        try {
            readParam = super.read(httpServletRequest);
            request = JSON.parseObject(readParam, OrderQueryRequest.class);
        } catch (final IOException e) {
            log.error("订单查询读取数据流异常", e);
            response.setReturnCode(JKMTradeErrorCode.READ_PARAM_ERROR.getErrorCode());
            response.setReturnMsg(JKMTradeErrorCode.READ_PARAM_ERROR.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(response);
        }
        log.info("商户号[{}]-商户订单号[{}]-订单查询-参数[{}]", request.getMerchantNo(), request.getOrderNo(), request);
        response.setDealerMarkCode(request.getDealerMarkCode());
        response.setMerchantNo(request.getMerchantNo());
        response.setOrderNo(request.getOrderNo());
        final Optional<Dealer> dealerOptional = this.dealerService.getDealerByMarkCode(request.getDealerMarkCode());
        if (!dealerOptional.isPresent()) {
            response.setReturnCode(JKMTradeErrorCode.DEALER_NOT_EXIST.getErrorCode());
            response.setReturnMsg(JKMTradeErrorCode.DEALER_NOT_EXIST.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(response);
        }
        final Dealer dealer = dealerOptional.get();
        try {
            //参数校验
//            request.validateParam();
            if (request.verifySign(dealer.getApiKey())) {
                log.error("商户号[{}]-商户订单号[{}]-订单查询签名错误", request.getMerchantNo(), request.getOrderNo());
                response.setReturnCode(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorCode());
                response.setReturnMsg(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorMessage());
                response.setSign(response.createSign(dealer.getApiKey()));
                return SdkSerializeUtil.convertObjToMap(response);
            }
            final Optional<MerchantInfo> merchantOptional = this.merchantInfoService.getByMarkCode(request.getMerchantNo());
            if (merchantOptional.isPresent()) {
                response.setReturnCode(JKMTradeErrorCode.MERCHANT_NOT_EXIST.getErrorCode());
                response.setReturnMsg(JKMTradeErrorCode.MERCHANT_NOT_EXIST.getErrorMessage());
                response.setSign(response.createSign(dealer.getApiKey()));
                return SdkSerializeUtil.convertObjToMap(response);
            }
            //请求
            final Optional<Order> orderOptional = this.orderService.getByOrderNo(request.getTradeOrderNo());
            if (!orderOptional.isPresent()) {
                response.setReturnCode(JKMTradeErrorCode.ORDER_NOT_EXIST.getErrorCode());
                response.setReturnMsg(JKMTradeErrorCode.ORDER_NOT_EXIST.getErrorMessage());
                response.setSign(response.createSign(dealer.getApiKey()));
                return SdkSerializeUtil.convertObjToMap(response);
            }
            final Order order = orderOptional.get();
            response.setMerchantReqTime(DateFormatUtil.format(order.getMerchantReqTime(), DateFormatUtil.yyyyMMddHHmmssSSS));
            response.setOrderAmount(order.getTradeAmount().toPlainString());
            response.setChannelCode(order.getPayType());
            response.setPayProduct("KUAI_PAY");
            response.setCardNo(MerchantSupport.decryptBankCard(order.getPayee(), order.getPayBankCard()));
            if (order.isDuePay()) {
                response.setOrderStatus(EnumApiOrderStatus.INIT.getCode());
            } else if (order.isPaying()) {
                response.setOrderStatus(EnumApiOrderStatus.PROCESSING.getCode());
            } else if (order.isPaySuccess()) {
                response.setOrderStatus(EnumApiOrderStatus.SUCCESS.getCode());
            } else {
                response.setOrderStatus(EnumApiOrderStatus.FAIL.getCode());
            }
            if (order.isDueSettle()) {
                response.setSettleStatus(EnumApiOrderSettleStatus.WAIT.getCode());
            } else if (order.isSettleing()) {
                response.setSettleStatus(EnumApiOrderSettleStatus.PROCESSING.getCode());
            } else if (order.isSettled()) {
                response.setSettleStatus(EnumApiOrderSettleStatus.SUCCESS.getCode());
            } else {
                response.setSettleStatus(EnumApiOrderSettleStatus.FAIL.getCode());
            }
            response.setReturnCode(JKMTradeErrorCode.SUCCESS.getErrorCode());
            response.setReturnMsg("查询成功");
        } catch (final JKMTradeServiceException e) {
            log.error("商户号[{}]-商户订单号[{}]-确认支付异常", e);
            response.setReturnCode(e.getErrorCode());
            response.setReturnMsg(e.getErrorMessage());
        } catch (final Throwable e) {
            log.error("商户号[{}]-商户订单号[{}]-确认支付异常", e);
            response.setReturnCode(JKMTradeErrorCode.SYS_ERROR.getErrorCode());
            response.setReturnMsg(JKMTradeErrorCode.SYS_ERROR.getErrorMessage());
        }
        response.setSign(response.createSign(dealer.getApiKey()));
        return SdkSerializeUtil.convertObjToMap(response);
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

    /**
     * H5快捷支付绑卡
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "kuaiPayOpenCard", method = RequestMethod.POST)
    public Object kuaiPayOpenCard(final HttpServletRequest httpServletRequest) {
        final OpenCardResponse openCardResponse = new OpenCardResponse();
        String readParam;
        OpenCardRequest request;
        try {
            readParam = super.read(httpServletRequest);
            request = JSON.parseObject(readParam, OpenCardRequest.class);
        } catch (final IOException e) {
            log.error("快捷绑卡读取数据流异常", e);
            openCardResponse.setReturnCode(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR.getErrorCode());
            openCardResponse.setReturnCode(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(openCardResponse);
        }
        log.info("快捷绑卡入网参数[{}]", JSON.toJSON(request).toString());
        openCardResponse.setDealerMarkCode(request.getDealerMarkCode());
        openCardResponse.setMerchantNo(request.getMerchantNo());
        openCardResponse.setCardNo(request.getCardNo());
        try {
            final Optional<Dealer> dealerOptional = this.dealerService.getDealerByMarkCode(request.getDealerMarkCode());
            if (!dealerOptional.isPresent()) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.DEALER_NOT_EXIST);
            }
            final Dealer dealer = dealerOptional.get();
            //取秘钥
            //参数校验
            if (request.verifySign("")) {
                log.error("快捷绑卡签名错误");
                openCardResponse.setReturnCode(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorCode());
                openCardResponse.setReturnMsg(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorMessage());
                openCardResponse.setSign(openCardResponse.createSign(""));
                return SdkSerializeUtil.convertObjToMap(openCardResponse);
            }
            //请求
            String html = openCardService.kuaiPayOpenCard(request);
            openCardResponse.setHtml(html);
            openCardResponse.setReturnCode(JKMTradeErrorCode.SUCCESS.getErrorCode());
            openCardResponse.setReturnMsg("绑卡成功");
        } catch (final JKMTradeServiceException e) {
            log.error("快捷绑卡异常", e);
            openCardResponse.setReturnCode(e.getErrorCode());
            openCardResponse.setReturnMsg(e.getErrorMessage());
        } catch (final Throwable e) {
            log.error("快捷绑卡异常", e);
            openCardResponse.setReturnCode(JKMTradeErrorCode.SYS_ERROR.getErrorCode());
            openCardResponse.setReturnMsg(JKMTradeErrorCode.SYS_ERROR.getErrorMessage());
        }
        openCardResponse.setSign(openCardResponse.createSign(""));
        return SdkSerializeUtil.convertObjToMap(openCardResponse);
    }


    /**
     * 快捷绑卡查询
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "kuaiPayOpenCardQuery", method = RequestMethod.POST)
    public Object kuaiPayOpenCardQuery(final HttpServletRequest httpServletRequest) {
        final OpenCardQueryResponse openCardQueryResponse = new OpenCardQueryResponse();
        String readParam;
        OpenCardQueryRequest request;
        try {
            readParam = super.read(httpServletRequest);
            request = JSON.parseObject(readParam, OpenCardQueryRequest.class);
        } catch (final IOException e) {
            log.error("快捷绑卡查询读取数据流异常", e);
            openCardQueryResponse.setReturnCode(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR.getErrorCode());
            openCardQueryResponse.setReturnCode(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR.getErrorMessage());
            return SdkSerializeUtil.convertObjToMap(openCardQueryResponse);
        }
        log.info("快捷绑卡查询入网参数[{}]", JSON.toJSON(request).toString());
        openCardQueryResponse.setDealerMarkCode(request.getDealerMarkCode());
        try {
            final Optional<Dealer> dealerOptional = this.dealerService.getDealerByMarkCode(request.getDealerMarkCode());
            if (!dealerOptional.isPresent()) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.DEALER_NOT_EXIST);
            }
            final Dealer dealer = dealerOptional.get();
            //取秘钥
            //参数校验
            if (request.verifySign("")) {
                log.error("快捷绑卡查询签名错误");
                openCardQueryResponse.setReturnCode(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorCode());
                openCardQueryResponse.setReturnMsg(JKMTradeErrorCode.CHECK_SIGN_FAIL.getErrorMessage());
                openCardQueryResponse.setSign(openCardQueryResponse.createSign(""));
                return SdkSerializeUtil.convertObjToMap(openCardQueryResponse);
            }
            //业务处理
           // TODO: 2017/8/17
            openCardQueryResponse.setReturnCode(JKMTradeErrorCode.SUCCESS.getErrorCode());
            openCardQueryResponse.setReturnMsg("查询成功");
        } catch (final JKMTradeServiceException e) {
            log.error("快捷绑卡查询异常", e);
            openCardQueryResponse.setReturnCode(e.getErrorCode());
            openCardQueryResponse.setReturnMsg(e.getErrorMessage());
        } catch (final Throwable e) {
            log.error("快捷绑卡查询异常", e);
            openCardQueryResponse.setReturnCode(JKMTradeErrorCode.SYS_ERROR.getErrorCode());
            openCardQueryResponse.setReturnMsg(JKMTradeErrorCode.SYS_ERROR.getErrorMessage());
        }
        openCardQueryResponse.setSign(openCardQueryResponse.createSign(""));
        return SdkSerializeUtil.convertObjToMap(openCardQueryResponse);
    }

}
