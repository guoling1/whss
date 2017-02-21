package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

/**
 * Created by Allen on 2017/2/15.
 */
public interface HsyAppVersionService {
    public String getAppVersion(String dataParam,AppParam appParam)throws ApiHandleException;
    public String getAppVersionAndroid(String dataParam,AppParam appParam);
    public String findVersionDetailByVersionCode(String dataParam,AppParam appParam)throws ApiHandleException;
}
