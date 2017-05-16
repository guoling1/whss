package com.jkm.hss.bill.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.bill.service.CalculateService;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.DealerUpgerdeRate;
import com.jkm.hss.dealer.entity.PartnerShallProfitDetail;
import com.jkm.hss.dealer.enums.EnumDealerRateType;
import com.jkm.hss.dealer.service.*;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.MerchantPromoteShallService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.enums.EnumUpperChannel;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import com.jkm.hss.product.servcie.UpgradeRulesService;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private BasicChannelService basicChannelService;
    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param channelSign  101,102,103
     * @return
     */
    @Override
    public BigDecimal getMerchantPayPoundageRate(EnumProductType type,final long merchantId, final int channelSign) {
        //hss活动
        final Date beginDate = DateFormatUtil.parse("2017-04-22 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        final Date endDate = DateFormatUtil.parse("2017-05-31 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        final Date currentDate = new Date();
        final boolean isActTime = currentDate.after(beginDate) && currentDate.before(endDate);
        if ((EnumPayChannelSign.EL_UNIONPAY.getId() == channelSign) && isActTime){
            return new BigDecimal("0.0038");
        }

        if (type.getId().equals(EnumProductType.HSS.getId())){
            //hss
            final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
            return getMerchantRate(channelSign, merchant);

        }else{
            //hsy
            final List<AppAuUser> appAuUsers = this.hsyShopDao.findCorporateUserByShopID(merchantId);
            final AppAuUser appAuUser = appAuUsers.get(0);
            if (EnumPayChannelSign.idOf(channelSign).getPaymentChannel().getId() == EnumPaymentChannel.WECHAT_PAY.getId()){
                return  appAuUser.getWeixinRate();
            }else if (EnumPayChannelSign.idOf(channelSign).getPaymentChannel().getId() == EnumPaymentChannel.ALIPAY.getId()){
                return appAuUser.getAlipayRate();
            }else{
                return appAuUser.getFastRate();
            }

        }

    }

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param channelSign  101,102,103
     * @return
     */
    @Override
    public BigDecimal getMerchantWithdrawPoundage(EnumProductType type,final long merchantId, final int channelSign) {
        final Date beginDate = DateFormatUtil.parse("2017-04-22 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        final Date endDate = DateFormatUtil.parse("2017-05-31 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        final Date currentDate = new Date();
        final boolean isActTime = currentDate.after(beginDate) && currentDate.before(endDate);
        if ((EnumPayChannelSign.EL_UNIONPAY.getId() == channelSign) && isActTime){
            return new BigDecimal("3");
        }

        if (type.getId().equals(EnumProductType.HSS.getId())){
            //HSS
            final MerchantInfo merchant = this.merchantInfoService.selectById(merchantId).get();
            final Product product = this.productService.selectByType(type.getId()).get();
            /*if (0 == merchant.getDealerId()) {

                final ProductChannelDetail productChannelDetail = this.productChannelDetailService.selectByProductIdAndChannelId(product.getId(), channelSign).get();
                return productChannelDetail.getProductMerchantWithdrawFee().setScale(2);
            }*/
            return this.getMerchantWithdrawFee(merchant, channelSign);

        }else {
            //HSY
            final List<AppAuUser> appAuUsers = this.hsyShopDao.findCorporateUserByShopID(merchantId);
            final AppAuUser appAuUser = appAuUsers.get(0);
            if ( appAuUser.getDealerID() == 0){
                final Product product = this.productService.selectByType(type.getId()).get();
                final ProductChannelDetail productChannelDetail =
                        this.productChannelDetailService.selectByProductIdAndChannelId(product.getId(),channelSign).get();
                return productChannelDetail.getProductMerchantWithdrawFee().setScale(2);
            }
            final Dealer dealer = this.dealerService.getById(appAuUser.getDealerID()).get();
            final DealerChannelRate dealerChannelRate = this.dealerRateService.getByDealerIdAndProductIdAndChannelType(dealer.getId(), appAuUser.getProductID(),channelSign).get();
            return dealerChannelRate.getDealerMerchantWithdrawFee().setScale(2);
        }

    }

    private BigDecimal getMerchantWithdrawFee(MerchantInfo merchant, int channelSign) {

        final MerchantChannelRateRequest request = new MerchantChannelRateRequest();
        request.setMerchantId(merchant.getId());
        request.setProductId(merchant.getProductId());
        request.setChannelTypeSign(channelSign);
        final Optional<MerchantChannelRate> merchantChannelRateOptional = this.merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(request);
        Preconditions.checkArgument(merchantChannelRateOptional.isPresent(), merchant.getId() + "《《《商户对应的通道费率不存在！");

        return merchantChannelRateOptional.get().getMerchantWithdrawFee();
    }

    /**
     * {@inheritDoc}
     *
     * @param traderAmount  交易金额
     * @param merchantPayPoundageRate  商户支付手续费率
     * @return
     */
    @Override
    public BigDecimal getMerchantPayPoundage(final BigDecimal traderAmount, final BigDecimal merchantPayPoundageRate, final int channelSign) {

        //原始手续费
        final BigDecimal originDueSplitAmount = traderAmount.multiply(merchantPayPoundageRate);

        return this.calculateMerchantFee(traderAmount, originDueSplitAmount, channelSign);
    }

    //按照通道计算商户手续费，
    private BigDecimal calculateMerchantFee(BigDecimal totalFee, BigDecimal waitOriginMoney, int channelSign) {
        final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(channelSign).get();
        BigDecimal waitMoney;
        final EnumUpperChannel upperChannel = EnumPayChannelSign.idOf(channelSign).getUpperChannel();
        switch (upperChannel){
            case SAOMI:
                if (basicChannel.getLowestFee().compareTo(waitOriginMoney) == 1){
                    //手续费不足一分 , 按一分收
                    if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                        //支付金额一分,不收手续费
                        waitMoney = new BigDecimal("0");
                    }else{
                        waitMoney = basicChannel.getLowestFee();
                    }
                }else{
                    //收手续费,进一位,保留两位有效数字
                    waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
                }
                return waitMoney;
            case KAMENG:
                if (basicChannel.getLowestFee().compareTo(waitOriginMoney) == 1){
                    //手续费不足一分 , 按一分收
                    if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                        //支付金额一分,不收手续费
                        waitMoney = new BigDecimal("0");
                    }else{
                        waitMoney = basicChannel.getLowestFee();
                    }
                }else{
                    //收手续费,进一位,保留两位有效数字
                    waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
                }
                return waitMoney;
            case MOBAO:
                if (basicChannel.getLowestFee().compareTo(waitOriginMoney) == 1){
                    //手续费不足2毛 , 按2毛收
                    waitMoney = basicChannel.getLowestFee();
                }else{
                    //收手续费,进一位,保留两位有效数字
                    waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
                }
                return waitMoney;
            case EASY_LINK:
                if (basicChannel.getLowestFee().compareTo(waitOriginMoney) == 1){
                    //手续费不足两毛 , 按2毛收
                    waitMoney = basicChannel.getLowestFee();
                }else{
                    //hss活动
                    final Date beginDate = DateFormatUtil.parse("2017-04-22 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
                    final Date endDate = DateFormatUtil.parse("2017-05-31 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
                    final Date currentDate = new Date();
                    final boolean isActTime = currentDate.after(beginDate) && currentDate.before(endDate);
                    if ((EnumPayChannelSign.EL_UNIONPAY.getId() == channelSign) && isActTime){
                        waitMoney = totalFee.multiply(new BigDecimal("0.0038")).setScale(2,BigDecimal.ROUND_UP);
                        if (basicChannel.getLowestFee().compareTo(waitMoney) == 1){
                            waitMoney = basicChannel.getLowestFee();
                        }
                    }else{
                        //收手续费,进一位,保留两位有效数字
                        waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
                    }

                }
                return waitMoney;
            case SYJ:
                waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_HALF_UP);
                return  waitMoney;
            case HE_LI_UNIONPAY:
                //收手续费,进一位,保留两位有效数字
                waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
                return waitMoney;
            default:
                waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
                return waitMoney;
        }

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
            BigDecimal productMoney = waitAmount.subtract(inDirectMoney).subtract(directMoney).subtract(firstMoney).subtract(secondMoney);

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

        final MerchantChannelRateRequest request = new MerchantChannelRateRequest();
        request.setMerchantId(merchantInfo.getId());
        request.setProductId(merchantInfo.getProductId());
        request.setChannelTypeSign(channelSign);

        final Optional<MerchantChannelRate> merchantChannelRateOptional =
                this.merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(request);
        Preconditions.checkArgument(merchantChannelRateOptional.isPresent(), merchantInfo.getId() + "《《《商户对应的通道费率不存在！");

        return merchantChannelRateOptional.get().getMerchantPayRate();
    }
}
