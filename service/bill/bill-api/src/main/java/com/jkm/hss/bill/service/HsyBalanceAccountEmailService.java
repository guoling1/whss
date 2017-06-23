package com.jkm.hss.bill.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

/**
 * Created by yulong.zhang on 2017/6/22.
 */
public interface HsyBalanceAccountEmailService {

    /**
     * 启用/禁用自动发送对账邮件
     *
     * @param paramData
     * @param appParam
     * @return
     */
    String updateAutoSendBalanceAccountEmail(String paramData, AppParam appParam) throws ApiHandleException;

    /**
     * 周账单发送
     */
    void sendWeekBalanceAccountEmail();

    /**
     * 周账单发送
     */
    void sendMonthBalanceAccountEmail();

    /**
     * 发送对账邮件
     *
     * @param paramData
     * @param appParam
     * @return
     */
    String sendBalanceAccountEmail(String paramData, AppParam appParam);
}
