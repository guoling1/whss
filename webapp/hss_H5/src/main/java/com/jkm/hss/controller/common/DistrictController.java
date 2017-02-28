package com.jkm.hss.controller.common;

import com.jkm.base.common.entity.CommonResponse;
import com.jkm.hss.admin.helper.requestparam.AppBizDistrictRequest;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictListResponse;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;
import com.jkm.hss.admin.service.AppBizDistrictService;
import com.jkm.hss.controller.BaseController;
import com.jkm.hss.helper.request.DistrictRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
@RequestMapping(value = "/district")
public class DistrictController extends BaseController{
    @Autowired
    private AppBizDistrictService appBizDistrictService;
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

    /**
     * 查询所有地区
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findAllProvinces", method = RequestMethod.POST)
    public CommonResponse findAllProvinces(final HttpServletResponse response) {
        List<AppBizDistrictResponse> list = this.appBizDistrictService.findAllProvinces();
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

    /**
     * 查询所有市
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findAllCity", method = RequestMethod.POST)
    public CommonResponse findAllCity(final HttpServletResponse response,@RequestBody AppBizDistrictRequest appBizDistrictRequest) {
        if(StringUtils.isEmpty(appBizDistrictRequest.getCode())){
            return CommonResponse.simpleResponse(-1, "请选择省份");
        }
        if("110000,120000,310000,500000".contains(appBizDistrictRequest.getCode())){
            List<AppBizDistrictResponse> appBizDistrictResponseList = appBizDistrictService.findByCode(appBizDistrictRequest.getCode());
            return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",appBizDistrictResponseList);
        }
        List<AppBizDistrictResponse> list = this.appBizDistrictService.findCityByProvinceCode(appBizDistrictRequest.getCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }
    /**
     * 查询所有县
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findAllCounty", method = RequestMethod.POST)
    public CommonResponse findAllCounty(final HttpServletResponse response,@RequestBody AppBizDistrictRequest appBizDistrictRequest) {
        if(StringUtils.isEmpty(appBizDistrictRequest.getCode())){
            return CommonResponse.simpleResponse(-1, "请选择市");
        }
        List<AppBizDistrictResponse> list = this.appBizDistrictService.findCityByProvinceCode(appBizDistrictRequest.getCode());
        return CommonResponse.objectResponse(CommonResponse.SUCCESS_CODE, "查询成功",list);
    }

}
