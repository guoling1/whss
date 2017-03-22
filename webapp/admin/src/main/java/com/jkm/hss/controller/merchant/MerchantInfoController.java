package com.jkm.hss.controller.merchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.service.DealerService;
import com.jkm.hss.merchant.enums.EnumChangeType;
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
    @Autowired
    private DealerService dealerService;

    /**
     * 切换代理
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeDealer",method = RequestMethod.GET)
    public CommonResponse changeDealer(@RequestBody ChangeDealerRequest changeDealerRequest){
        if(changeDealerRequest.getMerchantId()<=0){
            return CommonResponse.simpleResponse(-1, "商户编码不正确");
        }
        if(changeDealerRequest.getChangeType()<=0){
            return CommonResponse.simpleResponse(-1, "请选择切换对象");
        }
        if(changeDealerRequest.getChangeType()== EnumChangeType.BOSS.getId()){//是boss
            changeDealerRequest.setCurrentDealerId(0);
            changeDealerRequest.setFirstDealerId(0);
            changeDealerRequest.setSecondDealerId(0);
        }
        if(changeDealerRequest.getChangeType()== EnumChangeType.FIRSTDEALER.getId()){//是一代
            if(changeDealerRequest.getMarkCode()==null||"".equals(changeDealerRequest.getMarkCode())){
                return CommonResponse.simpleResponse(-1, "请输入代理商编码");
            }

        }
        if(changeDealerRequest.getChangeType()== EnumChangeType.SECONDDEALER.getId()){//是二代
            if(changeDealerRequest.getMarkCode()==null||"".equals(changeDealerRequest.getMarkCode())){
                return CommonResponse.simpleResponse(-1, "请输入代理商编码");
            }
        }
        merchantInfoService.changeDealer(changeDealerRequest);
        return CommonResponse.simpleResponse(1, "success");
    }
}
