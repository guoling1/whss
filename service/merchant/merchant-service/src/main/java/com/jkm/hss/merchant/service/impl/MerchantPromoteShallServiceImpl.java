package com.jkm.hss.merchant.service.impl;


import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;
import com.jkm.hss.dealer.enums.EnumDealerRateType;
import com.jkm.hss.dealer.enums.EnumProfitType;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.DealerUpgerdeRateService;
import com.jkm.hss.dealer.service.PartnerShallProfitDetailService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.MerchantPromoteShallService;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.UpgradeRules;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradeRulesService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-01-09.
 */
@Data
@Slf4j
@Service
public class MerchantPromoteShallServiceImpl implements MerchantPromoteShallService {


    @Autowired
    private UpgradeRulesService upgradeRulesService;

//    @Autowired
//    private OrderService orderService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private DealerService dealerService;

    @Autowired
    private DealerUpgerdeRateService dealerUpgerdeRateService;

    @Autowired
    private ProductService productService;

    @Autowired
    private PartnerShallProfitDetailService partnerShallProfitDetailService;
    /**
     * {@inheritDoc}
     * @param merchantId
     * @return
     */
    @Transactional
    @Override
    public Map<String, Triple<Long, BigDecimal, String>> merchantPromoteShall(final long merchantId, final String orderNo, final String businessNo,final BigDecimal tradeAmount) {
        //获取商户
        Map<String, Triple<Long, BigDecimal, String>> map;
        final MerchantInfo merchantInfo = this.merchantInfoService.selectById(merchantId).get();
        //判断商户类型
        if (!merchantInfo.isBelongToOem()){
            //公司
             map = this.merchantPromoteShallToHss(merchantInfo, orderNo, businessNo, tradeAmount);
        }else{
            //分公司
            map = this.merchantPromoteShallToOem(merchantInfo, orderNo, businessNo, tradeAmount);
        }
            return  map;
    }

    private Map<String, Triple<Long, BigDecimal, String>> merchantPromoteShallToOem(MerchantInfo merchantInfo, String orderNo, String businessNo, BigDecimal tradeAmount) {
        try{
            log.info("商户【" +merchantInfo.getId() +"]请求进行升级费分润：" + orderNo);
            Pair<BigDecimal, BigDecimal> pair = merchantInfoService.getUpgradeShareProfit(businessNo);
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
                final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
                detail.setProductType(EnumProductType.HSS.getId());
                detail.setMerchantId(merchantInfo.getId());
                detail.setMerchantName(merchantInfo.getName());
                detail.setOrderNo(orderNo);
                detail.setChannelType(0);
                detail.setTotalFee(waitAmount);
                detail.setWaitShallAmount(waitAmount);
                detail.setWaitShallOriginAmount(waitAmount);
                detail.setProfitType(EnumDealerRateType.UPGRADE.getId());
                detail.setChannelCost(new BigDecimal(0));
                detail.setChannelShallAmount(new BigDecimal(0));
                detail.setProductShallAmount(productMoney);
                detail.setFirstDealerId(0);
                detail.setFirstDealerShallAmount(new BigDecimal(0));
                detail.setSecondDealerId(0);
                detail.setSecondDealerShallAmount(new BigDecimal(0));
                detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                log.info("<<<<<<<<<<<<<<<<<<<<<,"+ inDirectMoney.toString() + directMoney.toString());
                if (directMoney.compareTo(new BigDecimal("0")) == 1){
                    detail.setFirstMerchantId(directMerchantInfo.getId());
                    detail.setFirstMerchantShallAmount(directMoney);
                    map.put("directMoney", Triple.of(directMerchantInfo.getAccountId(), pair.getLeft(), "D0"));
                }else{
                    detail.setFirstMerchantId(0);
                    detail.setFirstMerchantShallAmount(new BigDecimal(0));
                }
                if (inDirectMoney.compareTo(new BigDecimal("0")) == 1){
                    detail.setSecondMerchantId(inDirectMerchantInfo.getId());
                    detail.setSecondMerchantShallAmount(inDirectMoney);
                    map.put("inDirectMoney", Triple.of(inDirectMerchantInfo.getAccountId(), pair.getRight(), "D0"));
                }else{
                    detail.setSecondMerchantId(0);
                    detail.setSecondMerchantShallAmount(new BigDecimal(0));
                }
                this.partnerShallProfitDetailService.init(detail);
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
                secondMoney = (waitAmount.subtract(directMoney).subtract(inDirectMoney))
                        .multiply(dealerUpgerdeRates.getSecondDealerShareProfitRate()).setScale(2, BigDecimal.ROUND_DOWN);
            }else{
                secondDealer = null;
                secondMoney = new BigDecimal("0");
            }
            //一级代理分润 = （升级费 - 直推分润 - 间推分润）* 一级代理分润比例
            BigDecimal firstMoney;
            if (merchantInfo.getSecondDealerId() != 0){
                firstMoney = (waitAmount.subtract(inDirectMoney).subtract(directMoney))
                        .multiply(dealerUpgerdeRates.getFirstDealerShareProfitRate()).setScale(2, BigDecimal.ROUND_DOWN);
            }else{
                //没有二级代理
                firstMoney = (waitAmount.subtract(inDirectMoney).subtract(directMoney))
                        .multiply(dealerUpgerdeRates.getFirstDealerShareProfitRate().add(dealerUpgerdeRates.getSecondDealerShareProfitRate())).setScale(2, BigDecimal.ROUND_DOWN);
            }

            //oem 分润
            final Dealer oemInfo = this.dealerService.getById(merchantInfo.getOemId()).get();
            BigDecimal oemMoney = (waitAmount.subtract(inDirectMoney).subtract(directMoney))
                    .multiply(dealerUpgerdeRates.getOemShareRate()).setScale(2, BigDecimal.ROUND_DOWN);
            //金开门利润 = 升级费 - 直推分润 - 间推分润 - 一级代理分润 - 二级代理分润
            BigDecimal productMoney = waitAmount.subtract(directMoney).subtract(inDirectMoney).subtract(firstMoney).subtract(secondMoney).subtract(oemMoney);

            final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
            detail.setProductType(EnumProductType.HSS.getId());
            detail.setMerchantId(merchantInfo.getId());
            detail.setMerchantName(merchantInfo.getName());
            detail.setOrderNo(orderNo);
            detail.setChannelType(0);
            detail.setTotalFee(waitAmount);
            detail.setWaitShallAmount(waitAmount);
            detail.setWaitShallOriginAmount(waitAmount);
            detail.setProfitType(EnumDealerRateType.UPGRADE.getId());
            detail.setChannelCost(new BigDecimal(0));
            detail.setChannelShallAmount(new BigDecimal(0));
            detail.setProductShallAmount(productMoney);
            detail.setFirstDealerId(dealer.getId());
            detail.setFirstDealerShallAmount(firstMoney);
            detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));

            Map<String, Triple<Long, BigDecimal, String>> map = new HashMap<>();
            map.put("companyMoney", Triple.of(AccountConstants.JKM_ACCOUNT_ID, productMoney, "D0"));
            map.put("firstMoney", Triple.of(dealer.getAccountId(), firstMoney, "D0"));
            if (merchantInfo.getSecondDealerId() != 0){
                detail.setSecondDealerId(secondDealer.getId());
                detail.setSecondDealerShallAmount(secondMoney);
                map.put("secondMoney", Triple.of(secondDealer.getAccountId(), secondMoney, "D0"));
            }else{
                detail.setSecondDealerId(0);
                detail.setSecondDealerShallAmount(new BigDecimal(0));
            }

            if (directMoney.compareTo(new BigDecimal("0")) == 1){
                detail.setFirstMerchantId(directMerchantInfo.getId());
                detail.setFirstMerchantShallAmount(directMoney);
                map.put("directMoney", Triple.of(directMerchantInfo.getAccountId(), pair.getLeft(), "D0"));
            }else{
                detail.setFirstMerchantId(0);
                detail.setFirstMerchantShallAmount(new BigDecimal(0));
            }
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + inDirectMoney);
            if (inDirectMoney.compareTo(new BigDecimal("0")) == 1){
                detail.setSecondMerchantId(inDirectMerchantInfo.getId());
                detail.setSecondMerchantShallAmount(inDirectMoney);
                map.put("inDirectMoney", Triple.of(inDirectMerchantInfo.getAccountId(), pair.getRight(), "D0"));
            }else{
                detail.setSecondMerchantId(0);
                detail.setSecondMerchantShallAmount(new BigDecimal(0));
            }
            map.put("oemMoney",  Triple.of(oemInfo.getAccountId(), oemMoney, "D0"));
            this.partnerShallProfitDetailService.init(detail);
            log.info("交易单号[" + orderNo + "]请求就行收单分润:"+
                    "产品分润:" + productMoney + "O单分润:" + oemMoney + "1J分润:"+ firstMoney + "2J分润"+ secondMoney + directMoney + inDirectMoney);
            return map;
        }catch (final Throwable throwable){
            log.error("商户[" + merchantInfo.getId() + "]请求进行升级费分润异常，交易订单号：" +"异常信息：" + throwable.getMessage());
            throw throwable;
        }
    }

    private Map<String, Triple<Long, BigDecimal, String>> merchantPromoteShallToHss(MerchantInfo merchantInfo, String orderNo, String businessNo, BigDecimal tradeAmount) {
        try{
            log.info("商户【" +merchantInfo.getId() +"]请求进行升级费分润：" + orderNo);
            Pair<BigDecimal, BigDecimal> pair = merchantInfoService.getUpgradeShareProfit(businessNo);
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
                final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
                detail.setProductType(EnumProductType.HSS.getId());
                detail.setMerchantId(merchantInfo.getId());
                detail.setMerchantName(merchantInfo.getName());
                detail.setOrderNo(orderNo);
                detail.setChannelType(0);
                detail.setTotalFee(waitAmount);
                detail.setWaitShallAmount(waitAmount);
                detail.setWaitShallOriginAmount(waitAmount);
                detail.setProfitType(EnumDealerRateType.UPGRADE.getId());
                detail.setChannelCost(new BigDecimal(0));
                detail.setChannelShallAmount(new BigDecimal(0));
                detail.setProductShallAmount(productMoney);
                detail.setFirstDealerId(0);
                detail.setFirstDealerShallAmount(new BigDecimal(0));
                detail.setSecondDealerId(0);
                detail.setSecondDealerShallAmount(new BigDecimal(0));
                detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                log.info("<<<<<<<<<<<<<<<<<<<<<,"+ inDirectMoney.toString() + directMoney.toString());
                if (directMoney.compareTo(new BigDecimal("0")) == 1){
                    detail.setFirstMerchantId(directMerchantInfo.getId());
                    detail.setFirstMerchantShallAmount(directMoney);
                    map.put("directMoney", Triple.of(directMerchantInfo.getAccountId(), pair.getLeft(), "D0"));
                }else{
                    detail.setFirstMerchantId(0);
                    detail.setFirstMerchantShallAmount(new BigDecimal(0));
                }
                if (inDirectMoney.compareTo(new BigDecimal("0")) == 1){
                    detail.setSecondMerchantId(inDirectMerchantInfo.getId());
                    detail.setSecondMerchantShallAmount(inDirectMoney);
                    map.put("inDirectMoney", Triple.of(inDirectMerchantInfo.getAccountId(), pair.getRight(), "D0"));
                }else{
                    detail.setSecondMerchantId(0);
                    detail.setSecondMerchantShallAmount(new BigDecimal(0));
                }
                this.partnerShallProfitDetailService.init(detail);
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
                secondMoney = (waitAmount.subtract(directMoney).subtract(inDirectMoney))
                        .multiply(dealerUpgerdeRates.getSecondDealerShareProfitRate()).setScale(2, BigDecimal.ROUND_DOWN);
            }else{
                secondDealer = null;
                secondMoney = new BigDecimal("0");
            }
            //一级代理分润 = （升级费 - 直推分润 - 间推分润）* 一级代理分润比例
            BigDecimal firstMoney;
            if (merchantInfo.getSecondDealerId() != 0){
                firstMoney = (waitAmount.subtract(inDirectMoney).subtract(directMoney))
                        .multiply(dealerUpgerdeRates.getFirstDealerShareProfitRate()).setScale(2, BigDecimal.ROUND_DOWN);
            }else{
                //没有二级代理
                firstMoney = (waitAmount.subtract(inDirectMoney).subtract(directMoney))
                        .multiply(dealerUpgerdeRates.getFirstDealerShareProfitRate().add(dealerUpgerdeRates.getSecondDealerShareProfitRate())).setScale(2, BigDecimal.ROUND_DOWN);
            }

            //金开门利润 = 升级费 - 直推分润 - 间推分润 - 一级代理分润 - 二级代理分润
            BigDecimal productMoney = waitAmount.subtract(directMoney).subtract(inDirectMoney).subtract(firstMoney).subtract(secondMoney);

            final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
            detail.setProductType(EnumProductType.HSS.getId());
            detail.setMerchantId(merchantInfo.getId());
            detail.setMerchantName(merchantInfo.getName());
            detail.setOrderNo(orderNo);
            detail.setChannelType(0);
            detail.setTotalFee(waitAmount);
            detail.setWaitShallAmount(waitAmount);
            detail.setWaitShallOriginAmount(waitAmount);
            detail.setProfitType(EnumDealerRateType.UPGRADE.getId());
            detail.setChannelCost(new BigDecimal(0));
            detail.setChannelShallAmount(new BigDecimal(0));
            detail.setProductShallAmount(productMoney);
            detail.setFirstDealerId(dealer.getId());
            detail.setFirstDealerShallAmount(firstMoney);
            detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));

            Map<String, Triple<Long, BigDecimal, String>> map = new HashMap<>();
            map.put("companyMoney", Triple.of(AccountConstants.JKM_ACCOUNT_ID, productMoney, "D0"));
            map.put("firstMoney", Triple.of(dealer.getAccountId(), firstMoney, "D0"));
            if (merchantInfo.getSecondDealerId() != 0){
                detail.setSecondDealerId(secondDealer.getId());
                detail.setSecondDealerShallAmount(secondMoney);
                map.put("secondMoney", Triple.of(secondDealer.getAccountId(), secondMoney, "D0"));
            }else{
                detail.setSecondDealerId(0);
                detail.setSecondDealerShallAmount(new BigDecimal(0));
            }

            if (directMoney.compareTo(new BigDecimal("0")) == 1){
                detail.setFirstMerchantId(directMerchantInfo.getId());
                detail.setFirstMerchantShallAmount(directMoney);
                map.put("directMoney", Triple.of(directMerchantInfo.getAccountId(), pair.getLeft(), "D0"));
            }else{
                detail.setFirstMerchantId(0);
                detail.setFirstMerchantShallAmount(new BigDecimal(0));
            }
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>" + inDirectMoney);
            if (inDirectMoney.compareTo(new BigDecimal("0")) == 1){
                detail.setSecondMerchantId(inDirectMerchantInfo.getId());
                detail.setSecondMerchantShallAmount(inDirectMoney);
                map.put("inDirectMoney", Triple.of(inDirectMerchantInfo.getAccountId(), pair.getRight(), "D0"));
            }else{
                detail.setSecondMerchantId(0);
                detail.setSecondMerchantShallAmount(new BigDecimal(0));
            }
            this.partnerShallProfitDetailService.init(detail);
            return map;
        }catch (final Throwable throwable){
            log.error("商户[" + merchantInfo.getId()+ "]请求进行升级费分润异常，交易订单号：" +"异常信息：" + throwable.getMessage());
            throw throwable;
        }
    }


}
