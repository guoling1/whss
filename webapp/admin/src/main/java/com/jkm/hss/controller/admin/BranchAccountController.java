package com.jkm.hss.controller.admin;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.BranchAccountDetailResponse;
import com.jkm.hss.dealer.entity.BranchAccountRequest;
import com.jkm.hss.dealer.entity.BranchAccountResponse;
import com.jkm.hss.dealer.service.DealerService;
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
 * Created by zhangbin on 2017/5/3.
 */
@Controller
@RequestMapping(value = "/admin/branchAccount")
public class BranchAccountController extends BaseController {

    @Autowired
    private DealerService dealerService;

    @ResponseBody
    @RequestMapping(value = "/getBranch",method = RequestMethod.POST)
    public CommonResponse getBranch(@RequestBody BranchAccountRequest req){
        final PageModel<BranchAccountResponse> pageModel = new PageModel<BranchAccountResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        List<BranchAccountResponse> list = this.dealerService.getBranch(req);
        int count = this.dealerService.getBranchCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }


    @ResponseBody
    @RequestMapping(value = "/branchDetail",method = RequestMethod.POST)
    public CommonResponse branchDetail(@RequestBody BranchAccountRequest req) throws ParseException {
        final PageModel<BranchAccountDetailResponse> pageModel = new PageModel<BranchAccountDetailResponse>(req.getPageNo(), req.getPageSize());
        req.setOffset(pageModel.getFirstIndex());
        if(req.getEndTime()!=null&&!"".equals(req.getEndTime())){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = sdf.parse(req.getEndTime());
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(dt);
            rightNow.add(Calendar.DATE, 1);
            req.setEndTime(sdf.format(rightNow.getTime()));
        }
        List<BranchAccountDetailResponse> list = this.dealerService.getBranchDetail(req.getId());
        int count = this.dealerService.getBranchDetailCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }
}
