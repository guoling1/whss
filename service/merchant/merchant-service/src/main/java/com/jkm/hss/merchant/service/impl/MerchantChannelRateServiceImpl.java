package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.MerchantChannelRateDao;
import com.jkm.hss.merchant.dao.MerchantInfoDao;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.enums.EnumKmNetStatus;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.helper.request.MerchantEnterInRequest;
import com.jkm.hss.merchant.helper.request.MerchantGetRateRequest;
import com.jkm.hss.merchant.helper.request.MerchantUpgradeRequest;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingliujie on 2017/2/27.
 */
@Slf4j
@Service
public class MerchantChannelRateServiceImpl implements MerchantChannelRateService {
    @Autowired
    private MerchantChannelRateDao merchantChannelRateDao;
    @Autowired
    private MerchantInfoDao merchantInfoDao;
    @Autowired
    private AccountBankService accountBankService;
    /**
     * 费率初始化
     *
     * @param merchantChannelRate
     * @return
     */
    @Override
    public int initMerchantChannelRate(MerchantChannelRate merchantChannelRate) {
        return merchantChannelRateDao.initMerchantChannelRate(merchantChannelRate);
    }

    /**
     * 根据商户编码、通道标示、产品编码查询商户费用
     *
     * @param merchantChannelRateRequest
     * @return
     */
    @Override
    public Optional<MerchantChannelRate> selectByChannelTypeSignAndProductIdAndMerchantId(MerchantChannelRateRequest merchantChannelRateRequest) {
        return Optional.fromNullable(merchantChannelRateDao.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest));
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public List<Long> selectIngMerchantInfo() {
        return this.merchantChannelRateDao.selectIngMerchantInfo(EnumEnterNet.ENTING.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @param merchantId
     * @param enumEnterNet
     */
    @Override
    public void updateEnterNetStatus(long merchantId, EnumEnterNet enumEnterNet, String msg) {
            this.merchantChannelRateDao.updateEnterNetStatus(merchantId, enumEnterNet.getId(), msg);

    }


    /**
     * {@inheritDoc}
     * @param merchantId
     * @return
     */
    @Override
    public List<MerchantChannelRate> selectByMerchantId(long merchantId) {
        List<MerchantChannelRate> list = this.merchantChannelRateDao.selectByMerchantId(merchantId);
//        String dat = "2017-04-21 23:59:59";
//        String dat1 = "2017-06-01 00:00:00";
//        Date date = new Date();
//        Date date1 = new Date();
//        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        try {
//            date = sdf.parse(dat);
//            date1 = sdf.parse(dat1);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        if (list.size()>0) {
//            for (int i = 0; i < list.size(); i++) {
//                if (list.get(i).getCreateTime().getTime() > date.getTime() && list.get(i).getCreateTime().getTime() < date1.getTime() && list.get(i).getChannelTypeSign() == 601) {
//                    list.get(i).setMerchantPayRate(new BigDecimal(0.0038));
//                    list.get(i).setMerchantWithdrawFee(new BigDecimal(3));
//                }
//            }
//        }
        return list;
    }

    /**
     * 根据商户编码、通道标示、产品编码查询商户费用
     *
     * @param merchantGetRateRequest
     * @return
     */
    @Override
    public List<MerchantChannelRate> selectByThirdCompanyAndProductIdAndMerchantId(MerchantGetRateRequest merchantGetRateRequest) {
        return this.merchantChannelRateDao.selectByThirdCompanyAndProductIdAndMerchantId(merchantGetRateRequest);
    }

    /**
     * 根据商户编码、通道标示、产品编码查询商户费用
     *
     * @param merchantEnterInRequest
     * @return
     */
    @Override
    public List<MerchantChannelRate> selectByChannelCompanyAndProductIdAndMerchantId(MerchantEnterInRequest merchantEnterInRequest) {
        return this.merchantChannelRateDao.selectByChannelCompanyAndProductIdAndMerchantId(merchantEnterInRequest);
    }
    /**
     * 商户入网
     */
    @Override
    public void enterInterNet(long productId,long merchantId,String channelCompany) {
        MerchantEnterInRequest merchantEnterInRequest = new MerchantEnterInRequest();
        merchantEnterInRequest.setProductId(productId);
        merchantEnterInRequest.setMerchantId(merchantId);
        merchantEnterInRequest.setChannelCompany(channelCompany);
        List<MerchantChannelRate> list = this.merchantChannelRateDao.selectByChannelCompanyAndProductIdAndMerchantId(merchantEnterInRequest);
        if(list.size()>0){
            BigDecimal weixinMerchantPayRate=null;
            BigDecimal zhifubaoMerchantPayRate=null;
            int isNet = list.get(0).getEnterNet();
            List<Integer> signIdList = new ArrayList<Integer>();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getEnterNet()==EnumEnterNet.UNENT.getId()||list.get(i).getEnterNet()==EnumEnterNet.ENT_FAIL.getId()){
                    if("微信".equals(list.get(i).getThirdCompany())){
                        weixinMerchantPayRate = list.get(i).getMerchantPayRate();
                        signIdList.add(list.get(i).getChannelTypeSign());
                    }
                    if("支付宝".equals(list.get(i).getThirdCompany())){
                        zhifubaoMerchantPayRate = list.get(i).getMerchantPayRate();
                        signIdList.add(list.get(i).getChannelTypeSign());
                    }
                }
            }
            if(weixinMerchantPayRate!=null&&zhifubaoMerchantPayRate!=null){
                MerchantInfo merchantInfo = merchantInfoDao.selectById(merchantId);
                if(merchantInfo!=null&&isNet==1){
                    if(weixinMerchantPayRate!=null&&zhifubaoMerchantPayRate!=null){
                        Map<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("phone", MerchantSupport.decryptMobile(merchantId,merchantInfo.getReserveMobile()));
                        paramsMap.put("merchantName", merchantInfo.getMerchantName());
                        paramsMap.put("merchantNo", merchantInfo.getMarkCode());
                        paramsMap.put("address", merchantInfo.getAddress());
                        paramsMap.put("personName", merchantInfo.getName());
                        paramsMap.put("idCard", merchantInfo.getIdentity());
                        paramsMap.put("bankNo", merchantInfo.getBankNo());
                        paramsMap.put("wxRate", weixinMerchantPayRate.toString());
                        paramsMap.put("zfbRate", zhifubaoMerchantPayRate.toString());
                        paramsMap.put("bankName", merchantInfo.getBankName());
                        paramsMap.put("prov", merchantInfo.getProvinceName());
                        paramsMap.put("city", merchantInfo.getCityName());     //后台通知url
                        paramsMap.put("country", merchantInfo.getCountyName());
                        paramsMap.put("bankBranch", merchantInfo.getBranchName());
                        paramsMap.put("bankCode", merchantInfo.getBranchCode());
                        paramsMap.put("creditCardNo", merchantInfo.getCreditCard());
                        log.info("入网参数为："+JSONObject.fromObject(paramsMap).toString());
                        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantIN(), paramsMap);
                        if (result != null && !"".equals(result)) {
                            JSONObject jo = JSONObject.fromObject(result);
                            log.info("入网返回参数为："+jo.toString());
                            if (jo.getInt("code") == 1) {
                                merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENTING.getId(),merchantId,jo.getString("msg"));
                            } else {
                                merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENT_FAIL.getId(),merchantId,jo.getString("msg"));
                            }
                        } else {
                            merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENT_FAIL.getId(),merchantId,"入网超时");
                        }
                    }else{
                        log.info("商户已入网");
                    }
                }else{
                    log.info("商户{}不存在",merchantId);
                }
            }else{
                log.info("微信费率为空，两种情况，一种是商户已入网或不需入网，二种是商户信息有误，查询不到微信或支付宝的费率");
            }


        }
    }

    /**
     * 升级降费率
     *
     * @param merchantUpgradeRequest
     */
    @Override
    public void toUpgrade(MerchantUpgradeRequest merchantUpgradeRequest) {
            merchantChannelRateDao.toUpgrade(merchantUpgradeRequest);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<Long> selectFailMerchantInfo() {
        return this.merchantChannelRateDao.selectFailMerchantInfo(EnumEnterNet.ENT_FAIL.getId());
    }

    /**
     * 商户入网
     *
     * @param productId
     * @param merchantId
     * @param channelCompany
     */
    @Override
    public JSONObject enterInterNet1(long accountId,long productId, long merchantId, String channelCompany) {
        JSONObject resultJo = new JSONObject();
        MerchantEnterInRequest merchantEnterInRequest = new MerchantEnterInRequest();
        merchantEnterInRequest.setProductId(productId);
        merchantEnterInRequest.setMerchantId(merchantId);
        merchantEnterInRequest.setChannelCompany(channelCompany);
        List<MerchantChannelRate> list = this.merchantChannelRateDao.selectByChannelCompanyAndProductIdAndMerchantId(merchantEnterInRequest);
        if(list.size()>0){
            BigDecimal weixinMerchantPayRate=null;
            BigDecimal zhifubaoMerchantPayRate=null;
            int isNet = list.get(0).getEnterNet();
            log.info("isNet={}",isNet);
            List<Integer> signIdList = new ArrayList<Integer>();
            for(int i=0;i<list.size();i++){
                if(list.get(i).getEnterNet()==EnumEnterNet.UNENT.getId()||list.get(i).getEnterNet()==EnumEnterNet.ENT_FAIL.getId()){
                    if("微信".equals(list.get(i).getThirdCompany())){
                        weixinMerchantPayRate = list.get(i).getMerchantPayRate();
                        signIdList.add(list.get(i).getChannelTypeSign());
                    }
                    if("支付宝".equals(list.get(i).getThirdCompany())){
                        zhifubaoMerchantPayRate = list.get(i).getMerchantPayRate();
                        signIdList.add(list.get(i).getChannelTypeSign());
                    }
                    signIdList.add(list.get(i).getChannelTypeSign());
                }
            }
            if(weixinMerchantPayRate!=null&&zhifubaoMerchantPayRate!=null){
                MerchantInfo merchantInfo = merchantInfoDao.selectById(merchantId);
                if(merchantInfo!=null&&isNet==1){
                    if(weixinMerchantPayRate!=null&&zhifubaoMerchantPayRate!=null){
                        AccountBank accountBank = accountBankService.getDefault(accountId);
                        Map<String, String> paramsMap = new HashMap<String, String>();
                        paramsMap.put("phone", accountBank.getReserveMobile());
                        paramsMap.put("merchantName", merchantInfo.getMerchantName());
                        paramsMap.put("merchantNo", merchantInfo.getMarkCode());
                        paramsMap.put("address", merchantInfo.getAddress());
                        paramsMap.put("personName", merchantInfo.getName());
                        paramsMap.put("idCard", merchantInfo.getIdentity());
                        paramsMap.put("bankNo", MerchantSupport.encryptBankCard(accountBank.getBankNo()));
                        paramsMap.put("wxRate", weixinMerchantPayRate.toString());
                        paramsMap.put("zfbRate", zhifubaoMerchantPayRate.toString());
                        paramsMap.put("bankName", merchantInfo.getBankName());
                        paramsMap.put("prov", accountBank.getBranchProvinceName());
                        paramsMap.put("city", accountBank.getBranchCityName());
                        paramsMap.put("country", accountBank.getBranchCountyName());
                        paramsMap.put("bankBranch", accountBank.getBranchName());
                        paramsMap.put("bankCode", accountBank.getBranchCode());
                        paramsMap.put("creditCardNo", merchantInfo.getCreditCard());
                        paramsMap.put("provinceCode", accountBank.getBranchProvinceCode());
                        paramsMap.put("cityCode", accountBank.getBranchCityCode());
                        paramsMap.put("districtCode", accountBank.getBranchCountyCode());
                        log.info("入网参数为："+JSONObject.fromObject(paramsMap).toString());
                        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantIN(), paramsMap);
                        if (result != null && !"".equals(result)) {
                            JSONObject jo = JSONObject.fromObject(result);
                            log.info("入网返回参数为："+jo.toString());
                            if (jo.getInt("code") == 1) {
                                merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.HASENT.getId(),merchantId,jo.getString("msg"));
                                resultJo.put("code",1);
                                resultJo.put("msg","入网成功");
                            } else {
                                merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENT_FAIL.getId(),merchantId,jo.getString("msg"));
                                resultJo.put("code",-1);
                                resultJo.put("msg",jo.getString("msg"));
                            }
                        } else {
                            merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENT_FAIL.getId(),merchantId,"入网超时");
                            resultJo.put("code",-1);
                            resultJo.put("msg","网络超时，请重试");
                        }
                    }else{
                        log.info("微信或支付宝费率不存在，商户已入网");
                        resultJo.put("code",-1);
                        resultJo.put("msg","商户已入网");
                    }
                }else{
                    log.info("商户{}不存在",merchantId);
                    resultJo.put("code",-1);
                    resultJo.put("msg","商户不存在");
                }
            }else{
                log.info("微信费率为空，两种情况，一种是商户已入网或不需入网，二种是商户信息有误，查询不到微信或支付宝的费率");
                resultJo.put("code",-1);
                resultJo.put("msg","商户通道信息有误或已入网");
            }
        }else{
            log.info("查询不出商户费率{}",channelCompany);
            resultJo.put("code",-1);
            resultJo.put("msg","商户通道信息有误");
        }
        return resultJo;
    }

    /**
     * 修改商户入网
     *
     * @param accountId
     * @param merchantId
     */
    @Override
    public void updateInterNet(long accountId,long merchantId) {
        AccountBank accountBank = accountBankService.getDefault(accountId);
        MerchantInfo merchantInfo = merchantInfoDao.selectById(merchantId);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", merchantInfo.getMarkCode());
        paramsMap.put("bankNo", accountBank.getBankNo());
        paramsMap.put("oriBankNo", accountBank.getBankNo());
        paramsMap.put("bankBranch", accountBank.getBranchName());
        paramsMap.put("bankCode", accountBank.getBranchCode());
        paramsMap.put("bankName", accountBank.getBankName());
        paramsMap.put("prov", accountBank.getBranchProvinceName());
        paramsMap.put("city", accountBank.getBranchCityName());
        log.info("修改商户入网参数为："+JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantUpdate(), paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("修改商户入网返回参数为："+jo.toString());
            if (jo.getInt("code") == 1) {
                merchantInfoDao.updateKmNetStatus(merchantId, EnumKmNetStatus.SUCCESS.getId());
            } else {
                merchantInfoDao.updateKmNetStatus(merchantId,EnumKmNetStatus.FAIL.getId());
            }
        } else {
            merchantInfoDao.updateKmNetStatus(merchantId,EnumKmNetStatus.FAIL.getId());
        }
    }

    /**
     * 修改银行卡信息
     *
     * @param accountId
     * @param merchantId
     */
    @Override
    public JSONObject updateKmBankInfo(long accountId,long merchantId,String bankNo) {
        AccountBank accountBank = accountBankService.getDefault(accountId);
        MerchantInfo merchantInfo = merchantInfoDao.selectById(merchantId);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", merchantInfo.getMarkCode());
        paramsMap.put("bankNo", bankNo);
        paramsMap.put("oriBankNo", accountBank.getBankNo());
        paramsMap.put("bankBranch", accountBank.getBranchName());
        paramsMap.put("bankCode", accountBank.getBranchCode());
        paramsMap.put("bankName", accountBank.getBankName());
        paramsMap.put("prov", accountBank.getBranchProvinceName());
        paramsMap.put("city", accountBank.getBranchCityName());
        log.info("修改银行卡信息参数为："+JSONObject.fromObject(paramsMap).toString());
        JSONObject jo = new JSONObject();
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantUpdate(), paramsMap);
        if (result != null && !"".equals(result)) {
            jo = JSONObject.fromObject(result);
            log.info("修改卡盟联行号返回参数为："+jo.toString());
        }
        return jo;
    }

    /**
     * 修改卡盟联行号
     *
     * @param accountId
     * @param merchantId
     */
    @Override
    public void updateKmBranchInfo(long accountId,long merchantId) {
        AccountBank accountBank = accountBankService.getDefault(accountId);
        MerchantInfo merchantInfo = merchantInfoDao.selectById(merchantId);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", merchantInfo.getMarkCode());
        paramsMap.put("bankNo", accountBank.getBankNo());
        paramsMap.put("oriBankNo", accountBank.getBankNo());
        paramsMap.put("bankBranch", accountBank.getBranchName());
        paramsMap.put("bankCode", accountBank.getBranchCode());
        paramsMap.put("bankName", accountBank.getBankName());
        paramsMap.put("prov", accountBank.getBranchProvinceName());
        paramsMap.put("city", accountBank.getBranchCityName());
        log.info("修改卡盟联行号参数为："+JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantUpdate(), paramsMap);
        if (result != null && !"".equals(result)) {
            JSONObject jo = JSONObject.fromObject(result);
            log.info("修改卡盟联行号返回参数为："+jo.toString());
            if (jo.getInt("code") == 1) {
                merchantInfoDao.updateKmNetStatus(merchantId, EnumKmNetStatus.SUCCESS.getId());
            } else {
                merchantInfoDao.updateKmNetStatus(merchantId,EnumKmNetStatus.FAIL.getId());
            }
        } else {
            merchantInfoDao.updateKmNetStatus(merchantId,EnumKmNetStatus.FAIL.getId());
        }
    }

    /**
     *
     *
     * @param accountId
     * @param merchantId
     */
    @Override
    public JSONObject updateKmMerchantRateInfo(long accountId, long merchantId, long productId, int channelTypeSign) {
        AccountBank accountBank = accountBankService.getDefault(accountId);
        MerchantInfo merchantInfo = merchantInfoDao.selectById(merchantId);
        final MerchantChannelRateRequest merchantChannelRateRequest = new MerchantChannelRateRequest();
        merchantChannelRateRequest.setMerchantId(merchantId);
        merchantChannelRateRequest.setProductId(productId);
        merchantChannelRateRequest.setChannelTypeSign(channelTypeSign);
        final MerchantChannelRate merchantChannelRate = this.merchantChannelRateDao.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest);
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("merchantNo", merchantInfo.getMarkCode());
        paramsMap.put("wxRate", merchantChannelRate.getMerchantPayRate().toString());
        paramsMap.put("zfbRate", merchantChannelRate.getMerchantPayRate().toString());
        log.info("修改卡盟联商户费率参数为："+JSONObject.fromObject(paramsMap).toString());
        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantUpdateRate(), paramsMap);
        JSONObject jo = JSONObject.fromObject(result);
        if (result != null && !"".equals(result)) {

            log.info("修改卡盟商户费率返回参数为："+jo.toString());
            if (jo.getInt("code") == 1) {
                merchantChannelRateDao.updateKmRate(merchantId, "已同步");
            } else {
                merchantChannelRateDao.updateKmRate(merchantId,"同步失败");
            }
        } else {
            merchantChannelRateDao.updateKmRate(merchantId,"同步失败");
        }
        return jo;
    }
}
