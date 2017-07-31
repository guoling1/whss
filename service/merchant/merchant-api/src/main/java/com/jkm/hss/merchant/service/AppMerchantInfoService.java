package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.helper.AppParam;

/**
 * Created by xingliujie on 2017/7/24.
 */
public interface AppMerchantInfoService {
    public String getCode(String dataParam, AppParam appParam) throws ApiHandleException;
    public String register(String dataParam, AppParam appParam) throws ApiHandleException;
    public String login(String dataParam, AppParam appParam) throws ApiHandleException;
}
