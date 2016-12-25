package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/25.
 *
 * 计算  手续费， 分成等
 */
@Slf4j
@Service
public class CalculateServiceImpl implements CalculateService {

    @Autowired
    private DealerService dealerService;
    @Autowired
    private DealerRateService dealerRateService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param channelSign  101,102,103
     * @return
     */
    @Override
    public BigDecimal getMerchantPayPoundageRate(final long merchantId, final int channelSign) {
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        if (0 == merchant.getDealerId()) {
            final ProductChannelDetail productChannelDetail = this.productChannelDetailService.selectByChannelTypeSign(channelSign).get(0);
            return productChannelDetail.getProductMerchantPayRate();
        }
        final Dealer dealer = this.dealerService.getById(merchant.getDealerId()).get();
        final DealerChannelRate dealerChannelRate = this.dealerRateService.selectByDealerIdAndChannelId(dealer.getId(), channelSign).get(0);
        return dealerChannelRate.getDealerMerchantPayRate();
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param channelSign  101,102,103
     * @return
     */
    @Override
    public BigDecimal getMerchantWithdrawPoundage(final long merchantId, final int channelSign) {
        final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
        if (0 == merchant.getDealerId()) {
            final ProductChannelDetail productChannelDetail = this.productChannelDetailService.selectByChannelTypeSign(channelSign).get(0);
            return productChannelDetail.getProductMerchantWithdrawFee().setScale(2);
        }
        final Dealer dealer = this.dealerService.getById(merchant.getDealerId()).get();
        final DealerChannelRate dealerChannelRate = this.dealerRateService.selectByDealerIdAndChannelId(dealer.getId(), channelSign).get(0);
        return dealerChannelRate.getDealerMerchantWithdrawFee().setScale(2);
    }

    /**
     * {@inheritDoc}
     *
     * @param traderAmount  交易金额
     * @param merchantPayPoundageRate  商户支付手续费率
     * @return
     */
    @Override
    public BigDecimal getMerchantPayPoundage(final BigDecimal traderAmount, final BigDecimal merchantPayPoundageRate) {
        //原始手续费
        final BigDecimal originDueSplitAmount = traderAmount.multiply(merchantPayPoundageRate);
        final BigDecimal minPoundage = new BigDecimal("0.01");
        if (minPoundage.compareTo(originDueSplitAmount) > 0) {//手续费不足0.01元
            if (minPoundage.compareTo(traderAmount) == 0) {//交易金额是0.01
                return new BigDecimal(0);
            }
            return minPoundage;
        }
        return originDueSplitAmount.setScale(2, BigDecimal.ROUND_UP);
    }
}
