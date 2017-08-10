package com.jkm.hss.controller.merchant;


import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.response.MerchantRateResponse;
import com.jkm.hss.merchant.entity.*;
import com.jkm.hss.merchant.enums.EnumEnterNet;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.MerchantSupport;
import com.jkm.hss.merchant.service.AccountBankService;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.QueryMerchantInfoRecordService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.push.sevice.PushService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    private AccountBankService accountBankService;

    @Autowired
    private PushService pushService;

    @Autowired
    private BasicChannelService basicChannelService;

    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @ResponseBody
    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public JSONObject getAll(@RequestBody final MerchantInfoResponse merchantInfo) throws ParseException {
        JSONObject jsonObject = new JSONObject();
        int status = this.queryMerchantInfoRecordService.getStatus(merchantInfo.getId());
        int accountId = this.queryMerchantInfoRecordService.getAccountId(merchantInfo.getId());
        merchantInfo.setStatus(status);
        merchantInfo.setAccountId(accountId);
        List<MerchantInfoResponse> list = this.queryMerchantInfoRecordService.getAll(merchantInfo);
        MerchantInfoResponse response = this.queryMerchantInfoRecordService.getrecommenderInfo(merchantInfo.getId());

        if (list.size()>0 || response!=null){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getOemId()==0){
                    list.get(i).setBranchCompany("金开门");
                }
                if (list.get(i).getSecondDealerId()>0){
                    list.get(i).setMarkCode2(list.get(i).getMarkCode1());
                    MerchantInfoResponse proxyNames = dealerService.getProxyName(list.get(i).getFirstDealerId());
                    if (list.get(i).getMarkCode1()!=null&&!list.get(i).getMarkCode1().equals("")){
                        list.get(i).setMarkCode1(proxyNames.getMarkCode());
                    }
                }
                if (response!=null){
                    if(response.getMarkCode()!=null&&!response.getMarkCode().equals("")){
                        list.get(i).setRecommenderCode(response.getMarkCode());
                    }
                    if(response.getMerchantName()!=null&&!response.getMerchantName().equals("")){
                        list.get(i).setRecommenderName(response.getMerchantName());
                    }
                    if(response.getMobile()!=null&&!response.getMobile().equals("")){
                        list.get(i).setRecommenderPhone(MerchantSupport.decryptMobile(response.getMobile()));
                    }
                }
                if (list.get(i).getStatus()==0){
                    list.get(i).setStat(EnumMerchantStatus.INIT.getName());
                }
                if (list.get(i).getStatus()==1){
                    list.get(i).setStat(EnumMerchantStatus.ONESTEP.getName());
                }
                if (list.get(i).getStatus()==2){
                    list.get(i).setStat(EnumMerchantStatus.REVIEW.getName());
                }
                if (list.get(i).getStatus()==3 || list.get(i).getStatus()==6){
                    list.get(i).setStat(EnumMerchantStatus.PASSED.getName());
                }
                if (list.get(i).getStatus()==4){
                    list.get(i).setStat(EnumMerchantStatus.UNPASSED.getName());
                }
                if (list.get(i).getAccountId()>0){
                    AccountBank res = accountBankService.getDefault(list.get(i).getAccountId());
                    list.get(i).setBankNo(res.getBankNo());
                    list.get(i).setBankName(res.getBankName());
                    list.get(i).setBranchName(res.getBranchName());
//                    list.get(i).setIsAuthen(res.getIsAuthen());
                    if ("1".equals(res.getIsAuthen())){
                        list.get(i).setIsAuthen("认证通过");
                    }else {
                        list.get(i).setIsAuthen("认证未通过");
                    }
                }


            }
        }

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
                jsonObject.put("code",1);
                jsonObject.put("msg","success");
                JSONObject jo = new JSONObject();
                jo.put("list",list);
                jo.put("res",lists);
                List<MerchantChannelRate> rateList =
                        this.merchantChannelRateService.selectByMerchantId(merchantInfo.getId());
                List<MerchantRateResponse> listResponse = Lists.transform(rateList, new Function<MerchantChannelRate, MerchantRateResponse>() {
                    @Override
                    public MerchantRateResponse apply(MerchantChannelRate input) {
                        MerchantRateResponse mechantRateResponse = new MerchantRateResponse();
                        final BasicChannel basicChannel = basicChannelService.selectByChannelTypeSign(input.getChannelTypeSign()).get();
                        mechantRateResponse.setChannelName(basicChannel.getChannelShortName());
                        mechantRateResponse.setMerchantRate(input.getMerchantPayRate().toString());
                        mechantRateResponse.setWithdrawMoney(input.getMerchantWithdrawFee().setScale(2).toString());
                        mechantRateResponse.setEntNet(EnumEnterNet.idOf(input.getEnterNet()).getMsg());
                        mechantRateResponse.setMerchantBalanceType(EnumBalanceTimeType.of(input.getMerchantBalanceType()).getName());
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

    @ResponseBody
    @RequestMapping(value = "/saveNo",method = RequestMethod.POST)
    public CommonResponse saveNo(@RequestBody SaveLineNoRequest req) {
        try {
            if (req.getStatus()==2) {
                this.queryMerchantInfoRecordService.saveNo(req);
            }
            if (req.getStatus()==3||req.getStatus()==6) {
                this.queryMerchantInfoRecordService.saveNo1(req);
                merchantChannelRateService.updateKmBranchInfo(req.getAccountId(),req.getId());

            }
        }catch (Exception e){
            e.printStackTrace();
            log.debug("操作异常");
        }
        return CommonResponse.simpleResponse(1,"保存成功");

    }

}
