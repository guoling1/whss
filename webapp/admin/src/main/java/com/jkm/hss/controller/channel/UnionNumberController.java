package com.jkm.hss.controller.channel;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;
import com.jkm.hss.admin.service.AppBizDistrictService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.DistrictRequest;
import com.jkm.hss.merchant.entity.AppBizBankBranchResponse;
import com.jkm.hss.merchant.service.BankBranchService;
import com.jkm.hsy.user.entity.AppBizBankBranch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by zhangbin on 2017/5/26.
 */
@Controller
@RequestMapping(value = "/admin/unionNumber")
public class UnionNumberController extends BaseController {

    @Autowired
    private BankBranchService bankBranchService;
    @Autowired
    private AppBizDistrictService appBizDistrictService;

    /**
     * 选择银行名称
     * @param
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/bankName", method = RequestMethod.POST)
    public CommonResponse bankName(@RequestBody AppBizBankBranch appBizBankBranch) {
        List<AppBizBankBranchResponse> list = this.bankBranchService.getBankName(appBizBankBranch.getBankName());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功", list);
    }

    /**
     * 查询所有省
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findAllProvinces", method = RequestMethod.POST)
    public CommonResponse findAllProvinces(final HttpServletResponse response) {
        List<AppBizDistrictResponse> list =  this.appBizDistrictService.findAllProvinces();
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

    /**
     * 查询省对应下的市
     * @param districtRequest
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findAllCities", method = RequestMethod.POST)
    public CommonResponse findAllCities(@RequestBody final DistrictRequest districtRequest, final HttpServletResponse response) {
        List<AppBizDistrictResponse> list =  this.appBizDistrictService.findCityByProvinceCode(districtRequest.getCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

    /**
     * 新增联行号
     * @param districtRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addBankCode", method = RequestMethod.POST)
    public CommonResponse addBankCode(@RequestBody final DistrictRequest districtRequest) {
//        this.bankBranchService.addBankCode();
        return CommonResponse.simpleResponse(1, "添加成功");
    }

}
