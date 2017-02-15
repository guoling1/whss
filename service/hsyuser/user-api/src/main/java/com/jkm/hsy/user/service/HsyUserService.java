package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

public interface HsyUserService {
    public String insertHsyUser(String dataParam, AppParam appParam)throws ApiHandleException;
    public String login(String dataParam,AppParam appParam)throws ApiHandleException;
    public String insertAndSendVerificationCode(String dataParam,AppParam appParam)throws ApiHandleException;
    public String updateHsyUserForSetPassword(String dataParam, AppParam appParam)throws ApiHandleException;
    public String insertTokenDeviceClientInfoAndReturnKey(String dataParam, AppParam appParam)throws ApiHandleException;
    public String updateClientID(String dataParam, AppParam appParam)throws ApiHandleException;
    public String logout(String dataParam, AppParam appParam)throws ApiHandleException;
    public String inserHsyUserViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findHsyUserViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findHsyUserListViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException;
    public String updateHsyUserViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException;
    public String updateHsyShopUserViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException;
    public String updateHsyUserStatusViaCorporation(String dataParam, AppParam appParam)throws ApiHandleException;
    public String findLoginInfo(String dataParam,AppParam appParam)throws ApiHandleException;
}
