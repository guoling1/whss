package com.jkm.hss.controller.common;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by xingliujie on 2017/2/22.
 */
@Slf4j
@Controller
@RequestMapping("/upload")
public class OssController extends BaseController {
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
        for(String temp : allowType){
            if (temp.contains(file.getContentType())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 微信端图片上传
     *
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping("/picUpload")
    public CommonResponse<BaseEntity> picUpload(@RequestParam("file") MultipartFile file) {
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
     * 获取随机文件名
     *
     * @param originalFilename
     * @return
     */
    private String getFileName(final String originalFilename) {
        final String randomFileName = UUID.randomUUID().toString().replace("-", "");
        final String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        return randomFileName + extName;
    }

    /**
     * 获取随机文件名
     * @param originalFilename
     * @return
     */
    private String getOrginFileName(final String originalFilename) {
        SimpleDateFormat sdf =   new SimpleDateFormat("yyyyMMdd");
        final String extName = originalFilename.substring(originalFilename.lastIndexOf("."));
        String nowDate = sdf.format(new Date());
        Date date = new Date();
        long nousedate =  date.getTime();
        String fileName ="dealer/"+  nowDate + "/" + nousedate + RandomStringUtils.randomNumeric(5) +extName;
        return fileName;
    }
}
