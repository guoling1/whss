package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.enums.EnumStatus;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumUpperChannel;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hsy.user.Enum.*;
import com.jkm.hsy.user.dao.HsyCmbcDao;
import com.jkm.hsy.user.dao.HsyMerchantAuditDao;
import com.jkm.hsy.user.entity.*;
import com.jkm.hsy.user.help.requestparam.CmbcProductResponse;
import com.jkm.hsy.user.help.requestparam.CmbcResponse;
import com.jkm.hsy.user.help.requestparam.XmmsResponse;
import com.jkm.hsy.user.service.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingliujie on 2017/4/17.
 */
@Slf4j
@Service(value = "hsyCmbcService")
public class HsyCmbcServiceImpl implements HsyCmbcService {

    @Autowired
    private HsyCmbcDao hsyCmbcDao;
    @Autowired
    private HsyMerchantAuditDao hsyMerchantAuditDao;
    @Autowired
    private UserTradeRateService userTradeRateService;
    @Autowired
    private UserWithdrawRateService userWithdrawRateService;
    @Autowired
    private UserChannelPolicyService userChannelPolicyService;
    @Autowired
    private NetLogService netLogService;


    /**
     * 修改入网及开通产品信息
     * @param userId
     * @param shopId
     * @param adminId
     * @param opt
     * @return
     */
    @Override
    public boolean merchantInfoModify(long userId,long shopId,long adminId,String opt) {
        boolean backResult = true;
        Optional<UserChannelPolicy> userChannelPolicyOptional = userChannelPolicyService.selectByUserIdAndChannelTypeSign(userId,EnumPayChannelSign.SYJ_WECHAT.getId());
        if(userChannelPolicyOptional.isPresent()){
            if(userChannelPolicyOptional.get().getNetStatus()!=null&&userChannelPolicyOptional.get().getNetStatus()== EnumNetStatus.SUCCESS.getId()){
                CmbcResponse cr = this.merchantBaseInfoModify(userId,shopId);

                NetLog netLogCr = new NetLog();
                netLogCr.setAdminId(adminId);
                netLogCr.setUserId(userId);
                netLogCr.setChannelTypeSign(EnumPayChannelSign.SYJ_WECHAT.getId());
                netLogCr.setOpt(opt);
                netLogCr.setAct(EnumAct.SYJUPDATENET.getMsg());
                netLogCr.setResult(cr.getMsg());
                netLogCr.setStatus(EnumStatus.NORMAL.getId());
                netLogService.insert(netLogCr);

                if(cr.getCode()==1){
                    UserChannelPolicy netInfo = new UserChannelPolicy();
                    netInfo.setUserId(userId);
                    netInfo.setNetStatus(EnumNetStatus.SUCCESS.getId());
                    netInfo.setNetMarks(cr.getMsg());
                    userChannelPolicyService.updateHxNetInfo(netInfo);
                }else{
                    backResult = false;
                }
                if(userChannelPolicyOptional.get().getOpenProductStatus()!=null&&userChannelPolicyOptional.get().getOpenProductStatus()== EnumOpenProductStatus.PASS.getId()){
                    CmbcResponse cm = this.merchantUpdateBindChannel(userId);

                    NetLog netLogCm = new NetLog();
                    netLogCm.setAdminId(adminId);
                    netLogCm.setUserId(userId);
                    netLogCm.setChannelTypeSign(EnumPayChannelSign.SYJ_WECHAT.getId());
                    netLogCm.setOpt(opt);
                    netLogCm.setAct(EnumAct.SYJUPDATEPRODUCT.getMsg());
                    netLogCm.setResult(cm.getMsg());
                    netLogCm.setStatus(EnumStatus.NORMAL.getId());
                    netLogService.insert(netLogCm);

                    if(cm.getCode()==1){
                        UserChannelPolicy openProduct = new UserChannelPolicy();
                        openProduct.setUserId(userId);
                        openProduct.setOpenProductStatus(EnumOpenProductStatus.PASS.getId());
                        openProduct.setOpenProductMarks(cm.getMsg());
                        userChannelPolicyService.updateHxOpenProduct(openProduct);
                    }else{
                        backResult = false;
                    }
                }
            }
        }
        List<Integer> list=new ArrayList<Integer>(){
            {
                add(EnumPayChannelSign.XMMS_WECHAT_T1.getId());
                add(EnumPayChannelSign.XMMS_ALIPAY_T1.getId());
//                add(EnumPayChannelSign.XMMS_WECHAT_D0.getId());
//                add(EnumPayChannelSign.XMMS_ALIPAY_D0.getId());
            }
        };
        for(int i=0;i<list.size();i++){
            Optional<UserChannelPolicy> ucp = userChannelPolicyService.selectByUserIdAndChannelTypeSign(userId,list.get(i));
            if(ucp.isPresent()){
                if(ucp.get().getNetStatus()!=null&&ucp.get().getNetStatus()==EnumNetStatus.SUCCESS.getId()){
                    XmmsResponse.BaseResponse xmmsResponse = this.merchantModify(userId,shopId,list.get(i));
                    if(xmmsResponse.getCode()==1){
                        if((EnumXmmsStatus.SUCCESS.getId()).equals(xmmsResponse.getResult().getStatus())){
                            UserChannelPolicy netInfo = new UserChannelPolicy();
                            netInfo.setUserId(userId);
                            netInfo.setNetStatus(EnumNetStatus.SUCCESS.getId());
                            netInfo.setNetMarks(xmmsResponse.getResult().getMsg());
                            netInfo.setOpenProductStatus(EnumOpenProductStatus.PASS.getId());
                            netInfo.setOpenProductMarks(xmmsResponse.getResult().getMsg());
                            netInfo.setChannelTypeSign(EnumPayChannelSign.XMMS_WECHAT_T1.getId());
                            userChannelPolicyService.updateByUserIdAndChannelTypeSign(netInfo);
                        }
                        if((EnumXmmsStatus.FAIL.getId()).equals(xmmsResponse.getResult().getStatus())){
                            backResult = false;
                        }
                        NetLog netLogXmms = new NetLog();
                        netLogXmms.setAdminId(adminId);
                        netLogXmms.setUserId(userId);
                        netLogXmms.setChannelTypeSign(list.get(i));
                        netLogXmms.setOpt(opt);
                        netLogXmms.setAct(EnumAct.XMMSUPDATENET.getMsg());
                        netLogXmms.setResult(xmmsResponse.getResult().getMsg());
                        netLogXmms.setStatus(EnumStatus.NORMAL.getId());
                        netLogService.insert(netLogXmms);
                    }else{
                        backResult = false;
                        NetLog netLogXmms = new NetLog();
                        netLogXmms.setAdminId(adminId);
                        netLogXmms.setUserId(userId);
                        netLogXmms.setChannelTypeSign(list.get(i));
                        netLogXmms.setOpt(opt);
                        netLogXmms.setAct(EnumAct.XMMSUPDATENET.getMsg());
                        netLogXmms.setResult(xmmsResponse.getMsg());
                        netLogXmms.setStatus(EnumStatus.NORMAL.getId());
                        netLogService.insert(netLogXmms);
                    }
                }
            }
        }
        return backResult;
    }


    /**
     * 收银家商户进件
     * @param userId //用户编码
     * @param shopId //主店编码
     * @return
     */
    @Override
    public CmbcResponse merchantBaseInfoReg(long userId,long shopId) {
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(shopId);
        AppBizCard appBizCard = hsyCmbcDao.selectByCardId(shopId);
        CmbcResponse cmbcResponse = new CmbcResponse();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", appAuUser.getGlobalID());//商户编号
        paramsMap.put("fullName", appBizShop.getName());//商户全称
        paramsMap.put("shortName", appBizShop.getShortName());//商户简称
        paramsMap.put("servicePhone","4006226233");//客服电话
        if(appBizShop.getLicenceNO()==null||"".equals(appBizShop.getLicenceNO())){
            paramsMap.put("businessLicense","");//证据编号
            paramsMap.put("businessLicenseType","");//证件类型
        }else{
            paramsMap.put("businessLicense",appBizShop.getLicenceNO());//证据编号
            paramsMap.put("businessLicenseType","NATIONAL_LEGAL");//证件类型
        }
        paramsMap.put("contactName",appAuUser.getRealname());//联系人名称
        paramsMap.put("contactPhone",appAuUser.getCellphone());//联系人手机号
        paramsMap.put("contactPersonType","LEGAL_PERSON");//联系人类型
        paramsMap.put("contactIdCard",appAuUser.getIdcard());//身份证号
        paramsMap.put("contactEmail","");//邮箱
        paramsMap.put("bankAccountNo",appBizCard.getCardNO());//银行账号
        paramsMap.put("bankAccountName",appBizCard.getCardAccountName());//开户名称
        paramsMap.put("idCard",appBizCard.getIdcardNO());//开户身份证号
        if(appBizShop.getIsPublic()==1){//对公
            paramsMap.put("bankAccountType","2");//账户类型
        }else{
            paramsMap.put("bankAccountType","1");//账户类型
        }
        paramsMap.put("bankAccountLineNo",appBizCard.getBranchCode());//银行联行号
        paramsMap.put("bankAccountAddress",appBizCard.getBankAddress());//开户行地址
        String districtCode;
        if (appBizShop.getDistrictCode().equals("429021")){
            districtCode = "420325";
        }else {
            districtCode = appBizShop.getDistrictCode();
        }
        HsyMerchantAuditResponse district = hsyMerchantAuditDao.getCode(districtCode);
        paramsMap.put("district",district.getAName());//商户地址区
        HsyMerchantAuditResponse city = hsyMerchantAuditDao.getCode(district.getParentCode());
        paramsMap.put("city",city.getAName());//商户地址市
        if("110000,120000,310000,500000".contains(city.getCode())){
            paramsMap.put("province",city.getAName().replace("市",""));//商户地址省
        }else{
            HsyMerchantAuditResponse province = hsyMerchantAuditDao.getCode(city.getParentCode());
            paramsMap.put("province",province.getAName());//商户地址省
        }
        Optional<UserWithdrawRate> userWithdrawRateOptional = userWithdrawRateService.selectByUserId(userId);
        paramsMap.put("remitD0",userWithdrawRateOptional.get().getWithdrawRateD0().toString());
        paramsMap.put("address",appBizShop.getAddress());//详细地址
        log.info("收银家商户进件参数为："+ JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantBaseInfoReg(), paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("收银家商户进件结果为："+jo.toString());
            cmbcResponse.setCode(jo.getInt("code"));
            cmbcResponse.setMsg(jo.getString("msg"));
            cmbcResponse.setResult(jo.getString("result"));
        } else {
            cmbcResponse.setCode(-1);
            cmbcResponse.setMsg("请求超时");
        }
        return cmbcResponse;
    }

    /**
     * 收银家修改商户进件
     *
     * @param userId //用户编码
     * @param shopId //主店编码
     * @return
     */
    @Override
    public CmbcResponse merchantBaseInfoModify(long userId, long shopId) {
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(shopId);
        AppBizCard appBizCard = hsyCmbcDao.selectByCardId(shopId);
        CmbcResponse cmbcResponse = new CmbcResponse();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", appAuUser.getGlobalID());
        paramsMap.put("shortName", appBizShop.getShortName());
        paramsMap.put("servicePhone","4006226233");
        if(appBizShop.getLicenceNO()==null||"".equals(appBizShop.getLicenceNO())){
            paramsMap.put("businessLicense","");
            paramsMap.put("businessLicenseType","");
        }else{
            paramsMap.put("businessLicense",appBizShop.getLicenceNO());
            paramsMap.put("businessLicenseType","NATIONAL_LEGAL");
        }
        paramsMap.put("contactName",appAuUser.getRealname());
        paramsMap.put("contactPhone",appAuUser.getCellphone());
        paramsMap.put("contactPersonType","LEGAL_PERSON");
        paramsMap.put("contactIdCard",appAuUser.getIdcard());
        paramsMap.put("contactEmail","");
        paramsMap.put("bankAccountNo",appBizCard.getCardNO());
        paramsMap.put("bankAccountName",appBizCard.getCardAccountName());
        paramsMap.put("idCard",appBizCard.getIdcardNO());
        if(appBizShop.getIsPublic()==1){
            paramsMap.put("bankAccountType","2");
        }else{
            paramsMap.put("bankAccountType","1");
        }
        paramsMap.put("bankAccountLineNo",appBizCard.getBranchCode());
        paramsMap.put("bankAccountAddress",appBizCard.getBankAddress());
        String districtCode;
        if (appBizShop.getDistrictCode().equals("429021")){
            districtCode = "420325";
        }else {
            districtCode = appBizShop.getDistrictCode();
        }
        HsyMerchantAuditResponse district = hsyMerchantAuditDao.getCode(districtCode);
        paramsMap.put("district",district.getAName());//商户地址区
        HsyMerchantAuditResponse city = hsyMerchantAuditDao.getCode(district.getParentCode());
        paramsMap.put("city",city.getAName());
        if("110000,120000,310000,500000".contains(city.getCode())){
            paramsMap.put("province",city.getAName().replace("市",""));
        }else{
            HsyMerchantAuditResponse province = hsyMerchantAuditDao.getCode(city.getParentCode());
            paramsMap.put("province",province.getAName());
        }
        Optional<UserWithdrawRate> userWithdrawRateOptional = userWithdrawRateService.selectByUserId(userId);
        paramsMap.put("remitD0",userWithdrawRateOptional.get().getWithdrawRateD0().toString());
        paramsMap.put("address",appBizShop.getAddress());
        log.info("收银家修改商户进件参数为："+ JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantBaseInfoModify(), paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("收银家修改商户进件结果为："+jo.toString());
            cmbcResponse.setCode(jo.getInt("code"));
            cmbcResponse.setMsg(jo.getString("msg"));
            cmbcResponse.setResult(jo.getString("result"));
        } else {
            cmbcResponse.setCode(-1);
            cmbcResponse.setMsg("请求超时");
        }
        return cmbcResponse;
    }

    /**
     * 收银家开通产品
     *
     * @param userId
     * @return
     */
    @Override
    public CmbcProductResponse merchantBindChannel(long userId, long shopId) {
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(shopId);
        Optional<UserTradeRate> wxUt = userTradeRateService.selectByUserIdAndPolicyType(userId,EnumPolicyType.WECHAT.getId());
        Optional<UserTradeRate> zfbUt = userTradeRateService.selectByUserIdAndPolicyType(userId,EnumPolicyType.ALIPAY.getId());
        CmbcProductResponse cmbcResponse = new CmbcProductResponse();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", appAuUser.getGlobalID());
        paramsMap.put("wxOnlineRate", wxUt.get().getTradeRateT1().toString());
        paramsMap.put("wxBizCategory", getWxCategory(appBizShop.getIndustryCode()));
        paramsMap.put("zfbOnlineRate", zfbUt.get().getTradeRateT1().toString());
        paramsMap.put("zfbBizCategory",getAlipayCategory(appBizShop.getIndustryCode()));
        log.info("收银家开通产品参数为："+ JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantBindChannel(), paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("收银家开通产品结果为："+jo.toString());
            cmbcResponse.setCode(jo.getInt("code"));
            cmbcResponse.setMsg(jo.getString("msg"));
            if(jo.getInt("code")==1){
                JSONObject jr = jo.getJSONObject("result");
                CmbcProductResponse.ProductResponse productResponse = new CmbcProductResponse.ProductResponse();
                productResponse.setAliRespCode(jr.getString("aliRespCode"));
                productResponse.setWxRespCode(jr.getString("wxRespCode"));
                productResponse.setAliRespMsg(jr.getString("aliRespMsg"));
                productResponse.setWxRespMsg(jr.getString("wxRespMsg"));
                productResponse.setSmId(jr.getString("smId"));
                productResponse.setWxSmId(jr.getString("wxSmId"));
                cmbcResponse.setResult(productResponse);
            }
        } else {
            cmbcResponse.setCode(-1);
            cmbcResponse.setMsg("请求超时");
        }
        return cmbcResponse;
    }

    /**
     * 收银家修改产品
     *
     * @param userId
     * @return
     */
    @Override
    public CmbcResponse merchantUpdateBindChannel(long userId) {
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        Optional<UserTradeRate> wxUt = userTradeRateService.selectByUserIdAndPolicyType(userId,EnumPolicyType.WECHAT.getId());
        Optional<UserTradeRate> zfbUt = userTradeRateService.selectByUserIdAndPolicyType(userId,EnumPolicyType.ALIPAY.getId());
        CmbcResponse cmbcResponse = new CmbcResponse();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", appAuUser.getGlobalID());
        paramsMap.put("wxOnlineRate", wxUt.get().getTradeRateT1().toString());
        paramsMap.put("zfbOnlineRate", zfbUt.get().getTradeRateT1().toString());
        log.info("收银家修改产品参数为："+ JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantUpdateChannel(), paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("收银家修改产品结果为："+jo.toString());
            cmbcResponse.setCode(jo.getInt("code"));
            cmbcResponse.setMsg(jo.getString("msg"));
        } else {
            cmbcResponse.setCode(-1);
            cmbcResponse.setMsg("请求超时");
        }
        return cmbcResponse;
    }

    /**
     * 民生一次性入网
     *
     * @param userId //用户编码
     * @param shopId //主店编码
     */
    @Override
    public XmmsResponse merchantIn(long userId, long shopId) {
        XmmsResponse xmmsResponse = new XmmsResponse();
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(shopId);
        AppBizCard appBizCard = hsyCmbcDao.selectByCardId(shopId);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("upperChannel", EnumUpperChannel.XMMS_BANK.getId()+"");
        paramsMap.put("merchantNo", appAuUser.getGlobalID());
        paramsMap.put("fullName", appBizShop.getName());
        paramsMap.put("shortName", appBizShop.getShortName());
        paramsMap.put("servicePhone","4006226233");
        if(appBizShop.getLicenceNO()==null||"".equals(appBizShop.getLicenceNO())){
            paramsMap.put("businessLicense","");
        }else{
            paramsMap.put("businessLicense",appBizShop.getLicenceNO());
        }
        paramsMap.put("contactName",appAuUser.getRealname());
        paramsMap.put("contactPhone",appAuUser.getCellphone());
        paramsMap.put("contactIdCard",appAuUser.getIdcard());
        paramsMap.put("bankName",appBizCard.getCardBank());
        paramsMap.put("bankAccountNo",appBizCard.getCardNO());
        paramsMap.put("bankAccountName",appBizCard.getCardAccountName());
        paramsMap.put("idCard",appBizCard.getIdcardNO());
        paramsMap.put("bankAccountLineNo",appBizCard.getBranchCode());
        paramsMap.put("bankAccountAddress",appBizCard.getBankAddress());
        String districtCode;
        if (appBizShop.getDistrictCode().equals("429021")){
            districtCode = "420325";
        }else {
            districtCode = appBizShop.getDistrictCode();
        }
        HsyMerchantAuditResponse district = hsyMerchantAuditDao.getCode(districtCode);
        paramsMap.put("district","房县");//商户地址区
        paramsMap.put("districtCode",district.getCode());
        HsyMerchantAuditResponse city = hsyMerchantAuditDao.getCode(district.getParentCode());
        paramsMap.put("city",city.getAName());
        if("110000".equals(city.getCode())){
            paramsMap.put("cityCode","110100");
        }else if("120000".equals(city.getCode())){
            paramsMap.put("cityCode","120100");
        }else if("310000".equals(city.getCode())){
            paramsMap.put("cityCode","310100");
        }else if("500000".equals(city.getCode())){
            paramsMap.put("cityCode","500100");
        }else{
            paramsMap.put("cityCode",city.getCode());
        }
        if("110000,120000,310000,500000".contains(city.getCode())){
            paramsMap.put("province",city.getAName().replace("市",""));
            paramsMap.put("provinceCode",city.getCode());
        }else{
            HsyMerchantAuditResponse province = hsyMerchantAuditDao.getCode(city.getParentCode());
            paramsMap.put("province",province.getAName());
            paramsMap.put("provinceCode",province.getCode());
        }
        paramsMap.put("address",appBizShop.getAddress());
        Optional<UserWithdrawRate> userWithdrawRateOptional = userWithdrawRateService.selectByUserId(userId);
        paramsMap.put("t0drawFee",userWithdrawRateOptional.get().getWithdrawRateD0().toString());
        paramsMap.put("t1drawFee", userWithdrawRateOptional.get().getWithdrawRateT1().toString());

        XmmsResponse.BaseResponse baseResponse701 = getMerchantInResult(paramsMap,userId, EnumPayChannelSign.XMMS_WECHAT_T1.getId(),appBizShop.getIndustryCode());
        xmmsResponse.setWxT1(baseResponse701);
        XmmsResponse.BaseResponse baseResponse702 = getMerchantInResult(paramsMap,userId,EnumPayChannelSign.XMMS_ALIPAY_T1.getId(),appBizShop.getIndustryCode());
        xmmsResponse.setZfbT1(baseResponse702);
//        XmmsResponse.BaseResponse baseResponse703 = getMerchantInResult(paramsMap,userId,EnumPayChannelSign.XMMS_WECHAT_D0.getId(),appBizShop.getIndustryCode());
//        xmmsResponse.setWxD0(baseResponse703);
//        XmmsResponse.BaseResponse baseResponse704 = getMerchantInResult(paramsMap,userId,EnumPayChannelSign.XMMS_ALIPAY_D0.getId(),appBizShop.getIndustryCode());
//        xmmsResponse.setZfbD0(baseResponse704);
        return xmmsResponse;
    }

    /**
     *
     * 修改民生入网信息
     * @param userId //用户编码
     * @param shopId //主店编码
     */
    @Override
    public XmmsResponse.BaseResponse merchantModify(long userId, long shopId,int channelTypeSign) {
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(shopId);
        AppBizCard appBizCard = hsyCmbcDao.selectByCardId(shopId);

        Map<String, String> paramsMap = new HashMap<String, String>();
        //商户信息-MerchantInfo
        paramsMap.put("upperChannel", EnumUpperChannel.XMMS_BANK.getId()+"");//商户编号
        paramsMap.put("merchantNo", appAuUser.getGlobalID());//商户编号
        paramsMap.put("shortName", appBizShop.getShortName());//商户简称
        //联系人信息-contactInfo
        paramsMap.put("contactName",appAuUser.getRealname());//联系人名称
        paramsMap.put("contactIdCard",appAuUser.getIdcard());//身份证号
        //结算卡信息-bankCardInfo
        paramsMap.put("bankName",appBizCard.getCardBank());//银行
        paramsMap.put("bankAccountNo",appBizCard.getCardNO());//银行账号
        paramsMap.put("bankAccountName",appBizCard.getCardAccountName());//开户名称
        paramsMap.put("bankAccountLineNo",appBizCard.getBranchCode());//银行联行号
        //联系人地址信息-addressInfo
        String districtCode;
        if (appBizShop.getDistrictCode().equals("429021")){
            districtCode = "420325";
        }else {
            districtCode = appBizShop.getDistrictCode();
        }
        HsyMerchantAuditResponse district = hsyMerchantAuditDao.getCode(districtCode);
        paramsMap.put("district",district.getAName());//商户地址区
        paramsMap.put("districtCode",district.getCode());
        HsyMerchantAuditResponse city = hsyMerchantAuditDao.getCode(district.getParentCode());
        paramsMap.put("city",city.getAName());//商户地址市
        if("110000".equals(city.getCode())){
            paramsMap.put("cityCode","110100");//市编码
        }else if("120000".equals(city.getCode())){
            paramsMap.put("cityCode","120100");//市编码
        }else if("310000".equals(city.getCode())){
            paramsMap.put("cityCode","310100");//市编码
        }else if("500000".equals(city.getCode())){
            paramsMap.put("cityCode","500100");//市编码
        }else{
            paramsMap.put("cityCode",city.getCode());//市编码
        }
        if("110000,120000,310000,500000".contains(city.getCode())){
            paramsMap.put("province",city.getAName().replace("市",""));//商户地址省
            paramsMap.put("provinceCode",city.getCode());//省份编码
        }else{
            HsyMerchantAuditResponse province = hsyMerchantAuditDao.getCode(city.getParentCode());
            paramsMap.put("province",province.getAName());//商户地址省
            paramsMap.put("provinceCode",province.getCode());//省份编码
        }
        paramsMap.put("address",appBizShop.getAddress());//详细地址
        Optional<UserWithdrawRate> userWithdrawRateOptional = userWithdrawRateService.selectByUserId(userId);
        paramsMap.put("t0drawFee",userWithdrawRateOptional.get().getWithdrawRateD0().toString());
        paramsMap.put("t1drawFee",userWithdrawRateOptional.get().getWithdrawRateT1().toString());

        XmmsResponse.BaseResponse baseResponse = getMerchantModifyResult(paramsMap,userId, channelTypeSign,appBizShop.getIndustryCode());
        return baseResponse;
    }

    /**
     * 厦门民生入网
     *
     * @param userId //用户编码
     * @param shopId //主店编码
     */
    @Override
    public XmmsResponse.BaseResponse merchantIn(long userId, long shopId,int channelTypeSign) {
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        AppBizShop appBizShop = hsyCmbcDao.selectByShopId(shopId);
        AppBizCard appBizCard = hsyCmbcDao.selectByCardId(shopId);

        Map<String, String> paramsMap = new HashMap<String, String>();
        //商户信息-MerchantInfo
        paramsMap.put("upperChannel", EnumUpperChannel.XMMS_BANK.getId()+"");//商户编号
        paramsMap.put("merchantNo", appAuUser.getGlobalID());//商户编号
        paramsMap.put("fullName", appBizShop.getName());//商户全称
        paramsMap.put("shortName", appBizShop.getShortName());//商户简称
        paramsMap.put("servicePhone","4006226233");//客服电话
        if(appBizShop.getLicenceNO()==null||"".equals(appBizShop.getLicenceNO())){
            paramsMap.put("businessLicense","");//证据编号
        }else{
            paramsMap.put("businessLicense",appBizShop.getLicenceNO());//证据编号
        }
        //联系人信息-contactInfo
        paramsMap.put("contactName",appAuUser.getRealname());//联系人名称
        paramsMap.put("contactPhone",appAuUser.getCellphone());//联系人手机号
        paramsMap.put("contactIdCard",appAuUser.getIdcard());//身份证号
        //结算卡信息-bankCardInfo
        paramsMap.put("bankName",appBizCard.getCardBank());//银行联行号
        paramsMap.put("bankAccountNo",appBizCard.getCardNO());//银行账号
        paramsMap.put("bankAccountName",appBizCard.getCardAccountName());//开户名称
        paramsMap.put("idCard",appBizCard.getIdcardNO());//开户身份证号
        paramsMap.put("bankAccountLineNo",appBizCard.getBranchCode());//银行联行号
        paramsMap.put("bankAccountAddress",appBizCard.getBankAddress());//开户行地址
        //联系人地址信息-addressInfo
        String districtCode;
        if (appBizShop.getDistrictCode().equals("429021")){
            districtCode = "420325";
        }else {
            districtCode = appBizShop.getDistrictCode();
        }
        HsyMerchantAuditResponse district = hsyMerchantAuditDao.getCode(districtCode);
        paramsMap.put("district",district.getAName());//商户地址区
        paramsMap.put("districtCode",district.getCode());//商户地址区
        HsyMerchantAuditResponse city = hsyMerchantAuditDao.getCode(district.getParentCode());
        paramsMap.put("city",city.getAName());//商户地址市
        if("110000".equals(city.getCode())){
            paramsMap.put("cityCode","110100");//市编码
        }else if("120000".equals(city.getCode())){
            paramsMap.put("cityCode","120100");//市编码
        }else if("310000".equals(city.getCode())){
            paramsMap.put("cityCode","310100");//市编码
        }else if("500000".equals(city.getCode())){
            paramsMap.put("cityCode","500100");//市编码
        }else{
            paramsMap.put("cityCode",city.getCode());//市编码
        }
        if("110000,120000,310000,500000".contains(city.getCode())){
            paramsMap.put("province",city.getAName().replace("市",""));//商户地址省
            paramsMap.put("provinceCode",city.getCode());//省份编码
        }else{
            HsyMerchantAuditResponse province = hsyMerchantAuditDao.getCode(city.getParentCode());
            paramsMap.put("province",province.getAName());//商户地址省
            paramsMap.put("provinceCode",province.getCode());//省份编码
        }
        paramsMap.put("address",appBizShop.getAddress());//详细地址
        Optional<UserWithdrawRate> userWithdrawRateOptional = userWithdrawRateService.selectByUserId(userId);
        paramsMap.put("t0drawFee",userWithdrawRateOptional.get().getWithdrawRateD0().toString());
        paramsMap.put("t1drawFee",userWithdrawRateOptional.get().getWithdrawRateT1().toString());
        XmmsResponse.BaseResponse baseResponse = getMerchantInResult(paramsMap,userId, channelTypeSign,appBizShop.getIndustryCode());
        return baseResponse;
    }

    private XmmsResponse.BaseResponse getMerchantInResult(Map<String, String> paramsMap,long userId,int channelTypeSign,String industryCode){
        XmmsResponse.BaseResponse baseResponse = new XmmsResponse.BaseResponse();
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.putAll(paramsMap);
        if(channelTypeSign==EnumPayChannelSign.XMMS_WECHAT_T1.getId()){
            Optional<UserTradeRate> userTradeRateOptional =  userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.WECHAT.getId());
            resultMap.put("t0tradeRate",userTradeRateOptional.get().getTradeRateD0().toString());
            resultMap.put("t1tradeRate",userTradeRateOptional.get().getTradeRateT1().toString());
            resultMap.put("category",getMsWxCategory(industryCode));
            resultMap.put("settleType","T1");
            resultMap.put("payWay","WXZF");
            log.info("民生微信T1入网参数{}",JSONObject.fromObject(resultMap).toString());
        }
        if(channelTypeSign==EnumPayChannelSign.XMMS_ALIPAY_T1.getId()){
            Optional<UserTradeRate> userTradeRateOptional =  userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.ALIPAY.getId());
            resultMap.put("t0tradeRate",userTradeRateOptional.get().getTradeRateD0().toString());
            resultMap.put("t1tradeRate",userTradeRateOptional.get().getTradeRateT1().toString());
            resultMap.put("category",getAlipayCategory(industryCode));
            resultMap.put("settleType","T1");
            resultMap.put("payWay","ZFBZF");
            log.info("民生支付宝T1入网参数{}",JSONObject.fromObject(resultMap).toString());
        }
        if(channelTypeSign==EnumPayChannelSign.XMMS_WECHAT_D0.getId()){
            Optional<UserTradeRate> userTradeRateOptional =  userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.WECHAT.getId());
            resultMap.put("t0tradeRate",userTradeRateOptional.get().getTradeRateD0().toString());
            resultMap.put("t1tradeRate",userTradeRateOptional.get().getTradeRateT1().toString());
            resultMap.put("category",getMsWxCategory(industryCode));
            resultMap.put("settleType","D0");
            resultMap.put("payWay","WXZF");
            log.info("民生微信D0入网参数{}",JSONObject.fromObject(resultMap).toString());
        }
        if(channelTypeSign==EnumPayChannelSign.XMMS_ALIPAY_D0.getId()){
            Optional<UserTradeRate> userTradeRateOptional =  userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.ALIPAY.getId());
            resultMap.put("t0tradeRate",userTradeRateOptional.get().getTradeRateD0().toString());
            resultMap.put("t1tradeRate",userTradeRateOptional.get().getTradeRateT1().toString());
            resultMap.put("category",getAlipayCategory(industryCode));
            resultMap.put("settleType","D0");
            resultMap.put("payWay","ZFBZF");
            log.info("民生支付宝D0入网参数{}",JSONObject.fromObject(resultMap).toString());
        }
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantXmmsIn(), resultMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("民生入网返回结果为："+jo.toString());
            baseResponse.setCode(jo.getInt("code"));
            baseResponse.setMsg(jo.getString("msg"));
            if(jo.getInt("code")==1){
                XmmsResponse.Result rs = new XmmsResponse.Result();
                JSONObject jn = jo.getJSONObject("result");
                rs.setMerchantNo(jn.getString("merchantNo"));
                rs.setMsg(jn.getString("msg"));
                rs.setSmId(jn.getString("smId"));
                rs.setStatus(jn.getString("status"));
                baseResponse.setResult(rs);
            }
        } else {//超时
            baseResponse.setCode(-1);
            baseResponse.setMsg("请求超时");
        }
        return baseResponse;

    }

    /**
     * 修改民生入网信息
     * @param paramsMap
     * @param userId
     * @param channelTypeSign
     * @param industryCode
     * @return
     */
    private XmmsResponse.BaseResponse getMerchantModifyResult(Map<String, String> paramsMap,long userId,int channelTypeSign,String industryCode){
        XmmsResponse.BaseResponse baseResponse = new XmmsResponse.BaseResponse();
        Map<String, String> resultMap = new HashMap<String, String>();
        resultMap.putAll(paramsMap);
        if(channelTypeSign==EnumPayChannelSign.XMMS_WECHAT_T1.getId()){
            Optional<UserTradeRate> userTradeRateOptional =  userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.WECHAT.getId());
            resultMap.put("t0tradeRate",userTradeRateOptional.get().getTradeRateD0().toString());
            resultMap.put("t1tradeRate",userTradeRateOptional.get().getTradeRateT1().toString());
            resultMap.put("category",getMsWxCategory(industryCode));
            resultMap.put("settleType","T1");
            resultMap.put("payWay","WXZF");
        }
        if(channelTypeSign==EnumPayChannelSign.XMMS_ALIPAY_T1.getId()){
            Optional<UserTradeRate> userTradeRateOptional =  userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.ALIPAY.getId());
            resultMap.put("t0tradeRate",userTradeRateOptional.get().getTradeRateD0().toString());
            resultMap.put("t1tradeRate",userTradeRateOptional.get().getTradeRateT1().toString());
            resultMap.put("category",getAlipayCategory(industryCode));
            resultMap.put("settleType","T1");
            resultMap.put("payWay","ZFBZF");
        }
        if(channelTypeSign==EnumPayChannelSign.XMMS_WECHAT_D0.getId()){
            Optional<UserTradeRate> userTradeRateOptional =  userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.WECHAT.getId());
            resultMap.put("t0tradeRate",userTradeRateOptional.get().getTradeRateD0().toString());
            resultMap.put("t1tradeRate",userTradeRateOptional.get().getTradeRateT1().toString());
            resultMap.put("category",getMsWxCategory(industryCode));
            resultMap.put("settleType","D0");
            resultMap.put("payWay","WXZF");
        }
        if(channelTypeSign==EnumPayChannelSign.XMMS_ALIPAY_D0.getId()){
            Optional<UserTradeRate> userTradeRateOptional =  userTradeRateService.selectByUserIdAndPolicyType(userId, EnumPolicyType.ALIPAY.getId());
            resultMap.put("t0tradeRate",userTradeRateOptional.get().getTradeRateD0().toString());
            resultMap.put("t1tradeRate",userTradeRateOptional.get().getTradeRateT1().toString());
            resultMap.put("category",getAlipayCategory(industryCode));
            resultMap.put("settleType","D0");
            resultMap.put("payWay","ZFBZF");
        }
        log.info("修改民生入网参数{}",JSONObject.fromObject(resultMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantXmmsModify(), resultMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("修改民生入网返回结果为："+jo.toString());
            baseResponse.setCode(jo.getInt("code"));
            baseResponse.setMsg(jo.getString("msg"));
            if(jo.getInt("code")==1){
                XmmsResponse.Result rs = new XmmsResponse.Result();
                JSONObject jn = jo.getJSONObject("result");
                rs.setMerchantNo(jn.getString("merchantNo"));
                rs.setMsg(jn.getString("msg"));
                rs.setSmId(jn.getString("smId"));
                rs.setStatus(jn.getString("status"));
                baseResponse.setResult(rs);
            }
        } else {//超时
            baseResponse.setCode(-1);
            baseResponse.setMsg("请求超时");
        }
        return baseResponse;

    }
    /**
     * 支付宝经营类目
     * @param industryCode
     * @return
     */
    private String getAlipayCategory(String industryCode){
        int industry = Integer.parseInt(industryCode);
        String category = "";
        if(industry==1000){
            category="2015050700000000";
        }else if(industry==1001){
            category="2015091000052157";
        }else if(industry==1002){
            category="2015063000020189";
        }else if(industry==1003){
            category="2015062600002758";
        }else if(industry==1004){
            category="2015063000020189";
        }else if(industry==1005){
            category="2015062600004525";
        }else if(industry==1006){
            category="2015062600004525";
        }
        return category;
    }

    /**
     * 微信经营类目
     * @param industryCode
     * @return
     */
    private String getWxCategory(String industryCode){
        int industry = Integer.parseInt(industryCode);
        String category = "";
        if(industry==1000){
            category="90";
        }else if(industry==1001){
            category="205";
        }else if(industry==1002){
            category="311";
        }else if(industry==1003){
            category="207";
        }else if(industry==1004){
            category="294";
        }else if(industry==1005){
            category="54";
        }else if(industry==1006){
            category="280";
        }
        return category;
    }
    /**
     * 民生微信经营类目
     * @param industryCode
     * @return
     */
    private String getMsWxCategory(String industryCode){
        int industry = Integer.parseInt(industryCode);
        String category = "";
        if(industry==1000){
            category="153";
        }else if(industry==1001){
            category="205";
        }else if(industry==1002){
            category="311";
        }else if(industry==1003){
            category="207";
        }else if(industry==1004){
            category="294";
        }else if(industry==1005){
            category="54";
        }else if(industry==1006){
            category="280";
        }
        return category;
    }
}
