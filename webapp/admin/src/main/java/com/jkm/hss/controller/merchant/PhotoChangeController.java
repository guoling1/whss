package com.jkm.hss.controller.merchant;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.merchant.entity.HistoryPhotoChangeRequest;
import com.jkm.hss.merchant.entity.HistoryPhotoChangeResponse;
import com.jkm.hss.merchant.service.MerchantInfoQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2017/4/21.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/photoChange")
public class PhotoChangeController extends BaseController {

    @Autowired
    private MerchantInfoQueryService merchantInfoQueryService;
    @Autowired
    private OSSClient ossClient;

    @ResponseBody
    @RequestMapping(value = "/savePhotoChang")
    public CommonResponse savePhotoChang(@RequestBody HistoryPhotoChangeRequest request) {

        HistoryPhotoChangeResponse response = merchantInfoQueryService.getPhoto(request.getMerchantId());
        request.setOperator(super.getAdminUser().getUsername());
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(request.getPhoto());
            final ObjectMetadata meta = new ObjectMetadata();
            meta.setCacheControl("public, max-age=31536000");
            meta.setExpirationTime(new DateTime().plusYears(1).toDate());
            meta.setContentType("application/octet-stream ");
            SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
            String nowDate = sdf.format(new Date());
            Date date = new Date();
            long nousedate =  date.getTime();
            String photoName = "hss/"+  nowDate + "/" + nousedate + RandomStringUtils.randomNumeric(5) +".jpg";
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, inputStream,meta);
            if (request.getType()==1) {
                request.setPhoto(response.getBankPic());
                merchantInfoQueryService.saveHistory(request);
                request.setPhotoName(photoName);
                merchantInfoQueryService.savePhotoChang(request);
            }
            if (request.getType()==2) {
                request.setPhoto(response.getBankHandPic());
                merchantInfoQueryService.saveHistory(request);
                request.setPhotoName(photoName);
                merchantInfoQueryService.savePhotoChang1(request);
            }
            if (request.getType()==3) {
                request.setPhoto(response.getIdentityHandPic());
                merchantInfoQueryService.saveHistory(request);
                request.setPhotoName(photoName);
                merchantInfoQueryService.savePhotoChang2(request);
            }
            if (request.getType()==4) {
                request.setPhoto(response.getIdentityFacePic());
                merchantInfoQueryService.saveHistory(request);
                request.setPhotoName(photoName);
                merchantInfoQueryService.savePhotoChang3(request);
            }
            if (request.getType()==5) {
                request.setPhoto(response.getIdentityOppositePic());
                merchantInfoQueryService.saveHistory(request);
                request.setPhotoName(photoName);
                merchantInfoQueryService.savePhotoChang4(request);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }catch (Exception e) {
            e.printStackTrace();
            log.debug("操作异常");
        }

        return CommonResponse.simpleResponse(1,"操作成功");

    }

    /**
     * 查看照片
     */
    @ResponseBody
    @RequestMapping(value = "/selectHistory")
    public CommonResponse selectHistory(@RequestBody HistoryPhotoChangeRequest request) {
        final PageModel<HistoryPhotoChangeResponse> pageModel = new PageModel<HistoryPhotoChangeResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        List<HistoryPhotoChangeResponse> list = merchantInfoQueryService.selectHistory(request);
        if (list!=null&&list.size()>0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getPhoto() != null && !"".equals(list.get(i).getPhoto())) {
                    String photoName = list.get(i).getPhoto();
                    Date expiration = new Date(new Date().getTime() + 30 * 60 * 1000);
                    URL url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, expiration);
                    String urls = url.toString();
                    list.get(i).setPhoto(urls);
                }
            }
        }
        int count = merchantInfoQueryService.selectHistoryCount(request);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1,"success",pageModel);
    }
}
