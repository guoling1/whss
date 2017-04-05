package com.jkm.hss.controller.channel;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Maps;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.DynamicCodePayRequest;
import com.jkm.hss.helper.response.MerchantChannelResponse;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import com.jkm.hss.product.entity.BasicChannel;
import com.jkm.hss.product.entity.Product;
import com.jkm.hss.product.entity.ProductChannelGateway;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumProductType;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hss.product.servcie.ProductChannelGatewayService;
import com.jkm.hss.product.servcie.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yuxiang on 2017-03-30.
 */

@Controller
@RequestMapping("/channel")
public class ChannelGatewayController extends BaseController {

    @Autowired
    private MerchantChannelRateService merchantChannelRateService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private ProductChannelGatewayService productChannelGatewayService;
    @Autowired
    private ProductService productService;
    @Autowired
    private BasicChannelService basicChannelService;

    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public CommonResponse getChannelList(@RequestBody final DynamicCodePayRequest payRequest,
                                         final HttpServletRequest request){

       /* if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }*/
        //userInfoOptional.get().getMerchantId()
        Optional<MerchantInfo> merchantInfoOptional = merchantInfoService.selectById(94);
        if(!merchantInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        final MerchantInfo merchantInfo = merchantInfoOptional.get();
        if(merchantInfo.getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "未审核通过");
        }

        //获取该商户对应的产品通道模板
        final Product product = this.productService.selectByType(EnumProductType.HSS.getId()).get();
        final List<ProductChannelGateway> productChannelGatewayList =
                this.productChannelGatewayService.selectByProductTypeAndProductId(EnumProductType.HSS, product.getId());
        Map<Integer, ProductChannelGateway> productChannelGatewayMap = Maps.uniqueIndex(productChannelGatewayList, new Function<ProductChannelGateway, Integer>() {
            @Override
            public Integer apply(ProductChannelGateway input) {
                return input.getChannelSign();
            }
        });


        final List<MerchantChannelRate> merchantChannelRateList = this.merchantChannelRateService.selectByMerchantId(merchantInfo.getId());
        Map<Integer, MerchantChannelRate> merchantChannelRateMap = Maps.uniqueIndex(merchantChannelRateList, new Function<MerchantChannelRate, Integer>() {
            @Override
            public Integer apply(MerchantChannelRate input) {
                return input.getChannelTypeSign();
            }
        });

        final List<MerchantChannelResponse> merchantChannelResponseList = new ArrayList<>();
        for (ProductChannelGateway productChannelGateway : productChannelGatewayList){

            final int channelSign = productChannelGateway.getChannelSign();
            final BasicChannel basicChannel = this.basicChannelService.selectByChannelTypeSign(channelSign).get();
            final MerchantChannelResponse merchantChannelResponse = new MerchantChannelResponse();
            merchantChannelResponse.setPayMethod(EnumPayChannelSign.idOf(channelSign).getPaymentChannel().getValue());
            merchantChannelResponse.setChannelName(productChannelGatewayMap.get(channelSign).getViewChannelName());
            merchantChannelResponse.setChannelRate(merchantChannelRateMap.get(channelSign).getMerchantPayRate().toString());
            merchantChannelResponse.setChannelSign(channelSign);
            merchantChannelResponse.setLimitAmount(basicChannel.getLimitAmount().toString());
            merchantChannelResponse.setSettleType(merchantChannelRateMap.get(channelSign).getMerchantBalanceType());

            merchantChannelResponseList.add(merchantChannelResponse);
        }

        return CommonResponse.objectResponse(1,"SUCCESS", merchantChannelResponseList);
    }
}
