package com.jkm.hss.controller.common;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.merchant.entity.AppBizBankBranch;
import com.jkm.hss.merchant.helper.request.BankBranchRequest;
import com.jkm.hss.merchant.service.BankBranchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xingliujie on 2017/2/23.
 */
@Slf4j
@Controller
@RequestMapping(value = "/bankBranch")
public class BankBranchController extends BaseController {
    @Autowired
    private BankBranchService bankBranchService;


    /**
     * 获取支行信息
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getBankBranch", method = RequestMethod.POST)
    public CommonResponse getBankBranch(final HttpServletRequest request, final HttpServletResponse response,@RequestBody BankBranchRequest bankBranchRequest) {
        PageModel<AppBizBankBranch> result = new PageModel<>(bankBranchRequest.getPageNo(), bankBranchRequest.getPageSize());
        List<AppBizBankBranch> appBizBankBranches = bankBranchService.findBankBranchListByPage(bankBranchRequest);
        Long count = bankBranchService.findBankBranchListByPageCount(bankBranchRequest);
        result.setRecords(appBizBankBranches);
        result.setCount(count);
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", result);
    }

}
