package com.jkm.hss.schedule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    /**
     * 每隔五分钟同步商户入网结果
     */
    @Scheduled(cron = "0 0/5 * * * ?")
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
}
