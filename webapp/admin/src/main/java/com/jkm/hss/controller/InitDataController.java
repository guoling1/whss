package com.jkm.hss.controller;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.dealer.dao.DealerChannelRateDao;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumCommonStatus;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
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
 * Created by xingliujie on 2017/3/20.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/initData")
public class InitDataController extends BaseController{
    @Autowired
    private DealerChannelRateDao dealerChannelRateDao;
    @Autowired
    private DealerChannelRateService dealerChannelRateService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    /**
     * 初始化代理商数据
     */
    @ResponseBody
    @RequestMapping(value = "initDealer", method = RequestMethod.GET)
    public CommonResponse init(final HttpServletRequest request, final HttpServletResponse response) {
        //初始化数据start
        long productId = 6;
        int channelTypeSign = 301;
        BigDecimal dealerTradeRate = new BigDecimal("0.0046");
        String dealerBalanceType = "T1";
        BigDecimal dealerWithdrawFee = new BigDecimal("1.50");
        BigDecimal dealerMerchantPayRate = new BigDecimal("0.0060");
        BigDecimal dealerMerchantWithdrawFee = new BigDecimal("2.00");
        //初始化数据end
        List<Long> dealerIds = dealerChannelRateDao.initDealerData();
        for(int i=0;i<dealerIds.size();i++){
            log.info("第"+i+"数据初始化start，代理商编号"+dealerIds.get(i));
            DealerChannelRate dealerChannelRate = new DealerChannelRate();
            dealerChannelRate.setDealerId(dealerIds.get(i));
            dealerChannelRate.setProductId(productId);
            dealerChannelRate.setChannelTypeSign(channelTypeSign);
            dealerChannelRate.setDealerTradeRate(dealerTradeRate);
            dealerChannelRate.setDealerBalanceType(dealerBalanceType);
            dealerChannelRate.setDealerWithdrawFee(dealerWithdrawFee);
            dealerChannelRate.setDealerMerchantPayRate(dealerMerchantPayRate);
            dealerChannelRate.setDealerMerchantWithdrawFee(dealerMerchantWithdrawFee);
            dealerChannelRate.setStatus(2);
            DealerChannelRate dealerChannelRate1 = dealerChannelRateDao.selectByDealerIdAndProductIdAndChannelType(dealerIds.get(i),productId,channelTypeSign);
            if(dealerChannelRate1==null){
                dealerChannelRateDao.init(dealerChannelRate);
            }else{
                log.info("已存在");
            }
            log.info("第"+i+"数据初始化end，代理商编号"+dealerIds.get(i));
        }
        return CommonResponse.objectResponse(1,"",dealerIds);
    }


    /**
     * 初始化商户数据
     */
    @ResponseBody
    @RequestMapping(value = "initMerchant", method = RequestMethod.GET)
    public void initMerchant(final HttpServletRequest request, final HttpServletResponse response) {
        //微信费率
        int channelType = 301;
        String sysType = EnumProductType.HSS.getId();

        Optional<BasicChannel> basicChannelOptionalTemp = basicChannelService.selectByChannelTypeSign(channelType);
        List<MerchantInfo> merchantInfoList = merchantInfoService.getAll();
        for(int i=0;i<merchantInfoList.size();i++){
            log.info("第"+i+"数据初始化start，商户编号"+merchantInfoList.get(i).getId());
            if(merchantInfoList.get(i).getFirstDealerId()>0){//有一级代理
                Optional<DealerChannelRate> dealerChannelRateOptional = dealerChannelRateService.selectByDealerIdAndProductIdAndChannelType(merchantInfoList.get(i).getFirstDealerId(),merchantInfoList.get(i).getProductId(),channelType);
                if(dealerChannelRateOptional.isPresent()){//存在
                    MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
                    merchantChannelRate.setMerchantId(merchantInfoList.get(i).getId());
                    merchantChannelRate.setProductId(merchantInfoList.get(i).getProductId());
                    merchantChannelRate.setMarkCode(merchantInfoList.get(i).getMarkCode());
                    merchantChannelRate.setSysType(sysType);
                    merchantChannelRate.setChannelTypeSign(channelType);
                    merchantChannelRate.setMerchantBalanceType(dealerChannelRateOptional.get().getDealerBalanceType());
                    merchantChannelRate.setMerchantWithdrawFee(dealerChannelRateOptional.get().getDealerMerchantWithdrawFee());
                    merchantChannelRate.setMerchantPayRate(dealerChannelRateOptional.get().getDealerMerchantPayRate());
                    if(basicChannelOptionalTemp.get().getIsNeed()==1){//需要入网
                        merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
                    }else{
                        merchantChannelRate.setEnterNet(EnumEnterNet.UNSUPPORT.getId());
                    }
                    merchantChannelRate.setChannelCompany(basicChannelOptionalTemp.get().getChannelCompany());
                    merchantChannelRate.setThirdCompany(basicChannelOptionalTemp.get().getThirdCompany());
                    merchantChannelRate.setStatus(EnumCommonStatus.NORMAL.getId());
                    MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
                    merchantChannelRateRequest.setChannelTypeSign(channelType);
                    merchantChannelRateRequest.setProductId(merchantInfoList.get(i).getProductId());
                    merchantChannelRateRequest.setMerchantId(merchantInfoList.get(i).getId());
                    Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
                    if(!merchantChannelRateOptional.isPresent()){//不存在
                        merchantChannelRateService.initMerchantChannelRate(merchantChannelRate);
                    }else{
                        log.info("基础代理商费率配置有误");
                    }
                }
            }else{
                Optional<ProductChannelDetail> productChannelDetailOptional = productChannelDetailService.selectRateByProductIdAndChannelTypeSign(merchantInfoList.get(i).getProductId(),channelType);
                if(productChannelDetailOptional.isPresent()){
                    MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
                    merchantChannelRate.setMerchantId(merchantInfoList.get(i).getId());
                    merchantChannelRate.setProductId(merchantInfoList.get(i).getProductId());
                    merchantChannelRate.setMarkCode(merchantInfoList.get(i).getMarkCode());
                    merchantChannelRate.setSysType(sysType);
                    merchantChannelRate.setChannelTypeSign(channelType);
                    merchantChannelRate.setMerchantBalanceType(productChannelDetailOptional.get().getProductBalanceType());
                    merchantChannelRate.setMerchantWithdrawFee(productChannelDetailOptional.get().getProductMerchantWithdrawFee());
                    merchantChannelRate.setMerchantPayRate(productChannelDetailOptional.get().getProductMerchantPayRate());
                    if(basicChannelOptionalTemp.get().getIsNeed()==1){//需要入网
                        merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
                    }else{
                        merchantChannelRate.setEnterNet(EnumEnterNet.UNSUPPORT.getId());
                    }
                    merchantChannelRate.setChannelCompany(basicChannelOptionalTemp.get().getChannelCompany());
                    merchantChannelRate.setThirdCompany(basicChannelOptionalTemp.get().getThirdCompany());
                    merchantChannelRate.setStatus(EnumCommonStatus.NORMAL.getId());
                    MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
                    merchantChannelRateRequest.setChannelTypeSign(channelType);
                    merchantChannelRateRequest.setProductId(merchantInfoList.get(i).getProductId());
                    merchantChannelRateRequest.setMerchantId(merchantInfoList.get(i).getId());
                    Optional<MerchantChannelRate> merchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
                    if(!merchantChannelRateOptional.isPresent()){//不存在
                        merchantChannelRateService.initMerchantChannelRate(merchantChannelRate);
                    }else{
                        log.info("已存在");
                    }
                }else{
                    log.info("基础产品费率配置有误");
                }
            }
            log.info("第"+i+"数据初始化end，商户编号"+merchantInfoList.get(i).getId());
        }
    }
}
