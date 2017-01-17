package com.jkm.hss.dealer.service.impl;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.dealer.dao.DailyProfitDetailDao;
import com.jkm.hss.dealer.entity.DailyProfitDetail;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.enums.EnumShallMoneyType;
import com.jkm.hss.dealer.service.CompanyProfitDetailService;
import com.jkm.hss.dealer.service.DailyProfitDetailService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.dealer.service.ShallProfitDetailService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-25.
 */
@Service
@Slf4j
public class DailyProfitDetailServiceImpl implements DailyProfitDetailService {

    @Autowired
    private ShallProfitDetailService shallProfitDetailService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private DailyProfitDetailDao dailyProfitDetailDao;
    @Autowired
    private CompanyProfitDetailService companyProfitDetailService;
    /**
     *{@inheritDoc}
     */
    @Override
    @Transactional
    public void dailyProfitCount() {
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>大码每日分润定时任务开始执行.....<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        final String profitDate = this.getProfitDate(new Date());
        //公司分润
        final DailyProfitDetail companyDaily = new DailyProfitDetail();
        companyDaily.setShallMoneyType(EnumShallMoneyType.TOCOMPANY.getId());
        companyDaily.setMerchantId(0);
        companyDaily.setMerchantName("");
        companyDaily.setSecondDealerId(0);
        companyDaily.setDealerName("");
        companyDaily.setFirstDealerId(0);
        companyDaily.setFirstDealerName("");
        companyDaily.setStatisticsDate(profitDate);
        //公司收单分润
        BigDecimal companyCollectMoney = this.companyProfitDetailService.selectCollectProfitByProfitDate(profitDate);
        if (companyCollectMoney == null){
            companyCollectMoney = new BigDecimal("0");
        }
        BigDecimal companyDealerCollectMoney = this.shallProfitDetailService.selectCompanyCollectProfitByProfitDate(profitDate);
        if (companyDealerCollectMoney == null){
            companyDealerCollectMoney = new BigDecimal("0");
        }
        //公司提现分润
        BigDecimal companyWithdrawMoney = this.companyProfitDetailService.selectWithdrawProfitByProfitDate(profitDate);
        if (companyWithdrawMoney == null){
            companyWithdrawMoney = new BigDecimal("0");
        }
        BigDecimal companyDealerWithdrawMoney = this.shallProfitDetailService.selectCompanyWithdrawProfitByProfitDate(profitDate);
        if (companyDealerWithdrawMoney == null){
            companyDealerWithdrawMoney = new BigDecimal("0");
        }
        companyDaily.setCollectMoney(companyCollectMoney.add(companyDealerCollectMoney));
        companyDaily.setWithdrawMoney(companyWithdrawMoney.add(companyDealerWithdrawMoney));
        companyDaily.setTotalMoney(companyDaily.getCollectMoney().add(companyDaily.getWithdrawMoney()));
        this.dailyProfitDetailDao.init(companyDaily);

        //查昨日有分润记录的商户
        final List<Long> merchantIdList = this.shallProfitDetailService.selectMerchantIdByProfitDate(profitDate);
        //遍历,计算每个商户每天的收单分润,体现分润
        for (Long merchantId : merchantIdList){
            final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(merchantId);
            Preconditions.checkNotNull(merchantInfoOptional.isPresent(), "商户信息不存在");
            final MerchantInfo merchantInfo = merchantInfoOptional.get();
            // 判断该商户的代理是几级代理
            final Optional<Dealer> dealerOptional = this.dealerService.getById(merchantInfo.getDealerId());
            Preconditions.checkNotNull(dealerOptional.isPresent(), "代理商信息不存在");
            final Dealer dealer = dealerOptional.get();
            if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
                //收单分润
                final BigDecimal collectMoney =
                        this.shallProfitDetailService.selectFirstCollectMoneyByMerchantIdAndProfitDate(merchantId, profitDate);
                //提现分润
                BigDecimal withdrawMoney =
                        this.shallProfitDetailService.selectFirstWithdrawMoneyByMerchantIdAndProfitDate(merchantId, profitDate);
                if (withdrawMoney == null){
                    withdrawMoney = new BigDecimal(0);
                }
                final DailyProfitDetail dailyProfitDetail = new DailyProfitDetail();
                dailyProfitDetail.setMerchantName(merchantInfo.getMerchantName());
                dailyProfitDetail.setShallMoneyType(EnumShallMoneyType.TOMERCHANT.getId());
                dailyProfitDetail.setMerchantId(merchantId);
                dailyProfitDetail.setFirstDealerId(dealer.getId());
                dailyProfitDetail.setFirstDealerName(dealer.getProxyName());
                dailyProfitDetail.setSecondDealerId(0);
                dailyProfitDetail.setDealerName("");
                dailyProfitDetail.setStatisticsDate(profitDate);
                dailyProfitDetail.setCollectMoney(collectMoney);
                dailyProfitDetail.setWithdrawMoney(withdrawMoney);
                dailyProfitDetail.setTotalMoney(collectMoney.add(withdrawMoney));
                this.dailyProfitDetailDao.init(dailyProfitDetail);
                //创建该一级代理的每日收益记录
                final DailyProfitDetail firstRecord = this.dailyProfitDetailDao.selectByFirstDealerIdAndTypeAndProfitDate(dealer.getId(),EnumShallMoneyType.TOFIRST.getId(),profitDate);
                if (firstRecord == null){
                    //不存在.新建
                    final DailyProfitDetail firstDailyProfitDetail = new DailyProfitDetail();
                    firstDailyProfitDetail.setShallMoneyType(EnumShallMoneyType.TOFIRST.getId());
                    firstDailyProfitDetail.setMerchantId(0);
                    firstDailyProfitDetail.setMerchantName("");
                    firstDailyProfitDetail.setSecondDealerId(0);
                    firstDailyProfitDetail.setDealerName("");
                    firstDailyProfitDetail.setFirstDealerId(dealer.getId());
                    firstDailyProfitDetail.setFirstDealerName(dealer.getProxyName());
                    firstDailyProfitDetail.setStatisticsDate(profitDate);
                    firstDailyProfitDetail.setCollectMoney(collectMoney);
                    firstDailyProfitDetail.setWithdrawMoney(withdrawMoney);
                    firstDailyProfitDetail.setTotalMoney(collectMoney.add(withdrawMoney));
                    this.dailyProfitDetailDao.init(firstDailyProfitDetail);
                }else{
                    //存在, 累加
                    firstRecord.setCollectMoney(collectMoney.add(firstRecord.getCollectMoney()));
                    firstRecord.setWithdrawMoney(withdrawMoney.add(firstRecord.getWithdrawMoney()));
                    firstRecord.setTotalMoney(collectMoney.add(withdrawMoney).add(firstRecord.getTotalMoney()));
                    this.dailyProfitDetailDao.update(firstRecord);
                }
            }else if (dealer.getLevel() == EnumDealerLevel.SECOND.getId()){
                //一级代理信息
                final Optional<Dealer> firstDealerOptional = this.dealerService.getById(dealer.getFirstLevelDealerId());
                Preconditions.checkNotNull(dealerOptional.isPresent(), "代理商信息不存在");
                final Dealer firstDealer = firstDealerOptional.get();
                //收单分润
                final BigDecimal collectMoney =
                        this.shallProfitDetailService.selectSecondCollectMoneyByMerchantIdAndProfitDate(merchantId, profitDate);
                //提现分润
                BigDecimal withdrawMoney =
                        this.shallProfitDetailService.selectSecondWithdrawMoneyByMerchantIdAndProfitDate(merchantId, profitDate);
                if (withdrawMoney == null){
                    withdrawMoney = new BigDecimal(0);
                }

                final DailyProfitDetail dailyProfitDetail = new DailyProfitDetail();
                dailyProfitDetail.setMerchantName(merchantInfo.getMerchantName());
                dailyProfitDetail.setShallMoneyType(EnumShallMoneyType.TOMERCHANT.getId());
                dailyProfitDetail.setMerchantId(merchantId);
                dailyProfitDetail.setFirstDealerId(firstDealer.getId());
                dailyProfitDetail.setFirstDealerName(firstDealer.getProxyName());
                dailyProfitDetail.setSecondDealerId(dealer.getId());
                dailyProfitDetail.setDealerName(dealer.getProxyName());
                dailyProfitDetail.setStatisticsDate(profitDate);
                dailyProfitDetail.setCollectMoney(collectMoney);
                dailyProfitDetail.setWithdrawMoney(withdrawMoney);
                dailyProfitDetail.setTotalMoney(collectMoney.add(withdrawMoney));
                this.dailyProfitDetailDao.init(dailyProfitDetail);
                //创建该二级代理的每日收益记录
                final DailyProfitDetail secondRecord = this.dailyProfitDetailDao.selectBySecondDealerIdAndTypeAndProfitDate(dealer.getId(),EnumShallMoneyType.TOSECONDSELF.getId(),profitDate);
                if (secondRecord == null){
                    //不存在.新建
                    final DailyProfitDetail secondDailyProfitDetail = new DailyProfitDetail();
                    secondDailyProfitDetail.setShallMoneyType(EnumShallMoneyType.TOSECONDSELF.getId());
                    secondDailyProfitDetail.setMerchantId(0);
                    secondDailyProfitDetail.setMerchantName("");
                    secondDailyProfitDetail.setSecondDealerId(dealer.getId());
                    secondDailyProfitDetail.setDealerName(dealer.getProxyName());
                    secondDailyProfitDetail.setFirstDealerId(firstDealer.getId());
                    secondDailyProfitDetail.setFirstDealerName(firstDealer.getProxyName());
                    secondDailyProfitDetail.setStatisticsDate(profitDate);
                    secondDailyProfitDetail.setCollectMoney(collectMoney);
                    secondDailyProfitDetail.setWithdrawMoney(withdrawMoney);
                    secondDailyProfitDetail.setTotalMoney(collectMoney.add(withdrawMoney));
                    this.dailyProfitDetailDao.init(secondDailyProfitDetail);
                }else{
                    //存在, 累加
                    secondRecord.setCollectMoney(collectMoney.add(secondRecord.getCollectMoney()));
                    secondRecord.setWithdrawMoney(withdrawMoney.add(secondRecord.getWithdrawMoney()));
                    secondRecord.setTotalMoney(collectMoney.add(withdrawMoney).add(secondRecord.getTotalMoney()));
                    this.dailyProfitDetailDao.update(secondRecord);
                }
            }
        }
        //每个二级代理每天的分润
        //查询昨日二级代理有分润的帐号
        final List<Long> dealerIdList = this.shallProfitDetailService.selectDealerIdByProfitDate(profitDate);
        for (Long dealerId : dealerIdList){
            final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
            Preconditions.checkNotNull(dealerOptional.isPresent(), "代理商信息不存在");
            final Dealer secondDealer = dealerOptional.get();
            //查询该二级代理的一级代理
            final Optional<Dealer> firstDealerOptional = this.dealerService.getById(secondDealer.getFirstLevelDealerId());
            Preconditions.checkNotNull(firstDealerOptional.isPresent(), "代理商信息不存在");
            final Dealer firstDealer = firstDealerOptional.get();
            //收单分润
            final BigDecimal collectMoney =
                    this.shallProfitDetailService.selectFirstCollectMoneyByDealerIdAndProfitDate(secondDealer.getId(), profitDate);
            //提现分润
            final BigDecimal withdrawMoney =
                    this.shallProfitDetailService.selectFirstWithdrawMoneyByDealerIdAndProfitDate(secondDealer.getId(), profitDate);
            //创建该一级代理的每日收益记录
            final DailyProfitDetail firstRecord = this.dailyProfitDetailDao.selectByFirstDealerIdAndTypeAndProfitDate(firstDealer.getId(),EnumShallMoneyType.TOFIRST.getId(),profitDate);
            if (firstRecord == null){
                //不存在.新建
                final DailyProfitDetail firstDailyProfitDetail = new DailyProfitDetail();
                firstDailyProfitDetail.setShallMoneyType(EnumShallMoneyType.TOFIRST.getId());
                firstDailyProfitDetail.setMerchantId(0);
                firstDailyProfitDetail.setMerchantName("");
                firstDailyProfitDetail.setSecondDealerId(0);
                firstDailyProfitDetail.setDealerName("");
                firstDailyProfitDetail.setFirstDealerId(firstDealer.getId());
                firstDailyProfitDetail.setFirstDealerName(firstDealer.getProxyName());
                firstDailyProfitDetail.setStatisticsDate(profitDate);
                firstDailyProfitDetail.setCollectMoney(collectMoney);
                firstDailyProfitDetail.setWithdrawMoney(withdrawMoney);
                firstDailyProfitDetail.setTotalMoney(collectMoney.add(withdrawMoney));
                this.dailyProfitDetailDao.init(firstDailyProfitDetail);
            }else{
                //存在, 累加
                firstRecord.setCollectMoney(collectMoney.add(firstRecord.getCollectMoney()));
                firstRecord.setWithdrawMoney(withdrawMoney.add(firstRecord.getWithdrawMoney()));
                firstRecord.setTotalMoney(collectMoney.add(withdrawMoney).add(firstRecord.getTotalMoney()));
                this.dailyProfitDetailDao.update(firstRecord);
            }
            final DailyProfitDetail dailyProfitDetail = new DailyProfitDetail();
            dailyProfitDetail.setMerchantName("");
            dailyProfitDetail.setShallMoneyType(EnumShallMoneyType.TOSECOND.getId());
            dailyProfitDetail.setMerchantId(0);
            dailyProfitDetail.setFirstDealerId(firstDealer.getId());
            dailyProfitDetail.setFirstDealerName(firstDealer.getProxyName());
            dailyProfitDetail.setSecondDealerId(secondDealer.getId());
            dailyProfitDetail.setDealerName(secondDealer.getProxyName());
            dailyProfitDetail.setStatisticsDate(profitDate);
            dailyProfitDetail.setCollectMoney(collectMoney);
            dailyProfitDetail.setWithdrawMoney(withdrawMoney);
            dailyProfitDetail.setTotalMoney(collectMoney.add(withdrawMoney));
            this.dailyProfitDetailDao.init(dailyProfitDetail);
        }
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>大码每日分润定时任务执行完成,success...<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public List<DailyProfitDetail> toMerchant(long dealerId) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        Preconditions.checkNotNull(dealerOptional.isPresent(), "代理商不存在");
        final Dealer dealer = dealerOptional.get();
        final List<DailyProfitDetail> list;
        //判断是一级代理还是二级代理
        if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
            list = this.dailyProfitDetailDao.selectByFirstDealerId(dealerId);
        }else{
            list = this.dailyProfitDetailDao.selectBySecondDealerId(dealerId);
        }
        return list;
    }

    /**
     * {@inheritDoc}
     *
     * @param dealerId
     * @return
     */
    @Override
    public List<DailyProfitDetail> toDealer(long dealerId) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(dealerId);
        Preconditions.checkNotNull(dealerOptional.isPresent(), "代理商不存在");
        final Dealer dealer = dealerOptional.get();
        if (dealer.getLevel() == EnumDealerLevel.FIRST.getId()){
            final List<DailyProfitDetail> list =  this.dailyProfitDetailDao.selectToSecondDealer(dealerId);
            return list;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     * @param beginProfitDate
     * @param dealerName
     * @param endProfitDate
     * @param pageNO
     * @param pageSize
     * @return
     */
    @Override
    public PageModel<DailyProfitDetail> selectFirstByParam(String beginProfitDate, String firstDealerName, String endProfitDate, int pageNO, int pageSize) {
        PageModel<DailyProfitDetail> pageModel = new PageModel<>(pageNO, pageSize);
        Date beginDate =null;
        Date endDate =null;
        if (beginProfitDate !=null && endProfitDate!=null && beginProfitDate!="" && endProfitDate!=""){
            beginDate = DateFormatUtil.parse(beginProfitDate+ " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            endDate  = DateFormatUtil.parse(endProfitDate + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        List<DailyProfitDetail> list = this.dailyProfitDetailDao.selectFirstByParam(beginDate, firstDealerName, endDate, pageModel.getFirstIndex(), pageSize);
        if (list == null){
            list = Collections.emptyList();
        }
        pageModel.setRecords(list);
        return pageModel;
    }

    /**
     * {@inheritDoc}
     *
     * @param beginProfitDate
     * @param dealerName
     * @param endProfitDate
     * @param pageNO
     * @param pageSize
     * @return
     */
    @Override
    public PageModel<DailyProfitDetail> selectSecondByParam(String beginProfitDate, String dealerName, String endProfitDate, int pageNO, int pageSize) {
        PageModel<DailyProfitDetail> pageModel = new PageModel<>(pageNO, pageSize);
        Date beginDate =null;
        Date endDate =null;
        if (beginProfitDate !=null && endProfitDate!=null && beginProfitDate!="" && endProfitDate!=""){
            beginDate = DateFormatUtil.parse(beginProfitDate+ " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            endDate  = DateFormatUtil.parse(endProfitDate + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        List<DailyProfitDetail> list = this.dailyProfitDetailDao.selectSecondByParam(beginDate, dealerName, endDate, pageModel.getFirstIndex(), pageSize);
        if (list == null){
            list = Collections.emptyList();
        }
        pageModel.setRecords(list);
        return pageModel;
    }

    /**
     * {@inheritDoc}
     * @param beginProfitDate
     * @param dealerName
     * @param endProfitDate
     * @param pageNO
     * @param pageSize
     * @return
     */
    @Override
    public PageModel<DailyProfitDetail> selectCompanyByParam(String beginProfitDate, String dealerName, String endProfitDate, int pageNO, int pageSize) {
        PageModel<DailyProfitDetail> pageModel = new PageModel<>(pageNO, pageSize);
        Date beginDate =null;
        Date endDate =null;
        if (beginProfitDate !=null && endProfitDate!=null && beginProfitDate!="" && endProfitDate!=""){
            beginDate = DateFormatUtil.parse(beginProfitDate+ " 00:00:00", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
            endDate  = DateFormatUtil.parse(endProfitDate + " 23:59:59", DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        List<DailyProfitDetail> list = this.dailyProfitDetailDao.selectCompanyByParam(beginDate, dealerName, endDate, pageModel.getFirstIndex(), pageSize);
        if (list == null){
            list = Collections.emptyList();
        }
        pageModel.setRecords(list);
        return pageModel;
    }

    /**
     * {@inheritDoc}
     * @param id
     * @return
     */
    @Override
    public DailyProfitDetail selectById(long id) {
        return this.dailyProfitDetailDao.selectById(id);
    }

    private String getProfitDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        date = calendar.getTime();
        final String format = DateFormatUtil.format(date, DateFormatUtil.yyyy_MM_dd);
        return format;
    }
}
