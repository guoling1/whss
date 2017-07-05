package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.Advertisement;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

import java.util.List;

/**
 * Created by wayne on 17/5/22.
 */
public interface AdvertisementService {

   public String getvalidList(String dataParam,AppParam appParam)throws ApiHandleException;
}

