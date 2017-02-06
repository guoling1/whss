package com.jkm.hss.controller.hsyMerchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.controller.BaseController;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhangbin on 2017/1/20.
 */
@Controller
@RequestMapping(value = "/admin/hsyMerchantAudit")
public class HsyMerchantAuditController extends BaseController {

    @Autowired
    private HsyMerchantAuditService hsyMerchantAuditService;

    @ResponseBody
    @RequestMapping(value = "/throughAudit",method = RequestMethod.POST)
    public CommonResponse throughAudit(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
//        final PageModel<HsyMerchantAuditResponse> pageModel = new PageModel<HsyMerchantAuditResponse>(hsyMerchantAuditRequest.getPageNo(), hsyMerchantAuditRequest.getPageSize());
//        hsyMerchantAuditRequest.setOffset(pageModel.getFirstIndex());
        hsyMerchantAuditRequest.setStatus(AppConstant.SHOP_STATUS_NORMAL);
        hsyMerchantAuditService.auditPass(hsyMerchantAuditRequest);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"审核通过");

    }


    @ResponseBody
    @RequestMapping(value = "/rejectToExamine",method = RequestMethod.POST)
    public CommonResponse rejectToExamine(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
//        final PageModel<HsyMerchantAuditResponse> pageModel = new PageModel<HsyMerchantAuditResponse>(hsyMerchantAuditRequest.getPageNo(), hsyMerchantAuditRequest.getPageSize());
//        hsyMerchantAuditRequest.setOffset(pageModel.getFirstIndex());
        hsyMerchantAuditRequest.setStatus(AppConstant.SHOP_STATUS_REJECT);
        hsyMerchantAuditService.auditPass(hsyMerchantAuditRequest);
        return CommonResponse.simpleResponse(CommonResponse.SUCCESS_CODE,"审核未通过");

    }
}
