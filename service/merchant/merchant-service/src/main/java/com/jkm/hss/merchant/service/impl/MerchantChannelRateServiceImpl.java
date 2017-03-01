package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.merchant.dao.MerchantChannelRateDao;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumCheck;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.helper.request.MerchantGetRateRequest;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by xingliujie on 2017/2/27.
 */
@Slf4j
@Service
public class MerchantChannelRateServiceImpl implements MerchantChannelRateService {
    @Autowired
    private MerchantChannelRateDao merchantChannelRateDao;

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
     * 更新商户入网状态
     *
     * @param signIdList
     */
    @Override
    public int batchCheck(List<Integer> signIdList) {
        return merchantChannelRateDao.batchCheck(signIdList);
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

//    @Override
//    public Pair<Integer,String> enterInterNet(MerchantInfo merchantInfo) {
//        merchantChannelRateDao.selectByChannelTypeSignAndProductIdAndMerchantId();
//        Pair<Integer,Integer> payChannelSign = EnumPayChannelSign.getPayChannelSign(checkMerchantInfoRequest.getChannelTypeSign());
//
//        MerchantChannelRateRequest merchantChannelRateRequest1 = new MerchantChannelRateRequest();
//        merchantChannelRateRequest1.setChannelTypeSign(payChannelSign.getLeft());
//        merchantChannelRateRequest1.setMerchantId(merchantInfo.get().getId());
//        merchantChannelRateRequest1.setProductId(merchantInfo.get().getProductId());
//        Optional<MerchantChannelRate> weixinMerchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest1);
//        if(!weixinMerchantChannelRateOptional.isPresent()){
//            return CommonResponse.simpleResponse(-1, "微信通道"+payChannelSign.getLeft()+"不存在");
//        }
//
//        MerchantChannelRateRequest merchantChannelRateRequest2 = new MerchantChannelRateRequest();
//        merchantChannelRateRequest2.setChannelTypeSign(payChannelSign.getRight());
//        merchantChannelRateRequest2.setMerchantId(merchantInfo.get().getId());
//        merchantChannelRateRequest2.setProductId(merchantInfo.get().getProductId());
//        Optional<MerchantChannelRate> zhifubaoMerchantChannelRateOptional = merchantChannelRateService.selectByChannelTypeSignAndProductIdAndMerchantId(merchantChannelRateRequest2);
//        if(!zhifubaoMerchantChannelRateOptional.isPresent()){
//            return CommonResponse.simpleResponse(-1, "微信通道"+payChannelSign.getRight()+"不存在");
//        }
//
//        merchantChannelRateOptional.get().getChannelTypeSign();
//        Map<String, String> paramsMap = new HashMap<String, String>();
//        paramsMap.put("merchantName", merchantInfo.get().getMerchantName());
//        paramsMap.put("merchantNo", merchantInfo.get().getMarkCode());
//        paramsMap.put("address", merchantInfo.get().getAddress());
//        paramsMap.put("personName", merchantInfo.get().getName());
//        paramsMap.put("idCard", merchantInfo.get().getIdentity());
//        paramsMap.put("bankNo", merchantInfo.get().getBankNo());
//
//        paramsMap.put("wxRate", weixinMerchantChannelRateOptional.get().getMerchantPayRate().toString());
//        paramsMap.put("zfbRate", zhifubaoMerchantChannelRateOptional.get().getMerchantPayRate().toString());
//        paramsMap.put("bankName", merchantInfo.get().getBankName());
//        paramsMap.put("prov", merchantInfo.get().getProvinceName());
//        paramsMap.put("city", merchantInfo.get().getCityName());     //后台通知url
//        paramsMap.put("country", merchantInfo.get().getCountyName());
//        paramsMap.put("bankBranch", merchantInfo.get().getBranchName());
//        paramsMap.put("bankCode", merchantInfo.get().getBranchCode());
//        paramsMap.put("creditCardNo", merchantInfo.get().getCreditCard());
//        String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantIN(), paramsMap);
//        if (result != null && !"".equals(result)) {
//            JSONObject jo = JSONObject.fromObject(result);
//            if (jo.getInt("code") == 1) {
//                List<Integer> signIdList = new ArrayList<Integer>();
//                merchantChannelRateService.batchCheck(signIdList);
//                merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
//                merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
//                merchantChannelRateResponse.setIsNet(EnumEnterNet.ENTING.getId());
//                merchantChannelRateResponse.setMessage("商户入网中");
//                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "校验成功", merchantChannelRateResponse);
//            } else {
//                merchantChannelRateResponse.setIsBranch(EnumCheck.HAS.getId());
//                merchantChannelRateResponse.setIsCreditCard(EnumCheck.HAS.getId());
//                merchantChannelRateResponse.setIsNet(EnumEnterNet.ENT_FAIL.getId());
//                merchantChannelRateResponse.setMessage(jo.getString("msg"));
//                return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, jo.getString("msg"), merchantChannelRateResponse);
//            }
//        } else {
//            return CommonResponse.simpleResponse(-1, "入网异常");
//        }
//    }
}
