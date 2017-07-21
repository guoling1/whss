package com.jkm.hss.controller.member;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hsy.user.entity.CardNoResponse;
import com.jkm.hsy.user.entity.MemberRequest;
import com.jkm.hsy.user.entity.MerchantMemberResponse;
import com.jkm.hsy.user.entity.MerchantMemberShipResponse;
import com.jkm.hsy.user.service.HsyMembershipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by zhangbin on 2017/7/19.
 */
@Controller
@RequestMapping(value = "/admin/merchantMember")
public class MerchantMemberController {


    @Autowired
    private HsyMembershipService hsyMembershipService;

    /**
     * 商户列表（会员卡）
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMerchantMemberList",method = RequestMethod.POST)
    public CommonResponse getMerchantMemberList(@RequestBody MemberRequest request){
        final PageModel<MerchantMemberResponse> pageModel = new PageModel<MerchantMemberResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        List<MerchantMemberResponse> list = this.hsyMembershipService.getMerchantMemberList(request);
        List<MerchantMemberResponse> lists = this.hsyMembershipService.getMerchantMemberLists(request);
        pageModel.setCount(lists.size());
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }


    /**
     * 商家会员列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getMemberShipList",method = RequestMethod.POST)
    public CommonResponse getMemberShipList(@RequestBody MemberRequest request){
        final PageModel<MerchantMemberShipResponse> pageModel = new PageModel<MerchantMemberShipResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        List<MerchantMemberShipResponse> list = this.hsyMembershipService.getMemberShipList(request);
//        List<MerchantMemberResponse> lists = this.hsyMembershipService.getMerchantMemberLists(request);
//        pageModel.setCount(lists.size());
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }

    /**
     * 会员列表
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getcardList",method = RequestMethod.POST)
    public CommonResponse getcardList(@RequestBody MemberRequest request){
        final PageModel<CardNoResponse> pageModel = new PageModel<CardNoResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        List<CardNoResponse> list = this.hsyMembershipService.getcardList(request);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }

}
