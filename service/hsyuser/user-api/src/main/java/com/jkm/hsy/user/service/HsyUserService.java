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
    public String findLoginInfoShort(String dataParam,AppParam appParam)throws ApiHandleException;

    public String refreshlogin(String dataParam,AppParam appParam)throws ApiHandleException;
    public String updateProtocolSeenStatus(String dataParam,AppParam appParam)throws ApiHandleException;

    /**
     * 更新email
     *
     * @param email
     * @param id
     * @return
     */
    int updateEmailById(String email, long id);

    /**
     * 启用自动发送邮件
     *
     * @param id
     * @return
     */
    int enableAutoSendBalanceAccountEmail(long id);

    /**
     * 禁用自动发送邮件
     *
     * @param id
     * @return
     */
    int disableAutoSendBalanceAccountEmail(long id);
}
