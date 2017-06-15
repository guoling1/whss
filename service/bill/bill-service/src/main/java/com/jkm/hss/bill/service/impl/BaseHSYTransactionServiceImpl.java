package com.jkm.hss.bill.service.impl;

import com.jkm.hss.account.enums.EnumAppType;
import com.jkm.hss.bill.entity.HsyOrder;
import com.jkm.hss.bill.enums.EnumBasicStatus;
import com.jkm.hss.bill.enums.EnumHsyOrderStatus;
import com.jkm.hss.bill.enums.EnumServiceType;
import com.jkm.hss.bill.helper.PayParams;
import com.jkm.hss.bill.helper.PayResponse;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.TradeService;
import com.jkm.hss.merchant.helper.WxConstants;
import com.jkm.hss.product.enums.EnumMerchantPayType;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppBizShop;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/6/12.
 */
@Slf4j
@Service
public class BaseHSYTransactionServiceImpl implements BaseHSYTransactionService {

    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HSYOrderService hsyOrderService;
    @Autowired
    private TradeService tradeService;

    /**
     * {@inheritDoc}
     *
     * @param hsyOrder
     * @param totalAmount
     * @return
     */
    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public long isNeedCreateNewOrder(final HsyOrder hsyOrder, final String totalAmount) {
        if (hsyOrder.isHaveRequestedTrade()) {
            final HsyOrder newHsyOrder = new HsyOrder();
            BeanUtils.copyProperties(hsyOrder, newHsyOrder);
            newHsyOrder.setOrderstatus(EnumHsyOrderStatus.DUE_PAY.getId());
            newHsyOrder.setAmount(new BigDecimal(totalAmount));
            this.hsyOrderService.insert(newHsyOrder);
            return newHsyOrder.getId();
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param hsyOrder
     * @param amount
     * @return
     */
    @Override
    @Transactional
    public Triple<Integer, String, String> placeOrderImpl(final HsyOrder hsyOrder, final BigDecimal amount) {
        final AppBizShop shop = this.hsyShopDao.findAppBizShopByID(hsyOrder.getShopid()).get(0);
        final PayParams payParams = new PayParams();
        payParams.setBusinessOrderNo(hsyOrder.getOrdernumber());
        payParams.setChannel(hsyOrder.getPaychannelsign());
        payParams.setMerchantPayType(EnumMerchantPayType.MERCHANT_JSAPI);
        payParams.setAppId(EnumAppType.HSY.getId());
        payParams.setTradeAmount(amount);
        payParams.setRealPayAmount(amount);
        payParams.setServiceType(EnumServiceType.RECEIVE_MONEY.getId());
        payParams.setPayeeAccountId(shop.getAccountID());
        payParams.setGoodsName(hsyOrder.getGoodsname());
        payParams.setGoodsDescribe(hsyOrder.getGoodsdescribe());
        payParams.setWxAppId(WxConstants.APP_HSY_ID);
        payParams.setMemberId(hsyOrder.getMemberId());
        payParams.setMerchantNo(hsyOrder.getMerchantNo());
        payParams.setMerchantName(hsyOrder.getMerchantname());
        final PayResponse payResponse = this.tradeService.pay(payParams);
        final EnumBasicStatus status = EnumBasicStatus.of(payResponse.getCode());
        final HsyOrder updateOrder = new HsyOrder();
        switch (status) {
            case FAIL:
                updateOrder.setId(hsyOrder.getId());
                updateOrder.setOrderno(payResponse.getTradeOrderNo());
                updateOrder.setOrderid(payResponse.getTradeOrderId());
                updateOrder.setRemark(payResponse.getMessage());
                this.hsyOrderService.update(updateOrder);
                return Triple.of(-1, payResponse.getMessage(), shop.getName());
            case SUCCESS:
                updateOrder.setId(hsyOrder.getId());
                updateOrder.setOrderno(payResponse.getTradeOrderNo());
                updateOrder.setValidationcode(payResponse.getTradeOrderNo().substring(payResponse.getTradeOrderNo().length() - 4));
                updateOrder.setOrderid(payResponse.getTradeOrderId());
                updateOrder.setRemark(payResponse.getMessage());
                this.hsyOrderService.update(updateOrder);
                return Triple.of(0, payResponse.getUrl(), shop.getName());
             default:
                 return Triple.of(-1, payResponse.getMessage(), shop.getName());
        }
    }
}
