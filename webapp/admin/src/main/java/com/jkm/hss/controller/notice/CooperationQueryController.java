package com.jkm.hss.controller.notice;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.CooperationQueryRequest;
import com.jkm.hss.merchant.entity.CooperationQueryResponse;
import com.jkm.hss.merchant.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zhangbin on 2017/4/6.
 */
@Controller
@RequestMapping(value = "/admin/cooperationQuery")
public class CooperationQueryController extends BaseController {

    @Autowired
    private WebsiteService websiteService;

    @ResponseBody
    @RequestMapping(value = "/selectCooperation",method = RequestMethod.POST)
    public CommonResponse selectCooperation(@RequestBody CooperationQueryRequest request){
        final PageModel<CooperationQueryResponse> pageModel = new PageModel<CooperationQueryResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        List<CooperationQueryResponse> list = this.websiteService.selectCooperation(request);
        int count = this.websiteService.selectCooperationCount(request);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }
}
