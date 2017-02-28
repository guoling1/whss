package com.jkm.hss.controller.merchant;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.MerchantChannelRate;
import com.jkm.hss.merchant.entity.MerchantInfoRequest;
import com.jkm.hss.merchant.service.MerchantChannelRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;
import java.util.Set;

/**
 * Created by yuxiang on 2017-02-28.
 */
@Controller
@RequestMapping(value = "/merchantIn")
public class MerchantInController extends BaseController{

    @Autowired
    private MerchantChannelRateService merchantChannelRateService;

    /**
     * 同步商户入网结果
     *
     * @return
     * @throws ParseException
     */
    @ResponseBody
    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    public CommonResponse merchantIn() throws ParseException {

        //查询
        Set<MerchantChannelRate> set = this.merchantChannelRateService.selectIngMerchantInfo();

        //请求支付中心查询商户入网结果


        return CommonResponse.simpleResponse(1, "success");
    }
}
