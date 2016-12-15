package com.jkm.hss.dealer.service.impl;


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
     * @param orderRecord
     */
    @Transactional
    @Override
    public Map<String, Triple<Long, BigDecimal, String>> withdrawProfitCount(OrderRecord orderRecord) {
        final ShallProfitDetail detail = this.shallProfitDetailDao.selectByOrderId(orderRecord.getOrderId());
        if (detail != null){
            log.error("此订单分润业务已经处理过[" + orderRecord.getOrderId() +"]");
            return null;
        }
        //提现分润
        final Optional<MerchantInfo> merchantInfoOptional =
                this.merchantInfoService.selectById(orderRecord.getMerchantId());
        Preconditions.checkNotNull(merchantInfoOptional.isPresent(), "商户信息不存在");
        final Map<String, Triple<Long, BigDecimal, String>> map = new HashMap<>();
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if (merchantInfo.getDealerId() == 0){
            final List<ProductChannelDetail> list = this.productChannelDetailService.selectByChannelTypeSign(orderRecord.getPayChannel());
            final ProductChannelDetail productChannelDetail = list.get(0);
            final Optional<BasicChannel> channelOptional =  this.basicChannelService.selectByChannelTypeSign(orderRecord.getPayChannel());
            final BasicChannel basicChannel = channelOptional.get();
            //获取产品的信息, 产品通道的费率
            final Optional<Product> productOptional = this.productService.selectById(productChannelDetail.getProductId());
            final  Product product = productOptional.get();
            final BigDecimal productMoney = productChannelDetail.getProductMerchantWithdrawFee().subtract(productChannelDetail.getProductWithdrawFee());
            final BigDecimal channelMoney = productChannelDetail.getProductWithdrawFee().subtract(basicChannel.getBasicWithdrawFee());
            final CompanyProfitDetail companyProfitDetail = new CompanyProfitDetail();
            companyProfitDetail.setMerchantId(orderRecord.getMerchantId());
            companyProfitDetail.setPaymentSn(orderRecord.getOrderId());
            companyProfitDetail.setTotalFee(orderRecord.getTotalFee());
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
        List<ProductChannelDetail> list = this.productChannelDetailService.selectByProductId(dealer.getId());
        final ProductChannelDetail productChannelDetail = list.get(0);
        final Optional<BasicChannel> basicChannelOptional = this.basicChannelService.selectByChannelTypeSign(productChannelDetail.getChannelTypeSign());
        final BasicChannel basicChannel = basicChannelOptional.get();
        final Product product = this.productService.selectById(productChannelDetail.getProductId()).get();
        //获取代理商通道费率
        final List<DealerChannelRate> dealerChannelList =
                this.dealerChannelRateService.selectByDealerIdAndProductId(dealer.getId(), productChannelDetail.getProductId());
        final DealerChannelRate dealerChannelRate = dealerChannelList.get(0);
        //判断是几级代理
        if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
            //商户体现手续费
            final BigDecimal withdrawMoney = dealerChannelRate.getDealerMerchantWithdrawFee();
            final BigDecimal firstMoney = withdrawMoney.subtract(dealerChannelRate.getDealerWithdrawFee());
            final BigDecimal productMoney = dealerChannelRate.getDealerWithdrawFee().subtract(productChannelDetail.getProductWithdrawFee());
            final BigDecimal channelMoney = productChannelDetail.getProductWithdrawFee().subtract(basicChannel.getBasicWithdrawFee());
            final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
            shallProfitDetail.setPaymentSn(orderRecord.getOrderId());
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
                    this.dealerChannelRateService.selectByDealerIdAndProductId(firstDealer.getId(), productChannelDetail.getProductId());
            final DealerChannelRate firstDealerChannelRate = firstDealerChannelList.get(0);
            //商户体现手续费
            final BigDecimal withdrawMoney = firstDealerChannelRate.getDealerMerchantWithdrawFee();
            final BigDecimal secondMoney = withdrawMoney.subtract(firstDealerChannelRate.getDealerWithdrawFee());
            final BigDecimal firstMoney = dealerChannelRate.getDealerWithdrawFee().subtract(firstDealerChannelRate.getDealerWithdrawFee());
            final BigDecimal productMoney = firstDealerChannelRate.getDealerWithdrawFee().subtract(productChannelDetail.getProductWithdrawFee());
            final BigDecimal channelMoney = productChannelDetail.getProductWithdrawFee().subtract(basicChannel.getBasicWithdrawFee());
            final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
            shallProfitDetail.setPaymentSn(orderRecord.getOrderId());
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
        log.info("订单" + orderRecord.getOrderId() + "分润处理成功,返回map成功");
        return map;
    }

    /**
     *
     * @param merchantId
     * @return
     */
    @Override
    public Pair<BigDecimal, BigDecimal> withdrawParams(final long merchantId) {
        try{
            final Optional<MerchantInfo> merchantInfoOptional =
                    this.merchantInfoService.selectById(merchantId);
            Preconditions.checkNotNull(merchantInfoOptional.isPresent(), "商户信息不存在");
            final MerchantInfo merchantInfo = merchantInfoOptional.get();
            if (merchantInfo.getDealerId() == 0){
                //jkm直属商户
                final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(EnumPayChannelSign.YG_WEIXIN.getId()).get();
                return Pair.of(new BigDecimal("2").setScale(2), basicChannel.getBasicWithdrawFee().setScale(2));
            }
            final Optional<Dealer> dealerOptional = this.dealerService.getById(merchantInfo.getDealerId());
            Preconditions.checkNotNull(dealerOptional.isPresent(), "代理商不存在");
            final Dealer dealer = dealerOptional.get();
            //获取代理商通道费率
            final List<DealerChannelRate> dealerChannelList =
                    this.dealerChannelRateService.selectByDealerId(dealer.getId());
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
}
