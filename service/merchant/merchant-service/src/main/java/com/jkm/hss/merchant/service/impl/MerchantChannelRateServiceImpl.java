package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.merchant.dao.MerchantChannelRateDao;
import com.jkm.hss.merchant.dao.MerchantInfoDao;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumCheck;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.helper.request.MerchantEnterInRequest;
import com.jkm.hss.merchant.helper.request.MerchantGetRateRequest;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

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
        return this.merchantChannelRateDao.selectByMerchantId(merchantId);
    }

    /**
     * 根据商户编码、通道标示、产品编码查询商户费用
     *
     * @param merchantGetRateRequest
     * @return
     */
    @Override
    public Optional<MerchantChannelRate> selectByThirdCompanyAndProductIdAndMerchantId(MerchantGetRateRequest merchantGetRateRequest) {
        return Optional.fromNullable(this.merchantChannelRateDao.selectByThirdCompanyAndProductIdAndMerchantId(merchantGetRateRequest));
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
                if(merchantInfo!=null){
                    if(weixinMerchantPayRate!=null&&zhifubaoMerchantPayRate!=null){
                        Map<String, String> paramsMap = new HashMap<String, String>();
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
                        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantIN(), paramsMap);
                        if (result != null && !"".equals(result)) {
                            JSONObject jo = JSONObject.fromObject(result);
                            if (jo.getInt("code") == 1) {
                                merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENTING.getId(),merchantId);
                            } else {
                                merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENT_FAIL.getId(),merchantId);
                            }
                        } else {
                            merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENT_FAIL.getId(),merchantId);
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
}
