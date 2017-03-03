package com.jkm.hss.controller.merchant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.HttpClientPost;
import com.jkm.hss.bill.helper.PaymentSdkConstants;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-02-28.
 */
@Controller
@RequestMapping(value = "/merchantIn")
public class MerchantInController extends BaseController{

    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    /**
     * 同步商户入网结果
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/update",method = RequestMethod.GET)
    public CommonResponse merchantIn(){

        //查询商户入网中的id
        List<Long> idList = this.merchantChannelRateService.selectIngMerchantInfo();

        final List<MerchantInfo> merchantInfos = this.merchantInfoService.batchGetMerchantInfo(idList);

        if (CollectionUtils.isEmpty(merchantInfos)){
            return CommonResponse.simpleResponse(1, "success");
        }
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


        return CommonResponse.simpleResponse(1, "success");
    }
}
