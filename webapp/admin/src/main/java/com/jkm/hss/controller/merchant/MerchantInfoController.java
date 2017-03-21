package com.jkm.hss.controller.merchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.helper.request.ChangeDealerRequest;
import com.jkm.hss.merchant.service.MerchantInfoService;
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

    /**
     * 切换代理
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeDealer",method = RequestMethod.GET)
    public CommonResponse changeDealer(@RequestBody ChangeDealerRequest changeDealerRequest){
        merchantInfoService.sele
        return CommonResponse.simpleResponse(1, "success");
    }
}
