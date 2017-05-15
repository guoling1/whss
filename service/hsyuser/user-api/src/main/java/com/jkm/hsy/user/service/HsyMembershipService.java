package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppParam;
import com.jkm.hsy.user.exception.ApiHandleException;

/**
 * Created by Allen on 2017/5/12.
 */
public interface HsyMembershipService {
    public String insertMemshipCard(String dataParam, AppParam appParam)throws ApiHandleException;
}
