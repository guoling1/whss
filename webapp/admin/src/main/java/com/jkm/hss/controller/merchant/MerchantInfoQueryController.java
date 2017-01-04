package com.jkm.hss.controller.merchant;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.MerchantInfoResponse;
import com.jkm.hss.merchant.service.MerchantInfoQueryService;
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
 * Created by zhangbin on 2016/11/27.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/query")
public class MerchantInfoQueryController extends BaseController {

    @Autowired
    private MerchantInfoQueryService merchantInfoQueryService;

    @Autowired
    private OSSClient ossClient;


    @ResponseBody
    @RequestMapping(value = "/getAll",method = RequestMethod.POST)
    public CommonResponse<BaseEntity> getAll(@RequestBody MerchantInfoResponse merchantInfoResponse) {
        int pageNo = merchantInfoResponse.getPageNo();
        int pageSize = merchantInfoResponse.getPageSize();
        merchantInfoResponse.setPageNo((pageNo-1) * pageSize);
        List<MerchantInfoResponse> list1 = this.merchantInfoQueryService.getCount();
        List<MerchantInfoResponse> list = this.merchantInfoQueryService.getAll(merchantInfoResponse);

        if (list == null){

            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        PageModel<MerchantInfoResponse> pageModel = new PageModel<>(pageNo,pageSize);
        pageModel.setCount(list1.size());
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }


    @ResponseBody
    @RequestMapping(value = "/selectByName",method = RequestMethod.POST)
    public CommonResponse<BaseEntity> selectByName(int pageNo,int pageSize,String merchantName) {
        PageModel<MerchantInfoResponse> pageModel = new PageModel<>(pageNo,pageSize);

        List<MerchantInfoResponse> list1 = this.merchantInfoQueryService.getCount();
        List<MerchantInfoResponse> list = this.merchantInfoQueryService.selectByName((pageNo-1)*pageSize,pageSize,merchantName);
        if (list.size()>0){
            for (int i=0;i<list.size();i++){
                if (list.get(i).getDealerId()==0){
                    String proxyName = "金开门";
                    list.get(i).setProxyName(proxyName);
                }

            }
        }else {
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        pageModel.setCount(list1.size());
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);

    }

    /**
     * 导出全部
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/downLoad",method = RequestMethod.POST)
    private String downLoad(@RequestBody MerchantInfoResponse merchantInfoResponse){
        final String fileZip = this.merchantInfoQueryService.downloadExcel(merchantInfoResponse, ApplicationConsts.getApplicationConfig().ossBucke());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        String nowDate = sdf.format(new Date());
        Date date = new Date();
        long nousedate =  date.getTime();
        String fileName = "hss/"+  nowDate + "/" + "trade.xls";
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

}
