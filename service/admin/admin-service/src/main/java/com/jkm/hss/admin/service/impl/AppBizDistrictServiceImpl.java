package com.jkm.hss.admin.service.impl;

import com.jkm.hss.admin.dao.AppBizDistrictDao;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictListResponse;
import com.jkm.hss.admin.helper.responseparam.AppBizDistrictResponse;
import com.jkm.hss.admin.service.AppBizDistrictService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingliujie on 2017/2/8.
 */
@Slf4j
@Service
public class AppBizDistrictServiceImpl implements AppBizDistrictService{
    @Autowired
    private AppBizDistrictDao appBizDistrictDao;
    /**
     * 查询所有省
     *
     * @return
     */
    @Override
    public List<AppBizDistrictResponse> findAllProvinces() {
        List<AppBizDistrictResponse> list = appBizDistrictDao.findAllProvinces();
        return list;
    }

    /**
     * 根据省份编码查询该省下所有的市
     *
     * @param code
     * @return
     */
    @Override
    public List<AppBizDistrictResponse> findCityByProvinceCode(String code) {
        List<AppBizDistrictResponse> list = appBizDistrictDao.findCityByProvinceCode(code);
        return list;
    }

    /**
     * 查询所有地区
     *
     * @return
     */
    @Override
    public List<AppBizDistrictListResponse> findAllDistrict() {
        List<AppBizDistrictListResponse> list = new ArrayList<AppBizDistrictListResponse>();
        List<AppBizDistrictResponse> listProvinces = appBizDistrictDao.findAllProvinces();
        if(listProvinces.size()>0){
            for(int i=0;i<listProvinces.size();i++){
                AppBizDistrictListResponse appBizDistrictListResponse = new AppBizDistrictListResponse();
                appBizDistrictListResponse.setCode(listProvinces.get(i).getCode());
                appBizDistrictListResponse.setAname(listProvinces.get(i).getAname());
                appBizDistrictListResponse.setList(appBizDistrictDao.findCityByProvinceCode(appBizDistrictListResponse.getCode()));
                list.add(appBizDistrictListResponse);
            }
        }
        return list;
    }
}
