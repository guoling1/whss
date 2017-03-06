package com.jkm.hss.controller.merchant;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.response.MerchantRateResponse;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.QueryMerchantInfoRecordService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.List;


/**
 * Created by zhangbin on 2016/12/2.
 */
@Controller
@RequestMapping(value = "/admin/QueryMerchantInfoRecord")
public class QueryMerchantInfoRecordController extends BaseController {

    @Autowired
    private QueryMerchantInfoRecordService queryMerchantInfoRecordService;

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private DealerService dealerService;

    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @ResponseBody
    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public JSONObject getAll(@RequestBody final MerchantInfoResponse merchantInfo) throws ParseException {
        JSONObject jsonObject = new JSONObject();
//        ReferralResponse res = this.queryMerchantInfoRecordService.getRefInformation(merchantInfo.getId());
        List<MerchantInfoResponse> list = this.queryMerchantInfoRecordService.getAll(merchantInfo);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getLevel()==1){
                    list.get(i).setProxyName(list.get(i).getProxyName());

                }
                if (list.get(i).getLevel()==2){
                    list.get(i).setProxyName1(list.get(i).getProxyName());
                    list.get(i).setMarkCode2(list.get(i).getMarkCode1());
                    MerchantInfoResponse proxyNames = dealerService.getProxyName(list.get(i).getFirstLevelDealerId());
                    list.get(i).setProxyName(proxyNames.getProxyName());
                    if (list.get(i).getMarkCode1()!=null&&!list.get(i).getMarkCode1().equals("")){
                        list.get(i).setMarkCode1(proxyNames.getMarkCode());
                    }
                }

            }
        }
        SettleResponse lst = this.queryMerchantInfoRecordService.getSettle(merchantInfo.getId());

        List<LogResponse> lists = this.queryMerchantInfoRecordService.getLog(merchantInfo);
        if (list!=null&&list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getIdentityFacePic()!=null&&!"".equals(list.get(i).getIdentityFacePic())){
                    String photoName = list.get(i).getIdentityFacePic();
                    Date expiration = new Date(new Date().getTime() + 30*60*1000);
                    URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName,expiration);
                    String urls =url.toString();
                    list.get(i).setIdentityFacePic(urls);
                }

                if (list.get(i).getIdentityOppositePic()!=null&&!"".equals(list.get(i).getIdentityOppositePic())){
                    String photoName = list.get(i).getIdentityOppositePic();
                    Date expiration = new Date(new Date().getTime() + 30*60*1000);
                    URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName,expiration);
                    String urls =url.toString();
                    list.get(i).setIdentityOppositePic(urls);
                }
                if (list.get(i).getBankHandPic()!=null&&!"".equals(list.get(i).getBankHandPic())){
                    String photoName = list.get(i).getBankHandPic();
                    Date expiration = new Date(new Date().getTime() + 30*60*1000);
                    URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName,expiration);
                    String urls =url.toString();
                    list.get(i).setBankHandPic(urls);
                }
                if (list.get(i).getBankPic()!=null&&!"".equals(list.get(i).getBankPic())){
                    String photoName = list.get(i).getBankPic();
                    Date expiration = new Date(new Date().getTime() + 30*60*1000);
                    URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName,expiration);
                    String urls =url.toString();
                    list.get(i).setBankPic(urls);
                }
                if (list.get(i).getIdentityHandPic()!=null&&!"".equals(list.get(i).getIdentityHandPic())){
                    String photoName = list.get(i).getIdentityHandPic();
                    Date expiration = new Date(new Date().getTime() + 30*60*1000);
                    URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName,expiration);
                    String urls =url.toString();
                    list.get(i).setIdentityHandPic(urls);

                }
                list.get(i).setProxyName(list.get(i).getProxyName());
               list.get(i).setProxyName1(list.get(i).getProxyName1());
//                if (list.get(i).getDealerId()==0){
//                    String ProxyName = "金开门";
//                    list.get(i).setProxyName(ProxyName);
//                }
//                JSONObject jsonObject = new JSONObject();
                jsonObject.put("code",1);
                jsonObject.put("msg","success");
                JSONObject jo = new JSONObject();
                jo.put("list",list);
                jo.put("res",lists);
                /*jo.put("weixinRate",lst.getWeixinRate());
                jo.put("alipayRate",lst.getAlipayRate());
                jo.put("fastRate",lst.getFastRate());*/
                List<MerchantChannelRate> rateList =
                        this.merchantChannelRateService.selectByMerchantId(merchantInfo.getId());
                List<MerchantRateResponse> listResponse = Lists.transform(rateList, new Function<MerchantChannelRate, MerchantRateResponse>() {
                    @Override
                    public MerchantRateResponse apply(MerchantChannelRate input) {
                        MerchantRateResponse mechantRateResponse = new MerchantRateResponse();
                        mechantRateResponse.setChannelName(EnumPayChannelSign.idOf(input.getChannelTypeSign()).getName());
                        mechantRateResponse.setMerchantRate(input.getMerchantPayRate().toString());
                        mechantRateResponse.setWithdrawMoney(input.getMerchantWithdrawFee().setScale(2).toString());
                        mechantRateResponse.setEntNet(EnumEnterNet.idOf(input.getEnterNet()).getMsg());
                        mechantRateResponse.setRemarks(input.getRemarks());
                        return mechantRateResponse;
                    }
                });
                jo.put("rateInfo", listResponse);
                jsonObject.put("result",jo);

                return jsonObject;

            }
        }
        jsonObject.put("code",-1);
        jsonObject.put("msg","没有查到符合条件的数据");
        return jsonObject;
    }
}
