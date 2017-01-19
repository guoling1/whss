package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

/**
 * Created by Thinkpad on 2017/1/17.
 */
public interface HsyQrCodeService {
//    /**
//     * 判断是否能绑定
//     * @return
//     */
//    public String bindShop(String dataParam,AppParam appParam) throws ApiHandleException;

    /**
     * 绑定二维码
     * @return
     */
    public String bindQrCode(String dataParam,AppParam appParam) throws ApiHandleException;
}
