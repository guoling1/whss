package com.jkm.api.service.impl;

import com.google.common.base.Optional;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.requestparam.MerchantRequest;
import com.jkm.api.service.MerchantService;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.enums.EnumGlobalIDPro;
import com.jkm.base.common.enums.EnumGlobalIDType;
import com.jkm.base.common.util.GlobalID;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.*;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.request.RequestMerchantInfo;
import com.jkm.hss.merchant.service.*;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.PartnerRuleSetting;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.enums.EnumUpGradeType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.PartnerRuleSettingService;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/8/16.
 */
@Slf4j
@Service
public class MerchantServiceImpl implements MerchantService {
    @Autowired
    private DealerService dealerService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private ProductService productService;
    @Autowired
    private VerifyIdService verifyIdService;
    @Autowired
    private DealerChannelRateService dealerChannelRateService;
    @Autowired
    private PartnerRuleSettingService partnerRuleSettingService;
    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MerchantInfoCheckRecordService merchantInfoCheckRecordService;
    /**
     * 商户入网
     *
     * @return
     */
    @Override
    public CommonResponse merchantIn(MerchantRequest apiMerchantRequest) {
        long oemId = 0;
        if (StringUtils.isBlank(apiMerchantRequest.getMerchantName())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"商户名称不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getMobile())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"手机号不能为空");
        }
        if (!ValidateUtils.isMobile(apiMerchantRequest.getMobile())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.FORMAT_ERROR,"手机号格式错误");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getProvinceCode())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"省份编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getCityCode())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"市编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getCountyCode())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"县编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getAddress())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"地址不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getBranchCode())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"联行号不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getBranchName())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"支行名称不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getBankProvinceCode())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"支行所在地省份编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getBankCityCode())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"支行所在地市编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getBankCountryCode())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"支行所在地区县编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getDealerMarkCode())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"代理商编号不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getBankNo())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"银行卡号不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getName())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"真实姓名不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getIdentity())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"身份证号不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getReserveMobile())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL,"预留手机号不能为空");
        }
        Optional<Dealer> dealerOptional =  dealerService.getDealerByMarkCode(apiMerchantRequest.getDealerMarkCode());
        if (!dealerOptional.isPresent()){
            throw new JKMTradeServiceException(JKMTradeErrorCode.DEALER_NOT_EXIST);
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(apiMerchantRequest.getMobile()),oemId);
        if (merchantInfoOptional.isPresent()){
            throw new JKMTradeServiceException(JKMTradeErrorCode.MCT_EXIST);
        }
        Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
        if(!productOptional.isPresent()){
            throw new JKMTradeServiceException(JKMTradeErrorCode.PRODUCT_NOT_EXIST);
        }
        final Pair<Integer, String> pair = this.verifyIdService.verifyID(MerchantSupport.decryptMobile(merchantInfoOptional.get().getMobile()),
                apiMerchantRequest.getBankNo(), apiMerchantRequest.getIdentity(), apiMerchantRequest.getReserveMobile(), apiMerchantRequest.getName());
        if (0 != pair.getLeft()) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.FOUR_FACTOR_AUTHEN,pair.getRight());
        }
        MerchantInfo mi = new MerchantInfo();

        mi.setStatus(EnumMerchantStatus.PASSED.getId());
        mi.setMobile(MerchantSupport.encryptMobile(apiMerchantRequest.getMobile()));
        mi.setMdMobile(MerchantSupport.passwordDigest(apiMerchantRequest.getMobile(),"JKM"));
        mi.setProductId(productOptional.get().getId());
        mi.setSource(EnumSource.DEALERRECOMMEND.getId());
        if(dealerOptional.get().getLevel()== EnumDealerLevel.FIRST.getId()){
            mi.setDealerId(dealerOptional.get().getId());
            mi.setFirstDealerId(dealerOptional.get().getId());
            mi.setSecondDealerId(0);
        }
        if(dealerOptional.get().getLevel()==EnumDealerLevel.SECOND.getId()){
            mi.setDealerId(dealerOptional.get().getId());
            mi.setFirstDealerId(dealerOptional.get().getFirstLevelDealerId());
            mi.setSecondDealerId(dealerOptional.get().getId());
        }
        mi.setFirstMerchantId(0);
        mi.setSecondMerchantId(0);
        mi.setAccountId(0);
        mi.setLevel(EnumUpGradeType.COMMON.getId());
        mi.setHierarchy(0);
        mi.setIsUpgrade(EnumIsUpgrade.CANNOTUPGRADE.getId());
        mi.setOemId(oemId);
        List<DealerChannelRate> dealerChannelRateList = dealerChannelRateService.selectByDealerIdAndProductId(dealerOptional.get().getId(),mi.getProductId());
        if(dealerChannelRateList.size()<=0){
            throw new JKMTradeServiceException(JKMTradeErrorCode.DEALER_RETE_ERROR);
        }
        merchantInfoService.regByWx(mi);
        //初始化费率
        for(int i=0;i<dealerChannelRateList.size();i++){
            MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
            merchantChannelRate.setMerchantId(mi.getId());
            merchantChannelRate.setProductId(mi.getProductId());
            merchantChannelRate.setMarkCode(mi.getMarkCode());
            merchantChannelRate.setSysType(EnumProductType.HSS.getId());
            merchantChannelRate.setChannelTypeSign(dealerChannelRateList.get(i).getChannelTypeSign());
            merchantChannelRate.setMerchantBalanceType(dealerChannelRateList.get(i).getDealerBalanceType());
            if(mi.getIsUpgrade()==EnumIsUpgrade.CANUPGRADE.getId()){
                Optional<PartnerRuleSetting> partnerRuleSettingOptional = partnerRuleSettingService.selectByProductIdAndChannelTypeSign(mi.getProductId(),dealerChannelRateList.get(i).getChannelTypeSign());
                if(partnerRuleSettingOptional.isPresent()&&partnerRuleSettingOptional.get().getCommonRate()!=null){
                    merchantChannelRate.setMerchantPayRate(partnerRuleSettingOptional.get().getCommonRate());
                }else{
                    merchantChannelRate.setMerchantPayRate(dealerChannelRateList.get(i).getDealerMerchantPayRate());
                }
            }else{
                merchantChannelRate.setMerchantPayRate(dealerChannelRateList.get(i).getDealerMerchantPayRate());
            }
            merchantChannelRate.setMerchantWithdrawFee(dealerChannelRateList.get(i).getDealerMerchantWithdrawFee());
            Optional<BasicChannel> basicChannelOptionalTemp = basicChannelService.selectByChannelTypeSign(dealerChannelRateList.get(i).getChannelTypeSign());
            if(!basicChannelOptionalTemp.isPresent()){
                log.info("基本通道配置{}有误",dealerChannelRateList.get(i).getChannelTypeSign());
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
        //初始化用户
        UserInfo uo = new UserInfo();
        uo.setMobile(MerchantSupport.encryptMobile(apiMerchantRequest.getMobile()));
        uo.setType(EnumUserInfoType.HSS.getId());
        uo.setRoleId(0);
        uo.setStatus(EnumCommonStatus.NORMAL.getId());
        uo.setMerchantId(mi.getId());
        userInfoService.insertUserInfo(uo);
        String tempMarkCode = GlobalID.GetGlobalID(EnumGlobalIDType.USER, EnumGlobalIDPro.MIN,uo.getId()+"");
        userInfoService.updatemarkCode(tempMarkCode,uo.getId());
        //审核记录
        RequestMerchantInfo requestMerchantInfo = new RequestMerchantInfo();
        requestMerchantInfo.setStatus(EnumMerchantStatus.PASSED.getId());
        requestMerchantInfo.setName("root");
        requestMerchantInfo.setDescr("审核通过");
        requestMerchantInfo.setMerchantId(mi.getId());
        this.merchantInfoCheckRecordService.save(requestMerchantInfo);

        return null;
    }

}
