package com.jkm.hss.controller.member;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hsy.user.entity.MemberRequest;
import com.jkm.hsy.user.entity.MemberResponse;
import com.jkm.hsy.user.service.HsyMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;


/**
 * Created by zhangbin on 2017/7/10.
 */
@Controller
@RequestMapping(value = "/admin/memberManagement")
public class MemberManagementController {

    @Autowired
    private HsyMembershipService hsyMembershipService;

    @ResponseBody
    @RequestMapping(value = "/getMemberList",method = RequestMethod.POST)
    public CommonResponse getMemberList(@RequestBody MemberRequest request){
        final PageModel<MemberResponse> pageModel = new PageModel<MemberResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());

        List<MemberResponse> list = this.hsyMembershipService.getMemberList(request);
        int count = this.hsyMembershipService.getMemberListCount(request);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }

    @ResponseBody
    @RequestMapping(value = "/getMemberDetails",method = RequestMethod.POST)
    public CommonResponse getMemberDetails(@RequestBody MemberRequest request){

        MemberResponse result = this.hsyMembershipService.getMemberDetails(request);
        return CommonResponse.objectResponse(1, "success", result);
    }
}
