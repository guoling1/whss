package com.jkm.hss.controller.common;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.MerchantLoginCodeRequest;
import com.jkm.hss.merchant.entity.AccountBank;
import com.jkm.hss.merchant.entity.BankBranch;
import com.jkm.hss.merchant.entity.MerchantInfo;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.enums.EnumMerchantStatus;
import com.jkm.hss.merchant.helper.request.BankBranchRequest;
import com.jkm.hss.merchant.service.AccountBankService;
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

    @Autowired
    private AccountBankService accountBankService;

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
        if(merchantInfo.get().getStatus()!= EnumMerchantStatus.PASSED.getId()&&merchantInfo.get().getStatus()!= EnumMerchantStatus.FRIEND.getId()){
            return CommonResponse.simpleResponse(-2, "信息未完善或待审核");
        }
        AccountBank accountBank = accountBankService.getDefault(merchantInfo.get().getAccountId());
        if(accountBank==null){
            return CommonResponse.simpleResponse(-2, "银行卡号不完善");
        }
        if(accountBank.getBankName()==null||"".equals(accountBank.getBankName())){
            return CommonResponse.simpleResponse(-2, "银行名称不完善");
        }
        if("建设银行".equals(accountBank.getBankName())){
            bankBranchRequest.setCityName("");
            bankBranchRequest.setProvinceName("");
        }
        List<BankBranch> bankBranchList = bankBranchService.findByBankName(accountBank.getBankName(),bankBranchRequest.getContions(),bankBranchRequest.getProvinceName(),bankBranchRequest.getCityName());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", bankBranchList);
    }

}
