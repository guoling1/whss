package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.WebsiteDao;
import com.jkm.hss.merchant.entity.WebsiteRequest;
import com.jkm.hss.merchant.service.WebsiteService;

/**
 * Created by zhangbin on 2017/4/5.
 */
public class WebsiteServiceImpl implements WebsiteService {

    private WebsiteDao  websiteDao;

    @Override
    public int selectWebsite(WebsiteRequest req) {
        return this.websiteDao.selectWebsite(req);
    }

    @Override
    public void insertWebsite(WebsiteRequest req) {
        this.websiteDao.insertWebsite(req);
    }
}
