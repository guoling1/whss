package com.jkm.hss.controller.QueryMerchant;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hsy.user.entity.HsyQueryMerchantRequest;
import com.jkm.hsy.user.entity.HsyQueryMerchantResponse;
import com.jkm.hsy.user.service.HsyMerchantAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.util.List;

/**
 * Created by zhangbin on 2017/3/29.
 */
@Controller
@RequestMapping(value = "/daili/HsyQueryMerchant")
public class HsyQueryMerchantController extends BaseController {

    @Autowired
    private HsyMerchantAuditService hsyMerchantAuditService;

    @ResponseBody
    @RequestMapping(value = "/hsyMerchantList", method = RequestMethod.POST)
    public CommonResponse hsyMerchantList(@RequestBody HsyQueryMerchantRequest request) throws ParseException {
        final PageModel<HsyQueryMerchantResponse> pageModel = new PageModel<HsyQueryMerchantResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        long dealerId = super.getDealerId();
        int level = super.getDealer().get().getLevel();
        long firstLevelDealerId = super.getDealer().get().getFirstLevelDealerId();

        request.setDealerId(dealerId);
        if (level==1){
            if (firstLevelDealerId>0){
                request.setFirstLevelDealerId(firstLevelDealerId);
//                List<HsyQueryMerchantResponse> list = hsyMerchantAuditService.hsyMerchantList1(request);
//                int count = hsyMerchantAuditService.hsyMerchantListCount1(request);
//                pageModel.setCount(count);
//                pageModel.setRecords(list);
                return CommonResponse.objectResponse(1, "success", pageModel);
            }
            List<HsyQueryMerchantResponse> list = hsyMerchantAuditService.hsyMerchantList(request);
            int count = hsyMerchantAuditService.hsyMerchantListCount(request);
            pageModel.setCount(count);
            pageModel.setRecords(list);
            return CommonResponse.objectResponse(1, "success", pageModel);
        }
        if (level==2){
            List<HsyQueryMerchantResponse> list = hsyMerchantAuditService.hsyMerchantSecondList(request);
            int count = hsyMerchantAuditService.hsyMerchantSecondListCount(request);
            pageModel.setCount(count);
            pageModel.setRecords(list);
            return CommonResponse.objectResponse(1, "success", pageModel);
        }
        return CommonResponse.simpleResponse(-1, "查询异常");
    }
}
