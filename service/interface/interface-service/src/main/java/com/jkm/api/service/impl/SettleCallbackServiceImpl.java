package com.jkm.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jkm.api.enums.EnumApiOrderStatus;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.helper.responseparam.PayCallbackResponse;
import com.jkm.api.helper.responseparam.SettleCallbackResponse;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.service.SettleCallbackService;
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
 * Created by yulong.zhang on 2017/8/18.
 */
@Slf4j
@Service
public class SettleCallbackServiceImpl implements SettleCallbackService {

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
     * @return
     */
    @Override
    public Pair<Integer, String> settleCallback(final long orderId, final long businessOrderId) {
        try {
            final Order order = orderService.getById(orderId).get();
            final BusinessOrder businessOrder = businessOrderService.getById(businessOrderId).get();
            final MerchantInfo merchant = merchantInfoService.selectById(businessOrder.getMerchantId()).get();
            final Dealer dealer = dealerService.getById(merchant.getDealerId()).get();
            final SettleCallbackResponse response = new SettleCallbackResponse();
            response.setDealerMarkCode(dealer.getMarkCode());
            response.setMerchantNo(merchant.getMarkCode());
            response.setOrderNo(businessOrder.getOrderNo());
            response.setTradeOrderNo(order.getOrderNo());
            response.setMerchantReqTime(DateFormatUtil.format(businessOrder.getMerchantReqTime(), DateFormatUtil.yyyyMMddHHmmssSSS));
            response.setOrderAmount(businessOrder.getTradeAmount().toPlainString());
            response.setCardNo(MerchantSupport.decryptBankCard(merchant.getId(), order.getPayBankCard()));
            response.setSettleStatus(EnumApiOrderStatus.SUCCESS.getCode());
            response.setReturnCode(JKMTradeErrorCode.SUCCESS.getErrorCode());
            response.setReturnMsg(JKMTradeErrorCode.SUCCESS.getErrorMessage());
            response.setSign(response.createSign(dealer.getApiKey()));
            log.info("商户[{}]-商户订单号[{}]-交易订单号[{}],结算通知商户，参数-[{}]", order.getOrderNo(), order.getBusinessOrderNo(), order.getOrderNo(), response);
            final String result = httpClientFacade.jsonPost(order.getNotifyUrl(), SdkSerializeUtil.convertObjToMap(response));
            final JSONObject resultJo = JSON.parseObject(result);
            if ("SUCCESS".equalsIgnoreCase(resultJo.getString("returnCode"))) {
                log.info("商户[{}]-商户订单号[{}]-交易订单号[{}],结算[{}]-通知商户成功", order.getOrderNo(), order.getBusinessOrderNo(), order.getOrderNo(), response.getSettleStatus());
                return Pair.of(0, "通知成功");
            }
            log.error("商户[{}]-商户订单号[{}]-交易订单号[{}],结算[{}]-通知商户失败", order.getOrderNo(), order.getBusinessOrderNo(), order.getOrderNo(), response.getSettleStatus());
            return Pair.of(1, "重试通知");
        } catch (final Throwable e) {
            return Pair.of(-2, "通知异常");
        }
    }
}
