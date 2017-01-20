package com.jkm.hss.controller.hsyMerchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hsy.user.entity.HsyMerchantAuditRequest;
import com.jkm.hsy.user.entity.HsyMerchantAuditResponse;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by zhangbin on 2017/1/20.
 */
@Controller
@RequestMapping(value = "/admin/hsyMerchantList")
public class HsyMerchantListController extends BaseController {

    @Autowired
    private HsyMerchantAuditService hsyMerchantAuditService;

    @RequestMapping(value = "/getMerchantList",method = RequestMethod.POST)
    public CommonResponse getMerchantList(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        final PageModel<HsyMerchantAuditResponse> pageModel = new PageModel<HsyMerchantAuditResponse>(hsyMerchantAuditRequest.getPageNo(), hsyMerchantAuditRequest.getPageSize());
        hsyMerchantAuditRequest.setOffset(pageModel.getFirstIndex());
        int count = hsyMerchantAuditService.getCount(hsyMerchantAuditRequest);
        List<HsyMerchantAuditResponse> list = hsyMerchantAuditService.getMerchant(hsyMerchantAuditRequest);
        if (list == null){
            return CommonResponse.simpleResponse(-1,"未查到相关数据");
        }
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }
    @RequestMapping(value = "/getDetails",method = RequestMethod.POST)
    public CommonResponse getDetails(@RequestBody final HsyMerchantAuditRequest hsyMerchantAuditRequest){
        HsyMerchantAuditResponse res = hsyMerchantAuditService.getDetails(hsyMerchantAuditRequest.getId());
        return CommonResponse.objectResponse(1, "success", res);
    }


}
