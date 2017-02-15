package com.jkm.hss.controller.DealerController;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.entity.DistributeQRCodeRecord;
import com.jkm.hss.admin.enums.EnumQRCodeDistributeType;
import com.jkm.hss.admin.service.AdminUserService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.entity.DealerChannelRate;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.service.DealerChannelRateService;
import com.jkm.hss.dealer.service.DealerService;
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

    /**
     * 判断登录代理商是否代理产品
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/proxyProduct", method = RequestMethod.POST)
    public CommonResponse proxyProduct() {
        long dealerId = super.getDealerId();
        long hssProductId = dealerChannelRateService.getDealerBindProductId(dealerId, EnumProductType.HSS.getId());
        long hsyProductId = dealerChannelRateService.getDealerBindProductId(dealerId, EnumProductType.HSY.getId());
        ProxyProductResponse proxyProductResponse = new ProxyProductResponse();
        proxyProductResponse.setProxyHss(hssProductId);
        proxyProductResponse.setProxyHsy(hsyProductId);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", proxyProductResponse);
    }

    /**
     * 分配二维码
     * @param distributeQrCodeRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/distributeQrCodeToSecond", method = RequestMethod.POST)
    public CommonResponse distributeQrCodeToSecond(@RequestBody DistributeQrCodeRequest distributeQrCodeRequest) {
        final Optional<Dealer> dealerOptional = this.dealerService.getById(distributeQrCodeRequest.getDealerId());
        if(!dealerOptional.isPresent()) {
            return CommonResponse.simpleResponse(-1, "代理商不存在");
        }
        Preconditions.checkState(dealerOptional.get().getLevel() == EnumDealerLevel.FIRST.getId(), "不是一级代理不可以分配二维码");
        Preconditions.checkState(dealerOptional.get().getFirstLevelDealerId() == super.getDealerId(),
                "二级代理商[{}]不是当前一级代理商[{}]的二级代理", distributeQrCodeRequest.getDealerId(), super.getDealerId());
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
            return CommonResponse.simpleResponse(-1, "未开通");
        }
        List<DistributeQRCodeRecord> distributeQRCodeRecords = new ArrayList<DistributeQRCodeRecord>();
        if(distributeQrCodeRequest.getDistributeType()==1){//按码段
            if (distributeQrCodeRequest.getCount() <= 0) {
                return CommonResponse.simpleResponse(-1, "分配个数不可以是0");
            }
            distributeQRCodeRecords = this.dealerService.distributeQRCodeByCount(distributeQrCodeRequest.getType(),distributeQrCodeRequest.getSysType(),super.getDealerId(),
                    distributeQrCodeRequest.getDealerId(), distributeQrCodeRequest.getCount());

        }
        if(distributeQrCodeRequest.getDistributeType()==2){//按个数
            if (distributeQrCodeRequest.getCount() <= 0) {
                return CommonResponse.simpleResponse(-1, "分配个数不可以是0");
            }
            distributeQRCodeRecords = this.dealerService.distributeQRCodeByCount(distributeQrCodeRequest.getType(),distributeQrCodeRequest.getSysType(),super.getDealerId(),
                    distributeQrCodeRequest.getDealerId(), distributeQrCodeRequest.getCount());
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "分配成功", distributeQRCodeRecords);
    }
}
