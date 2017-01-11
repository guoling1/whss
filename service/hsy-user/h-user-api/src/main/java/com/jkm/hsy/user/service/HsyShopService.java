package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

/**
 * Created by Allen on 2017/1/10.
 */
public interface HsyShopService {
    public String insertHsyShop(String dataParam,AppParam appParam)throws ApiHandleException;
    public String updateHsyShopContact(String dataParam,AppParam appParam)throws ApiHandleException;
    public String insertHsyCard(String dataParam,AppParam appParam)throws ApiHandleException;
}
