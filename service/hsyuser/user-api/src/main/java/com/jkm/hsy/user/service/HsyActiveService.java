package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.AppAuUserToken;

/**
 * Created by Allen on 2017/6/6.
 */
public interface HsyActiveService {
    public AppAuUserToken findLoginInfoByAccessToken(String accessToken);
}
