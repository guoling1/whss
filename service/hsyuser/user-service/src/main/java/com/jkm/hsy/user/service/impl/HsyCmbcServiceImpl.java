package com.jkm.hsy.user.service.impl;

import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hsy.user.dao.HsyCmbcDao;
import com.jkm.hsy.user.dao.HsyMerchantAuditDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizCard;
import com.jkm.hsy.user.entity.AppBizShop;
import com.jkm.hsy.user.entity.HsyMerchantAuditResponse;
import com.jkm.hsy.user.help.requestparam.CmbcResponse;
import com.jkm.hsy.user.service.HsyCmbcService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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

    /**
     * 民生银行商户基础信息注册
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
        //商户信息-MerchantInfo
        paramsMap.put("merchantNo", appAuUser.getGlobalID());//商户编号
        paramsMap.put("fullName", appAuUser.getShopName());//商户全称
        paramsMap.put("shortName", appAuUser.getShopShortName());//商户简称
        paramsMap.put("servicePhone","4006226233");//客服电话
        paramsMap.put("businessLicense",appBizShop.getLicenceNO());//证据编号
        paramsMap.put("businessLicenseType","NATIONAL_LEGAL");//证件类型

        //联系人信息-contactInfo
        paramsMap.put("contactName",appAuUser.getRealname());//联系人名称
        paramsMap.put("contactPhone",appAuUser.getCellphone());//联系人手机号
        paramsMap.put("contactPersonType","LEGAL_PERSON");//联系人类型
        paramsMap.put("contactIdCard",appAuUser.getIdcard());//身份证号
        paramsMap.put("contactEmail","");//邮箱
        //结算卡信息-bankCardInfo
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
        //联系人地址信息-addressInfo
        HsyMerchantAuditResponse district = hsyMerchantAuditDao.getCode(appBizShop.getDistrictCode());
        paramsMap.put("district",district.getAName());//商户地址区
        HsyMerchantAuditResponse city = hsyMerchantAuditDao.getCode(district.getParentCode());
        paramsMap.put("city",city.getAName());//商户地址市
        if("110000,120000,310000,500000".contains(city.getCode())){
            paramsMap.put("province",city.getAName());//商户地址省
        }else{
            HsyMerchantAuditResponse province = hsyMerchantAuditDao.getCode(city.getParentCode());
            paramsMap.put("province",province.getAName());//商户地址省
        }
        paramsMap.put("address",appBizShop.getAddress());//详细地址
        log.info("民生银行商户基础信息注册参数为："+ JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantBaseInfoReg(), paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("民生银行商户基础信息注册返回结果为："+jo.toString());
            cmbcResponse.setCode(jo.getInt("code"));
            cmbcResponse.setMsg(jo.getString("msg"));
        } else {//超时
            cmbcResponse.setCode(-1);
            cmbcResponse.setMsg("进件超时");
        }
        return cmbcResponse;
    }

    /**
     * 民生银行商户支付通道绑定
     *
     * @param userId
     * @return
     */
    @Override
    public CmbcResponse merchantBindChannel(long userId) {
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        CmbcResponse cmbcResponse = new CmbcResponse();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", appAuUser.getGlobalID());
        paramsMap.put("wxOnlineRate", appAuUser.getWeixinRate().toString());
        paramsMap.put("wxBizCategory", getWxCategory(appAuUser.getIndustryCode()));
        paramsMap.put("zfbOnlineRate", appAuUser.getAlipayRate().toString());
        paramsMap.put("zfbBizCategory",getAlipayCategory(appAuUser.getIndustryCode()));
        log.info("民生银行商户支付通道绑定参数为："+ JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantBaseInfoReg(), paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("民生银行商户支付通道绑定返回结果为："+jo.toString());
            cmbcResponse.setCode(jo.getInt("code"));
            cmbcResponse.setMsg(jo.getString("msg"));
        } else {//超时
            cmbcResponse.setCode(-1);
            cmbcResponse.setMsg("商户支付通道绑定请求超时");
        }
        return cmbcResponse;
    }

    /**
     * 民生银行商户支付修改通道绑定
     *
     * @param userId
     * @return
     */
    @Override
    public CmbcResponse merchantUpdateBindChannel(long userId) {
        AppAuUser appAuUser = hsyCmbcDao.selectByUserId(userId);
        CmbcResponse cmbcResponse = new CmbcResponse();
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", appAuUser.getGlobalID());
        paramsMap.put("wxOnlineRate", appAuUser.getWeixinRate().toString());
        paramsMap.put("zfbOnlineRate", appAuUser.getAlipayRate().toString());
        log.info("民生银行商户支付修改通道绑定参数为："+ JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantUpdateChannel(), paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("民生银行商户支付通道绑定返回结果为："+jo.toString());
            cmbcResponse.setCode(jo.getInt("code"));
            cmbcResponse.setMsg(jo.getString("msg"));
        } else {//超时
            cmbcResponse.setCode(-1);
            cmbcResponse.setMsg("商户支付通道绑定请求超时");
        }
        return cmbcResponse;
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
}
