package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.RequestUrlParamDao;
import com.jkm.hss.merchant.entity.RequestUrlParam;
import com.jkm.hss.merchant.service.RequestUrlParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xingliujie on 2017/6/21.
 */
@Service
public class RequestUrlParamServiceImpl implements RequestUrlParamService{
    @Autowired
    private RequestUrlParamDao requestUrlParamDao;
    @Override
    public void insert(RequestUrlParam requestUrlParam) {
        requestUrlParamDao.insert(requestUrlParam);
    }

    @Override
    public String getRequestUrlByUuid(String uuid) {
        return requestUrlParamDao.getRequestUrlByUuid(uuid);
    }
}
