package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.spring.http.client.impl.HttpClientFacade;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.entity.HsyOrderPrintTicketRecord;
import com.jkm.hss.bill.enums.EnumOrderStatus;
import com.jkm.hss.bill.enums.EnumPaymentTerminal;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.HsyOrderPrintTicketRecordService;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hss.push.sevice.PushService;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppBizShop;
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
    private HsyShopDao hsyShopDao;
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
     * @param orderNumber 订单号
     * @param orderNo 交易订单号
     * @param successTime
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void pushAndSendPrintMsg(final String orderNumber, final String orderNo, final int paymentChannel, final Date successTime) {
        final HsyOrder hsyOrder = this.hsyOrderService.getByOrderNumber(orderNumber).get();
        log.info("店铺[{}], 订单[{}], 交易[{}], 开始推送", hsyOrder.getShopid(), hsyOrder.getId(), orderNo);
        try {
            this.pushService.pushCashMsg(hsyOrder.getShopid(), EnumPaymentChannel.of(paymentChannel).getValue(),
                    hsyOrder.getAmount().doubleValue(), orderNo.substring(orderNo.length() - 4), orderNo);
        } catch (final Throwable e) {
            log.error("店铺-[" + hsyOrder.getShopid() + "], 交易-[" + orderNo + "]，推送异常", e);
        }

        //打印
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByID(hsyOrder.getShopid()).get(0);
        if (EnumBoolean.TRUE.getCode() == shop.getOpenScanPrint() && EnumPaymentTerminal.CELLPHONE.getId() == hsyOrder.getPaymentTerminal()) {
            try {
                final String discountAmount = hsyOrder.getRealAmount().subtract(hsyOrder.getAmount()).toPlainString();
                final JSONObject jo = new JSONObject();
                jo.put("shopId", hsyOrder.getShopid());
                jo.put("orderNo", hsyOrder.getOrdernumber());
                jo.put("tradeOrderNo", orderNo);
                jo.put("status", EnumOrderStatus.PAY_SUCCESS.getId());
                jo.put("paySuccessTime", successTime);
                jo.put("shopName", hsyOrder.getShopname());
                jo.put("tradeAmount", hsyOrder.getRealAmount().toPlainString());
                jo.put("discountAmount", discountAmount);
                jo.put("totalAmount", hsyOrder.getAmount());
                jo.put("payChannel", paymentChannel);
                final HsyOrderPrintTicketRecord printTicketRecord = new HsyOrderPrintTicketRecord();
                printTicketRecord.setTradeOrderNo(orderNo);
                printTicketRecord.setMsg(jo.toJSONString());
                this.hsyOrderPrintTicketRecordService.add(printTicketRecord);
                if (printTicketRecord.getId() > 0) {
                    log.info("店铺[{}], 订单[{}], 交易[{}], 打印推送", hsyOrder.getShopid(), hsyOrder.getId(), orderNo);
                    this.httpClientFacade.post(PaymentSdkConstants.SOCKET_SEND_MSG_URL, jo.toJSONString());
                }
            } catch (final Throwable e) {
                log.error("店铺-[" + hsyOrder.getShopid() + "], 交易-[" + orderNo + "]，发送打印socket异常", e);
            }
        }
    }
}
