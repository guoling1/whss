package com.jkm.hss.controller.channel;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.account.sevice.AccountService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.ProductAddRequest;
import com.jkm.hss.helper.response.ProductListHsyResponse;
import com.jkm.hss.helper.response.ProductListResponse;
import com.jkm.hss.product.entity.*;
import com.jkm.hss.product.enums.*;
import com.jkm.hss.product.servcie.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2016-11-29.
 */
@Controller
@RequestMapping(value = "/admin/product")
@Slf4j
public class ProductController extends BaseController {

    @Autowired
    private BasicChannelService basicChannelService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ProductChannelGatewayService productChannelGatewayService;
    @Autowired
    private ProductRatePolicyService productRatePolicyService;
    /**
     * 录入产品
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public CommonResponse add(@RequestBody final ProductAddRequest request) {
        try{
            final long accountId = this.accountService.initAccount(request.getProductName());
            final Product product = new Product();
            product.setAccountId(accountId);
            product.setProductName(request.getProductName());
            product.setLimitPayFeeRate(request.getLimitPayFeeRate().divide(new BigDecimal(100)));
            product.setLimitWithdrawFeeRate(request.getLimitWithdrawFeeRate());
            product.setMerchantWithdrawType(request.getMerchantWithdrawType());
            product.setDealerBalanceType(request.getDealerBalanceType());
            product.setStatus(EnumProductStatus.USEING.getId());
            if (request.getType().equals("hss")){
                product.setType(EnumProductType.HSS.getId());
            }
            if (request.getType().equals("hsy")){
                product.setType(EnumProductType.HSY.getId());
            }
            this.productService.init(product);
            for (ProductChannelDetail detail : request.getChannels()){
                detail.setProductTradeRate(detail.getProductTradeRate().divide(new BigDecimal(100)));
                detail.setProductMerchantPayRate(detail.getProductMerchantPayRate().divide(new BigDecimal(100)));
                detail.setProductId(product.getId());
                detail.setStatus(EnumProductChannelDetailStatus.USEING.getId());
                detail.setChannelType(EnumPayChannelSign.idOf(detail.getChannelTypeSign()).getPaymentChannel().getId());
                this.productChannelDetailService.init(detail);
            }
            return CommonResponse.simpleResponse(1,"success");
        }catch (final Throwable throwable){
            log.error("录入产品异常,异常信息:" + throwable.getMessage());
        }
            return CommonResponse.simpleResponse(-1, "fail");
    }

    /**
     * 产品列表
     *
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "list", method = RequestMethod.POST)
    public CommonResponse list() {
        final List<Product> list = this.productService.selectAll();
        if (list == null){
            return CommonResponse.objectResponse(1, "success", Collections.EMPTY_LIST);
        }
        final Map<String, Object> map = new HashMap<>();
        for (Product product : list){
            //根据产品查找产品详情
            final List<ProductChannelDetail> detailList = this.productChannelDetailService.selectByProductId(product.getId());
            for (ProductChannelDetail detail : detailList){
                detail.setProductMerchantPayRate(detail.getProductMerchantPayRate().multiply(new BigDecimal(100)).setScale(2));
                detail.setProductTradeRate(detail.getProductTradeRate().multiply(new BigDecimal(100)).setScale(2));
            }
            final ProductListResponse response = new ProductListResponse();
            if (product.getType().equals("hss")) {
                response.setType(EnumProductType.HSS.getId());
                response.setProductId(product.getId());
                response.setProductName(product.getProductName());
                response.setAccountId(product.getAccountId());
                response.setLimitPayFeeRate(product.getLimitPayFeeRate().multiply(new BigDecimal(100)).setScale(2));
                response.setLimitWithdrawFeeRate(product.getLimitWithdrawFeeRate());
                response.setMerchantWithdrawType(product.getMerchantWithdrawType());
                response.setDealerBalanceType(product.getDealerBalanceType());
                response.setList(detailList);
                map.put("hss", response);
            }else {
                final List<ProductRatePolicy> productRatePolicies = this.productRatePolicyService.selectByProductId(product.getId());
                final ProductListHsyResponse productListHsyResponse = new ProductListHsyResponse();
                productListHsyResponse.setProductId(product.getId());
                productListHsyResponse.setProductName(product.getProductName());
                productListHsyResponse.setList(productRatePolicies);
                productListHsyResponse.setType(EnumProductType.HSY.getId());
                map.put("hsy", productListHsyResponse);
            }
        }
        return  CommonResponse.objectResponse(1, "success", map);
    }

    /**
     * 修改产品
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public CommonResponse update(@RequestBody final ProductListHsyResponse request) {
        try{
            final Product product = this.productService.selectById(request.getProductId()).get();
            product.setProductName(request.getProductName());
            this.productService.update(product);
            for (ProductRatePolicy detail : request.getList()){
                detail.setProductId(request.getProductId());
                this.productRatePolicyService.update(detail);
            }
            return  CommonResponse.simpleResponse(1, "success");
        }catch (final Throwable throwable){
            log.error("修改产品信息异常,异常信息:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }

    /**
     * 修改Hsy产品
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "updateHsy", method = RequestMethod.POST)
    public CommonResponse updateHsy(@RequestBody final ProductListResponse request) {
        try{
            final Product product = this.productService.selectById(request.getProductId()).get();
            product.setProductName(request.getProductName());
            product.setAccountId(request.getAccountId());
            product.setLimitPayFeeRate(request.getLimitPayFeeRate().divide(new BigDecimal(100)));
            product.setLimitWithdrawFeeRate(request.getLimitWithdrawFeeRate());
            product.setMerchantWithdrawType(request.getMerchantWithdrawType());
            product.setDealerBalanceType(request.getDealerBalanceType());
            this.productService.update(product);
            for (ProductChannelDetail detail : request.getList()){
                detail.setProductTradeRate(detail.getProductTradeRate().divide(new BigDecimal(100)));
                detail.setProductMerchantPayRate(detail.getProductMerchantPayRate().divide(new BigDecimal(100)));
                detail.setProductId(request.getProductId());
                detail.setChannelType(EnumPayChannelSign.idOf(detail.getChannelTypeSign()).getPaymentChannel().getId());
                this.productChannelDetailService.updateOrAdd(detail);
            }
            return  CommonResponse.simpleResponse(1, "success");
        }catch (final Throwable throwable){
            log.error("修改产品信息异常,异常信息:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }


    /**
     * 新增网关通道
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addGateway", method = RequestMethod.POST)
    public CommonResponse addProductChannelGateway(@RequestBody final ProductChannelGateway request){

        try{
            final Optional<BasicChannel> basicChannelOptional =
                    this.basicChannelService.selectByChannelTypeSign(request.getChannelSign());
            Preconditions.checkArgument(basicChannelOptional.isPresent(), "基础通道不存在");
            final Product product = this.productService.selectByType(request.getProductType()).get();
            final BasicChannel basicChannel = basicChannelOptional.get();
            request.setGatewayType(EnumGatewayType.PRODUCT.getId());
            request.setDealerGatewayId(0);
            request.setChannelShortName(basicChannel.getChannelShortName());
            request.setProductId(product.getId());
            request.setStatus(EnumProductChannelGatewayStatus.USEING.getId());

            this.productChannelGatewayService.addNew(request);
            return CommonResponse.simpleResponse(1, "success");
        }catch (final Throwable e){

            log.error("添加网关失败，异常信息:" + e.getMessage());
            return CommonResponse.simpleResponse(-1, "fail");
        }
    }

    /**
     * 网关通道列表
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/listGateway", method = RequestMethod.POST)
    public CommonResponse listProductChannelGateway(@RequestBody final ProductChannelGateway request){

        try{
            final Product product = this.productService.selectByType(request.getProductType()).get();
            final List<ProductChannelGateway> list =
                    this.productChannelGatewayService.selectByProductTypeAndGatewayAndProductId(EnumProductType.of(request.getProductType()), EnumGatewayType.PRODUCT,product.getId());
            return CommonResponse.objectResponse(1, "success", list);
        }catch (final Throwable e){

            log.error("获取网关列表失败，异常信息:" + e.getMessage());
            return CommonResponse.simpleResponse(-1, "fail");
        }
    }

    /**
     * 推荐通道
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/recommend", method = RequestMethod.POST)
    public CommonResponse recommend(@RequestBody final ProductChannelGateway request){
        if (("").equals(request.getId())){
            return CommonResponse.simpleResponse(-1, "fail");
        }
        try{
            this.productChannelGatewayService.recommend(request);
            return CommonResponse.simpleResponse(1, "已成功推荐");
        }catch (final Throwable e){

            log.error("推荐通道失败，异常信息:" + e.getMessage());
            return CommonResponse.simpleResponse(-1, "fail");
        }
    }

    /**
     * 修改网关通道
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateGateway", method = RequestMethod.POST)
    public CommonResponse updateProductChannelGateway(@RequestBody final ProductChannelGateway request){


        try{
            final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(request.getChannelSign()).get();
            request.setGatewayType(EnumGatewayType.PRODUCT.getId());
            request.setChannelShortName(basicChannel.getChannelShortName());
            request.setStatus(EnumProductChannelGatewayStatus.USEING.getId());
            this.productChannelGatewayService.update(request);
            return CommonResponse.simpleResponse(1, "success");
        }catch (final Throwable e){

            log.error("修改网关失败，异常信息:" + e.getMessage());
            return CommonResponse.simpleResponse(-1, "fail");
        }
    }

}
