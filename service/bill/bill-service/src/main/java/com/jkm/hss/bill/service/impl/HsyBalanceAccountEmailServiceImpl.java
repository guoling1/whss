package com.jkm.hss.bill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.service.HSYOrderService;
import com.jkm.hss.bill.service.HsyBalanceAccountEmailService;
import com.jkm.hsy.user.dao.HsyShopDao;
import com.jkm.hsy.user.dao.HsyUserDao;
import com.jkm.hsy.user.entity.AppAuUser;
import com.jkm.hsy.user.entity.AppBizShopUserRole;
import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;
import com.jkm.hsy.user.exception.ResultCode;
import com.jkm.hsy.user.service.HsyUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/6/22.
 */
@Slf4j
@Service
public class HsyBalanceAccountEmailServiceImpl implements HsyBalanceAccountEmailService {

    @Autowired
    private HsyUserService hsyUserService;
    @Autowired
    private HsyShopDao hsyShopDao;
    @Autowired
    private HsyUserDao hsyUserDao;
    @Autowired
    private HSYOrderService hsyOrderService;

    /**
     * {@inheritDoc}
     *
     * @param paramData
     * @param appParam
     * @return
     */
    @Override
    public String updateAutoSendBalanceAccountEmail(String paramData, AppParam appParam) throws ApiHandleException {
        final JSONObject paramJo = JSONObject.parseObject(paramData);
        final long userId = paramJo.getLongValue("userId");
        //1:启用，2禁用
        final int autoSend = paramJo.getIntValue("autoSend");
        if (1 == autoSend) {
            this.hsyUserService.enableAutoSendBalanceAccountEmail(userId);
            return "success";
        } else if (2 == autoSend) {
            this.hsyUserService.disableAutoSendBalanceAccountEmail(userId);
            return "success";
        }
        throw new ApiHandleException(ResultCode.PARAM_EXCEPTION, "autoSend值错误");
    }

    /**
     * {@inheritDoc}
     *
     * @param paramData
     * @param appParam
     * @return
     */
    @Override
    public String sendBalanceAccountEmail(final String paramData, final AppParam appParam) {
        final JSONObject paramJo = JSONObject.parseObject(paramData);
        final long shopId = paramJo.getLongValue("shopId");
        final String email = paramJo.getString("email");
        final String startTimeStr = paramJo.getString("startTime");
        final String endTimeStr = paramJo.getString("endTime");
        Date startTime = null;
        Date endTime = null;
        if (!StringUtils.isEmpty(startTimeStr)) {
            startTime = DateFormatUtil.parse(startTimeStr, DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        if (!StringUtils.isEmpty(endTimeStr)) {
            endTime = DateFormatUtil.parse(endTimeStr, DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
        }
        final AppBizShopUserRole userRole = this.hsyShopDao.findsurByRoleTypeSid(shopId).get(0);
        final AppAuUser appAuUser = this.hsyUserDao.findAppAuUserByID(userRole.getUid()).get(0);
        this.hsyUserService.updateEmailById(email, appAuUser.getId());
        final String merchantNo = appAuUser.getGlobalID();

        return null;
    }
}
