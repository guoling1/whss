package com.jkm.hss.controller.admin;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.QRCodeUtil;
import com.jkm.hss.admin.entity.ProductionQrCodeRecord;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.entity.RevokeQrCodeRecord;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.helper.requestparam.DownLoadQrCodeRequest;
import com.jkm.hss.admin.helper.requestparam.ProductionRequest;
import com.jkm.hss.admin.helper.requestparam.QrCodeDetailRequest;
import com.jkm.hss.admin.helper.requestparam.QrCodeListRequest;
import com.jkm.hss.admin.helper.responseparam.ProductionListResponse;
import com.jkm.hss.admin.helper.responseparam.QrCodeDetailResponse;
import com.jkm.hss.admin.helper.responseparam.QrCodeListResponse;
import com.jkm.hss.admin.service.ProductionQrCodeRecordService;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.admin.service.RevokeQrCodeRecordService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.helper.requestparam.ListFirstDealerRequest;
import com.jkm.hss.dealer.helper.response.FirstDealerResponse;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.ProductionQrCodeRequest;
import com.jkm.hss.helper.request.RevokeQrCodeRequest;
import com.jkm.hss.helper.response.ProductionQrCodeResponse;
import com.jkm.hss.helper.response.RevokeQrCodeResponse;
import com.jkm.hss.merchant.enums.EnumCommonStatus;
import com.jkm.hss.merchant.enums.EnumPlatformType;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
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

    @Autowired
    private ProductionQrCodeRecordService productionQrCodeRecordService;

    @Autowired
    private RevokeQrCodeRecordService revokeQrCodeRecordService;

    /**
     * 产码
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "productionQrCode", method = RequestMethod.POST)
    public CommonResponse productionQrCode(@RequestBody final ProductionQrCodeRequest request) {
        if(!(EnumQRCodeSysType.HSS.getId()).equals(request.getSysType())&&!(EnumQRCodeSysType.HSY.getId()).equals(request.getSysType())){
            return CommonResponse.simpleResponse(-1, "产品参数错误");
        }
        if(request.getType()!= EnumQRCodeDistributeType.ENTITYCODE.getCode()&&request.getType()!= EnumQRCodeDistributeType.ELECTRONICCODE.getCode()){
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
        ProductionQrCodeRecord productionQrCodeRecord = this.qrCodeService.productionQrCode(super.getAdminUser().getId(), request.getCount(),
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
            productionQrCodeResponse.setProductName("好收收");
        }
        if((EnumQRCodeSysType.HSY.getId()).equals(productionQrCodeRecord.getSysType())){
            productionQrCodeResponse.setProductName("好收银");
        }
        if(EnumQRCodeDistributeType.ENTITYCODE.getCode()==productionQrCodeRecord.getQrType()){
            productionQrCodeResponse.setQrType("实体码");
        }
        if(EnumQRCodeDistributeType.ELECTRONICCODE.getCode()==productionQrCodeRecord.getQrType()){
            productionQrCodeResponse.setQrType("电子码");
        }
        productionQrCodeResponse.setCount(productionQrCodeRecord.getCount());
        productionQrCodeResponse.setProductionTime(productionQrCodeRecord.getCreateTime());
        productionQrCodeResponse.setStartCode(productionQrCodeRecord.getStartCode());
        productionQrCodeResponse.setEndCode(productionQrCodeRecord.getEndCode());
        productionQrCodeResponse.setDownloadUrl(url.getHost() + url.getFile());
        productionQrCodeRecordService.updateDownUrl(productionQrCodeRecord.getId(),productionQrCodeResponse.getDownloadUrl());
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "产码成功")
                .addParam("url", url.getHost() + url.getFile()).addParam("productionQrCodeRecord",productionQrCodeResponse).build();
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

    /**
     * 二维码生成记录
     * @param productionRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/productionList", method = RequestMethod.POST)
    public CommonResponse productionList(@RequestBody final ProductionRequest productionRequest) {
        final PageModel<ProductionListResponse> pageModel = this.productionQrCodeRecordService.selectList(productionRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 所有二维码
     * @param qrCodeListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectQrCodeList", method = RequestMethod.POST)
    public CommonResponse selectQrCodeList(@RequestBody final QrCodeListRequest qrCodeListRequest) {
        final PageModel<QrCodeListResponse> pageModel = this.qrCodeService.selectQrCodeList(qrCodeListRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }

    /**
     * 二维码详情
     * @param qrCodeDetailRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/qrCodeDetail", method = RequestMethod.POST)
    public CommonResponse qrCodeDetail (@RequestBody QrCodeDetailRequest qrCodeDetailRequest) {
        Optional<QRCode> qrCodeOptional = this.qrCodeService.getByCode(qrCodeDetailRequest.getCode());
        if(!qrCodeOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "二维码不存在");
        }
        QrCodeDetailResponse qrCodeDetailResponse = new QrCodeDetailResponse();
        if((EnumQRCodeSysType.HSS.getId()).equals(qrCodeOptional.get().getSysType())){
            qrCodeDetailResponse.setProductName("好收收");
            String url = "http://hss.qianbaojiajia.com/code/scanCode?code="+qrCodeOptional.get().getCode()+"&sign="+qrCodeOptional.get().getSign();
            qrCodeDetailResponse.setQrUrl(url);
        }
        if((EnumQRCodeSysType.HSY.getId()).equals(qrCodeOptional.get().getSysType())){
            qrCodeDetailResponse.setProductName("好收银");
            String url = "http://hsy.qianbaojiajia.com/code/scanCode?code="+qrCodeOptional.get().getCode()+"&sign="+qrCodeOptional.get().getSign();
            qrCodeDetailResponse.setQrUrl(url);
        }
        qrCodeDetailResponse.setCode(qrCodeOptional.get().getCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", qrCodeDetailResponse);
    }
    /**
     * 下载二维码
     * @param downLoadQrCodeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/downLoadQrCode", method = RequestMethod.POST)
    public CommonResponse downLoadQrCode (@RequestBody DownLoadQrCodeRequest downLoadQrCodeRequest) throws FileNotFoundException {
        if(StringUtils.isBlank(downLoadQrCodeRequest.getCode())){
            return CommonResponse.simpleResponse(-1, "code不能为空");
        }
        Optional<QRCode> qrCodeOptional = this.qrCodeService.getByCode(downLoadQrCodeRequest.getCode());
        if(!qrCodeOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "二维码不存在");
        }
        String url = "";
        String productName = "";
        if((EnumQRCodeSysType.HSS.getId()).equals(qrCodeOptional.get().getSysType())){
            url = "http://hss.qianbaojiajia.com/code/scanCode?code="+qrCodeOptional.get().getCode()+"&sign="+qrCodeOptional.get().getSign();
            productName = "好收收";
        }
        if((EnumQRCodeSysType.HSY.getId()).equals(qrCodeOptional.get().getSysType())){
            url = "http://hsy.qianbaojiajia.com/code/scanCode?code="+qrCodeOptional.get().getCode()+"&sign="+qrCodeOptional.get().getSign();
            productName = "好收银";
        }
        final String tempDir = QRCodeUtil.getTempDir();
        final String filePath = QRCodeUtil.generateCode(tempDir, url, downLoadQrCodeRequest.getCode());

        final ObjectMetadata meta = new ObjectMetadata();
        meta.setCacheControl("public, max-age=31536000");
        meta.setExpirationTime(new DateTime().plusYears(1).toDate());
        meta.setContentType("image/jpeg");
        final Date expireDate = new Date(new Date().getTime() + 30 * 60 * 1000);
        Calendar cal = Calendar.getInstance();
        int day = cal.get(Calendar.DATE);
        int month = cal.get(Calendar.MONTH) + 1;
        String fileName = "qrcode/0/" +downLoadQrCodeRequest.getCode()+"-"+productName+"-"+month+"-"+day+".jpg";
        ossClient.putObject("jkm-file", fileName, new File(filePath), meta);
        URL downloadUrl = ossClient.generatePresignedUrl("jkm-file", fileName, expireDate);
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "下载成功")
                .addParam("url", "http://"+downloadUrl.getHost() + downloadUrl.getFile()).build();
    }


    /**
     * 回收二维码
     * @param revokeQrCodeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/revokeQrCode", method = RequestMethod.POST)
    public CommonResponse revokeQrCode (@RequestBody RevokeQrCodeRequest revokeQrCodeRequest) {
        if(revokeQrCodeRequest.getSysType()==null||"".equals(revokeQrCodeRequest.getSysType())){
            return CommonResponse.simpleResponse(-1, "请选择产品类型");
        }
        if(revokeQrCodeRequest.getStartCode()==null||"".equals(revokeQrCodeRequest.getStartCode())){
            return CommonResponse.simpleResponse(-1, "请填写开始码段");
        }
        if(revokeQrCodeRequest.getEndCode()==null||"".equals(revokeQrCodeRequest.getEndCode())){
            return CommonResponse.simpleResponse(-1, "请填写结束码段");
        }
        Optional<Product> productOptional = productService.selectByType(revokeQrCodeRequest.getSysType());
        if(!productOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "请选择产品");
        }
        if((Long.parseLong(revokeQrCodeRequest.getEndCode())-Long.parseLong(revokeQrCodeRequest.getStartCode()))<0){
            return CommonResponse.simpleResponse(-1, "结束码段必须大于开始码段");
        }
        long totalCount = qrCodeService.getRevokeTotalCount(revokeQrCodeRequest.getSysType(),revokeQrCodeRequest.getStartCode(),revokeQrCodeRequest.getEndCode());
        long resultCount = qrCodeService.revokeQrCode(revokeQrCodeRequest.getSysType(),revokeQrCodeRequest.getStartCode(),revokeQrCodeRequest.getEndCode());
        RevokeQrCodeRecord revokeQrCodeRecord = new RevokeQrCodeRecord();
        revokeQrCodeRecord.setStartCode(revokeQrCodeRequest.getStartCode());
        revokeQrCodeRecord.setEndCode(revokeQrCodeRequest.getEndCode());
        revokeQrCodeRecord.setStatus(EnumCommonStatus.NORMAL.getId());
        revokeQrCodeRecord.setFailCount(totalCount-resultCount);
        revokeQrCodeRecord.setSuccessCount(resultCount);
        revokeQrCodeRecord.setOperatorId(1);
        revokeQrCodeRecord.setPlatformType(EnumPlatformType.BOSS.getId());
        revokeQrCodeRecordService.add(revokeQrCodeRecord);
        RevokeQrCodeResponse revokeQrCodeResponse = new RevokeQrCodeResponse();
        revokeQrCodeResponse.setStartCode(revokeQrCodeRequest.getStartCode());
        revokeQrCodeResponse.setEndCode(revokeQrCodeRequest.getEndCode());
        revokeQrCodeResponse.setSuccessCount(revokeQrCodeRecord.getSuccessCount());
        revokeQrCodeResponse.setFailCount(revokeQrCodeRecord.getFailCount());
        return CommonResponse.objectResponse(-1, "回收二维码成功",revokeQrCodeResponse);

    }
}
