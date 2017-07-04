package com.jkm.hsy.user.service;

/**
 * Created by wayne on 17/5/4.
 */

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

/**
 * 公告
 */
public interface HsyNoticeService {
    public String noticeList(String dataParam,AppParam appParam)throws ApiHandleException;
}
