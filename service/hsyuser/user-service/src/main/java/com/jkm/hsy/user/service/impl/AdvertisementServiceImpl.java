package com.jkm.hsy.user.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jkm.hsy.user.constant.AppConstant;
import com.jkm.hsy.user.dao.AdvertisementDao;
import com.jkm.hsy.user.entity.Advertisement;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.AdvertisementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by wayne on 17/5/22.
 */
@Slf4j
@Service("AdvertisementService")
public class AdvertisementServiceImpl implements AdvertisementService {

    @Autowired
    private AdvertisementDao advertisementDao;

    @Override
    public String getvalidList(String dataParam, AppParam appParam) throws ApiHandleException {
        Gson gson=new GsonBuilder().setDateFormat(AppConstant.DATE_FORMAT).create();
        Date nowd=new Date();
        /**数据验证*/
        List<Advertisement> list = advertisementDao.getByTime(nowd);
        Advertisement advertisement=null;
        if (list != null && list.size() != 0) {
            advertisement=list.get(0);
            return gson.toJson(advertisement);
        }
        else
        {
            return "";
        }
    }
}
