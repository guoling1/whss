package com.jkm.hss.controller.admin;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.admin.entity.ProductionQrCodeRecord;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.ProductionQrCodeRequest;
import com.jkm.hss.helper.response.ProductionQrCodeResponse;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
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
import java.util.Date;

/**
 * Created by xingliujie on 2017/2/20.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/code")
public class QrCodeController extends BaseController {
    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private OSSClient ossClient;

    @Autowired
    private ProductService productService;

    /**
     * 产码
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "productionQrCode", method = RequestMethod.POST)
    public CommonResponse productionQrCode(@RequestBody final ProductionQrCodeRequest request) {
        if(request.getSysType()!= EnumQRCodeSysType.HSS.getId()||request.getSysType()!=EnumQRCodeSysType.HSY.getId()){
            return CommonResponse.simpleResponse(-1, "产品参数错误");
        }
        if(request.getType()!= EnumQRCodeDistributeType.ENTITYCODE.getCode()||request.getType()!= EnumQRCodeDistributeType.ELECTRONICCODE.getCode()){
            return CommonResponse.simpleResponse(-1, "类型参数错误");
        }
        if(request.getCount()<=0){
            return CommonResponse.simpleResponse(-1, "个数至少为1");
        }
        Optional<Product> productOptional = productService.selectByType(request.getSysType());
        if(!productOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "无此产品");
        }
        String domainName = "http://"+request.getSysType()+".qianbaojiajia.com/code/scanCode";
        ProductionQrCodeRecord productionQrCodeRecord = this.qrCodeService.productionQrCode(1, request.getCount(),
                domainName,productOptional.get().getId(),request.getSysType(),request.getType());
        final String fileName = getFileName(productionQrCodeRecord.getDownloadUrl(),request.getSysType());
        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("application/x-xls");
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        URL url;
        try {
            ossClient.putObject(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, new FileInputStream(new File(productionQrCodeRecord.getDownloadUrl())), meta);
            url = ossClient.generatePresignedUrl(ApplicationConsts.getApplicationConfig().ossBucke(), fileName, expireDate);
        } catch (IOException e) {
            log.error("上传文件失败", e);
            return CommonResponse.simpleResponse(-1, "文件上传失败");
        }
        FileUtils.deleteQuietly(new File(productionQrCodeRecord.getDownloadUrl()));

        ProductionQrCodeResponse productionQrCodeResponse = new ProductionQrCodeResponse();
        if((EnumQRCodeSysType.HSS.getId()).equals(productionQrCodeRecord.getSysType())){
            productionQrCodeResponse.setProductName(EnumQRCodeSysType.HSS.getValue());
        }
        if((EnumQRCodeSysType.HSY.getId()).equals(productionQrCodeRecord.getSysType())){
            productionQrCodeResponse.setProductName(EnumQRCodeSysType.HSY.getValue());
        }
//        if(EnumQRCodeDistributeType.ENTITYCODE.getCode()==productionQrCodeRecord.getQrType()){
//            productionQrCodeResponse.setProductName(EnumQRCodeSysType.HSS.getValue());
//        }
//        productionQrCodeResponse.setQrType();
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "产码成功")
                .addParam("url", url.getHost() + url.getFile()).addParam("productionQrCodeRecord",productionQrCodeRecord).build();
    }
    /**
     * 获取随机文件名
     *
     * @param originalFilename
     * @return
     */
    private String getFileName(final String originalFilename,final String directoryName) {
        final String dateFileName = DateFormatUtil.format(new Date(), DateFormatUtil.yyyyMMdd);
        final String extName = originalFilename.substring(originalFilename.lastIndexOf(File.separator) + 1);
        return directoryName+"/" + dateFileName + "/" + extName;
    }
}
