package com.jkm.hss.controller.common;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.MerchantLoginCodeRequest;
import com.jkm.hss.merchant.entity.BankBranch;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.helper.request.BankBranchRequest;
import com.jkm.hss.merchant.service.BankBranchService;
import com.jkm.hss.merchant.service.MerchantInfoService;
import com.jkm.hss.merchant.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xingliujie on 2017/2/23.
 */
@Slf4j
@Controller
@RequestMapping(value = "/bankBranch")
public class BankBranchController extends BaseController {
    @Autowired
    private BankBranchService bankBranchService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    /**
     * 获取商户银行卡信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBankBranch", method = RequestMethod.POST)
    public CommonResponse getBankBranch(final HttpServletRequest request, final HttpServletResponse response,@RequestBody BankBranchRequest bankBranchRequest) {
        if(!super.isLogin(request)){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<UserInfo> userInfoOptional = userInfoService.selectByOpenId(super.getOpenId(request));
        if(!userInfoOptional.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        Optional<MerchantInfo> merchantInfo = merchantInfoService.selectById(userInfoOptional.get().getMerchantId());
        if(!merchantInfo.isPresent()){
            return CommonResponse.simpleResponse(-2, "未登录");
        }
        if(merchantInfo.get().getBankNo()==null||"".equals(merchantInfo.get().getBankNo())){
            return CommonResponse.simpleResponse(-2, "银行卡号不完善");
        }
        if(merchantInfo.get().getBankName()==null||"".equals(merchantInfo.get().getBankName())){
            return CommonResponse.simpleResponse(-2, "银行名称不完善");
        }
        if("北京市,天津市,上海市,重庆市".contains(bankBranchRequest.getProvinceName())){
            bankBranchRequest.setCityName(bankBranchRequest.getProvinceName());
        }
        List<BankBranch> bankBranchList = bankBranchService.findByBankName(merchantInfo.get().getBankName(),bankBranchRequest.getContions(),bankBranchRequest.getProvinceName(),bankBranchRequest.getCityName());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", bankBranchList);
    }

}
