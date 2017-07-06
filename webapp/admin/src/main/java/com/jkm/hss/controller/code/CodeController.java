package com.jkm.hss.controller.code;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.CodeDownloadRequest;
import com.jkm.hss.admin.service.QRCodeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/qrcode")
public class CodeController extends BaseController {

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private OSSClient ossClient;

    /**
     * 下载codeZip
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "downloadZip", method = RequestMethod.POST)
    public CommonResponse downloadCodeZip(@RequestBody final CodeDownloadRequest request) {
        String fileZip = "";
        if("hss".equals(request.getSysType())){
            fileZip = this.qrCodeService.downloadCodeZip(1, request.getCount(),
                    "http://hss.qianbaojiajia.com/code/scanCode",request.getProductId(),request.getSysType());
        }
        if("hsy".equals(request.getSysType())){
            fileZip = this.qrCodeService.downloadCodeZip(1, request.getCount(),
                    "http://hsy.qianbaojiajia.com/code/scanCode",request.getProductId(),request.getSysType());
        }
        final String fileName = getFileName(fileZip);

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-zip-compressed ");
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url = null;

        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            return CommonResponse.simpleResponse(-1, "图片上传失败");
        }
        FileUtils.deleteQuietly(new File(fileZip));
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("url", url.getHost() + url.getFile()).build();
    }

    /**
     * 下载code Excel
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "downloadExcel", method = RequestMethod.POST)
    public CommonResponse downloadExcel(@RequestBody final CodeDownloadRequest request) {
        String fileZip = "";
        if("hss".equals(request.getSysType())){
            fileZip = this.qrCodeService.downloadExcel(1, request.getCount(),
                    "http://hss.qianbaojiajia.com/code/scanCode",request.getProductId(),request.getSysType());
        }
        if("hsy".equals(request.getSysType())){
            fileZip = this.qrCodeService.downloadExcel(1, request.getCount(),
                    "http://hsy.qianbaojiajia.com/code/scanCode",request.getProductId(),request.getSysType());
        }

        final String fileName = getFileName(fileZip);

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url;
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            return CommonResponse.simpleResponse(-1, "图片上传失败");
        }
        FileUtils.deleteQuietly(new File(fileZip));
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("url", url.getHost() + url.getFile()).build();
    }


    /**
     * 下载code Excel
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "downloadExcelByCode", method = RequestMethod.GET)
    public CommonResponse downloadExcelByCode(final HttpServletRequest request) {
        final String startId = request.getParameter("startId");
        final String endId = request.getParameter("endId");
        final String fileZip = this.qrCodeService.downloadExcelByCode(1, Long.valueOf(startId), Long.valueOf(endId),
                ApplicationConsts.getApplicationConfig().QRCodeUrl());
        final String fileName = getFileName(fileZip);

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url;
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(fileZip)), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            return CommonResponse.simpleResponse(-1, "图片上传失败");
        }
        FileUtils.deleteQuietly(new File(fileZip));
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "success")
                .addParam("url", url.getHost() + url.getFile()).build();
    }

    /**
     * 修改数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "test", method = RequestMethod.GET)
    public CommonResponse test(final HttpServletRequest request) {
        final String startCode = request.getParameter("startCode");
        final String endCode = request.getParameter("endCode");
        log.info("test startCode[{}]--endCode[{}]", startCode, endCode);
        this.qrCodeService.test(startCode, endCode);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "success");
    }
    /**
     * 获取随机文件名
     *
     * @param originalFilename
     * @return
     */
    private String getFileName(final String originalFilename) {
        final String dateFileName = DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMdd);
        final String extName = originalFilename.substring(originalFilename.lastIndexOf(File.separator) + 1);
        return "hss/" + dateFileName + "/" + extName;
    }
}
