package com.jkm.hss.controller.admin;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.dealer.entity.BranchAccountRequest;
import com.jkm.hss.dealer.entity.BranchAccountResponse;
import com.jkm.hss.dealer.service.DealerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
        List<BranchAccountResponse> list = this.dealerService.getBranch(req);
        int count = this.dealerService.getBranchCount(req);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", pageModel);
    }
}
