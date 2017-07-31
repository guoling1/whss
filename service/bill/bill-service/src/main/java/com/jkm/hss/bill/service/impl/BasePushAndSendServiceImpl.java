package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.entity.HsyOrderPrintTicketRecord;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.HsyOrderPrintTicketRecordService;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hss.push.sevice.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/7/31.
 */
@Slf4j
@Service
public class BasePushAndSendServiceImpl implements BasePushAndSendService {

    @Autowired
    private PushService pushService;
    @Autowired
    private HSYOrderService hsyOrderService;
    @Autowired
    private HttpClientFacade httpClientFacade;
    @Autowired
    private HsyOrderPrintTicketRecordService hsyOrderPrintTicketRecordService;

    /**
     * {@inheritDoc}
     *
     * @param tradeOrderNo
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void pushAndSendPrintMsg(final String tradeOrderNo, final Date successTime) {
        final HsyOrder hsyOrder = this.hsyOrderService.selectByOrderNo(tradeOrderNo).get();
        log.info("店铺[{}], 订单[{}], 交易[{}], 开始推送", hsyOrder.getShopid(), hsyOrder.getId(), hsyOrder.getOrderno());
        try {
            this.pushService.pushCashMsg(hsyOrder.getShopid(), EnumPaymentChannel.of(hsyOrder.getPaymentChannel()).getValue(),
                    hsyOrder.getAmount().doubleValue(), tradeOrderNo.substring(tradeOrderNo.length() - 4), hsyOrder.getOrderno());
        } catch (final Throwable e) {
            log.error("订单[" + hsyOrder.getOrderno() + "]，推送异常", e);
        }

        //打印
        try {
            final String discountAmount = hsyOrder.getRealAmount().subtract(hsyOrder.getAmount()).toPlainString();
            final JSONObject jo = new JSONObject();
            jo.put("shopId", hsyOrder.getShopid());
            jo.put("orderNo", hsyOrder.getOrdernumber());
            jo.put("tradeOrderNo", tradeOrderNo);
            jo.put("status", EnumOrderStatus.PAY_SUCCESS.getId());
            jo.put("paySuccessTime", successTime);
            jo.put("shopName", hsyOrder.getShopname());
            jo.put("tradeAmount", hsyOrder.getRealAmount().toPlainString());
            jo.put("discountAmount", discountAmount);
            jo.put("totalAmount", hsyOrder.getAmount());
            jo.put("payChannel", hsyOrder.getPaymentChannel());
            final HsyOrderPrintTicketRecord printTicketRecord = new HsyOrderPrintTicketRecord();
            printTicketRecord.setTradeOrderNo(hsyOrder.getOrderno());
            printTicketRecord.setMsg(jo.toJSONString());
            this.hsyOrderPrintTicketRecordService.add(printTicketRecord);
            if (printTicketRecord.getId() > 0) {
                log.info("店铺[{}], 订单[{}], 交易[{}], 打印推送", hsyOrder.getShopid(), hsyOrder.getId(), hsyOrder.getOrderno());
                this.httpClientFacade.post(PaymentSdkConstants.SOCKET_SEND_MSG_URL, jo.toJSONString());
            }
        } catch (final Throwable e) {
            log.error("店铺-[" + hsyOrder.getShopid() + "], 交易-[" + hsyOrder.getOrderno() + "]，发送打印socket异常", e);
        }

    }
}
