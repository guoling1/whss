package com.jkm.hss.dealer.service.impl;


import com.fasterxml.jackson.databind.ObjectReader;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.dealer.dao.ShallProfitDetailDao;
import com.jkm.hss.dealer.entity.*;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.enums.EnumProfitType;
import com.jkm.hss.dealer.service.*;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
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
 * Created by yuxiang on 2016-11-24.
 */
@Slf4j
@Service
public class ShallProfitDetailServiceImpl implements ShallProfitDetailService{

    @Autowired
    private ShallProfitDetailDao shallProfitDetailDao;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DealerChannelRateService dealerChannelRateService;
    @Autowired
    private CompanyProfitDetailService companyProfitDetailService;
    @Autowired
    private DailyProfitDetailService dailyProfitDetailService;
    @Autowired
    private DealerRateService dealerRateService;
    /**
     * {@inheritDoc}
     *
     * @param shallProfitDetail
     */
    @Override
    public void init(ShallProfitDetail shallProfitDetail) {
        this.shallProfitDetailDao.init(shallProfitDetail);
    }

    /**
     * {@inheritDoc}
     *
     * @param orderNo
     * @param tradeAmount
     * @param channelSign
     * @param merchantId
     * @return
     */
    @Transactional
    @Override
    public Map<String, Triple<Long, BigDecimal, String>> withdrawProfitCount(final EnumProductType type,final String orderNo, final BigDecimal tradeAmount,
                                                                             final int channelSign, final long merchantId) {

        log.info("商户[" + merchantId + "]请求进行提现分润，交易订单号:" + orderNo);
        //提现只有直接商户参与分润，分润计算方式不变
        final Optional<MerchantInfo> merchantInfoOptional =
                this.merchantInfoService.selectById(merchantId);
        Preconditions.checkNotNull(merchantInfoOptional.isPresent(), "商户信息不存在");
        final MerchantInfo merchantInfo = merchantInfoOptional.get();

        if (merchantInfo.getFirstMerchantId() != 0){
            log.info("商户[" + merchantId + "]请求进行提现分润，由于该商户是间接商户，不参与分润，直接进入公司账户，交易订单号:" + orderNo);

            final List<ProductChannelDetail> list = this.productChannelDetailService.selectByChannelTypeSign(channelSign);
            final ProductChannelDetail productChannelDetail = list.get(0);
            final Optional<BasicChannel> channelOptional =  this.basicChannelService.selectByChannelTypeSign(channelSign);
            final BasicChannel basicChannel = channelOptional.get();
            final BigDecimal channelMoney = productChannelDetail.getProductWithdrawFee().subtract(basicChannel.getBasicWithdrawFee());
            //获取产品的信息, 产品通道的费率
            final Optional<Product> productOptional = this.productService.selectById(productChannelDetail.getProductId());
            final  Product product = productOptional.get();
            final BigDecimal merchantWithdrawFee = this.getMerchantWithdrawFee(merchantId, channelSign);
            final BigDecimal productMoney = merchantWithdrawFee.subtract(productChannelDetail.getProductWithdrawFee());
            Map<String, Triple<Long, BigDecimal, String>> map = new HashMap<>();
            map.put("productMoney",Triple.of(product.getAccountId(), merchantWithdrawFee,"M1"));
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney,"M1"));
            final CompanyProfitDetail companyProfitDetail = new CompanyProfitDetail();
            companyProfitDetail.setMerchantId(merchantId);
            companyProfitDetail.setPaymentSn(orderNo);
            companyProfitDetail.setTotalFee(tradeAmount);
            companyProfitDetail.setWaitShallAmount(productChannelDetail.getProductMerchantWithdrawFee());
            companyProfitDetail.setWaitShallOriginAmount(productChannelDetail.getProductMerchantWithdrawFee());
            companyProfitDetail.setProfitType(EnumProfitType.INDIRECTWITHDRAW.getId());
            companyProfitDetail.setProductShallAmount(productMoney);
            companyProfitDetail.setChannelShallAmount(channelMoney);
            companyProfitDetail.setChannelCost(basicChannel.getBasicWithdrawFee());
            companyProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
            this.companyProfitDetailService.add(companyProfitDetail);

            return map;
        }
        try{
            //提现分润
            final Map<String, Triple<Long, BigDecimal, String>> map = new HashMap<>();
            if (merchantInfo.getDealerId() == 0){
                final List<ProductChannelDetail> list = this.productChannelDetailService.selectByChannelTypeSign(channelSign);
                final ProductChannelDetail productChannelDetail = list.get(0);
                final Optional<BasicChannel> channelOptional =  this.basicChannelService.selectByChannelTypeSign(channelSign);
                final BasicChannel basicChannel = channelOptional.get();
                //获取产品的信息, 产品通道的费率
                final Optional<Product> productOptional = this.productService.selectById(productChannelDetail.getProductId());
                final  Product product = productOptional.get();
                final BigDecimal productMoney = productChannelDetail.getProductMerchantWithdrawFee().subtract(productChannelDetail.getProductWithdrawFee());
                final BigDecimal channelMoney = productChannelDetail.getProductWithdrawFee().subtract(basicChannel.getBasicWithdrawFee());
                final CompanyProfitDetail companyProfitDetail = new CompanyProfitDetail();
                companyProfitDetail.setMerchantId(merchantId);
                companyProfitDetail.setPaymentSn(orderNo);
                companyProfitDetail.setTotalFee(tradeAmount);
                companyProfitDetail.setWaitShallAmount(productChannelDetail.getProductMerchantWithdrawFee());
                companyProfitDetail.setWaitShallOriginAmount(productChannelDetail.getProductMerchantWithdrawFee());
                companyProfitDetail.setProfitType(EnumProfitType.WITHDRAW.getId());
                companyProfitDetail.setProductShallAmount(productMoney);
                companyProfitDetail.setChannelShallAmount(channelMoney);
                companyProfitDetail.setChannelCost(basicChannel.getBasicWithdrawFee());
                companyProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                this.companyProfitDetailService.add(companyProfitDetail);
                map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney,"M1"));
                map.put("productMoney",Triple.of(product.getAccountId(), productMoney,"M1"));
                return map;
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getById(merchantInfo.getDealerId());
            Preconditions.checkNotNull(dealerOptional.isPresent(), "代理商不存在");
            final Dealer dealer = dealerOptional.get();
            //根据代理商id查询其产品通道费率,产品费率,通道成本费率
            List<ProductChannelDetail> list = this.productChannelDetailService.selectByChannelTypeSign(channelSign);
            final ProductChannelDetail productChannelDetail = list.get(0);
            final Optional<BasicChannel> basicChannelOptional = this.basicChannelService.selectByChannelTypeSign(productChannelDetail.getChannelTypeSign());
            final BasicChannel basicChannel = basicChannelOptional.get();
            final Product product = this.productService.selectById(productChannelDetail.getProductId()).get();
            //获取代理商通道费率
            final List<DealerChannelRate> dealerChannelList =
                    this.dealerChannelRateService.selectByDealerIdAndPayChannelSign(dealer.getId(), channelSign);
            final DealerChannelRate dealerChannelRate = dealerChannelList.get(0);
            //判断是几级代理
            if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
                //商户体现手续费
                final BigDecimal withdrawMoney = dealerChannelRate.getDealerMerchantWithdrawFee();
                final BigDecimal firstMoney = withdrawMoney.subtract(dealerChannelRate.getDealerWithdrawFee());
                final BigDecimal productMoney = dealerChannelRate.getDealerWithdrawFee().subtract(productChannelDetail.getProductWithdrawFee());
                final BigDecimal channelMoney = productChannelDetail.getProductWithdrawFee().subtract(basicChannel.getBasicWithdrawFee());
                final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
                shallProfitDetail.setMerchantId(merchantId);
                shallProfitDetail.setPaymentSn(orderNo);
                shallProfitDetail.setTotalFee(tradeAmount);
                shallProfitDetail.setChannelType(channelSign);
                shallProfitDetail.setWaitShallAmount(withdrawMoney);
                shallProfitDetail.setWaitShallOriginAmount(withdrawMoney);
                shallProfitDetail.setIsDirect(1);
                shallProfitDetail.setProfitType(EnumProfitType.WITHDRAW.getId());
                shallProfitDetail.setChannelShallAmount(channelMoney);
                shallProfitDetail.setProductShallAmount(productMoney);
                shallProfitDetail.setFirstDealerId(dealer.getId());
                shallProfitDetail.setFirstShallAmount(firstMoney);
                shallProfitDetail.setSecondDealerId(0);
                shallProfitDetail.setChannelCost(basicChannel.getBasicWithdrawFee());
                shallProfitDetail.setSecondShallAmount(new BigDecimal(0));
                shallProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                this.shallProfitDetailDao.init(shallProfitDetail);
                map.put("firstMoney", Triple.of(dealer.getAccountId(), firstMoney, "M1"));
                map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney, "M1"));
                map.put("productMoney",Triple.of(product.getAccountId(), productMoney, "M1"));
            }else if(dealer.getLevel() == EnumDealerLevel.SECOND.getId()){
                //查找一级代理的代理通道费率
                final Optional<Dealer> firstDealerOptional = this.dealerService.getById(dealer.getFirstLevelDealerId());
                Preconditions.checkNotNull(firstDealerOptional.isPresent(), "一级代理信息不存在");
                final Dealer firstDealer = firstDealerOptional.get();
                //获取一级代理商通道费率
                final List<DealerChannelRate> firstDealerChannelList =
                        this.dealerChannelRateService.selectByDealerIdAndPayChannelSign(firstDealer.getId(), channelSign);
                final DealerChannelRate firstDealerChannelRate = firstDealerChannelList.get(0);
                //商户体现手续费
                final BigDecimal withdrawMoney = dealerChannelRate.getDealerMerchantWithdrawFee();
                final BigDecimal secondMoney = withdrawMoney.subtract(dealerChannelRate.getDealerWithdrawFee());
                final BigDecimal firstMoney = dealerChannelRate.getDealerWithdrawFee().subtract(firstDealerChannelRate.getDealerWithdrawFee());
                final BigDecimal productMoney = firstDealerChannelRate.getDealerWithdrawFee().subtract(productChannelDetail.getProductWithdrawFee());
                final BigDecimal channelMoney = productChannelDetail.getProductWithdrawFee().subtract(basicChannel.getBasicWithdrawFee());
                final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
                shallProfitDetail.setMerchantId(merchantId);
                shallProfitDetail.setPaymentSn(orderNo);
                shallProfitDetail.setChannelType(channelSign);
                shallProfitDetail.setTotalFee(tradeAmount);
                shallProfitDetail.setWaitShallAmount(withdrawMoney);
                shallProfitDetail.setWaitShallOriginAmount(withdrawMoney);
                shallProfitDetail.setIsDirect(0);
                shallProfitDetail.setProfitType(EnumProfitType.WITHDRAW.getId());
                shallProfitDetail.setChannelShallAmount(channelMoney);
                shallProfitDetail.setProductShallAmount(productMoney);
                shallProfitDetail.setFirstDealerId(firstDealer.getId());
                shallProfitDetail.setFirstShallAmount(firstMoney);
                shallProfitDetail.setSecondDealerId(dealer.getId());
                shallProfitDetail.setChannelCost(basicChannel.getBasicWithdrawFee());
                shallProfitDetail.setSecondShallAmount(secondMoney);
                shallProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
                this.shallProfitDetailDao.init(shallProfitDetail);
                map.put("firstMoney", Triple.of(firstDealer.getAccountId(), firstMoney, "M1"));
                map.put("secondMoney", Triple.of(dealer.getAccountId(),secondMoney, "M1"));
                map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney,"M1"));
                map.put("productMoney",Triple.of(product.getAccountId(), productMoney,"M1"));
            }
            log.info("订单" + orderNo + "分润处理成功,返回map成功");
            return map;
        }catch (final Throwable throwable){

            log.error("商户[" + merchantId + "]请求进行提现分润异常，交易订单号:" + orderNo + "异常信息：" + throwable.getMessage());
            throw throwable;
        }

    }

    private BigDecimal getMerchantWithdrawFee(final long merchantId, final int channelSign) {
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
     *
     * @param merchantId
     * @return
     */
    @Override
    public Pair<BigDecimal, BigDecimal> withdrawParams(final long merchantId, final int payChannel) {
        try{
            final Optional<MerchantInfo> merchantInfoOptional =
                    this.merchantInfoService.selectById(merchantId);
            Preconditions.checkNotNull(merchantInfoOptional.isPresent(), "商户信息不存在");
            final MerchantInfo merchantInfo = merchantInfoOptional.get();
            if (merchantInfo.getDealerId() == 0){
                //jkm直属商户
                final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(payChannel).get();
                final List<ProductChannelDetail> list = this.productChannelDetailService.selectByChannelTypeSign(payChannel);
                return Pair.of(list.get(0).getProductMerchantWithdrawFee().setScale(2), basicChannel.getBasicWithdrawFee().setScale(2));
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getById(merchantInfo.getDealerId());
            Preconditions.checkNotNull(dealerOptional.isPresent(), "代理商不存在");
            final Dealer dealer = dealerOptional.get();
            //获取代理商通道费率
            final List<DealerChannelRate> dealerChannelList =
                    this.dealerChannelRateService.selectByDealerIdAndPayChannelSign(dealer.getId(), payChannel);
            final DealerChannelRate dealerChannelRate = dealerChannelList.get(0);
            final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(dealerChannelRate.getChannelTypeSign()).get();
            //判断是几级代理
            if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
                return Pair.of(dealerChannelRate.getDealerMerchantWithdrawFee().setScale(2), basicChannel.getBasicWithdrawFee().setScale(2));
            } else if(dealer.getLevel() == EnumDealerLevel.SECOND.getId()){
                return Pair.of(dealerChannelRate.getDealerMerchantWithdrawFee().setScale(2), basicChannel.getBasicWithdrawFee().setScale(2));
            }
        }catch (final Throwable throwable){
            log.error(throwable.getMessage());
        }
        return Pair.of(new BigDecimal("2.00"), new BigDecimal("0.50"));
    }

    /**
     * {@inheritDoc}
     * @param profitDate
     * @return
     */
    @Override
    public List<ShallProfitDetail> selectDeatailByProfitDate(String profitDate) {
        return this.shallProfitDetailDao.selectDeatailByProfitDate(profitDate);
    }

    /**
     * {@inheritDoc}
     *
     * @param profitDate
     * @return
     */
    @Override
    public List<Long> selectMerchantIdByProfitDate(String profitDate) {
        return this.shallProfitDetailDao.selectMerchantIdByProfitDate(profitDate);
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectSecondCollectMoneyByMerchantIdAndProfitDate(Long merchantId, String profitDate) {
        final BigDecimal bigDecimal = shallProfitDetailDao.selectSecondCollectMoneyByMerchantIdAndProfitDate(merchantId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param merchantId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectFirstCollectMoneyByMerchantIdAndProfitDate(Long merchantId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectFirstCollectMoneyByMerchantIdAndProfitDate(merchantId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param merchantId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectFirstWithdrawMoneyByMerchantIdAndProfitDate(Long merchantId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectFirstWithdrawMoneyByMerchantIdAndProfitDate(merchantId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param merchantId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectSecondWithdrawMoneyByMerchantIdAndProfitDate(Long merchantId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectSecondWithdrawMoneyByMerchantIdAndProfitDate(merchantId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param profitDate
     * @return
     */
    @Override
    public List<Long> selectDealerIdByProfitDate(String profitDate) {
        return this.shallProfitDetailDao.selectDealerIdByProfitDate(profitDate);
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectFirstCollectMoneyByDealerIdAndProfitDate(long dealerId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectFirstCollectMoneyByDealerIdAndProfitDate(dealerId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectFirstWithdrawMoneyByDealerIdAndProfitDate(long dealerId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectFirstWithdrawMoneyByDealerIdAndProfitDate(dealerId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectSecondYesterdayProfitMoney(long dealerId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectSecondYesterdayProfitMoney(dealerId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectSecondYesterdayDealMoney(long dealerId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectSecondYesterdayDealMoney(dealerId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @param
     * @return
     */
    @Override
    public BigDecimal selectSecondHistoryProfitMoney(long dealerId) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectSecondHistoryProfitMoney(dealerId);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectFirstSecondYesterdayProfitMoney(long dealerId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectFirstSecondYesterdayProfitMoney(dealerId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @return
     */
    @Override
    public BigDecimal selectFirstSecondHistoryProfitMoney(long dealerId) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectFirstSecondHistoryProfitMoney(dealerId);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectFirstMerchantYesterdayProfitMoney(long dealerId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectFirstMerchantYesterdayProfitMoney(dealerId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @return
     */
    @Override
    public BigDecimal selectFirstMerchantHistoryProfitMoney(long dealerId) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectFirstMerchantHistoryProfitMoney(dealerId);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     * @param dealerId
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectFirstYesterdayDealMoney(long dealerId, String profitDate) {
        final BigDecimal bigDecimal = this.shallProfitDetailDao.selectFirstYesterdayDealMoney(dealerId, profitDate);
        if (bigDecimal == null){
            return new BigDecimal(0);
        }
        return bigDecimal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ShallProfitDetail selectByOrderId(String orderId) {
        return this.shallProfitDetailDao.selectByOrderId(orderId);
    }

    /**
     * {@inheritDoc}
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectCompanyCollectProfitByProfitDate(String profitDate) {
        return this.shallProfitDetailDao.selectCompanyCollectProfitByProfitDate(profitDate);
    }

    /**
     * {@inheritDoc}
     * @param profitDate
     * @return
     */
    @Override
    public BigDecimal selectCompanyWithdrawProfitByProfitDate(String profitDate) {
        return this.shallProfitDetailDao.selectCompanyWithdrawProfitByProfitDate(profitDate);
    }

    /**
     * {@inheritDoc}
     * @param dailyProfitId
     * @return
     */
    @Override
    public List<ShallProfitDetail> getSecondDealerDeatail(long dailyProfitId) {
        final DailyProfitDetail dailyProfitDetail = this.dailyProfitDetailService.selectById(dailyProfitId);
        final List<ShallProfitDetail> list = this.shallProfitDetailDao.selectByProfitDateAndSecondDealerId(
                dailyProfitDetail.getSecondDealerId(), dailyProfitDetail.getStatisticsDate());
        return list;
    }

    /**
     * {@inheritDoc}
     * @param dailyProfitId
     * @return
     */
    @Override
    public List<ShallProfitDetail> getFirstDealerDeatail(long dailyProfitId) {
        final DailyProfitDetail dailyProfitDetail = this.dailyProfitDetailService.selectById(dailyProfitId);
        final List<ShallProfitDetail> list = this.shallProfitDetailDao.selectByProfitDateAndFirstDealerId(
                dailyProfitDetail.getFirstDealerId(), dailyProfitDetail.getStatisticsDate());
        return list;
    }


    /**
     * {@inheritDoc}
     * @param profitDate
     * @return
     */
    @Override
    public List<ShallProfitDetail> selectCompanyByProfitDate(String profitDate) {
        return this.shallProfitDetailDao.selectCompanyByProfitDate(profitDate);
    }

    /**
     * {@inheritDoc}
     * @param statisticsDate
     * @return
     */
    @Override
    public List<Long> getMerchantIdByProfitDate(String statisticsDate) {
        return this.shallProfitDetailDao.getMerchantIdByProfitDate(statisticsDate);
    }
}
