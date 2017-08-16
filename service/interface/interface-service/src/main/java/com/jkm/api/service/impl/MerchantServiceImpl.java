package com.jkm.api.service.impl;

import com.google.common.base.Optional;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.requestparam.MerchantRequest;
import com.jkm.api.service.MerchantService;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    /**
     * 商户入网
     *
     * @return
     */
    @Override
    public CommonResponse merchantIn(MerchantRequest apiMerchantRequest) {
        long oemId = 0;
        if (StringUtils.isBlank(apiMerchantRequest.getMerchantName())) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.PARAM_NOT_NULL);
        }
        if (StringUtils.isBlank(apiMerchantRequest.getMobile())) {
            return CommonResponse.simpleResponse(-1, "手机号不能为空");
        }
        if (!ValidateUtils.isMobile(apiMerchantRequest.getMobile())) {
            return CommonResponse.simpleResponse(-1, "手机号格式错误");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getProvinceCode())) {
            return CommonResponse.simpleResponse(-1, "省份编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getProvinceName())) {
            return CommonResponse.simpleResponse(-1, "省份名称不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getCityCode())) {
            return CommonResponse.simpleResponse(-1, "市编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getCityName())) {
            return CommonResponse.simpleResponse(-1, "市名称不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getCountyCode())) {
            return CommonResponse.simpleResponse(-1, "县编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getCountyName())) {
            return CommonResponse.simpleResponse(-1, "县名称不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getAddress())) {
            return CommonResponse.simpleResponse(-1, "地址不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getBranchCode())) {
            return CommonResponse.simpleResponse(-1, "联行号不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getBranchName())) {
            return CommonResponse.simpleResponse(-1, "支行名称不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getDistrictCode())) {
            return CommonResponse.simpleResponse(-1, "支行所在地区编码不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getDealerMarkCode())) {
            return CommonResponse.simpleResponse(-1, "代理商编号不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getBankNo())) {
            return CommonResponse.simpleResponse(-1, "银行卡号不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getName())) {
            return CommonResponse.simpleResponse(-1, "真实姓名不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getIdentity())) {
            return CommonResponse.simpleResponse(-1, "身份证号不能为空");
        }
        if (StringUtils.isBlank(apiMerchantRequest.getReserveMobile())) {
            return CommonResponse.simpleResponse(-1, "预留手机号不能为空");
        }
        Optional<Dealer> dealerOptional =  dealerService.getDealerByMarkCode(apiMerchantRequest.getDealerMarkCode());
        if (!dealerOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(apiMerchantRequest.getMobile()),oemId);
        if (merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "该商户已入网");
        }


//        Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
//        if(!productOptional.isPresent()){
//            return CommonResponse.simpleResponse(-1, "产品信息有误");
//        }
//        long productId = productOptional.get().getId();
//        Optional<MerchantInfo> merchantInfoOptional1 = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(mobile),oemId);
//        if(merchantInfoOptional1.isPresent()){
//            return CommonResponse.simpleResponse(2, "该商户已注册，请直接登录");
//        }else{
//            MerchantInfo mi = new MerchantInfo();
//            mi.setStatus(EnumMerchantStatus.INIT.getId());
//            mi.setMobile(MerchantSupport.encryptMobile(mobile));
//            mi.setMdMobile(MerchantSupport.passwordDigest(mobile,"JKM"));
//            mi.setProductId(productId);
//            if(loginRequest.getInviteCode().length()==6){
//                mi.setSource(EnumSource.DEALERRECOMMEND.getId());
//                Optional<Dealer> dealerOptional = dealerService.getDealerByInviteCode(loginRequest.getInviteCode());
//                if(!dealerOptional.isPresent()){
//                    return CommonResponse.simpleResponse(-1, "邀请码(代理商)不存在");
//                }
//                if(dealerOptional.get().getLevel()==EnumDealerLevel.FIRST.getId()){
//                    mi.setDealerId(dealerOptional.get().getId());
//                    mi.setFirstDealerId(dealerOptional.get().getId());
//                    mi.setSecondDealerId(0);
//                }
//                if(dealerOptional.get().getLevel()==EnumDealerLevel.SECOND.getId()){
//                    mi.setDealerId(dealerOptional.get().getId());
//                    mi.setFirstDealerId(dealerOptional.get().getFirstLevelDealerId());
//                    mi.setSecondDealerId(dealerOptional.get().getId());
//                }
//                mi.setFirstMerchantId(0);
//                mi.setSecondMerchantId(0);
//                mi.setAccountId(0);
//                mi.setLevel(EnumUpGradeType.COMMON.getId());
//                mi.setHierarchy(0);
//                mi.setIsUpgrade(EnumIsUpgrade.CANUPGRADE.getId());
//                mi.setOemId(oemId);
//                merchantInfoService.regByWx(mi);
//                //初始化费率
//                List<DealerChannelRate> dealerChannelRateList = dealerChannelRateService.selectByDealerIdAndProductId(dealerOptional.get().getId(),productId);
//                if(dealerChannelRateList.size()>0){
//                    for(int i=0;i<dealerChannelRateList.size();i++){
//                        MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
//                        merchantChannelRate.setMerchantId(mi.getId());
//                        merchantChannelRate.setProductId(productId);
//                        merchantChannelRate.setMarkCode(mi.getMarkCode());
//                        merchantChannelRate.setSysType(EnumProductType.HSS.getId());
//                        merchantChannelRate.setChannelTypeSign(dealerChannelRateList.get(i).getChannelTypeSign());
//                        merchantChannelRate.setMerchantBalanceType(dealerChannelRateList.get(i).getDealerBalanceType());
//
//                        if(mi.getIsUpgrade()==EnumIsUpgrade.CANUPGRADE.getId()){
//                            Optional<PartnerRuleSetting> partnerRuleSettingOptional = partnerRuleSettingService.selectByProductIdAndChannelTypeSign(productId,dealerChannelRateList.get(i).getChannelTypeSign());
//                            if(partnerRuleSettingOptional.isPresent()&&partnerRuleSettingOptional.get().getCommonRate()!=null){
//                                merchantChannelRate.setMerchantPayRate(partnerRuleSettingOptional.get().getCommonRate());
//                            }else{
//                                merchantChannelRate.setMerchantPayRate(dealerChannelRateList.get(i).getDealerMerchantPayRate());
//                            }
//                        }else{
//                            merchantChannelRate.setMerchantPayRate(dealerChannelRateList.get(i).getDealerMerchantPayRate());
//                        }
//                        merchantChannelRate.setMerchantWithdrawFee(dealerChannelRateList.get(i).getDealerMerchantWithdrawFee());
//                        Optional<BasicChannel> basicChannelOptionalTemp = basicChannelService.selectByChannelTypeSign(dealerChannelRateList.get(i).getChannelTypeSign());
//                        if(!basicChannelOptionalTemp.isPresent()){
//                            log.info("基本通道配置{}有误",dealerChannelRateList.get(i).getChannelTypeSign());
//                        }
//                        if(basicChannelOptionalTemp.get().getIsNeed()==1){//需要入网
//                            merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
//                        }else{
//                            merchantChannelRate.setEnterNet(EnumEnterNet.UNSUPPORT.getId());
//                        }
//                        merchantChannelRate.setChannelCompany(basicChannelOptionalTemp.get().getChannelCompany());
//                        merchantChannelRate.setThirdCompany(basicChannelOptionalTemp.get().getThirdCompany());
//                        merchantChannelRate.setStatus(EnumCommonStatus.NORMAL.getId());
//                        merchantChannelRateService.initMerchantChannelRate(merchantChannelRate);
//                    }
//                }else{
//                    log.info("代理商产品费率配置有误");
//                    return CommonResponse.simpleResponse(-1, "代理商产品费率配置有误");
//                }
//                //添加用户
//                UserInfo uo = new UserInfo();
//                uo.setMobile(MerchantSupport.encryptMobile(mobile));
//                uo.setType(EnumUserInfoType.HSS.getId());
//                uo.setRoleId(0);
//                uo.setStatus(EnumCommonStatus.NORMAL.getId());
//                uo.setMerchantId(mi.getId());
//                userInfoService.insertUserInfo(uo);
//                String tempMarkCode = GlobalID.GetGlobalID(EnumGlobalIDType.USER,EnumGlobalIDPro.MIN,uo.getId()+"");
//                userInfoService.updatemarkCode(tempMarkCode,uo.getId());
//                //添加好友
//                DealerRecommend dealerRecommend = new DealerRecommend();
//                dealerRecommend.setDealerId(dealerOptional.get().getId());
//                dealerRecommend.setRecommendMerchantId(mi.getId());
//                dealerRecommend.setType(EnumRecommendType.DIRECT.getId());
//                dealerRecommend.setStatus(1);
//                dealerRecommendService.insert(dealerRecommend);
//
//                if(mi.getFirstDealerId()>0&&dealerOptional.get().getLevel()>1){//间接好友
//                    DealerRecommend dealerRecommend1 = new DealerRecommend();
//                    dealerRecommend1.setDealerId(mi.getFirstDealerId());
//                    dealerRecommend1.setRecommendMerchantId(mi.getId());
//                    dealerRecommend1.setType(EnumRecommendType.INDIRECT.getId());
//                    dealerRecommend1.setStatus(1);
//                    dealerRecommendService.insert(dealerRecommend1);
//                }
//                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "注册成功",mi.getId());
//            }else{
//                mi.setSource(EnumSource.RECOMMEND.getId());
//                //初始化代理商和商户
//                Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectByMobileAndOemId(MerchantSupport.encryptMobile(loginRequest.getInviteCode()),oemId);
//                if(!merchantInfoOptional.isPresent()){
//                    return CommonResponse.simpleResponse(-1, "您的朋友没有开通分享功能");
//                }
//                mi.setDealerId(0);
//                mi.setFirstDealerId(merchantInfoOptional.get().getFirstDealerId());
//                mi.setSecondDealerId(merchantInfoOptional.get().getSecondDealerId());
//                mi.setFirstMerchantId(merchantInfoOptional.get().getId());
//                mi.setSecondMerchantId(merchantInfoOptional.get().getFirstMerchantId());
//                mi.setAccountId(0);
//                mi.setLevel(EnumUpGradeType.COMMON.getId());
//                mi.setHierarchy(merchantInfoOptional.get().getHierarchy()+1);
//                mi.setIsUpgrade(EnumIsUpgrade.CANUPGRADE.getId());
//                mi.setOemId(oemId);
//                merchantInfoService.regByWx(mi);
//                List<ProductChannelDetail> productChannelDetailList = productChannelDetailService.selectByProductId(productId);
//                if(productChannelDetailList.size()>0){
//                    for(int i=0;i<productChannelDetailList.size();i++){
//                        MerchantChannelRate merchantChannelRate = new MerchantChannelRate();
//                        merchantChannelRate.setMerchantId(mi.getId());
//                        merchantChannelRate.setProductId(productId);
//                        merchantChannelRate.setMarkCode(mi.getMarkCode());
//                        merchantChannelRate.setSysType(EnumProductType.HSS.getId());
//                        merchantChannelRate.setChannelTypeSign(productChannelDetailList.get(i).getChannelTypeSign());
//                        merchantChannelRate.setMerchantBalanceType(productChannelDetailList.get(i).getProductBalanceType());
//                        if(mi.getIsUpgrade()==EnumIsUpgrade.CANUPGRADE.getId()){
//                            Optional<PartnerRuleSetting> partnerRuleSettingOptional = partnerRuleSettingService.selectByProductIdAndChannelTypeSign(productId,productChannelDetailList.get(i).getChannelTypeSign());
//                            if(partnerRuleSettingOptional.isPresent()&&partnerRuleSettingOptional.get().getCommonRate()!=null){
//                                merchantChannelRate.setMerchantPayRate(partnerRuleSettingOptional.get().getCommonRate());
//                            }else{
//                                merchantChannelRate.setMerchantPayRate(productChannelDetailList.get(i).getProductMerchantPayRate());
//                            }
//                        }else{
//                            merchantChannelRate.setMerchantPayRate(productChannelDetailList.get(i).getProductMerchantPayRate());
//                        }
//                        merchantChannelRate.setMerchantWithdrawFee(productChannelDetailList.get(i).getProductMerchantWithdrawFee());
//                        Optional<BasicChannel> basicChannelOptionalTemp = basicChannelService.selectByChannelTypeSign(productChannelDetailList.get(i).getChannelTypeSign());
//                        if(!basicChannelOptionalTemp.isPresent()){
//                            log.info("基本通道配置{}有误",productChannelDetailList.get(i).getChannelTypeSign());
//                        }
//                        if(basicChannelOptionalTemp.get().getIsNeed()==1){//需要入网
//                            merchantChannelRate.setEnterNet(EnumEnterNet.UNENT.getId());
//                        }else{
//                            merchantChannelRate.setEnterNet(EnumEnterNet.UNSUPPORT.getId());
//                        }
//                        merchantChannelRate.setChannelCompany(basicChannelOptionalTemp.get().getChannelCompany());
//                        merchantChannelRate.setThirdCompany(basicChannelOptionalTemp.get().getThirdCompany());
//                        merchantChannelRate.setStatus(EnumCommonStatus.NORMAL.getId());
//                        merchantChannelRateService.initMerchantChannelRate(merchantChannelRate);
//                    }
//                }else{
//                    log.info("基础产品费率配置有误");
//                    return CommonResponse.simpleResponse(-1, "基础产品费率配置有误");
//                }
//                //添加用户
//                UserInfo uo = new UserInfo();
//                uo.setMobile(MerchantSupport.encryptMobile(mobile));
//                uo.setType(EnumUserInfoType.HSS.getId());
//                uo.setRoleId(0);
//                uo.setStatus(EnumCommonStatus.NORMAL.getId());
//                uo.setMerchantId(mi.getId());
//                userInfoService.insertUserInfo(uo);
//                String tempMarkCode = GlobalID.GetGlobalID(EnumGlobalIDType.USER,EnumGlobalIDPro.MIN,uo.getId()+"");
//                userInfoService.updatemarkCode(tempMarkCode,uo.getId());
//                //添加好友
//                if(mi.getFirstMerchantId()!=0){//直接好友
//                    Recommend recommend = new Recommend();
//                    recommend.setMerchantId(mi.getId());
//                    recommend.setRecommendMerchantId(mi.getFirstMerchantId());
//                    recommend.setType(EnumRecommendType.DIRECT.getId());
//                    recommend.setStatus(1);
//                    recommendService.insert(recommend);
//                }
//                if(mi.getSecondMerchantId()!=0){//间接好友
//                    Recommend recommend = new Recommend();
//                    recommend.setMerchantId(mi.getId());
//                    recommend.setRecommendMerchantId(mi.getSecondMerchantId());
//                    recommend.setType(EnumRecommendType.INDIRECT.getId());
//                    recommend.setStatus(1);
//                    recommendService.insert(recommend);
//                }
//                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "注册成功",mi.getId());
//            }
//
//        }

        return null;
    }
}
