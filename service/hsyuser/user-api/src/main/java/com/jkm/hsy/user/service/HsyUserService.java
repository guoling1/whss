package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

public interface HsyUserService {
    public String insertHsyUser(String dataParam, AppParam appParam)throws ApiHandleException;
    public String login(String dataParam,AppParam appParam)throws ApiHandleException;
    public String insertAndSendVerificationCode(String dataParam,AppParam appParam)throws ApiHandleException;
    public String updateHsyUserForSetPassword(String dataParam, AppParam appParam)throws ApiHandleException;
    public String insertDevice(String dataParam, AppParam appParam)throws ApiHandleException;
}
