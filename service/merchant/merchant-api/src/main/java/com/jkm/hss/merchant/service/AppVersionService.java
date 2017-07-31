package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.helper.AppParam;

/**
 * Created by xingliujie on 2017/7/25.
 */
public interface AppVersionService {
    public String findVersionDetail(String dataParam, AppParam appParam)throws ApiHandleException;
    public String getAppVersion(String dataParam,AppParam appParam)throws ApiHandleException;
}
