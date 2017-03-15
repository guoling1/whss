package com.jkm.hss.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.merchant.dao.MerchantChannelRateDao;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.helper.MerchantConsts;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.helper.SmPost;
import com.jkm.hss.merchant.helper.request.MerchantChannelRateRequest;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-03-09.
 */
@Slf4j
@Component
@Lazy(false)
public class MerchantInTask {

    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private MerchantChannelRateDao merchantChannelRateDao;
    /**
     * 每隔五S同步商户入网结果
     */
    @Scheduled(cron = "*/5 * * * * ?")
    public void handleMerchantInTask() {
        log.info("商户入网同步定时任务--start");
        //查询商户入网中的id
        List<Long> idList = this.merchantChannelRateService.selectIngMerchantInfo();
        if (CollectionUtils.isEmpty(idList)){
            return ;
        }

        final List<MerchantInfo> merchantInfos = this.merchantInfoService.batchGetMerchantInfo(idList);
        for (MerchantInfo merchantInfo : merchantInfos){

            //请求支付中心查询商户入网结果
            final Map<String, String> map = new HashMap<>();
            map.put("merchantNo", merchantInfo.getMarkCode());
            final String jsonStr = HttpClientPost.postJson(PaymentSdkConstants.SDK_PAY_QUERYIN, map);
            final JSONObject jsonObject = JSON.parseObject(jsonStr);
            if (jsonObject.getString("code").equals("1")){
                //成功
                this.merchantChannelRateService.updateEnterNetStatus(merchantInfo.getId(), EnumEnterNet.HASENT, jsonObject.getString("msg"));
            }else if (jsonObject.getString("code").equals("-1")){
                //失败 -1
                this.merchantChannelRateService.updateEnterNetStatus(merchantInfo.getId(), EnumEnterNet.ENT_FAIL, jsonObject.getString("msg"));
            }else if (jsonObject.getString("code").equals("2")){
                //ing 稍后再查
                this.merchantChannelRateService.updateEnterNetStatus(merchantInfo.getId(), EnumEnterNet.ENTING, jsonObject.getString("msg"));
            }

        }
        log.info("商户入网同步定时任务--end");
    }

    /**
     * 每隔59S检查入网结果
     */
    @Scheduled(cron = "*/59 * * * * ?")
    public void handleMerchantTask() {

        log.info("商户入网提交定时任务--start");
        //查询商户入网中的id
        List<Long> idList = this.merchantChannelRateService.selectFailMerchantInfo();
        if (CollectionUtils.isEmpty(idList)){
            return ;
        }

        final List<MerchantInfo> merchantInfos = this.merchantInfoService.batchGetMerchantInfo(idList);
        for (MerchantInfo merchantInfo : merchantInfos){

            final MerchantChannelRateRequest request = new MerchantChannelRateRequest();
            request.setMerchantId(merchantInfo.getId());
            request.setProductId(merchantInfo.getProductId());
            request.setChannelTypeSign(EnumPayChannelSign.KM_WECHAT.getId());
            final MerchantChannelRate merchantChannelRate =
                    this.merchantChannelRateDao.selectByChannelTypeSignAndProductIdAndMerchantId(request);

            final MerchantChannelRateRequest request1 = new MerchantChannelRateRequest();
            request1.setMerchantId(merchantInfo.getId());
            request1.setProductId(merchantInfo.getProductId());
            request1.setChannelTypeSign(EnumPayChannelSign.KM_ALIPAY.getId());
            final MerchantChannelRate merchantChannelRate1 =
                    this.merchantChannelRateDao.selectByChannelTypeSignAndProductIdAndMerchantId(request1);

            Map<String, String> paramsMap = new HashMap<String, String>();
            paramsMap.put("phone", MerchantSupport.decryptMobile(merchantInfo.getId(),merchantInfo.getReserveMobile()));
            paramsMap.put("merchantName", merchantInfo.getMerchantName());
            paramsMap.put("merchantNo", merchantInfo.getMarkCode());
            paramsMap.put("address", merchantInfo.getAddress());
            paramsMap.put("personName", merchantInfo.getName());
            paramsMap.put("idCard", merchantInfo.getIdentity());
            paramsMap.put("bankNo", merchantInfo.getBankNo());
            paramsMap.put("wxRate", merchantChannelRate.getMerchantPayRate().toString());
            paramsMap.put("zfbRate", merchantChannelRate1.getMerchantPayRate().toString());
            paramsMap.put("bankName", merchantInfo.getBankName());
            paramsMap.put("prov", merchantInfo.getProvinceName());
            paramsMap.put("city", merchantInfo.getCityName());     //后台通知url
            paramsMap.put("country", merchantInfo.getCountyName());
            paramsMap.put("bankBranch", merchantInfo.getBranchName());
            paramsMap.put("bankCode", merchantInfo.getBranchCode());
            paramsMap.put("creditCardNo", merchantInfo.getCreditCard());
            String result = SmPost.post(MerchantConsts.getMerchantConfig().merchantIN(), paramsMap);
            List<Integer> signIdList = new ArrayList<Integer>();
            signIdList.add(201);
            signIdList.add(202);
            if (result != null && !"".equals(result)) {
                net.sf.json.JSONObject jo = net.sf.json.JSONObject.fromObject(result);
                if (jo.getInt("code") == 1) {
                    merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENTING.getId(),merchantInfo.getId(),jo.getString("msg"));
                } else {
                    merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENT_FAIL.getId(),merchantInfo.getId(),jo.getString("msg"));
                }
            } else {
                merchantChannelRateDao.batchCheck(signIdList,EnumEnterNet.ENT_FAIL.getId(),merchantInfo.getId(),"入网超时");
            }

            log.info("商户入网提交定时任务--send");
        }
    }
}
