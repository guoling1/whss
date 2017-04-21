package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumGlobalDealerLevel;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.GlobalID;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType2;
import com.jkm.hss.admin.helper.requestparam.DistributeQrCodeRequest;
import com.jkm.hss.admin.helper.responseparam.ActiveCodeCount;
import com.jkm.hss.admin.helper.responseparam.BossDistributeQRCodeRecordResponse;
import com.jkm.hss.admin.helper.responseparam.DistributeCodeCount;
import com.jkm.hss.admin.service.DistributeQRCodeRecordService;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.dealer.dao.DealerDao;
import com.jkm.hss.dealer.entity.*;
import com.jkm.hss.dealer.enums.*;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.helper.requestparam.*;
import com.jkm.hss.dealer.helper.response.DealerOfFirstDealerResponse;
import com.jkm.hss.dealer.helper.response.DistributeRecordResponse;
import com.jkm.hss.dealer.helper.response.FirstDealerResponse;
import com.jkm.hss.dealer.helper.response.SecondDealerResponse;
import com.jkm.hss.dealer.service.*;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.enums.EnumSource;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
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
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.entity.AppAuUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Slf4j
@Service
public class DealerServiceImpl implements DealerService {

    @Autowired
    private DealerDao dealerDao;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private DealerRateService dealerRateService;
    @Autowired
    private ShallProfitDetailService shallProfitDetailService;
    @Autowired
    private ProductService productService;
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private DistributeQRCodeRecordService distributeQRCodeRecordService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private CompanyProfitDetailService companyProfitDetailService;
    @Autowired
    private DealerUpgerdeRateService dealerUpgerdeRateService;
    @Autowired
    private PartnerShallProfitDetailService partnerShallProfitDetailService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    /**
     * {@inheritDoc}
     * 有问题
     * @param orderRecord
     * @return
     */
    public Map<String, Pair<Long,BigDecimal>> merchantAmount(OrderRecord orderRecord){
        return null;
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo  交易订单号
     * @param tradeAmount  交易金额
     * @param channelSign  101,102,103
     * @param merchantId  商户id
     * @return
     */
    @Override
    @Transactional
    public Map<String, Triple<Long, BigDecimal, BigDecimal>> shallProfit(final EnumProductType type,final String orderNo, final BigDecimal tradeAmount,

                                                                         final int channelSign, final long merchantId) {

        if (type.getId().equals(EnumProductType.HSS.getId())){

            //好收收收单分润
            try{
                final MerchantInfo merchantInfo = this.merchantInfoService.selectById(merchantId).get();
                //判断商户是否是直属商户
                if (merchantInfo.getFirstMerchantId() == 0){
                    //直属商户
                    return this.getShallProfitDirect(orderNo, tradeAmount, channelSign, merchantId);
                }else {
                    //推荐商户
                    return this.getShallProfitInDirect(orderNo, tradeAmount, channelSign, merchantId);
                }

            }catch (final Throwable throwable){
                log.error("交易订单号["+ orderNo + "]分润异常，异常信息：" + throwable.getMessage());
                throw  throwable;
            }
        }else{

            //好收银收单分润
            return this.getShallProfitDirectToHsy(orderNo, tradeAmount, channelSign, merchantId);
        }


    }

    //hsy 商户分润
    private Map<String,Triple<Long,BigDecimal,BigDecimal>> getShallProfitDirectToHsy(String orderNo, BigDecimal tradeAmount, int channelSign, long merchantId) {

        log.info("交易单号[" + orderNo + "]请求就行收单分润，分润金额：" + tradeAmount);
        final ShallProfitDetail detail = this.shallProfitDetailService.selectByOrderIdToHss(orderNo);
        if (detail != null){
            log.error("此订单分润业务已经处理过[" + orderNo +"]");
            return null;
        }
        //根据代理商id查询代理商信息
        final List<AppAuUser> appAuUsers = hsyShopDao.findCorporateUserByShopID(merchantId);
        final AppAuUser appAuUser = appAuUsers.get(0);
        //待分润金额
        final BigDecimal totalFee = tradeAmount;
        final Map<String, Triple<Long, BigDecimal, BigDecimal>> map = new HashMap<>();
        //判断该经销商属于哪个代理, 若不属于代理, 则分润进入公司资金帐户
        if (appAuUser.getDealerID() == 0){
            final ProductChannelDetail productChannelDetail =
                    this.productChannelDetailService.selectByProductIdAndChannelId(appAuUser.getProductID(), channelSign).get();
            final Optional<BasicChannel> channelOptional =  this.basicChannelService.selectByChannelTypeSign(channelSign);
            final BasicChannel basicChannel = channelOptional.get();
            //商户手续费
            BigDecimal merchantRate;
            final EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.idOf(channelSign);
            if (enumPayChannelSign.getPaymentChannel().getId() == EnumPaymentChannel.WECHAT_PAY.getId()){
                merchantRate = appAuUser.getWeixinRate();
            }else if (enumPayChannelSign.getPaymentChannel().getId() == EnumPaymentChannel.ALIPAY.getId()){
                merchantRate = appAuUser.getAlipayRate();
            }else{
                merchantRate = appAuUser.getFastRate();
            }
            final BigDecimal waitOriginMoney = totalFee.multiply(merchantRate);
            //计算商户的手续费
            final BigDecimal waitMoney = this.calculateMerchantFee(totalFee,waitOriginMoney,channelSign);

            //获取产品的信息, 产品通道的费率
            final Optional<Product> productOptional = this.productService.selectById(appAuUser.getProductID());
            final  Product product = productOptional.get();

            //通道成本
            BigDecimal basicMoney;
            final BigDecimal basicTrade = totalFee.multiply(basicChannel.getBasicTradeRate());
           //计算通道成本
            basicMoney = this.calculateChannelFee(basicTrade,channelSign);
            //通道分润
            final BigDecimal channelMoney = totalFee.multiply(productChannelDetail.getProductTradeRate().
                    subtract(basicChannel.getBasicTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //产品分润
            final BigDecimal productMoney;
            if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                //支付金额一分,不收手续费
                productMoney = new BigDecimal("0");
            }else{
                productMoney = waitMoney.subtract(channelMoney).subtract(basicMoney);
            }
            //记录通道, 产品分润明细
            final CompanyProfitDetail companyProfitDetail = new CompanyProfitDetail();
            companyProfitDetail.setProductType(EnumProductType.HSY.getId());
            companyProfitDetail.setMerchantId(merchantId);
            companyProfitDetail.setPaymentSn(orderNo);
            companyProfitDetail.setChannelType(channelSign);
            companyProfitDetail.setTotalFee(totalFee);
            companyProfitDetail.setWaitShallAmount(waitMoney);
            companyProfitDetail.setWaitShallOriginAmount(waitOriginMoney);
            companyProfitDetail.setProfitType(EnumProfitType.BALANCE.getId());
            companyProfitDetail.setProductShallAmount(productMoney);
            companyProfitDetail.setChannelCost(basicMoney);
            companyProfitDetail.setChannelShallAmount(channelMoney);
            companyProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
            this.companyProfitDetailService.add(companyProfitDetail);
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
            map.put("productMoney",Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
            return map;
        }

        final Dealer dealer = this.dealerDao.selectById(appAuUser.getDealerID());
        //查询该代理商的收单利率
        final DealerChannelRate dealerChannelRate =
                this.dealerRateService.getByDealerIdAndProductIdAndChannelType(dealer.getId(), appAuUser.getProductID(),channelSign).get();
        //获取产品的信息, 产品通道的费率
        final Optional<Product> productOptional = this.productService.selectById(dealerChannelRate.getProductId());
        final  Product product = productOptional.get();
        final Optional<ProductChannelDetail> productChannelDetailOptional =
                this.productChannelDetailService.selectByProductIdAndChannelId(product.getId(), channelSign);
        final ProductChannelDetail productChannelDetail = productChannelDetailOptional.get();
        final Optional<BasicChannel> channelOptional =  this.basicChannelService.selectByChannelTypeSign(channelSign);
        final BasicChannel basicChannel = channelOptional.get();
        //商户手续费
        BigDecimal merchantRate;
        final EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.idOf(channelSign);
        if (enumPayChannelSign.getPaymentChannel().getId() == EnumPaymentChannel.WECHAT_PAY.getId()){
            merchantRate = appAuUser.getWeixinRate();
        }else if (enumPayChannelSign.getPaymentChannel().getId() == EnumPaymentChannel.ALIPAY.getId()){
            merchantRate = appAuUser.getAlipayRate();
        }else{
            merchantRate = appAuUser.getFastRate();
        }
        //待分润金额
        final BigDecimal waitOriginMoney = totalFee.multiply(merchantRate);
        //计算商户手续费
        final BigDecimal waitMoney = this.calculateMerchantFee(totalFee, waitOriginMoney, channelSign);
        //判断代理商等级
        if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
            //一级
            //一级分润
            final BigDecimal firstMoney = totalFee.multiply(merchantRate.
                    subtract(dealerChannelRate.getDealerTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            final BigDecimal basicTrade = totalFee.multiply(basicChannel.getBasicTradeRate());
            //计算通道成本
            final BigDecimal basicMoney = this.calculateChannelFee(basicTrade, channelSign);

            //通道分润
            final BigDecimal channelMoney = totalFee.multiply(productChannelDetail.getProductTradeRate().
                    subtract(basicChannel.getBasicTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //产品分润
            final BigDecimal productMoney;
            if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                //支付金额一分,不收手续费
                productMoney = new BigDecimal("0");
            }else{
                productMoney = waitMoney.subtract(firstMoney).subtract(channelMoney).subtract(basicMoney);
            }
            //记录分润明细
            final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
            shallProfitDetail.setProductType(EnumProductType.HSY.getId());
            shallProfitDetail.setMerchantId(appAuUser.getId());
            shallProfitDetail.setTotalFee(totalFee);
            shallProfitDetail.setChannelType(channelSign);
            shallProfitDetail.setPaymentSn(orderNo);
            shallProfitDetail.setWaitShallAmount(waitMoney);
            shallProfitDetail.setWaitShallOriginAmount(waitOriginMoney);
            shallProfitDetail.setIsDirect(1);
            shallProfitDetail.setProfitType(EnumProfitType.BALANCE.getId());
            shallProfitDetail.setChannelCost(basicMoney);
            shallProfitDetail.setChannelShallAmount(channelMoney);
            shallProfitDetail.setProductShallAmount(productMoney);
            shallProfitDetail.setFirstDealerId(dealer.getId());
            shallProfitDetail.setFirstShallAmount(firstMoney);
            shallProfitDetail.setSecondDealerId(0);
            shallProfitDetail.setSecondShallAmount(new BigDecimal(0));
            shallProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
            this.shallProfitDetailService.init(shallProfitDetail);
            map.put("firstMoney", Triple.of(dealer.getAccountId(), firstMoney, dealerChannelRate.getDealerTradeRate()));
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
            map.put("productMoney",Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
        }else{

            //二级
            //获取二级的一级dealer信息
            final Dealer firstDealer = this.dealerDao.selectById(dealer.getFirstLevelDealerId());
            //查询二级的一级dealer 收单费率
            final DealerChannelRate firstDealerChannelRate =
                    this.dealerRateService.getByDealerIdAndProductIdAndChannelType(firstDealer.getId(), appAuUser.getProductID(),channelSign).get();
            //一级分润
            final BigDecimal firstMoney = totalFee.multiply(dealerChannelRate.getDealerTradeRate().
                    subtract(firstDealerChannelRate.getDealerTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //二级分润
            final BigDecimal secondMoney = totalFee.multiply(merchantRate.
                    subtract(dealerChannelRate.getDealerTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //通道分润
            final BigDecimal channelMoney = totalFee.multiply(productChannelDetail.getProductTradeRate().
                    subtract(basicChannel.getBasicTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);

            final BigDecimal basicTrade = totalFee.multiply(basicChannel.getBasicTradeRate());
            //通道成本
            final BigDecimal basicMoney = this.calculateChannelFee(basicTrade, channelSign);
            //产品分润
            final BigDecimal productMoney;
            if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                //支付金额一分,不收手续费
                productMoney = new BigDecimal("0");
            }else{
                productMoney = waitMoney.subtract(firstMoney).subtract(secondMoney).subtract(channelMoney).subtract(basicMoney);
            }

            //记录分润明细
            final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
            shallProfitDetail.setProductType(EnumProductType.HSY.getId());
            shallProfitDetail.setMerchantId(appAuUser.getId());
            shallProfitDetail.setTotalFee(totalFee);
            shallProfitDetail.setChannelType(channelSign);
            shallProfitDetail.setPaymentSn(orderNo);
            shallProfitDetail.setWaitShallAmount(waitMoney);
            shallProfitDetail.setWaitShallOriginAmount(waitOriginMoney);
            shallProfitDetail.setIsDirect(0);
            shallProfitDetail.setProfitType(EnumProfitType.BALANCE.getId());
            shallProfitDetail.setChannelCost(basicMoney);
            shallProfitDetail.setChannelShallAmount(channelMoney);
            shallProfitDetail.setProductShallAmount(productMoney);
            shallProfitDetail.setFirstDealerId(firstDealer.getId());
            shallProfitDetail.setFirstShallAmount(firstMoney);
            shallProfitDetail.setSecondDealerId(dealer.getId());
            shallProfitDetail.setSecondShallAmount(secondMoney);
            shallProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
            this.shallProfitDetailService.init(shallProfitDetail);
            map.put("firstMoney", Triple.of(firstDealer.getAccountId(), firstMoney, firstDealerChannelRate.getDealerTradeRate()));
            map.put("secondMoney", Triple.of(dealer.getAccountId(),secondMoney, dealerChannelRate.getDealerTradeRate()));
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
            map.put("productMoney",Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
        }
        log.info("订单" + orderNo + "分润处理成功,返回map成功");
        return map;
    }

    //间接商户分润 hss
    private Map<String,Triple<Long,BigDecimal,BigDecimal>> getShallProfitInDirect(String orderNo, BigDecimal tradeAmount, int channelSign, long merchantId) {
        Map<String,Triple<Long,BigDecimal,BigDecimal>> map = new HashMap<>();
        log.info("交易单号[" + orderNo + "]请求就行收单分润，分润金额：" + tradeAmount);
        final MerchantInfo merchantInfo = this.merchantInfoService.selectById(merchantId).get();
        //商户手续费 = 交易金额 * 商户费率
        final Product product = this.productService.selectByType(EnumProductType.HSS.getId()).get();
        final BigDecimal merchantRate = getMerchantRate(channelSign, merchantInfo);
        final BigDecimal originMoney = tradeAmount.multiply(getMerchantRate(channelSign, merchantInfo)).setScale(2, BigDecimal.ROUND_UP);
        //final BigDecimal waitOriginMoney = originMoney.setScale(2, BigDecimal.ROUND_UP);
        //计算商户手续费
        final BigDecimal waitOriginMoney = this.calculateMerchantFee(tradeAmount, originMoney, channelSign);
        //通道成本
        final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(channelSign).get();
        final BigDecimal basicTrade = tradeAmount.multiply(basicChannel.getBasicTradeRate());
        final BigDecimal basicMoney = this.calculateChannelFee(basicTrade, channelSign);

        //通道分润
        final ProductChannelDetail productChannelDetail = this.productChannelDetailService.selectByProductIdAndChannelId(product.getId(), channelSign).get();
        final BigDecimal channelMoney = tradeAmount.multiply(productChannelDetail.getProductTradeRate().
                subtract(basicChannel.getBasicTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
        //判断是否是公司直属商户发展的商户
        if (merchantInfo.getFirstDealerId() == 0){
            map.put("channelMoney", Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
            map.put("basicMoney", Triple.of(0L,basicMoney,basicChannel.getBasicTradeRate()));
            //上级商户 = （商户费率 -  上级商户）* 商户交易金额（如果商户费率低于或等于上级商户，那么上级商户无润）
            final MerchantInfo firstMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getFirstMerchantId()).get();
            //上级商户的费率
            final BigDecimal firstMerchantRate = getMerchantRate(channelSign, firstMerchantInfo);
            final BigDecimal firstMerchantMoney;
            if (merchantRate.compareTo(firstMerchantRate) == 1){
                firstMerchantMoney = merchantRate.subtract(firstMerchantRate).multiply(tradeAmount).setScale(2, BigDecimal.ROUND_DOWN);
            }else{
                firstMerchantMoney =  new BigDecimal(0);
            }

            if (merchantInfo.getSecondMerchantId() == 0){

                final BigDecimal productMoney = waitOriginMoney.subtract(basicMoney).subtract(channelMoney).subtract(firstMerchantMoney);
                //没有上上级
                final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
                detail.setProductType(EnumProductType.HSS.getId());
                detail.setMerchantId(merchantId);
                detail.setMerchantName(merchantInfo.getName());
                detail.setOrderNo(orderNo);
                detail.setChannelType(channelSign);
                detail.setTotalFee(tradeAmount);
                detail.setWaitShallAmount(waitOriginMoney);
                detail.setWaitShallOriginAmount(originMoney);
                detail.setProfitType(EnumDealerRateType.TRADE.getId());
                detail.setChannelCost(basicMoney);
                detail.setChannelShallAmount(channelMoney);
                detail.setProductShallAmount(productMoney);
                detail.setFirstDealerId(0);
                detail.setFirstDealerShallAmount(new BigDecimal(0));
                detail.setSecondDealerId(0);
                detail.setSecondDealerShallAmount(new BigDecimal(0));
                detail.setFirstMerchantId(firstMerchantInfo.getId());
                detail.setFirstMerchantShallAmount(firstMerchantMoney);
                detail.setSecondMerchantId(0);
                detail.setSecondMerchantShallAmount(new BigDecimal(0));
                detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                this.partnerShallProfitDetailService.init(detail);
                map.put("firstMerchantMoney", Triple.of(firstMerchantInfo.getAccountId(), firstMerchantMoney, getMerchantRate(channelSign,firstMerchantInfo)));
                map.put("productMoney", Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
                return map;
            }else{
                //上上级商户 = 【（商户费率 -  上上级商户 ）- |（商户费率 -  上级商户）|】* 商户交易金额（如果商户费率低于或等于上级商户，那么上级商户无润）
                final MerchantInfo secondMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getSecondMerchantId()).get();
                final BigDecimal secondMerchantRate = getMerchantRate(channelSign, secondMerchantInfo);
                final BigDecimal secondSelfMerchantRate;
                final BigDecimal a = merchantRate.subtract(secondMerchantRate);
                final BigDecimal b = merchantRate.subtract(firstMerchantRate);
                if (a.compareTo(new BigDecimal("0"))  ==1 ){
                    //商户大于上上级
                    if (b.compareTo(new BigDecimal("0")) == 1){

                        final BigDecimal  d = a.subtract(b);
                        if (d.compareTo( new BigDecimal("0")) == 1){
                            secondSelfMerchantRate = d;
                        }else {
                            secondSelfMerchantRate = new BigDecimal("0");
                        }
                    }else{
                        secondSelfMerchantRate = a;
                    }

                }else{
                    //商户小于等于上上级
                    secondSelfMerchantRate = new BigDecimal("0");
                }

                final BigDecimal secondMerchantMoney = secondSelfMerchantRate.multiply(tradeAmount).setScale(2, BigDecimal.ROUND_DOWN);
                final BigDecimal productMoney = waitOriginMoney.subtract(basicMoney).subtract(channelMoney).subtract(firstMerchantMoney).subtract(secondMerchantMoney);
                //有上上级
                final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
                detail.setProductType(EnumProductType.HSS.getId());
                detail.setMerchantId(merchantId);
                detail.setMerchantName(merchantInfo.getName());
                detail.setOrderNo(orderNo);
                detail.setChannelType(channelSign);
                detail.setTotalFee(tradeAmount);
                detail.setWaitShallAmount(waitOriginMoney);
                detail.setWaitShallOriginAmount(originMoney);
                detail.setProfitType(EnumDealerRateType.TRADE.getId());
                detail.setChannelCost(basicMoney);
                detail.setChannelShallAmount(channelMoney);
                detail.setProductShallAmount(productMoney);
                detail.setFirstDealerId(0);
                detail.setFirstDealerShallAmount(new BigDecimal(0));
                detail.setSecondDealerId(0);
                detail.setSecondDealerShallAmount(new BigDecimal(0));
                detail.setFirstMerchantId(firstMerchantInfo.getId());
                detail.setFirstMerchantShallAmount(firstMerchantMoney);
                detail.setSecondMerchantId(secondMerchantInfo.getId());
                detail.setSecondMerchantShallAmount(secondMerchantMoney);
                detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                this.partnerShallProfitDetailService.init(detail);
                map.put("firstMerchantMoney", Triple.of(firstMerchantInfo.getAccountId(), firstMerchantMoney, getMerchantRate(channelSign,firstMerchantInfo)));
                map.put("secondMerchantMoney", Triple.of(secondMerchantInfo.getAccountId(), secondMerchantMoney, getMerchantRate(channelSign,secondMerchantInfo)));
                map.put("productMoney", Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
                return map;
            }

        }



        //一级代理
        final Dealer firstDealer = this.dealerDao.selectById(merchantInfo.getFirstDealerId());
        final DealerUpgerdeRate dealerUpgerdeRate = this.dealerUpgerdeRateService.selectByDealerIdAndTypeAndProductId
                (merchantInfo.getFirstDealerId(), EnumDealerRateType.TRADE, product.getId());
        final BigDecimal totalProfitSpace = firstDealer.getTotalProfitSpace();
        //二级代理信息
        final Dealer secondDealer = this.dealerDao.selectById(merchantInfo.getSecondDealerId());
        //上级商户 = （商户费率 -  上级商户）* 商户交易金额（如果商户费率低于或等于上级商户，那么上级商户无润）
        final MerchantInfo firstMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getFirstMerchantId()).get();
        //上级商户的费率
        final BigDecimal firstMerchantRate = getMerchantRate(channelSign, firstMerchantInfo);
        final BigDecimal firstMerchantMoney;
        final BigDecimal firstMerchantSelfRate;
        if (merchantRate.compareTo(firstMerchantRate) == 1){
            firstMerchantMoney = merchantRate.subtract(firstMerchantRate).multiply(tradeAmount).setScale(2, BigDecimal.ROUND_DOWN);
            firstMerchantSelfRate = merchantRate.subtract(firstMerchantRate);
        }else{
            firstMerchantMoney =  new BigDecimal(0);
            firstMerchantSelfRate = new BigDecimal(0);
        }
        //上上级商户 = 【（商户费率 -  上上级商户 ）- |（商户费率 -  上级商户）|】* 商户交易金额（如果商户费率低于或等于上级商户，那么上级商户无润）
        if (merchantInfo.getSecondMerchantId() == 0){

            if (merchantInfo.getSecondDealerId() == 0){
                //没有上上级,一级下的商户
                final BigDecimal space = totalProfitSpace.subtract(firstMerchantSelfRate);
                //一级代理（间接商户）=（利润空间 - 上级商户分润）*  一级分润比例
                final BigDecimal add = (dealerUpgerdeRate.getSecondDealerShareProfitRate()).add(dealerUpgerdeRate.getFirstDealerShareProfitRate());
                final BigDecimal firstMoney = tradeAmount.multiply(space.multiply(add)).setScale(2,BigDecimal.ROUND_DOWN);
                //金开门利润 = 商户手续费 -  一代  一级商户
                final BigDecimal productMoney = waitOriginMoney.subtract(firstMoney).subtract(firstMerchantMoney).subtract(basicMoney).subtract(channelMoney);
                map.put("basicMoney", Triple.of(0L,basicMoney,basicChannel.getBasicTradeRate()));
                map.put("channelMoney", Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
                map.put("productMoney", Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
                map.put("firstMerchantMoney", Triple.of(firstMerchantInfo.getAccountId(), firstMerchantMoney, getMerchantRate(channelSign, firstMerchantInfo)));
                map.put("firstMoney", Triple.of(firstDealer.getAccountId(), firstMoney, dealerUpgerdeRate.getFirstDealerShareProfitRate()));

                final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
                detail.setProductType(EnumProductType.HSS.getId());
                detail.setMerchantId(merchantId);
                detail.setMerchantName(merchantInfo.getName());
                detail.setOrderNo(orderNo);
                detail.setChannelType(channelSign);
                detail.setTotalFee(tradeAmount);
                detail.setWaitShallAmount(waitOriginMoney);
                detail.setWaitShallOriginAmount(originMoney);
                detail.setProfitType(EnumDealerRateType.TRADE.getId());
                detail.setChannelCost(basicMoney);
                detail.setChannelShallAmount(channelMoney);
                detail.setProductShallAmount(productMoney);
                detail.setFirstDealerId(firstDealer.getId());
                detail.setFirstDealerShallAmount(firstMoney);
                detail.setSecondDealerId(0);
                detail.setSecondDealerShallAmount(new BigDecimal(0));
                detail.setFirstMerchantId(firstMerchantInfo.getId());
                detail.setFirstMerchantShallAmount(firstMerchantMoney);
                detail.setSecondMerchantId(0);
                detail.setSecondMerchantShallAmount(new BigDecimal(0));
                detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                this.partnerShallProfitDetailService.init(detail);
                return map;
            }else{
                //没有上上级，二级下的商户
                final BigDecimal space = totalProfitSpace.subtract(firstMerchantSelfRate);
                //二级代理（间接商户）=（利润空间 - 上级商户分润 ）* 二级分润比例（利润空间，不同的一级代理不同）
                final BigDecimal secondMoney = tradeAmount.multiply(space.multiply(dealerUpgerdeRate.getSecondDealerShareProfitRate())).setScale(2,BigDecimal.ROUND_DOWN);
                //一级代理（间接商户）=（利润空间 - 上级商户分润 ）*  一级分润比例
                final BigDecimal firstMoney = tradeAmount.multiply(space.multiply(dealerUpgerdeRate.getFirstDealerShareProfitRate())).setScale(2,BigDecimal.ROUND_DOWN);
                //金开门利润 = 商户手续费 -  一代 - 二代 - 一级商户 - 二级商户
                final BigDecimal productMoney = waitOriginMoney.subtract(firstMoney).subtract(secondMoney).subtract(firstMerchantMoney).subtract(basicMoney).subtract(channelMoney);
                map.put("basicMoney", Triple.of(0L,basicMoney,basicChannel.getBasicTradeRate()));
                map.put("channelMoney", Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
                map.put("productMoney", Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
                map.put("firstMerchantMoney", Triple.of(firstMerchantInfo.getAccountId(), firstMerchantMoney, getMerchantRate(channelSign, firstMerchantInfo)));
                map.put("secondMoney", Triple.of(secondDealer.getAccountId(), secondMoney, dealerUpgerdeRate.getSecondDealerShareProfitRate()));
                map.put("firstMoney", Triple.of(firstDealer.getAccountId(), firstMoney, dealerUpgerdeRate.getFirstDealerShareProfitRate()));

                final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
                detail.setProductType(EnumProductType.HSS.getId());
                detail.setMerchantId(merchantId);
                detail.setMerchantName(merchantInfo.getName());
                detail.setOrderNo(orderNo);
                detail.setChannelType(channelSign);
                detail.setTotalFee(tradeAmount);
                detail.setWaitShallAmount(waitOriginMoney);
                detail.setWaitShallOriginAmount(originMoney);
                detail.setProfitType(EnumDealerRateType.TRADE.getId());
                detail.setChannelCost(basicMoney);
                detail.setChannelShallAmount(channelMoney);
                detail.setProductShallAmount(productMoney);
                detail.setFirstDealerId(firstDealer.getId());
                detail.setFirstDealerShallAmount(firstMoney);
                detail.setSecondDealerId(secondDealer.getId());
                detail.setSecondDealerShallAmount(secondMoney);
                detail.setFirstMerchantId(firstMerchantInfo.getId());
                detail.setFirstMerchantShallAmount(firstMerchantMoney);
                detail.setSecondMerchantId(0);
                detail.setSecondMerchantShallAmount(new BigDecimal(0));
                detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                this.partnerShallProfitDetailService.init(detail);
                return map;
            }

        }else {

            //上上级商户 = 【（商户费率 -  上上级商户 -  上级商户）|】* 商户交易金额（如果商户费率低于或等于上级商户，那么上级商户无润）
            final MerchantInfo secondMerchantInfo = this.merchantInfoService.selectById(merchantInfo.getSecondMerchantId()).get();
            final BigDecimal secondMerchantRate = getMerchantRate(channelSign, secondMerchantInfo);
            final BigDecimal secondSelfMerchantRate;
            final BigDecimal a = merchantRate.subtract(secondMerchantRate);
            final BigDecimal b = merchantRate.subtract(firstMerchantRate);
            if (a.compareTo(new BigDecimal("0"))  ==1 ){
                //商户大于上上级
                if (b.compareTo(new BigDecimal("0")) == 1){

                    final BigDecimal  d = a.subtract(b);
                    if (d.compareTo( new BigDecimal("0")) == 1){
                        secondSelfMerchantRate = d;
                    }else {
                        secondSelfMerchantRate = new BigDecimal("0");
                    }
                }else{
                    secondSelfMerchantRate = a;
                }

            }else{
                //商户小于等于上上级
                secondSelfMerchantRate = new BigDecimal("0");
            }
            final BigDecimal secondMerchantMoney = secondSelfMerchantRate.multiply(tradeAmount).setScale(2, BigDecimal.ROUND_DOWN);
            if (merchantInfo.getSecondDealerId() == 0){
                //有上上级，一级下的商户
                final BigDecimal space = totalProfitSpace.subtract(firstMerchantSelfRate).subtract(secondSelfMerchantRate);
                //一级代理（间接商户）=（利润空间 - 上级商户分润 - 上上级商户分润）*  一级分润比例
                final BigDecimal add = (dealerUpgerdeRate.getSecondDealerShareProfitRate()).add(dealerUpgerdeRate.getFirstDealerShareProfitRate());
                final BigDecimal firstMoney = tradeAmount.multiply(space.multiply(add)).setScale(2,BigDecimal.ROUND_DOWN);
                //金开门利润 = 商户手续费 -  一代  一级商户 - 二级商户
                final BigDecimal productMoney = waitOriginMoney.subtract(firstMoney).subtract(firstMerchantMoney).subtract(secondMerchantMoney).subtract(basicMoney).subtract(channelMoney);
                map.put("basicMoney", Triple.of(0L,basicMoney,basicChannel.getBasicTradeRate()));
                map.put("channelMoney", Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
                map.put("productMoney", Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
                map.put("firstMerchantMoney", Triple.of(firstMerchantInfo.getAccountId(), firstMerchantMoney, getMerchantRate(channelSign, firstMerchantInfo)));
                map.put("secondMerchantMoney", Triple.of(secondMerchantInfo.getAccountId(), secondMerchantMoney, getMerchantRate(channelSign, secondMerchantInfo)));
                map.put("firstMoney", Triple.of(firstDealer.getAccountId(), firstMoney, dealerUpgerdeRate.getFirstDealerShareProfitRate()));

                final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
                detail.setProductType(EnumProductType.HSS.getId());
                detail.setMerchantId(merchantId);
                detail.setMerchantName(merchantInfo.getName());
                detail.setOrderNo(orderNo);
                detail.setChannelType(channelSign);
                detail.setTotalFee(tradeAmount);
                detail.setWaitShallAmount(waitOriginMoney);
                detail.setWaitShallOriginAmount(originMoney);
                detail.setProfitType(EnumDealerRateType.TRADE.getId());
                detail.setChannelCost(basicMoney);
                detail.setChannelShallAmount(channelMoney);
                detail.setProductShallAmount(productMoney);
                detail.setFirstDealerId(firstDealer.getId());
                detail.setFirstDealerShallAmount(firstMoney);
                detail.setSecondDealerId(0);
                detail.setSecondDealerShallAmount(new BigDecimal(0));
                detail.setFirstMerchantId(firstMerchantInfo.getId());
                detail.setFirstMerchantShallAmount(firstMerchantMoney);
                detail.setSecondMerchantId(secondMerchantInfo.getId());
                detail.setSecondMerchantShallAmount(secondMerchantMoney);
                detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                this.partnerShallProfitDetailService.init(detail);
                return map;

            }else{
                //有上上级，二级下的商户
                final BigDecimal space = totalProfitSpace.subtract(firstMerchantSelfRate).subtract(secondSelfMerchantRate);
                //二级代理（间接商户）=（利润空间 - 上级商户分润 - 上上级商户分润）* 二级分润比例（利润空间，不同的一级代理不同）
                final BigDecimal secondMoney = tradeAmount.multiply(space.multiply(dealerUpgerdeRate.getSecondDealerShareProfitRate())).setScale(2,BigDecimal.ROUND_DOWN);
                //一级代理（间接商户）=（利润空间 - 上级商户分润 - 上上级商户分润）*  一级分润比例
                final BigDecimal firstMoney = tradeAmount.multiply(space.multiply(dealerUpgerdeRate.getFirstDealerShareProfitRate())).setScale(2,BigDecimal.ROUND_DOWN);
                //金开门利润 = 商户手续费 -  一代  - 二代 一级商户 - 二级商户
                final BigDecimal productMoney = waitOriginMoney.subtract(firstMoney).subtract(secondMoney).subtract(firstMerchantMoney).subtract(secondMerchantMoney).subtract(basicMoney).subtract(channelMoney);
                map.put("basicMoney", Triple.of(0L,basicMoney,basicChannel.getBasicTradeRate()));
                map.put("channelMoney", Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
                map.put("productMoney", Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
                map.put("firstMerchantMoney", Triple.of(firstMerchantInfo.getAccountId(), firstMerchantMoney, getMerchantRate(channelSign, firstMerchantInfo)));
                map.put("secondMerchantMoney", Triple.of(secondMerchantInfo.getAccountId(), secondMerchantMoney, getMerchantRate(channelSign, secondMerchantInfo)));
                map.put("firstMoney", Triple.of(firstDealer.getAccountId(), firstMoney, dealerUpgerdeRate.getFirstDealerShareProfitRate()));
                map.put("secondMoney", Triple.of(secondDealer.getAccountId(), secondMoney, dealerUpgerdeRate.getSecondDealerShareProfitRate()));

                final PartnerShallProfitDetail detail = new PartnerShallProfitDetail();
                detail.setProductType(EnumProductType.HSS.getId());
                detail.setMerchantId(merchantId);
                detail.setMerchantName(merchantInfo.getName());
                detail.setOrderNo(orderNo);
                detail.setChannelType(channelSign);
                detail.setTotalFee(tradeAmount);
                detail.setWaitShallAmount(waitOriginMoney);
                detail.setWaitShallOriginAmount(originMoney);
                detail.setProfitType(EnumDealerRateType.TRADE.getId());
                detail.setChannelCost(basicMoney);
                detail.setChannelShallAmount(channelMoney);
                detail.setProductShallAmount(productMoney);
                detail.setFirstDealerId(firstDealer.getId());
                detail.setFirstDealerShallAmount(firstMoney);
                detail.setSecondDealerId(secondDealer.getId());
                detail.setSecondDealerShallAmount(secondMoney);
                detail.setFirstMerchantId(firstMerchantInfo.getId());
                detail.setFirstMerchantShallAmount(firstMerchantMoney);
                detail.setSecondMerchantId(secondMerchantInfo.getId());
                detail.setSecondMerchantShallAmount(secondMerchantMoney);
                detail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                this.partnerShallProfitDetailService.init(detail);

                return map;

            }
        }

    }

    private BigDecimal getMerchantRate(int channelSign, final MerchantInfo merchantInfo){

        final MerchantChannelRateRequest request = new MerchantChannelRateRequest();
        request.setMerchantId(merchantInfo.getId());
        request.setChannelTypeSign(channelSign);
        request.setProductId(merchantInfo.getProductId());
        final Optional<MerchantChannelRate> merchantChannelRateOptional =
                this.merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(request);
        Preconditions.checkArgument(merchantChannelRateOptional.isPresent(), merchantInfo.getId() + "《《《《商户费率表不存在此通道的费率");

        return merchantChannelRateOptional.get().getMerchantPayRate();
    }
    //直属商户分润 hss
    private Map<String,Triple<Long,BigDecimal,BigDecimal>> getShallProfitDirect(String orderNo, BigDecimal tradeAmount, int channelSign, long merchantId) {
        log.info("交易单号[" + orderNo + "]请求就行收单分润，分润金额：" + tradeAmount);
        final ShallProfitDetail detail = this.shallProfitDetailService.selectByOrderIdToHss(orderNo);
        if (detail != null){
            log.error("此订单分润业务已经处理过[" + orderNo +"]");
            return null;
        }
        //根据代理商id查询代理商信息
        final Optional<MerchantInfo> optional = this.merchantInfoService.selectById(merchantId);
        Preconditions.checkArgument(optional.isPresent(), "商户信息不存在");
        final MerchantInfo merchantInfo = optional.get();
        //待分润金额
        final BigDecimal totalFee = tradeAmount;
        final Map<String, Triple<Long, BigDecimal, BigDecimal>> map = new HashMap<>();
        //判断该经销商属于哪个代理, 若不属于代理, 则分润进入公司资金帐户
        if (merchantInfo.getDealerId() == 0){
            //final List<ProductChannelDetail> list = this.productChannelDetailService.selectByChannelTypeSign(channelSign);
            final ProductChannelDetail productChannelDetail =
                    this.productChannelDetailService.selectByProductIdAndChannelId(merchantInfo.getProductId(),channelSign).get();
            final Optional<BasicChannel> channelOptional =  this.basicChannelService.selectByChannelTypeSign(channelSign);
            final BasicChannel basicChannel = channelOptional.get();
            //商户手续费
            final BigDecimal merchantRate = this.getMerchantRate(channelSign, merchantInfo);
            final BigDecimal waitOriginMoney = totalFee.multiply(merchantRate);

            //计算商户手续费，按照通道来
            final BigDecimal waitMoney = this.calculateMerchantFee(totalFee, waitOriginMoney, channelSign);
            //获取产品的信息, 产品通道的费率
            final Optional<Product> productOptional = this.productService.selectById(merchantInfo.getProductId());
            final  Product product = productOptional.get();

            //通道成本， 不同通道成本计算不同
            final BigDecimal basicTrade = totalFee.multiply(basicChannel.getBasicTradeRate());
            final BigDecimal basicMoney = this.calculateChannelFee(basicTrade, channelSign);

            //通道分润
            final BigDecimal channelMoney = totalFee.multiply(productChannelDetail.getProductTradeRate().
                    subtract(basicChannel.getBasicTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //产品分润
            final BigDecimal productMoney;
            if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                //支付金额一分,不收手续费
                productMoney = new BigDecimal("0");
            }else{
                productMoney = waitMoney.subtract(channelMoney).subtract(basicMoney);
            }
            //记录通道, 产品分润明细
            final CompanyProfitDetail companyProfitDetail = new CompanyProfitDetail();
            companyProfitDetail.setProductType(EnumProductType.HSS.getId());
            companyProfitDetail.setMerchantId(merchantId);
            companyProfitDetail.setPaymentSn(orderNo);
            companyProfitDetail.setChannelType(channelSign);
            companyProfitDetail.setTotalFee(totalFee);
            companyProfitDetail.setWaitShallAmount(waitMoney);
            companyProfitDetail.setWaitShallOriginAmount(waitOriginMoney);
            companyProfitDetail.setProfitType(EnumProfitType.BALANCE.getId());
            companyProfitDetail.setProductShallAmount(productMoney);
            companyProfitDetail.setChannelCost(basicMoney);
            companyProfitDetail.setChannelShallAmount(channelMoney);
            companyProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
            this.companyProfitDetailService.add(companyProfitDetail);
            map.put("basicMoney", Triple.of(0L,basicMoney,basicChannel.getBasicTradeRate()));
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
            map.put("productMoney",Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
            return map;
        }

        final Dealer dealer = this.dealerDao.selectById(merchantInfo.getDealerId());
        //查询该代理商的收单利率
        final DealerChannelRate dealerChannelRate =
                this.dealerRateService.getByDealerIdAndProductIdAndChannelType(dealer.getId(), merchantInfo.getProductId(),channelSign).get();
        //获取产品的信息, 产品通道的费率
        final Optional<Product> productOptional = this.productService.selectById(dealerChannelRate.getProductId());
        final  Product product = productOptional.get();
        final Optional<ProductChannelDetail> productChannelDetailOptional =
                this.productChannelDetailService.selectByProductIdAndChannelId(product.getId(), channelSign);
        final ProductChannelDetail productChannelDetail = productChannelDetailOptional.get();
        final Optional<BasicChannel> channelOptional =  this.basicChannelService.selectByChannelTypeSign(channelSign);
        final BasicChannel basicChannel = channelOptional.get();
        //商户手续费
        final BigDecimal merchantRate = this.getMerchantRate(channelSign, merchantInfo);
        //待分润金额
        final BigDecimal waitOriginMoney = totalFee.multiply(merchantRate);

        //计算商户手续费，按照通道来
        final BigDecimal waitMoney = this.calculateMerchantFee(totalFee, waitOriginMoney, channelSign);
        //判断代理商等级
        if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
            //一级
            //一级分润
            final BigDecimal firstMoney = totalFee.multiply(merchantRate.
                    subtract(dealerChannelRate.getDealerTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //通道成本
            final BigDecimal basicTrade = totalFee.multiply(basicChannel.getBasicTradeRate());
            final BigDecimal basicMoney = this.calculateChannelFee(basicTrade, channelSign);
            //通道分润
            final BigDecimal channelMoney = totalFee.multiply(productChannelDetail.getProductTradeRate().
                    subtract(basicChannel.getBasicTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //产品分润
            final BigDecimal productMoney;
            if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                //支付金额一分,不收手续费
                productMoney = new BigDecimal("0");
            }else{
                productMoney = waitMoney.subtract(firstMoney).subtract(channelMoney).subtract(basicMoney);
            }
            //记录分润明细
            final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
            shallProfitDetail.setProductType(EnumProductType.HSS.getId());
            shallProfitDetail.setMerchantId(merchantInfo.getId());
            shallProfitDetail.setTotalFee(totalFee);
            shallProfitDetail.setChannelType(channelSign);
            shallProfitDetail.setPaymentSn(orderNo);
            shallProfitDetail.setWaitShallAmount(waitMoney);
            shallProfitDetail.setWaitShallOriginAmount(waitOriginMoney);
            shallProfitDetail.setIsDirect(1);
            shallProfitDetail.setProfitType(EnumProfitType.BALANCE.getId());
            shallProfitDetail.setChannelCost(basicMoney);
            shallProfitDetail.setChannelShallAmount(channelMoney);
            shallProfitDetail.setProductShallAmount(productMoney);
            shallProfitDetail.setFirstDealerId(dealer.getId());
            shallProfitDetail.setFirstShallAmount(firstMoney);
            shallProfitDetail.setSecondDealerId(0);
            shallProfitDetail.setSecondShallAmount(new BigDecimal(0));
            shallProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
            this.shallProfitDetailService.init(shallProfitDetail);
            map.put("basicMoney", Triple.of(0L,basicMoney,basicChannel.getBasicTradeRate()));
            map.put("firstMoney", Triple.of(dealer.getAccountId(), firstMoney, dealerChannelRate.getDealerTradeRate()));
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
            map.put("productMoney",Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
        }else{

            //二级
            //获取二级的一级dealer信息
            final Dealer firstDealer = this.dealerDao.selectById(dealer.getFirstLevelDealerId());
            //查询二级的一级dealer 收单费率
            final DealerChannelRate firstDealerChannelRate =
                    this.dealerRateService.getByDealerIdAndProductIdAndChannelType(firstDealer.getId(), merchantInfo.getProductId(),channelSign).get();
            //一级分润
            final BigDecimal firstMoney = totalFee.multiply(dealerChannelRate.getDealerTradeRate().
                    subtract(firstDealerChannelRate.getDealerTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //二级分润
            final BigDecimal secondMoney = totalFee.multiply(merchantRate.
                    subtract(dealerChannelRate.getDealerTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //通道分润
            final BigDecimal channelMoney = totalFee.multiply(productChannelDetail.getProductTradeRate().
                    subtract(basicChannel.getBasicTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //通道成本
            final BigDecimal basicTrade = totalFee.multiply(basicChannel.getBasicTradeRate());
            final BigDecimal basicMoney = this.calculateChannelFee(basicTrade, channelSign);
            //产品分润
            final BigDecimal productMoney;
            if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                //支付金额一分,不收手续费
                productMoney = new BigDecimal("0");
            }else{
                productMoney = waitMoney.subtract(firstMoney).subtract(secondMoney).subtract(channelMoney).subtract(basicMoney);
            }

            //记录分润明细
            final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
            shallProfitDetail.setProductType(EnumProductType.HSS.getId());
            shallProfitDetail.setMerchantId(merchantInfo.getId());
            shallProfitDetail.setTotalFee(totalFee);
            shallProfitDetail.setChannelType(channelSign);
            shallProfitDetail.setPaymentSn(orderNo);
            shallProfitDetail.setWaitShallAmount(waitMoney);
            shallProfitDetail.setWaitShallOriginAmount(waitOriginMoney);
            shallProfitDetail.setIsDirect(0);
            shallProfitDetail.setProfitType(EnumProfitType.BALANCE.getId());
            shallProfitDetail.setChannelCost(basicMoney);
            shallProfitDetail.setChannelShallAmount(channelMoney);
            shallProfitDetail.setProductShallAmount(productMoney);
            shallProfitDetail.setFirstDealerId(firstDealer.getId());
            shallProfitDetail.setFirstShallAmount(firstMoney);
            shallProfitDetail.setSecondDealerId(dealer.getId());
            shallProfitDetail.setSecondShallAmount(secondMoney);
            shallProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
            this.shallProfitDetailService.init(shallProfitDetail);
            map.put("basicMoney", Triple.of(0L,basicMoney,basicChannel.getBasicTradeRate()));
            map.put("firstMoney", Triple.of(firstDealer.getAccountId(), firstMoney, firstDealerChannelRate.getDealerTradeRate()));
            map.put("secondMoney", Triple.of(dealer.getAccountId(),secondMoney, dealerChannelRate.getDealerTradeRate()));
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney, basicChannel.getBasicTradeRate()));
            map.put("productMoney",Triple.of(product.getAccountId(), productMoney, productChannelDetail.getProductTradeRate()));
        }
        log.info("订单" + orderNo + "分润处理成功,返回map成功");
        return map;
    }

    //按照通道计算通道成本
    private BigDecimal calculateChannelFee(BigDecimal basicTrade, int channelSign) {

        BigDecimal basicMoney;
        final EnumUpperChannel upperChannel = EnumPayChannelSign.idOf(channelSign).getUpperChannel();
        switch (upperChannel){
            case SAOMI:
                if (new BigDecimal("0.01").compareTo(basicTrade) == 1){
                    //通道成本不足一分 , 按一分收
                    basicMoney = new BigDecimal("0.01");

                }else{
                    //超过一分,四舍五入,保留两位有效数字
                    basicMoney = basicTrade.setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                return basicMoney;
            case KAMENG:
                if (new BigDecimal("0.01").compareTo(basicTrade) == 1){
                    //通道成本不足一分 , 按一分收
                    basicMoney = new BigDecimal("0.01");

                }else{
                    //超过一分,四舍五入,保留两位有效数字
                    basicMoney = basicTrade.setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                return basicMoney;
            case MOBAO:
                if (new BigDecimal("0.1").compareTo(basicTrade) == 1){
                    //通道成本不足一毛, 按一毛收
                    basicMoney = new BigDecimal("0.1");
                }else{
                    //超过一毛,四舍五入,保留两位有效数字
                    basicMoney = basicTrade.setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                return basicMoney;
            case EASY_LINK:
                if (new BigDecimal("0.1").compareTo(basicTrade) == 1){
                    //通道成本不足一毛, 按一毛收
                    basicMoney = new BigDecimal("0.1");
                }else{
                    //超过一毛,四舍五入,保留两位有效数字
                    basicMoney = basicTrade.setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                return basicMoney;
            case SYJ:
                basicMoney = basicTrade.setScale(2, BigDecimal.ROUND_HALF_UP);
                return  basicMoney;
            default:
                basicMoney = basicTrade.setScale(2, BigDecimal.ROUND_HALF_UP);
                return basicMoney;
        }
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
                    //手续费不足两毛 , 按2毛收
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
                    //收手续费,进一位,保留两位有效数字
                    waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
                }
                return waitMoney;
            case SYJ:
                waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_HALF_UP);
                return waitMoney;
            default:
                waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
                return waitMoney;
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param dealer
     */
    @Override
    @Transactional
    public void add(final Dealer dealer) {
        this.dealerDao.insert(dealer);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     */
    @Override
    public void updateLoginDate(final long dealerId) {
        this.dealerDao.updateLoginDate(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     * @param firstLevelDealerId
     * @return
     */
    @Override
    @Transactional
    public long createSecondDealer(final SecondLevelDealerAddRequest request, final long firstLevelDealerId) {
        final long accountId = this.accountService.initAccount(request.getName());
        final Dealer dealer = new Dealer();
        dealer.setAccountId(accountId);
        dealer.setMobile(request.getMobile());
        dealer.setProxyName(request.getName());
        dealer.setBankName(request.getBankName());
        dealer.setBankAccountName(request.getBankAccountName());
        dealer.setBelongArea(request.getBelongArea());
        dealer.setLevel(EnumDealerLevel.SECOND.getId());
        dealer.setFirstLevelDealerId(firstLevelDealerId);
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(request.getBankCard()));
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(request.getBankReserveMobile()));
        dealer.setIdCard(DealerSupport.encryptIdenrity(request.getIdCard()));
        dealer.setStatus(EnumDealerStatus.NORMAL.getId());
        this.add(dealer);
        this.updateMarkCode(GlobalID.GetGlobalID(EnumGlobalIDType.DEALER, EnumGlobalIDPro.MIN,dealer.getId()+""),dealer.getId());

        final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerId(firstLevelDealerId);
        for (DealerChannelRate channelRate : channelRates) {
            final DealerChannelRate dealerChannelRate = new DealerChannelRate();
            dealerChannelRate.setDealerId(dealer.getId());
            dealerChannelRate.setProductId(channelRate.getProductId());
            dealerChannelRate.setChannelTypeSign(channelRate.getChannelTypeSign());
            if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_WECHAT.getId()) {
                dealerChannelRate.setDealerTradeRate(new BigDecimal(request.getWeixinSettleRate()).divide(new BigDecimal("100")));
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_ALIPAY.getId()) {
                dealerChannelRate.setDealerTradeRate(new BigDecimal(request.getAlipaySettleRate()).divide(new BigDecimal("100")));
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_UNIONPAY.getId()) {
                dealerChannelRate.setDealerTradeRate(new BigDecimal(request.getQuickSettleRate()).divide(new BigDecimal("100")));
            }
            dealerChannelRate.setDealerBalanceType(channelRate.getDealerBalanceType());
            dealerChannelRate.setDealerWithdrawFee(new BigDecimal(request.getWithdrawSettleFee()));
            dealerChannelRate.setDealerMerchantPayRate(channelRate.getDealerMerchantPayRate());
            dealerChannelRate.setDealerMerchantWithdrawFee(channelRate.getDealerMerchantWithdrawFee());
            dealerChannelRate.setStatus(EnumDealerChannelRateStatus.USEING.getId());
            this.dealerRateService.init(dealerChannelRate);
        }
        return dealer.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param dealer
     * @return
     */
    @Override
    @Transactional
    public int update(final Dealer dealer) {
        return this.dealerDao.update(dealer);
    }

    /**
     * {@inheritDoc}
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Dealer> getById(final long id) {
        return Optional.fromNullable(this.dealerDao.selectById(id));
    }

    /**
     * {@inheritDoc}
     *
     * @param ids
     * @return
     */
    @Override
    public List<Dealer> getByIds(final List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return this.dealerDao.selectByIds(ids);
    }

    /**
     * {@inheritDoc}
     *
     * @param accountId
     * @return
     */
    @Override
    public Optional<Dealer> getByAccountId(final long accountId) {
        return Optional.fromNullable(this.dealerDao.selectByAccountId(accountId));
    }

    /**
     * {@inheritDoc}
     *
     * @param accountIds
     * @return
     */
    @Override
    public List<Dealer> getByAccountIds(final List<Long> accountIds) {
        if (CollectionUtils.isEmpty(accountIds)) {
            return Collections.emptyList();
        }
        return this.dealerDao.selectByAccountIds(accountIds);
    }

    /**
     * {@inheritDoc}
     *
     * @param mobile 已加密
     * @return
     */
    @Override
    public Optional<Dealer> getByMobile(final String mobile) {
        return Optional.fromNullable(this.dealerDao.selectByMobile(mobile));
    }

    /**
     * {@inheritDoc}
     *
     * @param mobile
     * @param dealerId
     * @return
     */
    @Override
    public Optional<Dealer> getByMobileUnIncludeNow(final String mobile, final long dealerId) {
        return Optional.fromNullable(this.dealerDao.selectByMobileUnIncludeNow(mobile, dealerId));
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<Dealer> getAllFirstLevelDealers() {
        return this.dealerDao.selectAllFirstLevelDealers();
    }

    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerId
     * @return
     */
    @Override
    public List<Dealer> getSecondLevelDealersByFirstLevelDealerId(final long firstLevelDealerId) {
        return this.dealerDao.selectSecondLevelDealersByFirstLevelDealerId(firstLevelDealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param secondLevelDealerId
     * @return
     */
    @Override
    public List<Dealer> getThirdLevelDealersBySecondLevelDealerId(long secondLevelDealerId) {
        return this.dealerDao.selectThirdLevelDealersBySecondLevelDealerId(secondLevelDealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param proxyMame
     * @return
     */
    @Override
    public long getByProxyName(final String proxyMame) {
        return this.dealerDao.selectByProxyName(proxyMame);
    }

    /**
     * {@inheritDoc}
     *
     * @param proxyMame
     * @param dealerId
     * @return
     */
    @Override
    public long getByProxyNameUnIncludeNow(final String proxyMame, final long dealerId) {
        return this.dealerDao.selectByProxyNameUnIncludeNow(proxyMame, dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param condition
     * @return
     */
    @Override
    public List<Dealer> findByCondition(final String condition, final long dealerId, final int level) {
        return this.dealerDao.selectByCondition(condition, dealerId, level);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId  一级代理商id
     * @param toDealerId  码段要分配给代理商的id
     * @param startCode  开始二维码
     * @param endCode  结束二维码
     * @return
     */
    @Override
    @Transactional
    public List<DistributeQRCodeRecord> distributeQRCode(final long dealerId, final long toDealerId,
                                                         final String startCode, final String endCode) {
        final List<DistributeQRCodeRecord> records = new ArrayList<>();
        final List<QRCode> qrCodeList = this.qrCodeService.getUnDistributeCodeByDealerIdAndRangeCode(dealerId, startCode, endCode);
        if (CollectionUtils.isEmpty(qrCodeList)) {
            return records;
        }
        final List<Long> qrCodeIds = Lists.transform(qrCodeList, new Function<QRCode, Long>() {
            @Override
            public Long apply(QRCode input) {
                return input.getId();
            }
        });
        if (dealerId == toDealerId) {
            this.qrCodeService.markAsDistribute(qrCodeIds);
        } else {
            this.qrCodeService.markAsDistribute2(qrCodeIds, toDealerId);
        }
        final List<Pair<QRCode, QRCode>> pairQRCodeList = this.qrCodeService.getPairQRCodeList(qrCodeList);
        for (Pair<QRCode, QRCode> pair : pairQRCodeList) {
            final QRCode left = pair.getLeft();
            final QRCode right = pair.getRight();
            final DistributeQRCodeRecord distributeQRCodeRecord = new DistributeQRCodeRecord();
            distributeQRCodeRecord.setFirstLevelDealerId(dealerId);
            distributeQRCodeRecord.setSecondLevelDealerId(dealerId == toDealerId ? 0 : toDealerId);
            distributeQRCodeRecord.setCount((int) (Long.valueOf(right.getCode()) - Long.valueOf(left.getCode()) + 1));
            distributeQRCodeRecord.setStartCode(left.getCode());
            distributeQRCodeRecord.setEndCode(right.getCode());
            records.add(distributeQRCodeRecord);
            this.distributeQRCodeRecordService.add(distributeQRCodeRecord);
        }
        return records;
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public long getUnDistributeCodeCount(final long dealerId) {
        return this.qrCodeService.getUnDistributeCodeCountByFirstLevelDealerId(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public List<Pair<QRCode, QRCode>> getUnDistributeCode(long dealerId) {
        final List<QRCode> qrCodes = this.qrCodeService.getUnDistributeCodeByFirstLevelDealerId(dealerId);
        if (CollectionUtils.isEmpty(qrCodes)) {
            return Collections.emptyList();
        }
        final List<Pair<QRCode, QRCode>> pairs = this.qrCodeService.getPairQRCodeList(qrCodes);
        return pairs;
    }

    /**
     * {@inheritDoc}
     *
     * @param startCode
     * @param endCode
     * @param dealerId
     * @return
     */
    @Override
    public int getUnDistributeCodeCountByRangeCode(final String startCode, final String endCode, final long dealerId) {
        return this.qrCodeService.getUnDistributeCodeCountByDealerIdAndRangeCode(startCode, endCode, dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param startCode
     * @param endCode
     * @return
     */
    @Override
    public boolean checkRangeQRCode(final String startCode, final String endCode) {
        return this.qrCodeService.checkRangeQRCode(startCode, endCode);
    }


    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerAddRequest
     * @return
     */
    @Override
    @Transactional
    public long createFirstDealer(final FirstLevelDealerAddRequest firstLevelDealerAddRequest) {
        final long accountId = this.accountService.initAccount(firstLevelDealerAddRequest.getName());
        final Dealer dealer = new Dealer();
        dealer.setAccountId(accountId);
        dealer.setMobile(firstLevelDealerAddRequest.getMobile());
        dealer.setProxyName(firstLevelDealerAddRequest.getName());
        dealer.setBankAccountName(firstLevelDealerAddRequest.getBankAccountName());
        dealer.setBankName(firstLevelDealerAddRequest.getBankName());
        dealer.setBelongArea(firstLevelDealerAddRequest.getBelongArea());
        dealer.setLevel(EnumDealerLevel.FIRST.getId());
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(firstLevelDealerAddRequest.getBankCard()));
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(firstLevelDealerAddRequest.getBankReserveMobile()));
        dealer.setIdCard(DealerSupport.encryptIdenrity(firstLevelDealerAddRequest.getIdCard()));
        dealer.setTotalProfitSpace(firstLevelDealerAddRequest.getTotalProfitSpace());
        dealer.setRecommendBtn(firstLevelDealerAddRequest.getRecommendBtn());
        dealer.setStatus(EnumDealerStatus.NORMAL.getId());
        this.add(dealer);
        this.updateMarkCode(GlobalID.GetGlobalID(EnumGlobalIDType.DEALER, EnumGlobalIDPro.MIN,dealer.getId()+""),dealer.getId());
        final FirstLevelDealerAddRequest.Product product = firstLevelDealerAddRequest.getProduct();
        final long productId = product.getProductId();
        final List<FirstLevelDealerAddRequest.Channel> channels = product.getChannels();
        for (FirstLevelDealerAddRequest.Channel channel : channels) {
            final DealerChannelRate dealerChannelRate = new DealerChannelRate();
            dealerChannelRate.setDealerId(dealer.getId());
            dealerChannelRate.setProductId(productId);
            dealerChannelRate.setChannelTypeSign(channel.getChannelType());
            dealerChannelRate.setDealerTradeRate(new BigDecimal(channel.getPaymentSettleRate()).divide(new BigDecimal(100)));
            dealerChannelRate.setDealerBalanceType(channel.getSettleType());
            dealerChannelRate.setDealerWithdrawFee(new BigDecimal(channel.getWithdrawSettleFee()));
            dealerChannelRate.setDealerMerchantPayRate(new BigDecimal(channel.getMerchantSettleRate()).divide(new BigDecimal(100)));
            dealerChannelRate.setDealerMerchantWithdrawFee(new BigDecimal(channel.getMerchantWithdrawFee()));
            dealerChannelRate.setStatus(EnumDealerChannelRateStatus.USEING.getId());
            this.dealerRateService.init(dealerChannelRate);
        }

        final List<FirstLevelDealerAddRequest.DealerUpgerdeRate> dealerUpgerdeRates =firstLevelDealerAddRequest.getDealerUpgerdeRates();
        for(FirstLevelDealerAddRequest.DealerUpgerdeRate dealerUpgerdeRate:dealerUpgerdeRates){
            DealerUpgerdeRate du = new DealerUpgerdeRate();
            du.setProductId(productId);
            du.setType(dealerUpgerdeRate.getType());
            du.setDealerId(dealer.getId());
            du.setFirstDealerShareProfitRate(new BigDecimal(dealerUpgerdeRate.getFirstDealerShareProfitRate()));
            du.setSecondDealerShareProfitRate(new BigDecimal(dealerUpgerdeRate.getSecondDealerShareProfitRate()));
            du.setBossDealerShareRate(new BigDecimal(dealerUpgerdeRate.getBossDealerShareRate()));
            du.setStatus(EnumDealerStatus.NORMAL.getId());
            this.dealerUpgerdeRateService.insert(du);
        }
        return dealer.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public List<DistributeQRCodeRecord> getDistributeToSecondDealerQRCode(final long dealerId) {
        return this.distributeQRCodeRecordService.getDistributeSecondDealerRecords(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<DistributeQRCodeRecord> getDistributeToSelfQRCode(final long dealerId) {
        return this.distributeQRCodeRecordService.getDistributeSelfRecords(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public List<MerchantInfo> getMyMerchants(long dealerId) {
        return this.merchantInfoService.selectByDealerId(dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     */
    @Override
    public List<Triple<Dealer, DistributeCodeCount, ActiveCodeCount>> getMyDealers(final long dealerId) {
        final List<Dealer> secondLevelDealers = this.dealerDao.selectSecondLevelDealersByFirstLevelDealerId(dealerId);

        final List<Long> secondLevelDealerIds = Lists.transform(secondLevelDealers, new Function<Dealer, Long>() {
            @Override
            public Long apply(Dealer input) {
                return input.getId();
            }
        });

        final List<DistributeCodeCount> distributeCodeCounts = this.qrCodeService.getDistributeCodeCount(dealerId, secondLevelDealerIds);
        final Map<Long, DistributeCodeCount> distributeCodeCountMap = Maps.uniqueIndex(distributeCodeCounts,
                new Function<DistributeCodeCount, Long>() {
                    @Override
                    public Long apply(DistributeCodeCount input) {
                        return input.getSecondLevelDealerId();
                    }
                });
        final List<ActiveCodeCount> activeCodeCounts = this.qrCodeService.getActiveCodeCount(dealerId, secondLevelDealerIds);

        final Map<Long, ActiveCodeCount> activeCodeCountMap = Maps.uniqueIndex(activeCodeCounts, new Function<ActiveCodeCount, Long>() {
            @Override
            public Long apply(ActiveCodeCount input) {
                return input.getSecondLevelDealerId();
            }
        });
        final List<Triple<Dealer, DistributeCodeCount, ActiveCodeCount>> results = Lists.transform(secondLevelDealers,
                new Function<Dealer, Triple<Dealer, DistributeCodeCount, ActiveCodeCount>>() {
                    @Override
                    public Triple<Dealer, DistributeCodeCount, ActiveCodeCount> apply(Dealer input) {
                        return Triple.of(input, distributeCodeCountMap.get(input.getId()),
                                activeCodeCountMap.get(input.getId()));
                    }
                });
        return results;
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param secondLevelDealerId
     */
    @Override
    public Triple<Dealer, List<DistributeQRCodeRecord>, List<DealerChannelRate>> getMyDealerDetail(final long dealerId, final long secondLevelDealerId) {
        final Optional<Dealer> secondLevelDealerOptional = this.getById(secondLevelDealerId);
        Preconditions.checkState(secondLevelDealerOptional.isPresent(), "二级代理不存在");

        final List<DistributeQRCodeRecord> records =
                this.distributeQRCodeRecordService.getRecordBySecondLevelDealerId(dealerId, secondLevelDealerId);

        final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerId(secondLevelDealerId);
        return Triple.of(secondLevelDealerOptional.get(), records, channelRates);
    }

    /**
     * {@inheritDoc}
     *
     * @param firstDealerId
     * @return
     */
    @Override
    public int getMyDealerCount(final long firstDealerId) {
        return this.dealerDao.selectMyDealerCount(firstDealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public Optional<Dealer> getPlainDealer(final long dealerId) {
        final Dealer dealer = this.dealerDao.selectById(dealerId);
        dealer.setSettleBankCard(DealerSupport.decryptBankCard(dealerId, dealer.getSettleBankCard()));
        dealer.setBankReserveMobile(DealerSupport.decryptMobile(dealerId, dealer.getBankReserveMobile()));
        return Optional.fromNullable(dealer);
    }

    /**
     * {@inheritDoc}
     *
     * @param listDealerRequest
     * @return
     */
    @Override
    public PageModel<Dealer> listDealer(final ListDealerRequest listDealerRequest) {
        final PageModel<Dealer> pageModel = new PageModel<>(listDealerRequest.getPageNo(), listDealerRequest.getPageSize());
        listDealerRequest.setOffset(pageModel.getFirstIndex());
        listDealerRequest.setCount(pageModel.getPageSize());
        final int count = this.dealerDao.selectDealerCountByPageParams(listDealerRequest);
        final List<Dealer> dealers = this.dealerDao.selectDealersByPageParams(listDealerRequest);
        pageModel.setCount(count);
        pageModel.setRecords(dealers);
        return pageModel;
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     */
    @Override
    @Transactional
    public void updateDealer(final FirstLevelDealerUpdateRequest request) {
        final Optional<Dealer> dealerOptional = this.getById(request.getDealerId());
        Preconditions.checkState(dealerOptional.isPresent(), "dealer[{}] is not exist", request.getDealerId());
        final Dealer dealer = dealerOptional.get();
        dealer.setMobile(request.getMobile());
        dealer.setProxyName(request.getName());
        dealer.setBankAccountName(request.getBankAccountName());
        dealer.setBelongArea(request.getBelongArea());
        dealer.setLevel(EnumDealerLevel.FIRST.getId());
        dealer.setRecommendBtn(request.getRecommendBtn());
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(request.getBankCard()));
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(request.getBankReserveMobile()));
        dealer.setStatus(EnumDealerStatus.NORMAL.getId());
        dealer.setTotalProfitSpace(request.getTotalProfitSpace());
        this.update(dealer);
        final FirstLevelDealerUpdateRequest.Product product = request.getProduct();
        final long productId = product.getProductId();
        final List<FirstLevelDealerUpdateRequest.Channel> channels = product.getChannels();
        for (FirstLevelDealerUpdateRequest.Channel channel : channels) {
            final Optional<DealerChannelRate> dealerChannelRateOptional =
                    this.dealerRateService.getByDealerIdAndProductIdAndChannelType(request.getDealerId(), productId, channel.getChannelType());
            Preconditions.checkState(dealerChannelRateOptional.isPresent(), "代理商对应的支付通道费率记录不存在");
            final DealerChannelRate dealerChannelRate = dealerChannelRateOptional.get();
            dealerChannelRate.setDealerTradeRate(new BigDecimal(channel.getPaymentSettleRate()).divide(new BigDecimal("100")));
            dealerChannelRate.setDealerWithdrawFee(new BigDecimal(channel.getWithdrawSettleFee()));
            dealerChannelRate.setDealerMerchantPayRate(new BigDecimal(channel.getMerchantSettleRate()).divide(new BigDecimal("100")));
            dealerChannelRate.setDealerMerchantWithdrawFee(new BigDecimal(channel.getMerchantWithdrawFee()));
            dealerChannelRate.setStatus(EnumDealerChannelRateStatus.USEING.getId());
            this.dealerRateService.update(dealerChannelRate);
        }

        final List<FirstLevelDealerUpdateRequest.DealerUpgerdeRate> dealerUpgerdeRates =request.getDealerUpgerdeRates();
        if (dealerUpgerdeRates.size()==0){
            for(FirstLevelDealerUpdateRequest.DealerUpgerdeRate dealerUpgerdeRate:dealerUpgerdeRates){
                DealerUpgerdeRate du = new DealerUpgerdeRate();
                du.setId(dealerUpgerdeRate.getId());
                du.setProductId(productId);
                du.setType(dealerUpgerdeRate.getType());
                du.setDealerId(dealer.getId());
                du.setFirstDealerShareProfitRate(new BigDecimal(dealerUpgerdeRate.getFirstDealerShareProfitRate()));
                du.setSecondDealerShareProfitRate(new BigDecimal(dealerUpgerdeRate.getSecondDealerShareProfitRate()));
                du.setBossDealerShareRate(new BigDecimal(dealerUpgerdeRate.getBossDealerShareRate()));
                du.setStatus(EnumDealerStatus.NORMAL.getId());
                this.dealerUpgerdeRateService.insert(du);
            }
        }
        for(FirstLevelDealerUpdateRequest.DealerUpgerdeRate dealerUpgerdeRate:dealerUpgerdeRates){
            DealerUpgerdeRate du = new DealerUpgerdeRate();
            du.setId(dealerUpgerdeRate.getId());
            du.setProductId(productId);
            du.setType(dealerUpgerdeRate.getType());
            du.setDealerId(dealer.getId());
            du.setFirstDealerShareProfitRate(new BigDecimal(dealerUpgerdeRate.getFirstDealerShareProfitRate()));
            du.setSecondDealerShareProfitRate(new BigDecimal(dealerUpgerdeRate.getSecondDealerShareProfitRate()));
            du.setBossDealerShareRate(new BigDecimal(dealerUpgerdeRate.getBossDealerShareRate()));
            du.setStatus(EnumDealerStatus.NORMAL.getId());
            this.dealerUpgerdeRateService.update(du);
        }

    }

    /**
     * 写入markCode
     *
     * @param markCode
     * @param dealerId
     * @return
     */
    @Override
    public int updateMarkCode(String markCode, long dealerId) {
        return this.dealerDao.updateMarkCode(markCode,dealerId);
    }

//==============================此处为对代理商进行重构=============================
    /**
     * {@inheritDoc}
     *
     * @param firstLevelDealerAdd2Request
     * @return
     */
    @Override
    @Transactional
    public long createFirstDealer2(final FirstLevelDealerAdd2Request firstLevelDealerAdd2Request) {
        final long accountId = this.accountService.initAccount(firstLevelDealerAdd2Request.getName());
        final Dealer dealer = new Dealer();
        dealer.setAccountId(accountId);
        dealer.setMobile(firstLevelDealerAdd2Request.getMobile());
        dealer.setBankAccountName(firstLevelDealerAdd2Request.getBankAccountName());
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(firstLevelDealerAdd2Request.getBankCard()));
        dealer.setBankName(firstLevelDealerAdd2Request.getBankName());
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(firstLevelDealerAdd2Request.getBankReserveMobile()));
        dealer.setProxyName(firstLevelDealerAdd2Request.getName());
        dealer.setBelongArea(firstLevelDealerAdd2Request.getBelongArea());
        dealer.setLevel(EnumDealerLevel.FIRST.getId());
        dealer.setFirstLevelDealerId(0);
        dealer.setSecondLevelDealerId(0);
        dealer.setStatus(EnumDealerStatus.NORMAL.getId());
        dealer.setRecommendBtn(EnumRecommendBtn.OFF.getId());
        dealer.setIdCard(DealerSupport.encryptIdenrity(firstLevelDealerAdd2Request.getIdCard()));
        dealer.setBelongProvinceCode(firstLevelDealerAdd2Request.getBelongProvinceCode());
        dealer.setBelongProvinceName(firstLevelDealerAdd2Request.getBelongProvinceName());
        dealer.setBelongCityCode(firstLevelDealerAdd2Request.getBelongCityCode());
        dealer.setBelongCityName(firstLevelDealerAdd2Request.getBelongCityName());
        dealer.setInviteBtn(EnumInviteBtn.OFF.getId());
        dealer.setLoginName(firstLevelDealerAdd2Request.getLoginName());
        dealer.setLoginPwd(DealerSupport.passwordDigest(firstLevelDealerAdd2Request.getLoginPwd(),"JKM"));
        dealer.setEmail(firstLevelDealerAdd2Request.getEmail());
        this.add2(dealer);
        this.updateMarkCodeAndInviteCode(GlobalID.GetGlobalID(EnumGlobalIDType.DEALER, EnumGlobalIDPro.MIN,dealer.getId()+""),
                GlobalID.GetInviteID(EnumGlobalDealerLevel.FIRSTDEALER,dealer.getId()+""),dealer.getId());
        return dealer.getId();
    }
    /**
     * {@inheritDoc}
     *
     * @param secondLevelDealerAdd2Request
     * @return
     */
    @Override
    @Transactional
    public long createSecondDealer2(final SecondLevelDealerAdd2Request secondLevelDealerAdd2Request,long dealerId) {
        final long accountId = this.accountService.initAccount(secondLevelDealerAdd2Request.getName());
        final Dealer dealer = new Dealer();
        dealer.setAccountId(accountId);
        dealer.setMobile(secondLevelDealerAdd2Request.getMobile());
        dealer.setBankAccountName(secondLevelDealerAdd2Request.getBankAccountName());
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(secondLevelDealerAdd2Request.getBankCard()));
        dealer.setBankName(secondLevelDealerAdd2Request.getBankName());
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(secondLevelDealerAdd2Request.getBankReserveMobile()));
        dealer.setProxyName(secondLevelDealerAdd2Request.getName());
        dealer.setBelongArea(secondLevelDealerAdd2Request.getBelongArea());
        dealer.setLevel(EnumDealerLevel.SECOND.getId());
        dealer.setFirstLevelDealerId(dealerId);
        dealer.setSecondLevelDealerId(0);
        dealer.setStatus(EnumDealerStatus.NORMAL.getId());
        dealer.setRecommendBtn(EnumRecommendBtn.OFF.getId());
        dealer.setIdCard(DealerSupport.encryptIdenrity(secondLevelDealerAdd2Request.getIdCard()));
        dealer.setBelongProvinceCode(secondLevelDealerAdd2Request.getBelongProvinceCode());
        dealer.setBelongProvinceName(secondLevelDealerAdd2Request.getBelongProvinceName());
        dealer.setBelongCityCode(secondLevelDealerAdd2Request.getBelongCityCode());
        dealer.setBelongCityName(secondLevelDealerAdd2Request.getBelongCityName());
        dealer.setInviteBtn(EnumInviteBtn.OFF.getId());
        dealer.setLoginName(secondLevelDealerAdd2Request.getLoginName());
        dealer.setLoginPwd(DealerSupport.passwordDigest(secondLevelDealerAdd2Request.getLoginPwd(),"JKM"));
        dealer.setEmail(secondLevelDealerAdd2Request.getEmail());
        this.add2(dealer);
        this.updateMarkCodeAndInviteCode(GlobalID.GetGlobalID(EnumGlobalIDType.DEALER, EnumGlobalIDPro.MIN,dealer.getId()+""),
                GlobalID.GetInviteID(EnumGlobalDealerLevel.SECORDDEALER,dealer.getId()+""),dealer.getId());
        return dealer.getId();
    }

    /**
     * {@inheritDoc}
     *
     * @param dealer
     */
    @Override
    @Transactional
    public void add2(final Dealer dealer) {
        this.dealerDao.insert(dealer);
    }

    /**
     * {@inheritDoc}
     *
     * @param listFirstDealerRequest
     * @return
     */
    @Override
    public PageModel<FirstDealerResponse> listFirstDealer(final ListFirstDealerRequest listFirstDealerRequest) {
        final PageModel<FirstDealerResponse> pageModel = new PageModel<>(listFirstDealerRequest.getPageNo(), listFirstDealerRequest.getPageSize());
        listFirstDealerRequest.setOffset(pageModel.getFirstIndex());
        listFirstDealerRequest.setCount(pageModel.getPageSize());
        final int count = this.dealerDao.selectFirstDealerCountByPageParams(listFirstDealerRequest);
        final List<FirstDealerResponse> dealers = this.dealerDao.selectFirstDealersByPageParams(listFirstDealerRequest);
        pageModel.setCount(count);
        pageModel.setRecords(dealers);
        return pageModel;
    }

    /**
     * {@inheritDoc}
     *
     * @param listSecondDealerRequest
     * @return
     */
    @Override
    public PageModel<SecondDealerResponse> listSecondDealer(final ListSecondDealerRequest listSecondDealerRequest) {
        final PageModel<SecondDealerResponse> pageModel = new PageModel<>(listSecondDealerRequest.getPageNo(), listSecondDealerRequest.getPageSize());
        listSecondDealerRequest.setOffset(pageModel.getFirstIndex());
        listSecondDealerRequest.setCount(pageModel.getPageSize());
        final int count = this.dealerDao.selectSecondDealerCountByPageParams(listSecondDealerRequest);
        final List<SecondDealerResponse> dealers = this.dealerDao.selectSecondDealersByPageParams(listSecondDealerRequest);
        pageModel.setCount(count);
        pageModel.setRecords(dealers);
        return pageModel;
    }

    /**
     * 写入markCode和inviteCode
     * @param markCode
     * @param inviteCode
     * @param dealerId
     * @return
     */
    @Override
    public int updateMarkCodeAndInviteCode(String markCode,String inviteCode, long dealerId) {
        return this.dealerDao.updateMarkCodeAndInviteCode(markCode,inviteCode,dealerId);
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     */
    @Override
    @Transactional
    public void updateDealer2(final FirstLevelDealerUpdate2Request request) {
        final Optional<Dealer> dealerOptional = this.getById(request.getDealerId());
        Preconditions.checkState(dealerOptional.isPresent(), "代理商[{}] 不存在", request.getDealerId());
        final Dealer dealer = dealerOptional.get();
        dealer.setMobile(request.getMobile());
        dealer.setProxyName(request.getName());
        dealer.setLoginName(request.getLoginName());
        dealer.setEmail(request.getEmail());
        dealer.setBelongProvinceCode(request.getBelongProvinceCode());
        dealer.setBelongProvinceName(request.getBelongProvinceName());
        dealer.setBelongCityCode(request.getBelongCityCode());
        dealer.setBelongCityName(request.getBelongCityName());
        dealer.setBelongArea(request.getBelongArea());
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(request.getBankCard()));
        dealer.setBankName(request.getBankName());
        dealer.setBankAccountName(request.getBankAccountName());
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(request.getBankReserveMobile()));
        dealer.setIdCard(DealerSupport.encryptIdenrity(request.getIdCard()));
        this.update2(dealer);
    }
    /**
     * {@inheritDoc}
     *
     * @param request
     */
    @Override
    @Transactional
    public void updateSecondDealer(final SecondLevelDealerUpdate2Request request) {
        final Optional<Dealer> dealerOptional = this.getById(request.getDealerId());
        Preconditions.checkState(dealerOptional.isPresent(), "代理商[{}] 不存在", request.getDealerId());
        final Dealer dealer = dealerOptional.get();
        dealer.setMobile(request.getMobile());
        dealer.setProxyName(request.getName());
        dealer.setLoginName(request.getLoginName());
        dealer.setEmail(request.getEmail());
        dealer.setBelongProvinceCode(request.getBelongProvinceCode());
        dealer.setBelongProvinceName(request.getBelongProvinceName());
        dealer.setBelongCityCode(request.getBelongCityCode());
        dealer.setBelongCityName(request.getBelongCityName());
        dealer.setBelongArea(request.getBelongArea());
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(request.getBankCard()));
        dealer.setBankName(request.getBankName());
        dealer.setBankAccountName(request.getBankAccountName());
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(request.getBankReserveMobile()));
        dealer.setIdCard(DealerSupport.encryptIdenrity(request.getIdCard()));
        this.update2(dealer);
    }
    /**
     * {@inheritDoc}
     *
     * @param dealer
     * @return
     */
    @Override
    @Transactional
    public int update2(final Dealer dealer) {
        return this.dealerDao.update2(dealer);
    }



    /**
     * {@inheritDoc}
     *
     * @param request
     */
    @Override
    @Transactional
    public void addOrUpdateHssDealer(final HssDealerAddOrUpdateRequest request) {
        final Optional<Dealer> dealerOptional = this.getById(request.getDealerId());
        final Dealer dealer = dealerOptional.get();
        if(request.getRecommendBtn()>0){
            dealer.setRecommendBtn(request.getRecommendBtn());
        }else{
            dealer.setRecommendBtn(EnumRecommendBtn.OFF.getId());
        }
        dealer.setInviteBtn(request.getInviteBtn());
        dealer.setTotalProfitSpace(request.getTotalProfitSpace());
        this.updateRecommendBtnAndTotalProfitSpace(dealer);
        final HssDealerAddOrUpdateRequest.Product product = request.getProduct();
        final long productId = product.getProductId();
        final List<HssDealerAddOrUpdateRequest.Channel> channels = product.getChannels();
        for (HssDealerAddOrUpdateRequest.Channel channel : channels) {
            final Optional<DealerChannelRate> dealerChannelRateOptional =
                    this.dealerRateService.getByDealerIdAndProductIdAndChannelType(request.getDealerId(), productId, channel.getChannelType());
            if(dealerChannelRateOptional.isPresent()){//修改
                final DealerChannelRate dealerChannelRate = dealerChannelRateOptional.get();
                dealerChannelRate.setDealerTradeRate(new BigDecimal(channel.getPaymentSettleRate()).divide(new BigDecimal("100")));
                dealerChannelRate.setDealerWithdrawFee(new BigDecimal(channel.getWithdrawSettleFee()));
                dealerChannelRate.setDealerMerchantPayRate(new BigDecimal(channel.getMerchantSettleRate()).divide(new BigDecimal("100")));
                dealerChannelRate.setDealerMerchantWithdrawFee(new BigDecimal(channel.getMerchantWithdrawFee()));
                dealerChannelRate.setStatus(EnumDealerChannelRateStatus.USEING.getId());
                this.dealerRateService.update(dealerChannelRate);
            }else{//新增
                final DealerChannelRate dealerChannelRate = new DealerChannelRate();
                dealerChannelRate.setDealerId(dealer.getId());
                dealerChannelRate.setProductId(productId);
                dealerChannelRate.setChannelTypeSign(channel.getChannelType());
                dealerChannelRate.setDealerTradeRate(new BigDecimal(channel.getPaymentSettleRate()).divide(new BigDecimal("100")));
                dealerChannelRate.setDealerBalanceType(channel.getSettleType());
                dealerChannelRate.setDealerWithdrawFee(new BigDecimal(channel.getWithdrawSettleFee()));
                dealerChannelRate.setDealerMerchantPayRate(new BigDecimal(channel.getMerchantSettleRate()).divide(new BigDecimal("100")));
                dealerChannelRate.setDealerMerchantWithdrawFee(new BigDecimal(channel.getMerchantWithdrawFee()));
                dealerChannelRate.setStatus(EnumDealerChannelRateStatus.USEING.getId());
                this.dealerRateService.init(dealerChannelRate);
            }

        }

        final List<HssDealerAddOrUpdateRequest.DealerUpgradeRate> dealerUpgradeRates =request.getDealerUpgerdeRates();
        if(dealerUpgradeRates.size()>0){
            for(HssDealerAddOrUpdateRequest.DealerUpgradeRate dealerUpgradeRate:dealerUpgradeRates){
                if(dealerUpgradeRate.getId()>0){//修改
                    DealerUpgerdeRate du = new DealerUpgerdeRate();
                    du.setId(dealerUpgradeRate.getId());
                    du.setProductId(productId);
                    du.setType(dealerUpgradeRate.getType());
                    du.setDealerId(dealerUpgradeRate.getDealerId());
                    du.setFirstDealerShareProfitRate(new BigDecimal(dealerUpgradeRate.getFirstDealerShareProfitRate()));
                    du.setSecondDealerShareProfitRate(new BigDecimal(dealerUpgradeRate.getSecondDealerShareProfitRate()));
                    du.setBossDealerShareRate(new BigDecimal(dealerUpgradeRate.getBossDealerShareRate()));
                    du.setStatus(EnumDealerStatus.NORMAL.getId());
                    this.dealerUpgerdeRateService.update(du);
                }else{//新增
                    DealerUpgerdeRate du = new DealerUpgerdeRate();
                    du.setProductId(productId);
                    du.setType(dealerUpgradeRate.getType());
                    du.setDealerId(dealerUpgradeRate.getDealerId());
                    du.setFirstDealerShareProfitRate(new BigDecimal(dealerUpgradeRate.getFirstDealerShareProfitRate()));
                    du.setSecondDealerShareProfitRate(new BigDecimal(dealerUpgradeRate.getSecondDealerShareProfitRate()));
                    du.setBossDealerShareRate(new BigDecimal(dealerUpgradeRate.getBossDealerShareRate()));
                    du.setStatus(EnumDealerStatus.NORMAL.getId());
                    this.dealerUpgerdeRateService.insert(du);
                }
            }
        }

    }

    /**
     * {@inheritDoc}
     *
     * @param request
     */
    @Override
    @Transactional
    public void addOrUpdateHsyDealer(final HsyDealerAddOrUpdateRequest request) {
        final Optional<Dealer> dealerOptional = this.getById(request.getDealerId());
        final Dealer dealer = dealerOptional.get();
        dealer.setInviteBtn(request.getInviteBtn());
        this.updateInviteBtn(dealer);
        final HsyDealerAddOrUpdateRequest.Product product = request.getProduct();
        final long productId = product.getProductId();
        final List<HsyDealerAddOrUpdateRequest.Channel> channels = product.getChannels();
        for (HsyDealerAddOrUpdateRequest.Channel channel : channels) {
            final Optional<DealerChannelRate> dealerChannelRateOptional =
                    this.dealerRateService.getByDealerIdAndProductIdAndChannelType(request.getDealerId(), productId, channel.getChannelType());
            if(dealerChannelRateOptional.isPresent()){//修改
                final DealerChannelRate dealerChannelRate = dealerChannelRateOptional.get();
                dealerChannelRate.setDealerTradeRate(new BigDecimal(channel.getPaymentSettleRate()).divide(new BigDecimal("100")));
                dealerChannelRate.setDealerWithdrawFee(new BigDecimal(channel.getWithdrawSettleFee()));
                dealerChannelRate.setDealerMerchantPayRate(new BigDecimal(channel.getMerchantSettleRate()).divide(new BigDecimal("100")));
                dealerChannelRate.setDealerMerchantWithdrawFee(new BigDecimal(channel.getMerchantWithdrawFee()));
                dealerChannelRate.setStatus(EnumDealerChannelRateStatus.USEING.getId());
                this.dealerRateService.update(dealerChannelRate);
            }else{//新增
                final DealerChannelRate dealerChannelRate = new DealerChannelRate();
                dealerChannelRate.setDealerId(dealer.getId());
                dealerChannelRate.setProductId(productId);
                dealerChannelRate.setChannelTypeSign(channel.getChannelType());
                dealerChannelRate.setDealerTradeRate(new BigDecimal(channel.getPaymentSettleRate()).divide(new BigDecimal("100")));
                dealerChannelRate.setDealerBalanceType(channel.getSettleType());
                dealerChannelRate.setDealerWithdrawFee(new BigDecimal(channel.getWithdrawSettleFee()));
                dealerChannelRate.setDealerMerchantPayRate(new BigDecimal(channel.getMerchantSettleRate()).divide(new BigDecimal("100")));
                dealerChannelRate.setDealerMerchantWithdrawFee(new BigDecimal(channel.getMerchantWithdrawFee()));
                dealerChannelRate.setStatus(EnumDealerChannelRateStatus.USEING.getId());
                this.dealerRateService.init(dealerChannelRate);
            }

        }
    }
    /**
     * {@inheritDoc}
     *
     * @param dealer
     * @return
     */
    @Override
    @Transactional
    public int updateRecommendBtnAndTotalProfitSpace(final Dealer dealer) {
        return this.dealerDao.updateRecommendBtnAndTotalProfitSpace(dealer);
    }
    /**
     * {@inheritDoc}
     *
     * @param dealer
     * @return
     */
    @Override
    @Transactional
    public int updateInviteBtn(final Dealer dealer) {
        return this.dealerDao.updateInviteBtn(dealer);
    }

    /**
     * 根据邀请码查询代理商
     *
     * @param inviteCode
     * @return
     */
    @Override
    public Optional<Dealer> getDealerByInviteCode(String inviteCode) {
        final Dealer dealer = this.dealerDao.selectByInviteCode(inviteCode);
        return Optional.fromNullable(dealer);
    }

    /**
     * 修改密码
     *
     * @param loginPwd
     * @param dealerId
     * @return
     */
    @Override
    public int updatePwd(String loginPwd, long dealerId) {
        return this.dealerDao.updatePwd(loginPwd,dealerId);
    }

    /**
     * 查询登录名称名称是否重复
     *
     * @param loginMame
     * @return
     */
    @Override
    public long getByLoginName(String loginMame) {
        return this.dealerDao.selectByLoginName(loginMame);
    }

    /**
     * 查询登录名是否重复
     *
     * @param loginName
     * @param dealerId
     * @return
     */
    @Override
    public long getByLoginNameUnIncludeNow(String loginName, long dealerId) {
        return this.dealerDao.selectByLoginNameUnIncludeNow(loginName, dealerId);
    }

    /**
     * 【代理商后台】二级代理商列表
     *
     * @param secondDealerSearchRequest
     * @return
     */
    @Override
    public PageModel<SecondDealerResponse> listSecondDealer(SecondDealerSearchRequest secondDealerSearchRequest) {
        final PageModel<SecondDealerResponse> pageModel = new PageModel<>(secondDealerSearchRequest.getPageNo(), secondDealerSearchRequest.getPageSize());
        secondDealerSearchRequest.setOffset(pageModel.getFirstIndex());
        secondDealerSearchRequest.setCount(pageModel.getPageSize());
        final int count = this.dealerDao.selectSecondDealerCountByPage(secondDealerSearchRequest);
        final List<SecondDealerResponse> dealers = this.dealerDao.selectSecondDealersByPage(secondDealerSearchRequest);
        pageModel.setCount(count);
        pageModel.setRecords(dealers);
        return pageModel;
    }

    /**
     * {@inheritDoc}
     *
     * @param request
     * @return
     */
    @Override
    @Transactional
    public void addOrUpdateDealerProduct(final DealerAddOrUpdateRequest request,long firstLevelDealerId) {
        final Optional<Dealer> dealerOptional = this.getById(request.getDealerId());
        final Dealer dealer = dealerOptional.get();
        dealer.setInviteBtn(request.getInviteBtn());
        this.updateInviteBtn(dealer);
        for (DealerAddOrUpdateRequest.Channel channels : request.getProduct().getChannels()) {
            Optional<DealerChannelRate> dealerChannelRateOptional = this.dealerRateService.getByDealerIdAndProductIdAndChannelType(firstLevelDealerId,request.getProduct().getProductId(),channels.getChannelType());
            final Optional<DealerChannelRate> secondDealerChannelRateOptional =
                    this.dealerRateService.getByDealerIdAndProductIdAndChannelType(request.getDealerId(), request.getProduct().getProductId(), channels.getChannelType());
            if(secondDealerChannelRateOptional.isPresent()){//修改
                final DealerChannelRate secondDealerChannelRate = secondDealerChannelRateOptional.get();
                secondDealerChannelRate.setDealerTradeRate(new BigDecimal(channels.getPaymentSettleRate()).divide(new BigDecimal("100")));
                secondDealerChannelRate.setDealerWithdrawFee(new BigDecimal(channels.getWithdrawSettleFee()));
                this.dealerRateService.updateSecondDealerRate(secondDealerChannelRate);
            }else{//新增
                final DealerChannelRate dealerChannelRate = new DealerChannelRate();
                dealerChannelRate.setDealerId(dealer.getId());
                dealerChannelRate.setProductId(request.getProduct().getProductId());
                dealerChannelRate.setChannelTypeSign(channels.getChannelType());
                dealerChannelRate.setDealerTradeRate(new BigDecimal(channels.getPaymentSettleRate()).divide(new BigDecimal("100")));
                dealerChannelRate.setDealerBalanceType(dealerChannelRateOptional.get().getDealerBalanceType());
                dealerChannelRate.setDealerWithdrawFee(new BigDecimal(channels.getWithdrawSettleFee()));
                dealerChannelRate.setDealerMerchantPayRate(dealerChannelRateOptional.get().getDealerMerchantPayRate());
                dealerChannelRate.setDealerMerchantWithdrawFee(dealerChannelRateOptional.get().getDealerMerchantWithdrawFee());
                dealerChannelRate.setStatus(EnumDealerChannelRateStatus.USEING.getId());
                this.dealerRateService.init(dealerChannelRate);
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId  一级代理商id
     * @param toDealerId  码段要分配给代理商的id
     * @param startCode  开始二维码
     * @param endCode  结束二维码
     * @return
     */
    @Override
    @Transactional
    public List<DistributeQRCodeRecord> distributeQRCodeByCode(final int type, final String sysType,final long dealerId, final long toDealerId,
                                                               final String startCode, final String endCode) {
        final List<DistributeQRCodeRecord> records = new ArrayList<>();
        final List<QRCode> qrCodeList = this.qrCodeService.getUnDistributeCodeByDealerIdAndRangeCodeAndSysType(dealerId, startCode, endCode,sysType);
        if (CollectionUtils.isEmpty(qrCodeList)) {
            return records;
        }
        final List<Long> qrCodeIds = Lists.transform(qrCodeList, new Function<QRCode, Long>() {
            @Override
            public Long apply(QRCode input) {
                return input.getId();
            }
        });
        this.qrCodeService.markAsDistribute2(qrCodeIds, toDealerId);
        final List<Pair<QRCode, QRCode>> pairQRCodeList = this.qrCodeService.getPairQRCodeList(qrCodeList);
        for (Pair<QRCode, QRCode> pair : pairQRCodeList) {
            final QRCode left = pair.getLeft();
            final QRCode right = pair.getRight();
            final DistributeQRCodeRecord distributeQRCodeRecord = new DistributeQRCodeRecord();
            distributeQRCodeRecord.setFirstLevelDealerId(dealerId);
            if(toDealerId==0){
                distributeQRCodeRecord.setSecondLevelDealerId(dealerId);
            }else{
                distributeQRCodeRecord.setSecondLevelDealerId(toDealerId);
            }
            distributeQRCodeRecord.setCount((int) (Long.valueOf(right.getCode()) - Long.valueOf(left.getCode()) + 1));
            distributeQRCodeRecord.setStartCode(left.getCode());
            distributeQRCodeRecord.setEndCode(right.getCode());
            distributeQRCodeRecord.setType(type);
            distributeQRCodeRecord.setDistributeType(EnumQRCodeDistributeType2.DEALER.getCode());
            distributeQRCodeRecord.setDistributeType(EnumQRCodeDistributeType2.DEALER.getCode());
            records.add(distributeQRCodeRecord);
            this.distributeQRCodeRecordService.add(distributeQRCodeRecord);
        }
        return records;
    }

    /**
     * 按个数分配
     *
     * @param type
     * @param dealerId
     * @param toDealerId
     * @param count
     * @return
     */
    @Override
    public List<DistributeQRCodeRecord> distributeQRCodeByCount(int type, String sysType, long dealerId, long toDealerId, int count) {
        final List<DistributeQRCodeRecord> records = new ArrayList<>();
        final List<QRCode> qrCodeList = this.qrCodeService.getUnDistributeCodeByDealerIdAndSysType(dealerId,sysType);
        if (CollectionUtils.isEmpty(qrCodeList)) {
            return records;
        }
        final List<Long> qrCodeIds = Lists.transform(qrCodeList, new Function<QRCode, Long>() {
            @Override
            public Long apply(QRCode input) {
                return input.getId();
            }
        });
        final List<Long> ids = qrCodeIds.subList(0, count);
        final List<QRCode> qrCodeList1 = qrCodeList.subList(0, count);
        this.qrCodeService.markAsDistribute2(ids, toDealerId);
        final List<Pair<QRCode, QRCode>> pairQRCodeList = this.qrCodeService.getPairQRCodeList(qrCodeList1);
        for (Pair<QRCode, QRCode> pair : pairQRCodeList) {
            final QRCode left = pair.getLeft();
            final QRCode right = pair.getRight();
            final DistributeQRCodeRecord distributeQRCodeRecord = new DistributeQRCodeRecord();
            distributeQRCodeRecord.setFirstLevelDealerId(dealerId);
            distributeQRCodeRecord.setSecondLevelDealerId(toDealerId);
            distributeQRCodeRecord.setCount((int) (Long.valueOf(right.getCode()) - Long.valueOf(left.getCode()) + 1));
            distributeQRCodeRecord.setStartCode(left.getCode());
            distributeQRCodeRecord.setEndCode(right.getCode());
            distributeQRCodeRecord.setType(type);
            distributeQRCodeRecord.setDistributeType(EnumQRCodeDistributeType2.DEALER.getCode());
            records.add(distributeQRCodeRecord);
            this.distributeQRCodeRecordService.add(distributeQRCodeRecord);
        }
        return records;
    }

    /**
     * 根据产品类型和手机号或代理商名称模糊查询
     *
     * @param dealerOfFirstDealerRequest
     * @return
     */
    @Override
    public List<DealerOfFirstDealerResponse> selectListOfFirstDealer(DealerOfFirstDealerRequest dealerOfFirstDealerRequest) {
        return this.dealerDao.selectListOfFirstDealer(dealerOfFirstDealerRequest);
    }

    /**
     * {@inheritDoc}
     * @param loginName
     * @return
     */
    @Override
    public Dealer getDealerByLoginName(String loginName) {
        return this.dealerDao.getDealerByLoginName(loginName);
    }

    /**
     * 【代理商后台】二维码分配记录
     *
     * @param distributeRecordRequest
     * @param firstLevelDealerId
     * @return
     */
    @Override
    public PageModel<DistributeRecordResponse> distributeRecord(DistributeRecordRequest distributeRecordRequest, long firstLevelDealerId) {
        final PageModel<DistributeRecordResponse> pageModel = new PageModel<>(distributeRecordRequest.getPageNo(), distributeRecordRequest.getPageSize());
        final int count = distributeQRCodeRecordService.selectDistributeCountByContions(firstLevelDealerId,distributeRecordRequest.getMarkCode(),distributeRecordRequest.getName());
        final List<DistributeQRCodeRecord> distributeQRCodeRecords = distributeQRCodeRecordService.selectDistributeRecordsByContions(firstLevelDealerId,distributeRecordRequest.getMarkCode(),distributeRecordRequest.getName(),pageModel.getFirstIndex(),pageModel.getPageSize());
        List<DistributeRecordResponse> distributeRecordResponses = new ArrayList<DistributeRecordResponse>();
        if(distributeQRCodeRecords.size()>0){
            for(int i=0;i<distributeQRCodeRecords.size();i++){
                DistributeQRCodeRecord distributeQRCodeRecord = distributeQRCodeRecords.get(i);
                DistributeRecordResponse distributeRecordResponse = new DistributeRecordResponse();
                distributeRecordResponse.setId(distributeQRCodeRecord.getId());
                distributeRecordResponse.setDistributeTime(distributeQRCodeRecord.getCreateTime());
                Dealer dealer = dealerDao.selectById(distributeQRCodeRecord.getSecondLevelDealerId());
                distributeRecordResponse.setProxyName(dealer.getProxyName());
                distributeRecordResponse.setMarkCode(dealer.getMarkCode());
                distributeRecordResponse.setCount(distributeQRCodeRecord.getCount());
                distributeRecordResponse.setStartCode(distributeQRCodeRecord.getStartCode());
                distributeRecordResponse.setEndCode(distributeQRCodeRecord.getEndCode());
                distributeRecordResponse.setType(distributeQRCodeRecord.getType());
                distributeRecordResponse.setOperateUser("admin");
                distributeRecordResponses.add(distributeRecordResponse);
            }
        }
        pageModel.setCount(count);
        pageModel.setRecords(distributeRecordResponses);
        return pageModel;
    }

    /**
     * 【boss后台】二维码分配记录
     *
     * @param distributeRecordRequest
     * @return
     */
    @Override
    public PageModel<BossDistributeQRCodeRecordResponse> distributeRecord(DistributeQrCodeRequest distributeRecordRequest) {

        final PageModel<BossDistributeQRCodeRecordResponse> pageModel = new PageModel<>(distributeRecordRequest.getPageNo(), distributeRecordRequest.getPageSize());
        distributeRecordRequest.setOffset(pageModel.getFirstIndex());
        distributeRecordRequest.setCount(pageModel.getPageSize());

        boolean isJkm = false;
        if("000000000000".equals(distributeRecordRequest.getFirstMarkCode())){
            isJkm=true;
        }
        if("金开门".equals(distributeRecordRequest.getFirstName())){
            isJkm=true;
        }
        int count = 0;
        List<DistributeQRCodeRecord> distributeQRCodeRecords = new ArrayList<DistributeQRCodeRecord>();
        if(isJkm==false){
            count = distributeQRCodeRecordService.selectDistributeCountByContions(distributeRecordRequest);
            distributeQRCodeRecords = distributeQRCodeRecordService.selectDistributeRecordsByContions(distributeRecordRequest);
        }else{
            count = distributeQRCodeRecordService.selectBossDistributeCountByContionsOfJKM(distributeRecordRequest);
            distributeQRCodeRecords = distributeQRCodeRecordService.selectBossDistributeRecordsByContionsOfJKM(distributeRecordRequest);
        }
        List<BossDistributeQRCodeRecordResponse> bossDistributeQRCodeRecordResponses = new ArrayList<BossDistributeQRCodeRecordResponse>();
        if(distributeQRCodeRecords.size()>0){
            for(int i=0;i<distributeQRCodeRecords.size();i++){
                DistributeQRCodeRecord distributeQRCodeRecord = distributeQRCodeRecords.get(i);
                BossDistributeQRCodeRecordResponse bossDistributeQRCodeRecordResponse = new BossDistributeQRCodeRecordResponse();
                bossDistributeQRCodeRecordResponse.setId(distributeQRCodeRecord.getId());
                bossDistributeQRCodeRecordResponse.setDistributeTime(distributeQRCodeRecord.getCreateTime());
                Dealer dealer = dealerDao.selectById(distributeQRCodeRecord.getSecondLevelDealerId());
                bossDistributeQRCodeRecordResponse.setProxyName(dealer.getProxyName());
                bossDistributeQRCodeRecordResponse.setMarkCode(dealer.getMarkCode());
                if(distributeQRCodeRecord.getFirstLevelDealerId()==0){
                    bossDistributeQRCodeRecordResponse.setFristProxyName("金开门");
                    bossDistributeQRCodeRecordResponse.setFirstMarkCode("000000000000");
                }else{
                    Dealer firstDealer = dealerDao.selectById(distributeQRCodeRecord.getFirstLevelDealerId());
                    bossDistributeQRCodeRecordResponse.setFristProxyName(firstDealer.getProxyName());
                    bossDistributeQRCodeRecordResponse.setFirstMarkCode(firstDealer.getMarkCode());
                }
                bossDistributeQRCodeRecordResponse.setCount(distributeQRCodeRecord.getCount());
                bossDistributeQRCodeRecordResponse.setStartCode(distributeQRCodeRecord.getStartCode());
                bossDistributeQRCodeRecordResponse.setEndCode(distributeQRCodeRecord.getEndCode());
                bossDistributeQRCodeRecordResponse.setType(distributeQRCodeRecord.getType());
                bossDistributeQRCodeRecordResponse.setOperateUser("admin");
                bossDistributeQRCodeRecordResponses.add(bossDistributeQRCodeRecordResponse);
            }
        }
        pageModel.setCount(count);
        pageModel.setRecords(bossDistributeQRCodeRecordResponses);
        return pageModel;
    }

    @Override
    public String selectProxyName(long firstLevelDealerId) {
        String proxyName = dealerDao.selectProxyName(firstLevelDealerId);
        return proxyName;
    }

    @Override
    public MerchantInfoResponse getProxyName(int firstLevelDealerId) {
        MerchantInfoResponse response = dealerDao.getProxyName(firstLevelDealerId);
        return response;
    }

    @Override
    public MerchantInfoResponse getInfo(long firstDealerId) {
        MerchantInfoResponse response = dealerDao.getInfo(firstDealerId);
        return response;
    }

    @Override
    public MerchantInfoResponse getInfo1(long secondDealerId) {
        MerchantInfoResponse response = dealerDao.getInfo1(secondDealerId);
        return response;
    }

    /**
     * 根据代理商编码查询代理商信息
     *
     * @param markCode
     * @return
     */
    @Override
    public Optional<Dealer> getDealerByMarkCode(String markCode) {
        return Optional.fromNullable(dealerDao.getDealerByMarkCode(markCode));
    }
    @Override
    public List<QueryMerchantResponse> dealerMerchantList(QueryMerchantRequest req) {
        List<QueryMerchantResponse> list = this.dealerDao.dealerMerchantList(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
                if (list.get(i).getAuthenticationTime()!=null){
                    String dates = sdf.format(list.get(i).getAuthenticationTime());
                    list.get(i).setAuthenticationTimes(dates);
                }
                if (list.get(i).getMobile()!=null&&!list.get(i).getMobile().equals("")){
                    list.get(i).setMobile(MerchantSupport.decryptMobile(list.get(i).getMobile()));
                }
                if (list.get(i).getSource()==0){
                    list.get(i).setRegistered(EnumSource.SCAN.getValue());
                }
                if (list.get(i).getSource()==1){
                    list.get(i).setRegistered(EnumSource.RECOMMEND.getValue());
                }
                if (list.get(i).getSource()==2){
                    list.get(i).setRegistered(EnumSource.DEALERRECOMMEND.getValue());
                }
                if(list.get(i).getStatus()==0){
                    list.get(i).setStatusValue(EnumMerchantStatus.INIT.getName());
                }
                if(list.get(i).getStatus()==1){
                    list.get(i).setStatusValue(EnumMerchantStatus.ONESTEP.getName());
                }
                if(list.get(i).getStatus()==2){
                    list.get(i).setStatusValue(EnumMerchantStatus.REVIEW.getName());
                }
                if(list.get(i).getStatus()==3||list.get(i).getStatus()==6){
                    list.get(i).setStatusValue(EnumMerchantStatus.PASSED.getName());
                }
                if(list.get(i).getStatus()==4){
                    list.get(i).setStatusValue(EnumMerchantStatus.UNPASSED.getName());
                }
            }
        }
        return list;
    }

    @Override
    public int dealerMerchantCount(QueryMerchantRequest req) {

        return this.dealerDao.dealerMerchantCount(req);
    }

    @Override
    public List<QueryMerchantResponse> dealerMerchantSecondList(QueryMerchantRequest req) {
        List<QueryMerchantResponse> list = this.dealerDao.dealerMerchantSecondList(req);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getCreateTime()!=null){
                    String dates = sdf.format(list.get(i).getCreateTime());
                    list.get(i).setCreateTimes(dates);
                }
                if (list.get(i).getAuthenticationTime()!=null){
                    String dates = sdf.format(list.get(i).getAuthenticationTime());
                    list.get(i).setAuthenticationTimes(dates);
                }
                if (list.get(i).getMobile()!=null&&!list.get(i).getMobile().equals("")){
                    list.get(i).setMobile(MerchantSupport.decryptMobile(list.get(i).getMobile()));
                }
                if (list.get(i).getSource()==0){
                    list.get(i).setRegistered(EnumSource.SCAN.getValue());
                }
                if (list.get(i).getSource()==1){
                    list.get(i).setRegistered(EnumSource.RECOMMEND.getValue());
                }
                if (list.get(i).getSource()==2){
                    list.get(i).setRegistered(EnumSource.DEALERRECOMMEND.getValue());
                }
                if(list.get(i).getStatus()==0){
                    list.get(i).setStatusValue(EnumMerchantStatus.INIT.getName());
                }
                if(list.get(i).getStatus()==1){
                    list.get(i).setStatusValue(EnumMerchantStatus.ONESTEP.getName());
                }
                if(list.get(i).getStatus()==2){
                    list.get(i).setStatusValue(EnumMerchantStatus.REVIEW.getName());
                }
                if(list.get(i).getStatus()==3||list.get(i).getStatus()==6){
                    list.get(i).setStatusValue(EnumMerchantStatus.PASSED.getName());
                }
                if(list.get(i).getStatus()==4){
                    list.get(i).setStatusValue(EnumMerchantStatus.UNPASSED.getName());
                }
            }
        }
        return list;
    }

    @Override
    public int dealerMerchantSecondCount(QueryMerchantRequest req) {
        return this.dealerDao.dealerMerchantSecondCount(req);
    }
}
