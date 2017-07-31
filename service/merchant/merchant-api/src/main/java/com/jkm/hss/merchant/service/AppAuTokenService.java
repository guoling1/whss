package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.AppAuUserToken;
import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.helper.AppParam;

/**
 * Created by xingliujie on 2017/7/24.
 */
public interface AppAuTokenService {
    String insertTokenDeviceClientInfoAndReturnKey(String dataParam, AppParam appParam)throws ApiHandleException;
    String updateClientID(String dataParam, AppParam appParam)throws ApiHandleException;
    AppAuUserToken findLoginInfoByAccessToken(String accessToken);

}
