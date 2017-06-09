package com.jkm.hss.controller.hsyMerchant;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;
import com.jkm.hsy.user.entity.HsyMerchantAuditResponse;
import com.jkm.hsy.user.entity.HsyMerchantInfoCheckRecord;
import com.jkm.hsy.user.help.requestparam.UserTradeRateResponse;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import com.jkm.hsy.user.service.UserChannelPolicyService;
import com.jkm.hsy.user.service.UserTradeRateService;
import lombok.extern.slf4j.Slf4j;
import org.immutables.value.internal.$processor$.meta.$TreesMirrors;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2017/1/20.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/hsyMerchantList")
public class HsyMerchantListController extends BaseController {

    @Autowired
    private HsyMerchantAuditService hsyMerchantAuditService;
    @Autowired
    private OSSClient ossClient;


    @ResponseBody
    @RequestMapping(value = "/getMerchantList",method = RequestMethod.POST)
    public CommonResponse getMerchantList(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest) throws ParseException {
        final PageModel<HsyMerchantAuditResponse> pageModel = new PageModel<HsyMerchantAuditResponse>(hsyMerchantAuditRequest.getPageNo(), hsyMerchantAuditRequest.getPageSize());
        hsyMerchantAuditRequest.setOffset(pageModel.getFirstIndex());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(hsyMerchantAuditRequest.getEndTime()!=null&&!"".equals(hsyMerchantAuditRequest.getEndTime())){
            Date dt = sdf.parse(hsyMerchantAuditRequest.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            hsyMerchantAuditRequest.setEndTime(sdf.format(rightNow.getTime()));
        }
        if(hsyMerchantAuditRequest.getAuditTime1()!=null&&!"".equals(hsyMerchantAuditRequest.getAuditTime1())){
            Date dt = sdf.parse(hsyMerchantAuditRequest.getAuditTime1());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            hsyMerchantAuditRequest.setAuditTime1(sdf.format(rightNow.getTime()));
        }
        List<HsyMerchantAuditResponse> list = hsyMerchantAuditService.getMerchant(hsyMerchantAuditRequest);
        int count = hsyMerchantAuditService.getCount(hsyMerchantAuditRequest);
        if (list == null){
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        pageModel.setCount(count);
        pageModel.setRecords(list);
        String downLoadHsyMerchant = downLoadHsyMerchant(hsyMerchantAuditRequest);
        pageModel.setExt(downLoadHsyMerchant);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }

    /**
     * 导出全部
     * @return
     */
    private String downLoadHsyMerchant(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest) throws ParseException {
        final String fileZip = this.hsyMerchantAuditService.downLoadHsyMerchant(hsyMerchantAuditRequest, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        String fileName = "hss/"+  nowDate + "/" + "hsyMerchant.xls";
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url = null;
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
            return url.getHost() + url.getFile();
        } catch (IOException e) {
            log.error("上传文件失败", e);
        }
        return null;
    }


    @ResponseBody
    @RequestMapping(value = "/getDetails",method = RequestMethod.POST)
    public JSONObject getDetails(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){


        JSONObject jsonObject = new JSONObject();
        HsyMerchantAuditResponse res = hsyMerchantAuditService.getDetails(hsyMerchantAuditRequest.getId());
        Date expiration = new Date(new Date().getTime() + 30*60*1000);
        if(res!=null){
            final String photoName = res.getLicenceID();
            final String photoName1 = res.getStorefrontID();
            final String photoName2 = res.getCounterID();
            final String photoName3 = res.getIndoorID();
            final String photoName4 = res.getIdcardf();
            final String photoName5 = res.getIdcardb();
            final String photoName6 = res.getIdcardc();
            final String photoName7 = res.getContractId();
            if (photoName!=null&&!"".equals(res.getLicenceID())) {
                URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, expiration);
                String urls =url.toString();
                res.setLicenceID(urls);
            }
            if (photoName1!=null&&!"".equals(res.getStorefrontID())) {
                URL url1 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName1,expiration);
                String urls1 =url1.toString();
                res.setStorefrontID(urls1);
            }
            if (photoName2!=null&&!"".equals(res.getCounterID())) {
                URL url2 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName2,expiration);
                String urls2 =url2.toString();
                res.setCounterID(urls2);
            }
            if (photoName3!=null&&!"".equals(res.getIndoorID())) {
                URL url3 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName3,expiration);
                String urls3 =url3.toString();
                res.setIndoorID(urls3);
            }
            if (photoName4!=null&&!"".equals(res.getIdcardf())) {
                URL url4 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName4,expiration);
                String urls4 =url4.toString();
                res.setIdcardf(urls4);
            }
            if (photoName5!=null&&!"".equals(res.getIdcardb())) {
                URL url5 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName5,expiration);
                String urls5 =url5.toString();
                res.setIdcardb(urls5);
            }
            if (photoName6!=null&&!"".equals(res.getIdcardc())) {
                URL url6 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName6,expiration);
                String urls6 =url6.toString();
                res.setIdcardc(urls6);
            }
            if (photoName7!=null&&!"".equals(res.getContractId())) {
                URL url7 = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName7,expiration);
                String urls7 =url7.toString();
                res.setContractId(urls7);
            }

        }
        List<HsyMerchantInfoCheckRecord> list = hsyMerchantAuditService.getLog(hsyMerchantAuditRequest.getId());
        jsonObject.put("code",1);
        jsonObject.put("msg","success");
        JSONObject jo = new JSONObject();
        jo.put("list",list);
        jo.put("res",res);
        jsonObject.put("result",jo);
        return jsonObject;
    }


    /**
     * 更改注册手机号
     * @param hsyMerchantAuditRequest
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/changeMobile",method = RequestMethod.POST)
    public CommonResponse changeMobile(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest) throws ParseException {
        HsyMerchantAuditResponse req = this.hsyMerchantAuditService.getCellphon(hsyMerchantAuditRequest.getId());
        if (req.getChangePhone()==null) {
            this.hsyMerchantAuditService.changeMobile(req.getUid(),hsyMerchantAuditRequest.getChangePhone());
        }else {
            this.hsyMerchantAuditService.updatePhone(req.getChangePhone(),req.getUid());
            this.hsyMerchantAuditService.changeMobile(req.getUid(),hsyMerchantAuditRequest.getChangePhone());
        }
        return CommonResponse.simpleResponse(1, "更改成功");

    }




    @ResponseBody
    @RequestMapping(value = "/getCheckPending",method = RequestMethod.POST)
    public CommonResponse getCheckPending(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        final PageModel<HsyMerchantAuditResponse> pageModel = new PageModel<HsyMerchantAuditResponse>(hsyMerchantAuditRequest.getPageNo(), hsyMerchantAuditRequest.getPageSize());
        hsyMerchantAuditRequest.setOffset(pageModel.getFirstIndex());
        List<HsyMerchantAuditResponse> list = hsyMerchantAuditService.getCheckPending(hsyMerchantAuditRequest);
        int count = hsyMerchantAuditService.getCheckPendingCount(hsyMerchantAuditRequest);
        if (list == null){
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }
}
