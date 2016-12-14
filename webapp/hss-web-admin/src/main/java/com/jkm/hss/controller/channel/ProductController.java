package com.jkm.hss.controller.channel;

import com.google.common.collect.Lists;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.helper.response.ProductListResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.ProductAddRequest;
import com.jkm.hss.merchant.service.AccountInfoService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelDetail;
import com.jkm.hss.product.enums.EnumProductChannelDetailStatus;
import com.jkm.hss.product.enums.EnumProductStatus;
import com.jkm.hss.product.servcie.ProductChannelDetailService;
import com.jkm.hss.product.servcie.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-29.
 */
@Controller
@RequestMapping(value = "/admin/product")
@Slf4j
public class ProductController extends BaseController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductChannelDetailService productChannelDetailService;
    @Autowired
    private AccountInfoService accountInfoService;
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
            final long accountId = this.accountInfoService.addNewAccount();
            final Product product = new Product();
            product.setAccountId(accountId);
            product.setProductName(request.getProductName());
            product.setLimitPayFeeRate(request.getLimitPayFeeRate().divide(new BigDecimal(100)));
            product.setLimitWithdrawFeeRate(request.getLimitWithdrawFeeRate());
            product.setMerchantWithdrawType(request.getMerchantWithdrawType());
            product.setDealerBalanceType(request.getDealerBalanceType());
            product.setStatus(EnumProductStatus.USEING.getId());
            this.productService.init(product);
            for (ProductChannelDetail detail : request.getChannels()){
                detail.setProductTradeRate(detail.getProductTradeRate().divide(new BigDecimal(100)));
                detail.setProductMerchantPayRate(detail.getProductMerchantPayRate().divide(new BigDecimal(100)));
                detail.setProductId(product.getId());
                detail.setStatus(EnumProductChannelDetailStatus.USEING.getId());
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
        final List<ProductListResponse> responseList = Lists.newArrayList();
        for (Product product : list){
            //根据产品查找产品详情
            final List<ProductChannelDetail> detailList = this.productChannelDetailService.selectByProductId(product.getId());
            for (ProductChannelDetail detail : detailList){
                detail.setProductMerchantPayRate(detail.getProductMerchantPayRate().multiply(new BigDecimal(100)).setScale(2));
                detail.setProductTradeRate(detail.getProductTradeRate().multiply(new BigDecimal(100)).setScale(2));
            }
            final ProductListResponse response = new ProductListResponse();
            response.setProductId(product.getId());
            response.setProductName(product.getProductName());
            response.setAccountId(product.getAccountId());
            response.setLimitPayFeeRate(product.getLimitPayFeeRate().multiply(new BigDecimal(100)).setScale(2));
            response.setLimitWithdrawFeeRate(product.getLimitWithdrawFeeRate());
            response.setMerchantWithdrawType(product.getMerchantWithdrawType());
            response.setDealerBalanceType(product.getDealerBalanceType());
            response.setList(detailList);
            responseList.add(response);
        }
        return  CommonResponse.objectResponse(1, "success", responseList);
    }

    /**
     * 修改产品
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public CommonResponse update(@RequestBody final ProductListResponse request) {
        try{
            final Product product = this.productService.selectById(request.getProductId()).get();
            product.setProductName(request.getProductName());
            product.setAccountId(request.getAccountId());
            product.setLimitPayFeeRate(request.getLimitPayFeeRate());
            product.setLimitWithdrawFeeRate(request.getLimitWithdrawFeeRate());
            product.setMerchantWithdrawType(request.getMerchantWithdrawType());
            product.setDealerBalanceType(request.getDealerBalanceType());
            this.productService.update(product);
            for (ProductChannelDetail detail : request.getList()){
                detail.setProductId(request.getProductId());
                this.productChannelDetailService.update(detail);
            }
            return  CommonResponse.simpleResponse(1, "success");
        }catch (final Throwable throwable){
            log.error("修改产品信息异常,异常信息:" + throwable.getMessage());
        }
        return CommonResponse.simpleResponse(-1, "fail");
    }
}
