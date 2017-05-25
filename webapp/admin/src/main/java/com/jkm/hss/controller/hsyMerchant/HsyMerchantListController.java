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
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
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
    public CommonResponse getMerchantList(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        final PageModel<HsyMerchantAuditResponse> pageModel = new PageModel<HsyMerchantAuditResponse>(hsyMerchantAuditRequest.getPageNo(), hsyMerchantAuditRequest.getPageSize());
        hsyMerchantAuditRequest.setOffset(pageModel.getFirstIndex());
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
    private String downLoadHsyMerchant(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
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
