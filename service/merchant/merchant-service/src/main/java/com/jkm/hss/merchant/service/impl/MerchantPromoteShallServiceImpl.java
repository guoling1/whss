package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.bill.service.OrderService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.enums.EnumDealerRateType;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.DealerUpgerdeRateService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.MerchantPromoteShallService;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.UpgradeRules;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradeRulesService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-01-09.
 */
@Data
@Slf4j
public class MerchantPromoteShallServiceImpl implements MerchantPromoteShallService {


    @Autowired
    private UpgradeRulesService upgradeRulesService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private DealerService dealerService;

    @Autowired
    private DealerUpgerdeRateService dealerUpgerdeRateService;

    @Autowired
    private ProductService productService;
    /**
     * {@inheritDoc}
     * @param merchantId
     * @return
     */
    @Transactional
    @Override
    public Map<String, Triple<Long, BigDecimal, String>> merchantPromoteShall(final long merchantId, final BigDecimal tradeAmount,
                            final String orderNo) {
        log.info("商户[" + merchantId + "]请求进行升级费分润，交易订单号：" + orderNo);
        try{
            //判断该支付订单是否已经参与分润

            //获取商户
            final MerchantInfo merchantInfo = this.merchantInfoService.selectById(merchantId).get();
            //获取分润金额
            final BigDecimal waitAmount = tradeAmount;
            //获取升级规则
            final UpgradeRules upgradeRules = this.upgradeRulesService.selectByProductIdAndType(2, EnumUpGradeType.CLERK.getId()).get();
            BigDecimal directMoney = null;
            MerchantInfo directMerchantInfo = null;
            //上级商户分润 ，直推分润
            if (merchantInfo.getFirstMerchantId() != 0){
                directMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getFirstMerchantId()).get();
                directMoney = upgradeRules.getDirectPromoteShall();
            }else{
                directMoney = new BigDecimal("0");
            }
            BigDecimal inDirectMoney = null;
            MerchantInfo inDirectMerchantInfo = null;
            //上上级商户分润，间推分润
            if (merchantInfo.getSecondDealerId() != 0){
                inDirectMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getSecondMerchantId()).get();
                inDirectMoney = upgradeRules.getInDirectPromoteShall();
            }else{
                inDirectMoney = new BigDecimal("0");
            }

            //查询商户的一级代理分润规则
            final Dealer dealer = this.dealerService.getById(merchantInfo.getFirstDealerId()).get();
            final DealerUpgerdeRate dealerUpgerdeRates = this.dealerUpgerdeRateService
                    .selectByDealerIdAndTypeAndProductId(merchantInfo.getFirstDealerId(), EnumDealerRateType.UPGRADE, 2);
            BigDecimal secondMoney = null;
            //二级代理分润 = （升级费 - 直推分润 - 间推分润）* 二级代理分润比例
            final Dealer secondDealer;
            if (merchantInfo.getSecondDealerId() != 0){
                secondDealer = this.dealerService.getById(merchantInfo.getSecondDealerId()).get();
                secondMoney = (waitAmount.subtract(directMoney).subtract(inDirectMoney))
                        .multiply(dealerUpgerdeRates.getSecondDealerShareProfitRate());
            }else{
                secondDealer = null;
                secondMoney = new BigDecimal("0");
            }
            //一级代理分润 = （升级费 - 直推分润 - 间推分润）* 一级代理分润比例
            BigDecimal firstMoney = (waitAmount.subtract(directMoney).subtract(inDirectMoney))
                        .multiply(dealerUpgerdeRates.getFirstDealerShareProfitRate());
            //金开门利润 = 升级费 - 直推分润 - 间推分润 - 一级代理分润 - 二级代理分润
            final Product product = this.productService.selectById(2).get();
            BigDecimal productMoney = waitAmount.subtract(directMoney).subtract(inDirectMoney).subtract(firstMoney).subtract(secondMoney);

            Map<String, Triple<Long, BigDecimal, String>> map = new HashMap<>();
            map.put("productMoney", Triple.of(product.getAccountId(), productMoney, "D0"));
            map.put("firstMoney", Triple.of(dealer.getAccountId(), firstMoney, "D0"));
            if (merchantInfo.getSecondDealerId() != 0){
                map.put("secondMoney", Triple.of(secondDealer.getAccountId(), secondMoney, "D0"));
            }
            if (directMoney.compareTo(new BigDecimal("0")) == 1){
                map.put("directMoney", Triple.of(directMerchantInfo.getAccountId(), directMoney, "D0"));
            }
            if (inDirectMoney.compareTo(new BigDecimal("0")) == 1){
                map.put("inDirectMoney", Triple.of(inDirectMerchantInfo.getAccountId(), inDirectMoney, "D0"));
            }

            return map;
        }catch (final Throwable throwable){
            log.error("商户[" + merchantId + "]请求进行升级费分润异常，交易订单号：" + orderNo +"异常信息：" + throwable.getMessage());
            throw throwable;
        }

    }


}
