package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;

/**
 * Created by yulong.zhang on 2017/1/19.
 */
public interface HsyAccountService {

    /**
     * 查询账户
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    String getAccount(String dataParam, AppParam appParam);

    /**
     * 获取验证码
     *
     * @param dataParam
     * @param appParam
     * @return
     */
    String getVerifyCode(String dataParam, AppParam appParam);
}
