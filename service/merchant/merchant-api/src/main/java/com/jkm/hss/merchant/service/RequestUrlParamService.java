package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.RequestUrlParam;

/**
 * Created by xingliujie on 2017/6/21.
 */
public interface RequestUrlParamService {
    void insert(RequestUrlParam requestUrlParam);
    String getRequestUrlByUuid(String uuid);
}
