package com.jkm.hss.controller.merchant;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.BaseEntity;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Arrays;
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

    /**
     * 判断文件是否是图片
     * @param file
     * @return
     */
    private static boolean isImage(final MultipartFile file) {
        List<String> allowType =
                Arrays.asList("image/png; charset=UTF-8", "image/gif; charset=UTF-8", "image/jpg; charset=UTF-8", "image/jpeg; charset=UTF-8", "image/x-png; charset=UTF-8", "image/pjpeg; charset=UTF-8");
        return allowType.contains(file.getContentType());
    }

//    @ResponseBody
//    @RequestMapping(value = "/savePhotoChang",method = RequestMethod.POST)
//    public CommonResponse<BaseEntity> savePhotoChang(@RequestParam("photo") MultipartFile file, @RequestBody HistoryPhotoChangeRequest request) {
//
//        HistoryPhotoChangeResponse response = merchantInfoQueryService.getPhoto(request.getMerchantId());
//        request.setOperator(super.getAdminUser().getUsername());
//
//            final String photoName = getOrginFileName(file.getOriginalFilename());
//
//            Preconditions.checkArgument(!file.isEmpty(), "图片不能为空");
//            Preconditions.checkArgument(isImage(file), "图片格式不正确");
//            final ObjectMetadata meta = new ObjectMetadata();
//            meta.setCacheControl("public, max-age=31536000");
//            meta.setExpirationTime(new DateTime().plusYears(1).toDate());
//            meta.setContentType(file.getContentType());
//        try {
//            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), photoName, file.getInputStream(),meta);
//            if (request.getType()==1) {
//                request.setPhoto(response.getBankPic());
//                merchantInfoQueryService.saveHistory(request);
//                request.setPhotoName(photoName);
//                merchantInfoQueryService.savePhotoChang(request);
//            }
//            if (request.getType()==2) {
//                request.setPhoto(response.getBankHandPic());
//                merchantInfoQueryService.saveHistory(request);
//                request.setPhotoName(photoName);
//                merchantInfoQueryService.savePhotoChang1(request);
//            }
//            if (request.getType()==3) {
//                request.setPhoto(response.getIdentityHandPic());
//                merchantInfoQueryService.saveHistory(request);
//                request.setPhotoName(photoName);
//                merchantInfoQueryService.savePhotoChang2(request);
//            }
//            if (request.getType()==4) {
//                request.setPhoto(response.getIdentityFacePic());
//                merchantInfoQueryService.saveHistory(request);
//                request.setPhotoName(photoName);
//                merchantInfoQueryService.savePhotoChang3(request);
//            }
//            if (request.getType()==5) {
//                request.setPhoto(response.getIdentityOppositePic());
//                merchantInfoQueryService.saveHistory(request);
//                request.setPhotoName(photoName);
//                merchantInfoQueryService.savePhotoChang4(request);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            log.debug("操作异常");
//        }
//
//        return CommonResponse.simpleResponse(1,"操作成功");
//
//    }
    @ResponseBody
    @RequestMapping("/savePhotoChang")
    public CommonResponse<BaseEntity> savePhotoChang(@RequestParam("photo") MultipartFile file,String merchantId) {
        Preconditions.checkArgument(!file.isEmpty(), "图片不能为空");
        Preconditions.checkArgument(isImage(file), "图片格式不正确");
        final String fileName = getOrginFileName(file.getOriginalFilename());
        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType(file.getContentType());
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, file.getInputStream(), meta);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            CommonResponse.simpleResponse(-1, "图片上传失败");
        }

        return CommonResponse.builder4MapResult(0, "success")
                .addParam("url", fileName).build();
    }


    /**
     * 查看照片
     */
    @ResponseBody
    @RequestMapping(value = "/selectHistory", method = RequestMethod.POST)
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


    /**
     * 获取随机文件名
     * @param originalFilename
     * @return
     */
    private String getOrginFileName(final String originalFilename) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        final String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String nowDate = sdf.format(new Date());
        Date date = new Date();
        long nousedate = date.getTime();
        String fileName = "hss/" + nowDate + "/" + nousedate + RandomStringUtils.randomNumeric(5) + extName;
        return fileName;

    }
}
