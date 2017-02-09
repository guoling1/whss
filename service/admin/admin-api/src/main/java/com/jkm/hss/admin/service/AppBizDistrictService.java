package com.jkm.hss.admin.service;

import com.jkm.hss.admin.helper.responseparam.AppBizDistrictListResponse;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/8.
 */
public interface AppBizDistrictService {
    /**
     * 查询所有省
     * @return
     */
    List<AppBizDistrictResponse> findAllProvinces();

    /**
     * 根据省份编码查询该省下所有的市
     * @param code
     * @return
     */
    List<AppBizDistrictResponse> findCityByProvinceCode(String code);

    /**
     * 查询所有地区
     * @return
     */
    List<AppBizDistrictListResponse> findAllDistrict();
}
