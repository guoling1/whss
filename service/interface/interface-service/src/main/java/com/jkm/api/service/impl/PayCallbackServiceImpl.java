package com.jkm.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jkm.api.enums.EnumApiOrderStatus;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.helper.requestparam.PayCallbackResponse;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.service.PayCallbackService;
import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.entity.BusinessOrder;
import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.service.BusinessOrderService;
import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yulong.zhang on 2017/8/17.
 */
@Slf4j
@Service
public class PayCallbackServiceImpl implements PayCallbackService {

    @Autowired
    private OrderService orderService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private HttpClientFacade httpClientFacade;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private BusinessOrderService businessOrderService;


    /**
     * {@inheritDoc}
     *
     * @param orderId
     * @param businessOrderId
     * @param isSuccess 1成功
     * @return
     */
    @Override
    public Pair<Integer, String> payCallback(final long orderId, final long businessOrderId, final int isSuccess, final String msg) {
        try {
            final Order order = orderService.getById(orderId).get();
            final BusinessOrder businessOrder = businessOrderService.getById(businessOrderId).get();
            final MerchantInfo merchant = merchantInfoService.selectById(businessOrder.getMerchantId()).get();
            final Dealer dealer = dealerService.getById(merchant.getDealerId()).get();
            final PayCallbackResponse payCallbackResponse = new PayCallbackResponse();
            payCallbackResponse.setDealerMarkCode(dealer.getMarkCode());
            payCallbackResponse.setMerchantNo(merchant.getMarkCode());
            payCallbackResponse.setOrderNo(businessOrder.getOrderNo());
            payCallbackResponse.setTradeOrderNo(order.getOrderNo());
            payCallbackResponse.setMerchantReqTime(DateFormatUtil.format(businessOrder.getMerchantReqTime(), DateFormatUtil.yyyyMMddHHmmssSSS));
            payCallbackResponse.setOrderAmount(businessOrder.getTradeAmount().toPlainString());
            payCallbackResponse.setCardNo(MerchantSupport.decryptBankCard(merchant.getId(), order.getPayBankCard()));
            if (1 == isSuccess) {
                payCallbackResponse.setOrderStatus(EnumApiOrderStatus.SUCCESS.getCode());
                payCallbackResponse.setReturnCode(JKMTradeErrorCode.SUCCESS.getErrorCode());
                payCallbackResponse.setReturnMsg(JKMTradeErrorCode.SUCCESS.getErrorMessage());
            } else {
                payCallbackResponse.setOrderStatus(EnumApiOrderStatus.FAIL.getCode());
                payCallbackResponse.setReturnCode(JKMTradeErrorCode.FAIL.getErrorCode());
                payCallbackResponse.setReturnMsg(msg);
            }
            final String result = httpClientFacade.jsonPost(order.getNotifyUrl(), SdkSerializeUtil.convertObjToMap(payCallbackResponse));
            final JSONObject resultJo = JSON.parseObject(result);
            if (!"SUCCESS".equalsIgnoreCase(resultJo.getString("returnCode"))) {
                log.info("商户[{}]-商户订单号[{}]-交易订单号[{}],支付[{}]-通知商户成功", order.getOrderNo(), order.getBusinessOrderNo(), order.getOrderNo(), payCallbackResponse.getOrderStatus());
                return Pair.of(0, "通知成功");
            }
            return Pair.of(1, "重试通知");
        } catch (final Throwable e) {
            return Pair.of(-2, "通知异常");
        }
    }
}
