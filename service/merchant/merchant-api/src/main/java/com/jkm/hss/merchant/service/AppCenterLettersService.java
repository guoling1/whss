package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.exception.ApiHandleException;
import com.jkm.hss.merchant.helper.AppParam;

/**
 * Created by xingliujie on 2017/8/11.
 */
public interface AppCenterLettersService {
    String plusDownLoad(String dataParam, AppParam appParam)throws ApiHandleException;
    String getCenterLettersList(String dataParam, AppParam appParam)throws ApiHandleException;
}
