package com.jkm.hss.controller.channel;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;
import com.jkm.hss.admin.service.AppBizDistrictService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.DistrictRequest;
import com.jkm.hss.merchant.entity.AppBizBankBranchResponse;
import com.jkm.hss.merchant.service.BankBranchService;
import com.jkm.hsy.user.entity.AppBizBankBranch;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/5/26.
 */
@Slf4j
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
        if ("".equals(districtRequest.getBankName())){
            return CommonResponse.simpleResponse(1, "银行名称不能为空");
        }
        if ("".equals(districtRequest.getProvince())){
            return CommonResponse.simpleResponse(1, "所在省不能为空");
        }
        if ("".equals(districtRequest.getCity())){
            return CommonResponse.simpleResponse(1, "所在市不能为空");
        }
        if ("".equals(districtRequest.getBranchName())){
            return CommonResponse.simpleResponse(1, "支行名称不能为空");
        }
        if ("".equals(districtRequest.getBranchCode())){
            return CommonResponse.simpleResponse(1, "联行号不能为空");
        }
        try {
            this.bankBranchService.addBankCode(districtRequest.getBankName(), districtRequest.getProvince()
                    , districtRequest.getCity(), districtRequest.getBranchName(), districtRequest.getBranchCode()
                    ,districtRequest.getBelongCityName(),districtRequest.getBelongProvinceName());
            return CommonResponse.simpleResponse(1, "添加成功");
        }catch (final Throwable throwable){
            log.error("新增联行号失败,异常信息:" + throwable.getMessage());
        }
        return  CommonResponse.simpleResponse(-1, "fail");
    }

    /**
     * 联行号管理
     * @param districtRequest
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/unionInfo", method = RequestMethod.POST)
    public CommonResponse unionInfo(@RequestBody final DistrictRequest districtRequest) {
        final PageModel<AppBizBankBranchResponse> pageModel = new PageModel<AppBizBankBranchResponse>(districtRequest.getPageNo(), districtRequest.getPageSize());
        districtRequest.setOffset(pageModel.getFirstIndex());
        Map map = new HashMap();
        String province = districtRequest.getProvince();
        if (!province.equals("")&& province!=null) {
            String substring = province.substring(0, 2);
            map.put("province",substring);
        }
        map.put("city",districtRequest.getCity());
        map.put("branchName",districtRequest.getBranchName());
        map.put("branchCode",districtRequest.getBranchCode());
        map.put("pageNo",districtRequest.getPageNo());
        map.put("pageSize",districtRequest.getPageSize());
        map.put("offset",districtRequest.getOffset());
        List<AppBizBankBranchResponse> list = this.bankBranchService.getUnionInfo(map);
        int count = this.bankBranchService.getUnionInfoCount(map);
        pageModel.setCount(count);
        pageModel.setRecords(list);
        return CommonResponse.objectResponse(1, "SUCCESS",pageModel);
    }
}
