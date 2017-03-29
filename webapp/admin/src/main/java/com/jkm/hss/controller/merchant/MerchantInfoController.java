package com.jkm.hss.controller.merchant;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.entity.QRCode;
import com.jkm.hss.admin.enums.EnumQRCodeSysType;
import com.jkm.hss.admin.service.QRCodeService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.enums.EnumDealerLevel;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.enums.EnumChangeType;
import com.jkm.hss.merchant.enums.EnumSource;
import com.jkm.hss.merchant.helper.request.ChangeDealerRequest;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by yuxiang on 2017-02-28.
 */
@Slf4j
@Controller
@RequestMapping(value = "/admin/merchantInfo")
public class MerchantInfoController extends BaseController{
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private DealerService dealerService;
    @Autowired
    private QRCodeService qrCodeService;
    @Autowired
    private ProductService productService;

    /**
     * 切换代理
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeDealer",method = RequestMethod.POST)
    public CommonResponse changeDealer(@RequestBody ChangeDealerRequest changeDealerRequest){
        if(changeDealerRequest.getMerchantId()<=0){
            return CommonResponse.simpleResponse(-1, "商户编码不正确");
        }
        if(changeDealerRequest.getChangeType()<=0){
            return CommonResponse.simpleResponse(-1, "请选择切换对象");
        }
        Optional<MerchantInfo> merchantInfoOptional =  merchantInfoService.selectById(changeDealerRequest.getMerchantId());
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "该商户不存在");
        }
        if(merchantInfoOptional.get().getSource()!= EnumSource.SCAN.getId()){
            return CommonResponse.simpleResponse(-1, "仅支持扫码注册");
        }
        Optional<QRCode> qrCodeOptional = qrCodeService.getByCode(merchantInfoOptional.get().getCode(), EnumQRCodeSysType.HSS.getId());
        if(!qrCodeOptional.isPresent()){
            return CommonResponse.simpleResponse(-1, "该商户的二维码不存在");
        }
        if(merchantInfoOptional.get().getId()!=qrCodeOptional.get().getMerchantId()){
            return CommonResponse.simpleResponse(-1, "商户编码和二维码中商户编码不一致");
        }
        if(changeDealerRequest.getChangeType()== EnumChangeType.BOSS.getId()){//是boss
            Optional<Product> productOptional = productService.selectByType(EnumProductType.HSS.getId());
            if(!productOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "产品通道未配置");
            }
            changeDealerRequest.setCurrentDealerId(0);
            changeDealerRequest.setFirstDealerId(0);
            changeDealerRequest.setSecondDealerId(0);
        }
        if(changeDealerRequest.getChangeType()== EnumChangeType.FIRSTDEALER.getId()){//是一代
            if(changeDealerRequest.getMarkCode()==null||"".equals(changeDealerRequest.getMarkCode())){
                return CommonResponse.simpleResponse(-1, "请输入代理商编码");
            }
            Optional<Dealer> dealerOptional = dealerService.getDealerByMarkCode(changeDealerRequest.getMarkCode());
            if(!dealerOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }
            if(dealerOptional.get().getLevel()!= EnumDealerLevel.FIRST.getId()){
                return CommonResponse.simpleResponse(-1, "代理商编码和切换对象不一致");
            }
            changeDealerRequest.setCurrentDealerId(dealerOptional.get().getId());
            changeDealerRequest.setFirstDealerId(dealerOptional.get().getId());
            changeDealerRequest.setSecondDealerId(0);
        }
        if(changeDealerRequest.getChangeType()== EnumChangeType.SECONDDEALER.getId()){//是二代
            if(changeDealerRequest.getMarkCode()==null||"".equals(changeDealerRequest.getMarkCode())){
                return CommonResponse.simpleResponse(-1, "请输入代理商编码");
            }
            Optional<Dealer> dealerOptional = dealerService.getDealerByMarkCode(changeDealerRequest.getMarkCode());
            if(!dealerOptional.isPresent()){
                return CommonResponse.simpleResponse(-1, "代理商不存在");
            }
            if(dealerOptional.get().getLevel()!= EnumDealerLevel.SECOND.getId()){
                return CommonResponse.simpleResponse(-1, "代理商编码和切换对象不一致");
            }
            changeDealerRequest.setCurrentDealerId(dealerOptional.get().getId());
            changeDealerRequest.setFirstDealerId(dealerOptional.get().getFirstLevelDealerId());
            changeDealerRequest.setSecondDealerId(dealerOptional.get().getId());
        }
        merchantInfoService.changeDealer(merchantInfoOptional.get().getCode(),changeDealerRequest);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE, "更改代理商成功");
    }
}
