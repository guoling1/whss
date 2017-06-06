package com.jkm.hss.controller.hsyMerchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.merchant.entity.AppBizBankBranchResponse;
import com.jkm.hss.merchant.service.BankBranchService;
import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zhangbin on 2017/6/6.
 */
@Controller
@RequestMapping(value = "/admin/wad")
public class WadController {

    @Autowired
    private BankBranchService bankBranchService;

    @ResponseBody
    @RequestMapping(value = "/branch",method = RequestMethod.POST)
    public CommonResponse branch(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        List<AppBizBankBranchResponse> list = this.bankBranchService.getBranch(hsyMerchantAuditRequest.getDistrictCode(),
                hsyMerchantAuditRequest.getBankName(),hsyMerchantAuditRequest.getBranchName());
        return CommonResponse.objectResponse(1,"SUCCESS",list);
    }
}
