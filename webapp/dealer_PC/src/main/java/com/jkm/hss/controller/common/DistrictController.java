package com.jkm.hss.controller.common;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictListResponse;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;
import com.jkm.hss.admin.service.AppBizDistrictService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.DistrictRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by xingliujie on 2017/2/8.
 */
@Slf4j
@Controller
@RequestMapping(value = "/daili/district")
public class DistrictController extends BaseController{
    @Autowired
    private AppBizDistrictService appBizDistrictService;

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
     * 查询所有地区
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findAllDistrict", method = RequestMethod.POST)
    public CommonResponse findAllDistrict(final HttpServletResponse response) {
        List<AppBizDistrictListResponse> list =  this.appBizDistrictService.findAllDistrict();
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

}
