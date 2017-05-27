package com.jkm.hss.controller.DealerController;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.QRCodeUtil;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.entity.ProductionQrCodeRecord;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType2;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.helper.requestparam.*;
import com.jkm.hss.admin.helper.responseparam.*;
import com.jkm.hss.admin.service.DistributeQRCodeRecordService;
import com.jkm.hss.admin.service.ProductionQrCodeRecordService;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.enums.EnumOemType;
import com.jkm.hss.dealer.helper.requestparam.DealerOfFirstDealerRequest;
import com.jkm.hss.dealer.helper.requestparam.DistributeRecordRequest;
import com.jkm.hss.dealer.helper.response.DealerOfFirstDealerResponse;
import com.jkm.hss.dealer.helper.response.DistributeRecordResponse;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.ApplicationConsts;
import com.jkm.hss.helper.request.DistributeQRCodeRecordResponse;
import com.jkm.hss.helper.request.DistributeQrCodeRequest;
import com.jkm.hss.helper.request.OemProductionQrCodeRequest;
import com.jkm.hss.helper.response.OemProductionQrCodeResponse;
import com.jkm.hss.helper.response.ProxyProductResponse;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by xingliujie on 2017/2/14.
 */
@Slf4j
@RestController
@RequestMapping(value = "/daili/qrCode")
public class QrCodeController extends BaseController {
    @Autowired
    private DealerService dealerService;
    @Autowired
    private DealerChannelRateService dealerChannelRateService;
    @Autowired
    private ProductService productService;
    @Autowired
    private DistributeQRCodeRecordService distributeQRCodeRecordService;
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private OSSClient ossClient;
    @Autowired
    private ProductionQrCodeRecordService productionQrCodeRecordService;
    /**
     * 产码
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "productionQrCode", method = RequestMethod.POST)
    public CommonResponse productionQrCode(@RequestBody final OemProductionQrCodeRequest request) {
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
        ProductionQrCodeRecord productionQrCodeRecord = this.qrCodeService.productionQrCode(super.getAdminUser().get().getId(), request.getCount(),
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


        OemProductionQrCodeResponse productionQrCodeResponse = new OemProductionQrCodeResponse();
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
     * 二维码生成记录
     * @param oemProductionRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/productionList", method = RequestMethod.POST)
    public CommonResponse productionList(@RequestBody final OemProductionRequest oemProductionRequest) {
        oemProductionRequest.setAdminId(super.getAdminUser().get().getId());
        final PageModel<ProductionListResponse> pageModel = this.productionQrCodeRecordService.selectOemList(oemProductionRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
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
     * 判断登录代理商是否代理产品
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/proxyProduct", method = RequestMethod.POST)
    public CommonResponse proxyProduct() {
        long dealerId = super.getDealerId();
        Long hssProductId = dealerChannelRateService.getDealerBindProductId(dealerId, EnumProductType.HSS.getId());
        Long hsyProductId = dealerChannelRateService.getDealerBindProductId(dealerId, EnumProductType.HSY.getId());
        ProxyProductResponse proxyProductResponse = new ProxyProductResponse();
        proxyProductResponse.setProxyHss((hssProductId==null?0:hssProductId));
        proxyProductResponse.setProxyHsy((hsyProductId==null?0:hsyProductId));
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", proxyProductResponse);
    }

    /**
     * 一级代理商下二级代理列表
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listSecondDealer", method = RequestMethod.POST)
    public CommonResponse listSecondDealer(@RequestBody DealerOfFirstDealerRequest dealerOfFirstDealerRequest) {
        if(super.getDealer().get().getOemType()== EnumOemType.OEM.getId()){
            final String condition = dealerOfFirstDealerRequest.getCondition();
            final String _condition = StringUtils.trim(condition);
            Preconditions.checkState(!Strings.isNullOrEmpty(_condition), "查询条件不能为空");
            long dealerId = super.getDealerId();
//            dealerOfFirstDealerRequest.setDealerId(dealerId);
            dealerOfFirstDealerRequest.setOemId(dealerId);
            List<DealerOfFirstDealerResponse> dealerOfFirstDealerResponses = this.dealerService.selectListOfFirstDealer(dealerOfFirstDealerRequest);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerOfFirstDealerResponses);
        }else{
            final String condition = dealerOfFirstDealerRequest.getCondition();
            final String _condition = org.apache.commons.lang.StringUtils.trim(condition);
            Preconditions.checkState(!Strings.isNullOrEmpty(_condition), "查询条件不能为空");
            long dealerId = super.getDealerId();
            dealerOfFirstDealerRequest.setDealerId(dealerId);
            List<DealerOfFirstDealerResponse> dealerOfFirstDealerResponses = this.dealerService.selectListOfFirstDealer(dealerOfFirstDealerRequest);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerOfFirstDealerResponses);
        }
    }

    /**
     * 分配二维码
     * @param distributeQrCodeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/distributeQrCodeToDealer", method = RequestMethod.POST)
    public CommonResponse distributeQrCodeToDealer (@RequestBody DistributeQrCodeRequest distributeQrCodeRequest) {
        long todealerId = 0;
        Dealer dealer = null;
        int dtype= EnumQRCodeDistributeType2.DEALER.getCode();
        if(super.getDealer().get().getOemType()==EnumOemType.OEM.getId()){
            dtype = EnumQRCodeDistributeType2.OEM.getCode();
        }
        if(distributeQrCodeRequest.getIsSelf()==EnumBoolean.FALSE.getCode()){//分配给别人
            final Optional<Dealer> dealerOptional = this.dealerService.getById(distributeQrCodeRequest.getDealerId());
            if(!dealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }
            Preconditions.checkState(super.getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "没有权限分配二维码");
            Preconditions.checkState(dealerOptional.get().getFirstLevelDealerId() == super.getDealerId(),
                    "二级代理商[{}]不是当前一级代理商[{}]的二级代理", distributeQrCodeRequest.getDealerId(), super.getDealerId());
            todealerId = dealerOptional.get().getId();
            dealer = dealerOptional.get();
        }else{
            todealerId = 0;
            dealer = super.getDealer().get();
        }
        if (StringUtils.isBlank(distributeQrCodeRequest.getSysType())) {
            return CommonResponse.simpleResponse(-1, "请选择产品");
        }
        if(distributeQrCodeRequest.getType()!= EnumQRCodeDistributeType.ENTITYCODE.getCode()
                &&distributeQrCodeRequest.getType()!= EnumQRCodeDistributeType.ELECTRONICCODE.getCode()){
            return CommonResponse.simpleResponse(-1, "请选择类型");
        }
        //判断是否有权限
        Optional<Product> productOptional = productService.selectByType(distributeQrCodeRequest.getSysType());
        long productId = productOptional.get().getId();
        List<DealerChannelRate> dealerChannelRateList = dealerChannelRateService.selectByDealerIdAndProductId(super.getDealerId(),productId);
        if(dealerChannelRateList==null||dealerChannelRateList.size()==0){
            return CommonResponse.simpleResponse(-1, "您未开通此产品");
        }
        List<DistributeQRCodeRecord> distributeQRCodeRecords = new ArrayList<DistributeQRCodeRecord>();

        if(distributeQrCodeRequest.getDistributeType()==1){//按码段
            distributeQRCodeRecords = this.dealerService.distributeQRCodeByCode(distributeQrCodeRequest.getType(),distributeQrCodeRequest.getSysType(),super.getDealerId(),
                    todealerId, distributeQrCodeRequest.getStartCode(),distributeQrCodeRequest.getEndCode(),dtype,super.getAdminUserId());
        }
        if(distributeQrCodeRequest.getDistributeType()==2){//按个数
            if (distributeQrCodeRequest.getCount() <= 0) {
                return CommonResponse.simpleResponse(-1, "分配个数不可以是0");
            }
            distributeQRCodeRecords = this.dealerService.distributeQRCodeByCount(distributeQrCodeRequest.getType(),distributeQrCodeRequest.getSysType(),super.getDealerId(),
                    todealerId, distributeQrCodeRequest.getCount(),dtype,super.getAdminUserId());
        }
        if(distributeQRCodeRecords.size()<=0){
            return CommonResponse.simpleResponse(-1, "二维码数量不足");
        }
        List<DistributeQRCodeRecordResponse> distributeQRCodeRecordResponseList = new ArrayList<DistributeQRCodeRecordResponse>();
        for(int i=0;i<distributeQRCodeRecords.size();i++){
            DistributeQRCodeRecordResponse distributeQRCodeRecordResponse = new DistributeQRCodeRecordResponse();
            distributeQRCodeRecordResponse.setDealerName(dealer.getProxyName());
            distributeQRCodeRecordResponse.setDealerMobile(dealer.getMobile());
            distributeQRCodeRecordResponse.setDistributeTime(distributeQRCodeRecords.get(i).getCreateTime());
            distributeQRCodeRecordResponse.setType(distributeQRCodeRecords.get(i).getType());
            distributeQRCodeRecordResponse.setCount(distributeQRCodeRecords.get(i).getCount());
            distributeQRCodeRecordResponse.setStartCode(distributeQRCodeRecords.get(i).getStartCode());
            distributeQRCodeRecordResponse.setEndCode(distributeQRCodeRecords.get(i).getEndCode());
            distributeQRCodeRecordResponseList.add(distributeQRCodeRecordResponse);
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", distributeQRCodeRecordResponseList);
    }

    /**
     * 二维码分配记录
     * @param distributeRecordRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/distributeRecord", method = RequestMethod.POST)
    public CommonResponse distributeRecord (@RequestBody DistributeRecordRequest distributeRecordRequest) {
        int dtype= EnumQRCodeDistributeType2.DEALER.getCode();
        if(super.getDealer().get().getOemType()==EnumOemType.OEM.getId()){
            dtype = EnumQRCodeDistributeType2.OEM.getCode();
        }
        PageModel<DistributeRecordResponse> distributeQRCodeRecords = this.dealerService.distributeRecord(distributeRecordRequest,super.getDealerId(),dtype);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", distributeQRCodeRecords);
    }

    /**
     * 所有二维码
     * @param myQrCodeListRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/myQrCodeList", method = RequestMethod.POST)
    public CommonResponse myQrCodeList (@RequestBody MyQrCodeListRequest myQrCodeListRequest) {
        QrCodeListPageResponse<MyQrCodeListResponse> qrCodeListPageResponse = new QrCodeListPageResponse<MyQrCodeListResponse>();
        if(super.getDealer().get().getOemType()==EnumOemType.OEM.getId()){
            myQrCodeListRequest.setFirstDealerId(super.getDealer().get().getId());
            int unDistributeCount = this.qrCodeService.getResidueCount(super.getAdminUserId(),EnumQRCodeSysType.HSS.getId());
            qrCodeListPageResponse.setUnDistributeCount(unDistributeCount);
            int distributeCount = this.qrCodeService.getDistributeCount(super.getAdminUserId(),EnumQRCodeSysType.HSS.getId());
            qrCodeListPageResponse.setDistributeCount(distributeCount);
            int unActivateCount = this.qrCodeService.getUnActivateCount(super.getAdminUserId(),EnumQRCodeSysType.HSS.getId());
            qrCodeListPageResponse.setUnActivateCount(unActivateCount);
            int activateCount = this.qrCodeService.getActivateCount(super.getAdminUserId(),EnumQRCodeSysType.HSS.getId());
            qrCodeListPageResponse.setActivateCount(activateCount);
            myQrCodeListRequest.setAdminId(super.getAdminUserId());
            final PageModel<MyQrCodeListResponse> pageModel = this.qrCodeService.selectOemQrCodeList(myQrCodeListRequest);
            qrCodeListPageResponse.setPageModel(pageModel);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", qrCodeListPageResponse);
        }else{
            if(super.getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId()){
                myQrCodeListRequest.setFirstDealerId(super.getDealer().get().getId());
                int unDistributeCount = this.qrCodeService.getFirstResidueCount(super.getDealer().get().getId(),myQrCodeListRequest.getSysType());
                qrCodeListPageResponse.setUnDistributeCount(unDistributeCount);
                int distributeCount = this.qrCodeService.getFirstDistributeCount(super.getDealer().get().getId(),myQrCodeListRequest.getSysType());
                qrCodeListPageResponse.setDistributeCount(distributeCount);
                int unActivateCount = this.qrCodeService.getFirstUnActivateCount(super.getDealer().get().getId(),myQrCodeListRequest.getSysType());
                qrCodeListPageResponse.setUnActivateCount(unActivateCount);
                int activateCount = this.qrCodeService.getFirstActivateCount(super.getDealer().get().getId(),myQrCodeListRequest.getSysType());
                qrCodeListPageResponse.setActivateCount(activateCount);
            }
            if(super.getDealer().get().getLevel() == EnumDealerLevel.SECOND.getId()){
                myQrCodeListRequest.setSecondDealerId(super.getDealer().get().getId());
                qrCodeListPageResponse.setUnDistributeCount(0);
                qrCodeListPageResponse.setDistributeCount(0);
                int unActivateCount = this.qrCodeService.getSecondUnActivateCount(super.getDealer().get().getId(),myQrCodeListRequest.getSysType());
                qrCodeListPageResponse.setUnActivateCount(unActivateCount);
                int activateCount = this.qrCodeService.getSecondActivateCount(super.getDealer().get().getId(),myQrCodeListRequest.getSysType());
                qrCodeListPageResponse.setActivateCount(activateCount);
            }
            final PageModel<MyQrCodeListResponse> pageModel = this.qrCodeService.selectDealerQrCodeList(myQrCodeListRequest);
            qrCodeListPageResponse.setPageModel(pageModel);
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", qrCodeListPageResponse);
        }
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
        String fileName = "qrcode/"+super.getDealerId()+"/" +downLoadQrCodeRequest.getCode()+"-"+productName+"-"+month+"-"+day+".jpg";
        ossClient.putObject("jkm-file", fileName, new File(filePath), meta);
        URL downloadUrl = ossClient.generatePresignedUrl("jkm-file", fileName, expireDate);
        return CommonResponse.builder4MapResult(CommonResponse.SUCCESS_CODE, "下载成功")
                .addParam("url", "http://"+downloadUrl.getHost() + downloadUrl.getFile()).build();
    }
}
