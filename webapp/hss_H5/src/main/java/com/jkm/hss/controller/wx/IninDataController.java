package com.jkm.hss.controller.wx;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.dao.DealerChannelRateDao;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumCommonStatus;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.dao.BasicChannelDao;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by xingliujie on 2017/3/2.
 */
@Slf4j
@Controller
@RequestMapping(value = "/test")
public class IninDataController extends BaseController{
    @Autowired
    private DealerChannelRateDao dealerChannelRateDao;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private DealerChannelRateService dealerChannelRateService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private BasicChannelDao basicChannelDao;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private AccountBankService accountBankService;

    /**
     * 初始化代理商数据
     */
    @ResponseBody
    @RequestMapping(value = "initDealer", method = RequestMethod.GET)
    public CommonResponse init(final HttpServletRequest request, final HttpServletResponse response) {
        List<Long> dealerIds = dealerChannelRateDao.initDealerData();
        for(int i=0;i<dealerIds.size();i++){
            //微信费率
            Optional<DealerChannelRate> weixinRate =  dealerChannelRateService.selectByDealerIdAndProductIdAndChannelType(dealerIds.get(i),6,101);
            //支付宝费率
            Optional<DealerChannelRate> zhifubaoRate =  dealerChannelRateService.selectByDealerIdAndProductIdAndChannelType(dealerIds.get(i),6,102);
            //银联费率
            Optional<DealerChannelRate> fastPayRate =  dealerChannelRateService.selectByDealerIdAndProductIdAndChannelType(dealerIds.get(i),6,103);
            List<BasicChannel> basicChannels = basicChannelDao.selectNewAll();
            for(int j=0;j<basicChannels.size();j++){
                if("微信".equals(basicChannels.get(j).getThirdCompany())){
                    DealerChannelRate dealerChannelRate = new DealerChannelRate();
                    dealerChannelRate.setDealerId(weixinRate.get().getDealerId());
                    dealerChannelRate.setProductId(weixinRate.get().getProductId());
                    dealerChannelRate.setChannelTypeSign(basicChannels.get(j).getChannelTypeSign());
                    dealerChannelRate.setDealerTradeRate(weixinRate.get().getDealerTradeRate());
                    dealerChannelRate.setDealerBalanceType(basicChannels.get(j).getBasicBalanceType());
                    dealerChannelRate.setDealerWithdrawFee(weixinRate.get().getDealerWithdrawFee());
                    dealerChannelRate.setDealerMerchantPayRate(weixinRate.get().getDealerMerchantPayRate());
                    dealerChannelRate.setDealerMerchantWithdrawFee(weixinRate.get().getDealerMerchantWithdrawFee());
                    dealerChannelRate.setStatus(2);
                    dealerChannelRateDao.init(dealerChannelRate);
                }
                if("支付宝".equals(basicChannels.get(j).getThirdCompany())){
                    DealerChannelRate dealerChannelRate = new DealerChannelRate();
                    dealerChannelRate.setDealerId(zhifubaoRate.get().getDealerId());
                    dealerChannelRate.setProductId(zhifubaoRate.get().getProductId());
                    dealerChannelRate.setChannelTypeSign(basicChannels.get(j).getChannelTypeSign());
                    dealerChannelRate.setDealerTradeRate(zhifubaoRate.get().getDealerTradeRate());
                    dealerChannelRate.setDealerBalanceType(basicChannels.get(j).getBasicBalanceType());
                    dealerChannelRate.setDealerWithdrawFee(zhifubaoRate.get().getDealerWithdrawFee());
                    dealerChannelRate.setDealerMerchantPayRate(zhifubaoRate.get().getDealerMerchantPayRate());
                    dealerChannelRate.setDealerMerchantWithdrawFee(zhifubaoRate.get().getDealerMerchantWithdrawFee());
                    dealerChannelRate.setStatus(2);
                    dealerChannelRateDao.init(dealerChannelRate);
                }
                if("银行".equals(basicChannels.get(j).getThirdCompany())){
                    DealerChannelRate dealerChannelRate = new DealerChannelRate();
                    dealerChannelRate.setDealerId(fastPayRate.get().getDealerId());
                    dealerChannelRate.setProductId(fastPayRate.get().getProductId());
                    dealerChannelRate.setChannelTypeSign(basicChannels.get(j).getChannelTypeSign());
                    dealerChannelRate.setDealerTradeRate(fastPayRate.get().getDealerTradeRate());
                    dealerChannelRate.setDealerBalanceType(basicChannels.get(j).getBasicBalanceType());
                    dealerChannelRate.setDealerWithdrawFee(fastPayRate.get().getDealerWithdrawFee());
                    dealerChannelRate.setDealerMerchantPayRate(fastPayRate.get().getDealerMerchantPayRate());
                    dealerChannelRate.setDealerMerchantWithdrawFee(fastPayRate.get().getDealerMerchantWithdrawFee());
                    dealerChannelRate.setStatus(2);
                    dealerChannelRateDao.init(dealerChannelRate);
                }

            }
        }
        return CommonResponse.objectResponse(1,"",dealerIds);
    }

    /**
     * 初始化商户数据
     */
    @ResponseBody
    @RequestMapping(value = "initMerchant", method = RequestMethod.GET)
    public void initMerchant(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("开始初始化商户数据");
        List<MerchantInfo> merchantInfoList = merchantInfoService.getAll();
        for(int i=0;i<merchantInfoList.size();i++){
            log.info("第"+i+"数据初始化start，商户编号"+merchantInfoList.get(i).getId());
            //微信费率
            BigDecimal weixinRate =  merchantInfoList.get(i).getWeixinRate();
            //支付宝费率
            BigDecimal zhifubaoRate =  merchantInfoList.get(i).getAlipayRate();
            //银联费率
            BigDecimal fastPayRate =  merchantInfoList.get(i).getFastRate();
            if(merchantInfoList.get(i).getFirstDealerId()>0){//有一级代理
                Optional<Dealer> dealerOptional = dealerService.getById(merchantInfoList.get(i).getFirstDealerId());
                if(dealerOptional.isPresent()){//存在
                    List<DealerChannelRate> dealerChannelRateList = dealerChannelRateService.selectByDealerIdAndProductId(merchantInfoList.get(i).getFirstDealerId(),merchantInfoList.get(i).getProductId());
                    if(dealerChannelRateList.size()>0){
                        for(int j=0;j<dealerChannelRateList.size();j++){
                            MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
                            merchantChannelRate.setMerchantId(merchantInfoList.get(i).getId());
                            merchantChannelRate.setProductId(merchantInfoList.get(i).getProductId());
                            merchantChannelRate.setMarkCode(merchantInfoList.get(i).getMarkCode());
                            merchantChannelRate.setSysType(EnumProductType.HSS.getId());
                            merchantChannelRate.setChannelTypeSign(dealerChannelRateList.get(j).getChannelTypeSign());
                            merchantChannelRate.setMerchantBalanceType(dealerChannelRateList.get(j).getDealerBalanceType());
                            merchantChannelRate.setMerchantWithdrawFee(dealerChannelRateList.get(j).getDealerMerchantWithdrawFee());
                            Optional<BasicChannel> basicChannelOptionalTemp = basicChannelService.selectByChannelTypeSign(dealerChannelRateList.get(j).getChannelTypeSign());
                            if(!basicChannelOptionalTemp.isPresent()){
                                log.info("基本通道配置{}有误",dealerChannelRateList.get(j).getChannelTypeSign());
                            }
                            if("微信".equals(basicChannelOptionalTemp.get().getThirdCompany())){
                                merchantChannelRate.setMerchantPayRate(weixinRate);
                            }
                            if("支付宝".equals(basicChannelOptionalTemp.get().getThirdCompany())){
                                merchantChannelRate.setMerchantPayRate(zhifubaoRate);
                            }
                            if("银行".equals(basicChannelOptionalTemp.get().getThirdCompany())){
                                merchantChannelRate.setMerchantPayRate(fastPayRate);
                            }
                            if(basicChannelOptionalTemp.get().getIsNeed()==1){//需要入网
                                merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
                            }else{
                                merchantChannelRate.setEnterNet(EnumEnterNet.UNSUPPORT.getId());
                            }
                            merchantChannelRate.setChannelCompany(basicChannelOptionalTemp.get().getChannelCompany());
                            merchantChannelRate.setThirdCompany(basicChannelOptionalTemp.get().getThirdCompany());
                            merchantChannelRate.setStatus(EnumCommonStatus.NORMAL.getId());
                            merchantChannelRateService.initMerchantChannelRate(merchantChannelRate);
                        }
                    }else{
                        log.info("代理商产品费率配置有误");
                    }
                }else{
                   log.info("代理商{}不存在",merchantInfoList.get(i).getFirstDealerId());
                }
            }else{
                List<ProductChannelDetail> productChannelDetailList = productChannelDetailService.selectByProductId(merchantInfoList.get(i).getProductId());
                if(productChannelDetailList.size()>0){
                    for(int j=0;j<productChannelDetailList.size();j++){
                        MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
                        merchantChannelRate.setMerchantId(merchantInfoList.get(i).getId());
                        merchantChannelRate.setProductId(merchantInfoList.get(i).getProductId());
                        merchantChannelRate.setMarkCode(merchantInfoList.get(i).getMarkCode());
                        merchantChannelRate.setSysType(EnumProductType.HSS.getId());
                        merchantChannelRate.setChannelTypeSign(productChannelDetailList.get(j).getChannelTypeSign());
                        merchantChannelRate.setMerchantBalanceType(productChannelDetailList.get(j).getProductBalanceType());
                        merchantChannelRate.setMerchantWithdrawFee(productChannelDetailList.get(j).getProductMerchantWithdrawFee());
                        Optional<BasicChannel> basicChannelOptionalTemp = basicChannelService.selectByChannelTypeSign(productChannelDetailList.get(j).getChannelTypeSign());
                        if(!basicChannelOptionalTemp.isPresent()){
                            log.info("基本通道配置{}有误",productChannelDetailList.get(j).getChannelTypeSign());
                        }
                        if("微信".equals(basicChannelOptionalTemp.get().getThirdCompany())){
                            merchantChannelRate.setMerchantPayRate(weixinRate);
                        }
                        if("支付宝".equals(basicChannelOptionalTemp.get().getThirdCompany())){
                            merchantChannelRate.setMerchantPayRate(zhifubaoRate);
                        }
                        if("银行".equals(basicChannelOptionalTemp.get().getThirdCompany())){
                            merchantChannelRate.setMerchantPayRate(fastPayRate);
                        }
                        if(basicChannelOptionalTemp.get().getIsNeed()==1){//需要入网
                            merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
                        }else{
                            merchantChannelRate.setEnterNet(EnumEnterNet.UNSUPPORT.getId());
                        }
                        merchantChannelRate.setChannelCompany(basicChannelOptionalTemp.get().getChannelCompany());
                        merchantChannelRate.setThirdCompany(basicChannelOptionalTemp.get().getThirdCompany());
                        merchantChannelRate.setStatus(EnumCommonStatus.NORMAL.getId());
                        merchantChannelRateService.initMerchantChannelRate(merchantChannelRate);
                    }
                }else{
                    log.info("基础产品费率配置有误");
                }
            }
            log.info("第"+i+"数据初始化end，商户编号"+merchantInfoList.get(i).getId());
        }
    }

    /**
     * 初始化默认银行卡
     */
    @ResponseBody
    @RequestMapping(value = "initBank", method = RequestMethod.GET)
    public void initBank(final HttpServletRequest request, final HttpServletResponse response) {
        log.info("开始初始化默认银行卡数据");
        List<MerchantInfo> merchantInfoList = merchantInfoService.getAll();
        for(int i=0;i<merchantInfoList.size();i++){
            if(merchantInfoList.get(i).getAccountId()>0){
                log.info("第"+i+"银行卡数据初始化start，商户编号"+merchantInfoList.get(i).getId());
                accountBankService.initAccountBank(merchantInfoList.get(i).getId(),merchantInfoList.get(i).getAccountId());
            }
        }
    }
}
