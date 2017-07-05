package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

/**
 * 二维码逻辑
 */
public interface HsyQrCodeService {
    /**
     * 绑定二维码
     * @return
     */
    public String bindQrCode(String dataParam,AppParam appParam) throws ApiHandleException;
}
