package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.helper.responseparam.ActiveCodeCount;
import com.jkm.hss.admin.helper.responseparam.DistributeCodeCount;
import com.jkm.hss.admin.service.DistributeQRCodeRecordService;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.dealer.dao.DealerDao;
import com.jkm.hss.dealer.entity.CompanyProfitDetail;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.entity.ShallProfitDetail;
import com.jkm.hss.dealer.enums.EnumDealerChannelRateStatus;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.enums.EnumProfitType;
import com.jkm.hss.dealer.enums.EnumDealerStatus;
import com.jkm.hss.dealer.helper.DealerSupport;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerAddRequest;
import com.jkm.hss.dealer.helper.requestparam.FirstLevelDealerUpdateRequest;
import com.jkm.hss.dealer.helper.requestparam.ListDealerRequest;
import com.jkm.hss.dealer.helper.requestparam.SecondLevelDealerAddRequest;
import com.jkm.hss.dealer.service.CompanyProfitDetailService;
import com.jkm.hss.dealer.service.DealerRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.merchant.entity.AccountInfo;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.OrderRecord;
import com.jkm.hss.merchant.service.AccountInfoService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private AccountInfoService accountInfoService;
    @Autowired
    private DistributeQRCodeRecordService distributeQRCodeRecordService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private CompanyProfitDetailService companyProfitDetailService;
    /**
     * {@inheritDoc}
     *
     * @param orderRecord
     * @return
     */
    @Override
    @Transactional
    public Map<String, Triple<Long, BigDecimal, String>> shallProfit(OrderRecord orderRecord) {
        final ShallProfitDetail detail = this.shallProfitDetailService.selectByOrderId(orderRecord.getOrderId());
        if (detail != null){
            log.error("此订单分润业务已经处理过[" + orderRecord.getOrderId() +"]");
            return null;
        }
        //根据代理商id查询代理商信息
        final Optional<MerchantInfo> optional = this.merchantInfoService.selectById(orderRecord.getMerchantId());
        Preconditions.checkArgument(optional.isPresent(), "商户信息不存在");
        final MerchantInfo merchantInfo = optional.get();
        //待分润金额
        final BigDecimal totalFee = orderRecord.getTotalFee();
        final Map<String, Triple<Long, BigDecimal, String>> map = new HashMap<>();
        //判断该经销商属于哪个代理, 若不属于代理, 则分润进入公司资金帐户
        if (merchantInfo.getDealerId() == 0){
            final List<ProductChannelDetail> list = this.productChannelDetailService.selectByChannelTypeSign(orderRecord.getPayChannel());
            final ProductChannelDetail productChannelDetail = list.get(0);
            final Optional<BasicChannel> channelOptional =  this.basicChannelService.selectByChannelTypeSign(orderRecord.getPayChannel());
            final BasicChannel basicChannel = channelOptional.get();
            final BigDecimal waitOriginMoney = totalFee.multiply(productChannelDetail.getProductMerchantPayRate());
            BigDecimal waitMoney;
            if (new BigDecimal("0.01").compareTo(waitOriginMoney) == 1){
                //手续费不足一分 , 按一分收
                if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                    //支付金额一分,不收手续费
                    waitMoney = new BigDecimal("0");
                }else{
                    waitMoney = new BigDecimal("0.01");
                }
            }else{
                //收手续费,进一位,保留两位有效数字
                waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
            }

            //获取产品的信息, 产品通道的费率
            final Optional<Product> productOptional = this.productService.selectById(productChannelDetail.getProductId());
            final  Product product = productOptional.get();

            //通道成本
            BigDecimal basicMoney;
            final BigDecimal basicTrade = totalFee.multiply(basicChannel.getBasicTradeRate());
            if (new BigDecimal("0.01").compareTo(basicTrade) == 1){
                //通道成本不足一分 , 按一分收
                basicMoney = new BigDecimal("0.01");

            }else{
                //超过一分,四舍五入,保留两位有效数字
                basicMoney = basicTrade.setScale(2, BigDecimal.ROUND_HALF_UP);
            }

            final BigDecimal merchantMoney = totalFee.subtract(waitMoney);
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
            companyProfitDetail.setMerchantId(orderRecord.getMerchantId());
            companyProfitDetail.setPaymentSn(orderRecord.getOrderId());
            companyProfitDetail.setChannelType(orderRecord.getPayChannel());
            companyProfitDetail.setTotalFee(orderRecord.getTotalFee());
            companyProfitDetail.setWaitShallAmount(waitMoney);
            companyProfitDetail.setWaitShallOriginAmount(waitOriginMoney);
            companyProfitDetail.setProfitType(EnumProfitType.BALANCE.getId());
            companyProfitDetail.setProductShallAmount(productMoney);
            companyProfitDetail.setChannelCost(basicMoney);
            companyProfitDetail.setChannelShallAmount(channelMoney);
            this.companyProfitDetailService.add(companyProfitDetail);
            map.put("merchant", Triple.of(merchantInfo.getAccountId(), merchantMoney, "D0"));
            map.put("merchantAndProfit", Triple.of(merchantInfo.getAccountId(), totalFee.subtract(basicMoney), "D0"));
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney,"M1"));
            map.put("productMoney",Triple.of(product.getAccountId(), productMoney,"M1"));
            return map;
        }

        final Dealer dealer = this.dealerDao.selectById(merchantInfo.getDealerId());
        //查询该代理商的收单利率
        final List<DealerChannelRate> dealerChannelRateList =  this.dealerRateService.selectByDealerIdAndChannelId(dealer.getId(), orderRecord.getPayChannel());
        final DealerChannelRate dealerChannelRate = dealerChannelRateList.get(0);
        //获取产品的信息, 产品通道的费率
        final Optional<Product> productOptional = this.productService.selectById(dealerChannelRate.getProductId());
        final  Product product = productOptional.get();
        final Optional<ProductChannelDetail> productChannelDetailOptional =
                this.productChannelDetailService.selectByProductIdAndChannelId(product.getId(), orderRecord.getPayChannel());
        final ProductChannelDetail productChannelDetail = productChannelDetailOptional.get();
        final Optional<BasicChannel> channelOptional =  this.basicChannelService.selectByChannelTypeSign(orderRecord.getPayChannel());
        final BasicChannel basicChannel = channelOptional.get();
        //待分润金额
        final BigDecimal waitOriginMoney = totalFee.multiply(dealerChannelRate.getDealerMerchantPayRate());
        BigDecimal waitMoney;
        if (new BigDecimal("0.01").compareTo(waitOriginMoney) == 1){
            //手续费不足一分 , 按一分收
            if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                //支付金额一分,不收手续费
                waitMoney = new BigDecimal("0");
            }else{
                waitMoney = new BigDecimal("0.01");
            }
        }else{
            //收手续费,进一位,保留两位有效数字
            waitMoney = waitOriginMoney.setScale(2,BigDecimal.ROUND_UP);
        }
        final BigDecimal merchantMoney = totalFee.subtract(waitMoney);
        //判断代理商等级
        if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
            //一级
            //一级分润
            final BigDecimal firestMoney = totalFee.multiply(dealerChannelRate.getDealerMerchantPayRate().
                    subtract(dealerChannelRate.getDealerTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //通道成本
            BigDecimal basicMoney;
            final BigDecimal basicTrade = totalFee.multiply(basicChannel.getBasicTradeRate());
            if (new BigDecimal("0.01").compareTo(basicTrade) == 1){
                //通道成本不足一分 , 按一分收
                basicMoney = new BigDecimal("0.01");

            }else{
                //超过一分,四舍五入,保留两位有效数字
                basicMoney = basicTrade.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            //通道分润
            final BigDecimal channelMoney = totalFee.multiply(productChannelDetail.getProductTradeRate().
                    subtract(basicChannel.getBasicTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //产品分润
            final BigDecimal productMoney;
            if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                //支付金额一分,不收手续费
                productMoney = new BigDecimal("0");
            }else{
                productMoney = waitMoney.subtract(firestMoney).subtract(channelMoney).subtract(basicMoney);
            }
            //记录分润明细
            final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
            shallProfitDetail.setMerchantId(merchantInfo.getId());
            shallProfitDetail.setTotalFee(totalFee);
            shallProfitDetail.setChannelType(orderRecord.getPayChannel());
            shallProfitDetail.setPaymentSn(orderRecord.getOrderId());
            shallProfitDetail.setWaitShallAmount(waitMoney);
            shallProfitDetail.setWaitShallOriginAmount(waitOriginMoney);
            shallProfitDetail.setIsDirect(1);
            shallProfitDetail.setProfitType(EnumProfitType.BALANCE.getId());
            shallProfitDetail.setChannelCost(basicMoney);
            shallProfitDetail.setChannelShallAmount(channelMoney);
            shallProfitDetail.setProductShallAmount(productMoney);
            shallProfitDetail.setFirstDealerId(dealer.getId());
            shallProfitDetail.setFirstShallAmount(firestMoney);
            shallProfitDetail.setSecondDealerId(0);
            shallProfitDetail.setSecondShallAmount(new BigDecimal(0));
            shallProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
            this.shallProfitDetailService.init(shallProfitDetail);
            map.put("merchant", Triple.of(merchantInfo.getAccountId(), merchantMoney,"D0"));
            map.put("merchantAndProfit", Triple.of(merchantInfo.getAccountId(), totalFee.subtract(basicMoney), "D0"));
            map.put("firstMoney", Triple.of(dealer.getAccountId(), firestMoney, "M1"));
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney, "M1"));
            map.put("productMoney",Triple.of(product.getAccountId(), productMoney, "M1"));
        }else{
            //二级
            //获取二级的一级dealer信息
            final Dealer firestDealer = this.dealerDao.selectById(dealer.getFirstLevelDealerId());
            //查询二级的一级dealer 收单费率
            final List<DealerChannelRate> firstDealerChannelRateList =  this.dealerRateService.selectByDealerIdAndChannelId(firestDealer.getId(), orderRecord.getPayChannel());
            final DealerChannelRate firstDealerChannelRate = firstDealerChannelRateList.get(0);
            //一级分润
            final BigDecimal firestMoney = totalFee.multiply(dealerChannelRate.getDealerTradeRate().
                    subtract(firstDealerChannelRate.getDealerTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //二级分润
            final BigDecimal secondMoney = totalFee.multiply(dealerChannelRate.getDealerMerchantPayRate().
                    subtract(dealerChannelRate.getDealerTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //通道分润
            final BigDecimal channelMoney = totalFee.multiply(productChannelDetail.getProductTradeRate().
                    subtract(basicChannel.getBasicTradeRate())).setScale(2,BigDecimal.ROUND_DOWN);
            //通道成本
            BigDecimal basicMoney;
            final BigDecimal basicTrade = totalFee.multiply(basicChannel.getBasicTradeRate());
            if (new BigDecimal("0.01").compareTo(basicTrade) == 1){
                //通道成本不足一分 , 按一分收
                basicMoney = new BigDecimal("0.01");

            }else{
                //超过一分,四舍五入,保留两位有效数字
                basicMoney = basicTrade.setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            //产品分润
            final BigDecimal productMoney;
            if (new BigDecimal("0.01").compareTo(totalFee) == 0){
                //支付金额一分,不收手续费
                productMoney = new BigDecimal("0");
            }else{
                productMoney = waitMoney.subtract(firestMoney).subtract(secondMoney).subtract(channelMoney).subtract(basicMoney);
            }

            //记录分润明细
            final ShallProfitDetail shallProfitDetail = new ShallProfitDetail();
            shallProfitDetail.setMerchantId(merchantInfo.getId());
            shallProfitDetail.setTotalFee(totalFee);
            shallProfitDetail.setChannelType(orderRecord.getPayChannel());
            shallProfitDetail.setPaymentSn(orderRecord.getOrderId());
            shallProfitDetail.setWaitShallAmount(waitMoney);
            shallProfitDetail.setWaitShallOriginAmount(waitOriginMoney);
            shallProfitDetail.setIsDirect(0);
            shallProfitDetail.setProfitType(EnumProfitType.BALANCE.getId());
            shallProfitDetail.setChannelCost(basicMoney);
            shallProfitDetail.setChannelShallAmount(channelMoney);
            shallProfitDetail.setProductShallAmount(productMoney);
            shallProfitDetail.setFirstDealerId(firestDealer.getId());
            shallProfitDetail.setFirstShallAmount(firestMoney);
            shallProfitDetail.setSecondDealerId(dealer.getId());
            shallProfitDetail.setSecondShallAmount(secondMoney);
            shallProfitDetail.setProfitDate(DateFormatUtil.format(new Date(), DateFormatUtil.yyyy_MM_dd));
            this.shallProfitDetailService.init(shallProfitDetail);
            map.put("merchant", Triple.of(merchantInfo.getAccountId(), merchantMoney, "D0"));
            map.put("merchantAndProfit", Triple.of(merchantInfo.getAccountId(), totalFee.subtract(basicMoney), "D0"));
            map.put("firstMoney", Triple.of(firestDealer.getAccountId(), firestMoney, "M1"));
            map.put("secondMoney", Triple.of(dealer.getAccountId(),secondMoney, "M1"));
            map.put("channelMoney",Triple.of(basicChannel.getAccountId(), channelMoney,"M1"));
            map.put("productMoney",Triple.of(product.getAccountId(), productMoney,"M1"));
        }
        log.info("订单" + orderRecord.getOrderId() + "分润处理成功,返回map成功");
        return map;
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
        final long accountId = this.accountInfoService.addNewAccount();
        final Dealer dealer = new Dealer();
        dealer.setAccountId(accountId);
        dealer.setMobile(request.getMobile());
        dealer.setProxyName(request.getName());
        dealer.setBankAccountName(request.getBankAccountName());
        dealer.setBelongArea(request.getBelongArea());
        dealer.setLevel(EnumDealerLevel.SECOND.getId());
        dealer.setFirstLevelDealerId(firstLevelDealerId);
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(request.getBankCard()));
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(request.getBankReserveMobile()));
        dealer.setStatus(EnumDealerStatus.NORMAL.getId());
        this.add(dealer);

        final List<DealerChannelRate> channelRates = this.dealerRateService.getByDealerId(firstLevelDealerId);
        for (DealerChannelRate channelRate : channelRates) {
            final DealerChannelRate dealerChannelRate = new DealerChannelRate();
            dealerChannelRate.setDealerId(dealer.getId());
            dealerChannelRate.setProductId(channelRate.getProductId());
            dealerChannelRate.setChannelTypeSign(channelRate.getChannelTypeSign());
            if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_WEIXIN.getId()) {
                dealerChannelRate.setDealerTradeRate(new BigDecimal(request.getWeixinSettleRate()).divide(new BigDecimal("100")));
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_ZHIFUBAO.getId()) {
                dealerChannelRate.setDealerTradeRate(new BigDecimal(request.getAlipaySettleRate()).divide(new BigDecimal("100")));
            } else if (channelRate.getChannelTypeSign() == EnumPayChannelSign.YG_YINLIAN.getId()) {
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
     * @param count  个数
     * @return
     */
    @Override
    @Transactional
    public DistributeQRCodeRecord distributeQRCode(final long dealerId, final long toDealerId, final int count) {
        final int unDistributeCount = this.qrCodeService.getUnDistributeCodeCountByFirstLevelDealerId(dealerId);
        Preconditions.checkState(unDistributeCount >= count, "未分配的码段个数小于要分配的个数");
        final List<QRCode> qrCodeList = this.qrCodeService.getUnDistributeCodeByFirstLevelDealerId(dealerId, count);
        final DistributeQRCodeRecord distributeQRCodeRecord = new DistributeQRCodeRecord();
        distributeQRCodeRecord.setFirstLevelDealerId(dealerId);
        distributeQRCodeRecord.setCount(count);
        distributeQRCodeRecord.setStartCode(qrCodeList.get(0).getCode());
        distributeQRCodeRecord.setEndCode(qrCodeList.get(qrCodeList.size() - 1).getCode());
        if (dealerId == toDealerId) {
            final int markCount = this.qrCodeService.markAsDistribute(dealerId, count);
            Preconditions.checkState(markCount == count, "一级代理商[{}]分配给自己码段出现数量异常", dealerId);
        } else {
            distributeQRCodeRecord.setSecondLevelDealerId(toDealerId);
            final int markCount = this.qrCodeService.markAsDistribute2(dealerId, toDealerId, count);
            Preconditions.checkState(markCount == count, "一级代理商[{}]分配给二级代理商[{}]码段出现数量异常", dealerId, toDealerId);
        }
        this.distributeQRCodeRecordService.add(distributeQRCodeRecord);
        final Optional<DistributeQRCodeRecord> recordOptional = this.distributeQRCodeRecordService.getById(distributeQRCodeRecord.getId());
        return recordOptional.get();
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
        final long accountId = this.accountInfoService.addNewAccount();
        final Dealer dealer = new Dealer();
        dealer.setAccountId(accountId);
        dealer.setMobile(firstLevelDealerAddRequest.getMobile());
        dealer.setProxyName(firstLevelDealerAddRequest.getName());
        dealer.setBankAccountName(firstLevelDealerAddRequest.getBankAccountName());
        dealer.setBelongArea(firstLevelDealerAddRequest.getBelongArea());
        dealer.setLevel(EnumDealerLevel.FIRST.getId());
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(firstLevelDealerAddRequest.getBankCard()));
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(firstLevelDealerAddRequest.getBankReserveMobile()));
        dealer.setStatus(EnumDealerStatus.NORMAL.getId());
        this.add(dealer);
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
        dealer.setSettleBankCard(DealerSupport.encryptBankCard(request.getBankCard()));
        dealer.setBankReserveMobile(DealerSupport.encryptMobile(request.getBankReserveMobile()));
        dealer.setStatus(EnumDealerStatus.NORMAL.getId());
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
    }
}
