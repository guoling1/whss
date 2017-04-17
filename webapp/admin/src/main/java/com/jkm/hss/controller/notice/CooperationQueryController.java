package com.jkm.hss.controller.notice;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.CooperationQueryRequest;
import com.jkm.hss.merchant.entity.CooperationQueryResponse;
import com.jkm.hss.merchant.entity.JoinRequest;
import com.jkm.hss.merchant.entity.JoinResponse;
import com.jkm.hss.merchant.service.WebsiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    public CommonResponse selectCooperation(@RequestBody CooperationQueryRequest request) throws ParseException {
        final PageModel<CooperationQueryResponse> pageModel = new PageModel<CooperationQueryResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        if(request.getEndTime()!=null&&!"".equals(request.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(request.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            request.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<CooperationQueryResponse> list = this.websiteService.selectCooperation(request);
        int count = this.websiteService.selectCooperationCount(request);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);
    }

    @ResponseBody
    @RequestMapping(value = "/selectJoin",method = RequestMethod.POST)
    public CommonResponse selectJoin(@RequestBody JoinRequest request) throws ParseException {
        final PageModel<JoinResponse> pageModel = new PageModel<JoinResponse>(request.getPageNo(), request.getPageSize());
        request.setOffset(pageModel.getFirstIndex());
        if(request.getEndTime()!=null&&!"".equals(request.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(request.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            request.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<JoinResponse> list = this.websiteService.selectJoin(request);
        int count = this.websiteService.selectJoinCount(request);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "success", pageModel);

    }
}
