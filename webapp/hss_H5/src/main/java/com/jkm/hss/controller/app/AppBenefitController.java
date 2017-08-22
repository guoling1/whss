package com.jkm.hss.controller.app;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.Dealer;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.service.MerchantInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Allen on 2017/8/22.
 */
@Slf4j
@Controller
@RequestMapping(value = "appBenefit")
public class AppBenefitController extends BaseController {

    @Autowired
    private MerchantInfoService merchantInfoService;
    @Autowired
    private DealerService dealerService;

    @ResponseBody
    @RequestMapping(value = "findMerchantAndDealerBenefit", method = RequestMethod.POST)
    public CommonResponse findMerchantAndDealerBenefit(HttpServletRequest request, HttpServletResponse response){
        final Optional<MerchantInfo> merchantInfoOptional = this.merchantInfoService.selectById(super.getAppMerchantInfo().get().getId());
        if(!merchantInfoOptional.isPresent())
            return CommonResponse.simpleResponse(-1, "商户不存在");

        Map map=new HashMap();
        MerchantInfo merchantInfo=merchantInfoOptional.get();
        BigDecimal merchantInfoBenefit= merchantInfoService.findSumBenefit(merchantInfo.getAccountId());
        map.put("merchantInfoBenefit",merchantInfoBenefit);
        if(merchantInfo.getSuperDealerId()!=null) {
            Optional<Dealer> dealerOptional = dealerService.getById(merchantInfo.getSuperDealerId());
            if(dealerOptional.isPresent()) {
                Dealer dealer = dealerOptional.get();
                BigDecimal dealerBenefit= merchantInfoService.findSumBenefit(dealer.getAccountId());
                map.put("dealerBenefit",dealerBenefit);
            }
        }
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE,"查询成功",map);
    }

}
