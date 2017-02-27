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
                appBizDistrictListResponse.setName(listProvinces.get(i).getAname());
                //查二级
                List<AppBizDistrictResponse> appCityResponse = appBizDistrictDao.findCityByProvinceCode(appBizDistrictListResponse.getCode());
                if(appCityResponse.size()>0){
                    List<AppBizDistrictListResponse.CityListResponse> cityListResponseList= new ArrayList<>();
                    for(int j=0;j<appCityResponse.size();j++){
                        AppBizDistrictListResponse.CityListResponse cityListResponse = new AppBizDistrictListResponse.CityListResponse();
                        cityListResponse.setCode(appCityResponse.get(j).getCode());
                        cityListResponse.setName(appCityResponse.get(j).getAname());
                        List<AppBizDistrictResponse> countryResponse = appBizDistrictDao.findCityByProvinceCode(appCityResponse.get(j).getCode());
                        cityListResponse.setCountryList(countryResponse);
                        cityListResponseList.add(cityListResponse);
                    }
                    appBizDistrictListResponse.setCityList(cityListResponseList);
                }
                list.add(appBizDistrictListResponse);
            }
        }
        return list;
    }
}
