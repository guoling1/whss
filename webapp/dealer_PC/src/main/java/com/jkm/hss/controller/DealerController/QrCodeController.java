package com.jkm.hss.controller.DealerController;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.base.common.enums.EnumBoolean;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType;
import com.jkm.hss.admin.helper.requestparam.MyQrCodeListRequest;
import com.jkm.hss.admin.helper.responseparam.MyQrCodeListResponse;
import com.jkm.hss.admin.helper.responseparam.QrCodeListPageResponse;
import com.jkm.hss.admin.helper.responseparam.QrCodeListResponse;
import com.jkm.hss.admin.service.DistributeQRCodeRecordService;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.helper.requestparam.DealerOfFirstDealerRequest;
import com.jkm.hss.dealer.helper.requestparam.DistributeRecordRequest;
import com.jkm.hss.dealer.helper.response.DealerOfFirstDealerResponse;
import com.jkm.hss.dealer.helper.response.DistributeRecordResponse;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.helper.request.DistributeQRCodeRecordResponse;
import com.jkm.hss.helper.request.DistributeQrCodeRequest;
import com.jkm.hss.helper.response.ProxyProductResponse;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
        final String condition = dealerOfFirstDealerRequest.getCondition();
        final String _condition = org.apache.commons.lang.StringUtils.trim(condition);
        Preconditions.checkState(!Strings.isNullOrEmpty(_condition), "查询条件不能为空");
        long dealerId = super.getDealerId();
        dealerOfFirstDealerRequest.setDealerId(dealerId);
        List<DealerOfFirstDealerResponse> dealerOfFirstDealerResponses = this.dealerService.selectListOfFirstDealer(dealerOfFirstDealerRequest);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", dealerOfFirstDealerResponses);
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
        if(distributeQrCodeRequest.getIsSelf()==EnumBoolean.FALSE.getCode()){//分配给别人
            final Optional<Dealer> dealerOptional = this.dealerService.getById(distributeQrCodeRequest.getDealerId());
            if(!dealerOptional.isPresent()) {
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }
            Preconditions.checkState(super.getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理不可以分配二维码");
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
                    todealerId, distributeQrCodeRequest.getStartCode(),distributeQrCodeRequest.getEndCode());
        }
        if(distributeQrCodeRequest.getDistributeType()==2){//按个数
            if (distributeQrCodeRequest.getCount() <= 0) {
                return CommonResponse.simpleResponse(-1, "分配个数不可以是0");
            }
            distributeQRCodeRecords = this.dealerService.distributeQRCodeByCount(distributeQrCodeRequest.getType(),distributeQrCodeRequest.getSysType(),super.getDealerId(),
                    todealerId, distributeQrCodeRequest.getCount());
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
        PageModel<DistributeRecordResponse> distributeQRCodeRecords = this.dealerService.distributeRecord(distributeRecordRequest,super.getDealerId());
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
        if(super.getDealer().get().getLevel() == EnumDealerLevel.FIRST.getId()){
            myQrCodeListRequest.setFirstDealerId(super.getDealer().get().getId());
            int unDistributeCount = this.qrCodeService.getFirstResidueCount(super.getDealer().get().getId());
            qrCodeListPageResponse.setUnDistributeCount(unDistributeCount);
            int distributeCount = this.qrCodeService.getFirstResidueCount(super.getDealer().get().getId());
            qrCodeListPageResponse.setDistributeCount(distributeCount);
            int unActivateCount = this.qrCodeService.getFirstUnActivateCount(super.getDealer().get().getId());
            qrCodeListPageResponse.setUnActivateCount(unActivateCount);
            int activateCount = this.qrCodeService.getFirstActivateCount(super.getDealer().get().getId());
            qrCodeListPageResponse.setActivateCount(activateCount);
        }
        if(super.getDealer().get().getLevel() == EnumDealerLevel.SECOND.getId()){
            myQrCodeListRequest.setSecondDealerId(super.getDealer().get().getId());
            qrCodeListPageResponse.setUnDistributeCount(0);
            qrCodeListPageResponse.setDistributeCount(0);
            int unActivateCount = this.qrCodeService.getSecondUnActivateCount(super.getDealer().get().getId());
            qrCodeListPageResponse.setUnActivateCount(unActivateCount);
            int activateCount = this.qrCodeService.getSecondActivateCount(super.getDealer().get().getId());
            qrCodeListPageResponse.setActivateCount(activateCount);
        }
        final PageModel<MyQrCodeListResponse> pageModel = this.qrCodeService.selectDealerQrCodeList(myQrCodeListRequest);
        qrCodeListPageResponse.setPageModel(pageModel);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }
}
