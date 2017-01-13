package com.jkm.hss.bill.service.impl;

import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;
import com.jkm.hss.dealer.enums.EnumDealerRateType;
import com.jkm.hss.dealer.service.*;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.MerchantPromoteShallService;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradeRulesService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    private UpgradeRulesService upgradeRulesService;
    @Autowired
    private DealerUpgerdeRateService dealerUpgerdeRateService;
    @Autowired
    private ProductService productService;
    @Autowired
    private MerchantPromoteShallService merchantPromoteShallService;

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

        return getMerchantRate(channelSign, merchant);
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

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @return
     */
    @Override
    public BigDecimal getMerchantUpgradePoundage(final long merchantId ,String orderNo, BigDecimal tradeAmount, String businessNo) {

        final Map<String, Triple<Long, BigDecimal, String>> map = this.merchantPromoteShallTo(merchantId, orderNo, businessNo, tradeAmount);

        return  tradeAmount.subtract(map.get("companyMoney").getMiddle());
    }

    private Map<String, Triple<Long, BigDecimal, String>> merchantPromoteShallTo(final long merchantId, final String orderNo, final String businessNo,final BigDecimal tradeAmount) {
        try{
            Pair<BigDecimal, BigDecimal> pair = merchantInfoService.getUpgradeShareProfit(businessNo);

            //获取商户
            final MerchantInfo merchantInfo = this.merchantInfoService.selectById(merchantId).get();
            //获取分润金额
            final BigDecimal waitAmount = tradeAmount;
            //判断有无代理商
            if(merchantInfo.getFirstDealerId() == 0){
                Map<String, Triple<Long, BigDecimal, String>> map = new HashMap<>();
                BigDecimal directMoney = null;
                MerchantInfo directMerchantInfo = null;
                //上级商户分润 ，直推分润
                if (merchantInfo.getFirstMerchantId() != 0){
                    directMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getFirstMerchantId()).get();
                    directMoney = pair.getLeft();
                }else{
                    directMoney = new BigDecimal("0");
                }
                BigDecimal inDirectMoney = null;
                MerchantInfo inDirectMerchantInfo = null;
                //上上级商户分润，间推分润
                if (merchantInfo.getSecondMerchantId() != 0){
                    inDirectMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getSecondMerchantId()).get();
                    inDirectMoney = pair.getRight();
                }else{
                    inDirectMoney = new BigDecimal("0");
                }
                //金开门利润 = 升级费 - 直推分润 - 间推分润
                BigDecimal productMoney = waitAmount.subtract(directMoney).subtract(inDirectMoney);

                if (directMoney.compareTo(new BigDecimal("0")) == 1){

                    map.put("directMoney", Triple.of(directMerchantInfo.getAccountId(), pair.getLeft(), "D0"));
                }else{

                }
                if (inDirectMoney.compareTo(new BigDecimal("0")) == 1){

                    map.put("inDirectMoney", Triple.of(inDirectMerchantInfo.getAccountId(), pair.getRight(), "D0"));
                }else{

                }

                map.put("companyMoney", Triple.of(AccountConstants.JKM_ACCOUNT_ID, productMoney, "D0"));
                return map;
            }

            final Product product = this.productService.selectByType(EnumProductType.HSS.getId()).get();
            //获取升级规则
            BigDecimal directMoney = null;
            MerchantInfo directMerchantInfo = null;
            //上级商户分润 ，直推分润
            if (merchantInfo.getFirstMerchantId() != 0){
                directMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getFirstMerchantId()).get();
                directMoney = pair.getLeft();
            }else{
                directMoney = new BigDecimal("0");
            }
            BigDecimal inDirectMoney = null;
            MerchantInfo inDirectMerchantInfo = null;
            //上上级商户分润，间推分润
            if (merchantInfo.getSecondMerchantId() != 0){
                inDirectMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getSecondMerchantId()).get();
                inDirectMoney = pair.getRight();
            }else{
                inDirectMoney = new BigDecimal("0");
            }

            //查询商户的一级代理分润规则
            final Dealer dealer = this.dealerService.getById(merchantInfo.getFirstDealerId()).get();
            final DealerUpgerdeRate dealerUpgerdeRates = this.dealerUpgerdeRateService
                    .selectByDealerIdAndTypeAndProductId(merchantInfo.getFirstDealerId(), EnumDealerRateType.UPGRADE, product.getId());
            BigDecimal secondMoney = null;
            //二级代理分润 = （升级费 - 直推分润 - 间推分润）* 二级代理分润比例
            final Dealer secondDealer;
            if (merchantInfo.getSecondDealerId() != 0){
                secondDealer = this.dealerService.getById(merchantInfo.getSecondDealerId()).get();
                secondMoney = (waitAmount.subtract(inDirectMoney).subtract(directMoney))
                        .multiply(dealerUpgerdeRates.getSecondDealerShareProfitRate());
            }else{
                secondDealer = null;
                secondMoney = new BigDecimal("0");
            }
            //一级代理分润 = （升级费 - 直推分润 - 间推分润）* 一级代理分润比例
            BigDecimal firstMoney = (waitAmount.subtract(directMoney).subtract(inDirectMoney))
                    .multiply(dealerUpgerdeRates.getFirstDealerShareProfitRate());
            //金开门利润 = 升级费 - 直推分润 - 间推分润 - 一级代理分润 - 二级代理分润
            BigDecimal productMoney = waitAmount.subtract(inDirectMoney).subtract(directMoney).subtract(firstMoney).subtract(secondMoney);

            final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();


            Map<String, Triple<Long, BigDecimal, String>> map = new HashMap<>();
            map.put("companyMoney", Triple.of(AccountConstants.JKM_ACCOUNT_ID, productMoney, "D0"));
            map.put("firstMoney", Triple.of(dealer.getAccountId(), firstMoney, "D0"));
            if (merchantInfo.getSecondDealerId() != 0){

                map.put("secondMoney", Triple.of(secondDealer.getAccountId(), secondMoney, "D0"));
            }else{

            }

            if (directMoney.compareTo(new BigDecimal("0")) == 1){

                map.put("directMoney", Triple.of(directMerchantInfo.getAccountId(), pair.getLeft(), "D0"));
            }else{

            }

            if (inDirectMoney.compareTo(new BigDecimal("0")) == 1){

                map.put("inDirectMoney", Triple.of(inDirectMerchantInfo.getAccountId(), pair.getRight(), "D0"));
            }else{

            }

            return map;
        }catch (final Throwable throwable){
            throw throwable;
        }

    }

    private BigDecimal getMerchantRate(int channelSign, final MerchantInfo merchantInfo){

        final BigDecimal merchantRate;
        if (channelSign == EnumPayChannelSign.YG_WEIXIN.getId()){
            return  merchantInfo.getWeixinRate();
        }else if (channelSign == EnumPayChannelSign.YG_ZHIFUBAO.getId()){
            return merchantInfo.getAlipayRate();
        }else{
            return merchantInfo.getFastRate();
        }
    }
}
