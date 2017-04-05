package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.WebsiteDao;
import com.jkm.hss.merchant.entity.Website;
import com.jkm.hss.merchant.entity.WebsiteRequest;
import com.jkm.hss.merchant.service.WebsiteService;

/**
 * Created by zhangbin on 2017/4/5.
 */
public class WebsiteServiceImpl implements WebsiteService {

    private WebsiteDao  websiteDao;

    @Override
    public Website selectWebsite(WebsiteRequest req) {
        Website res = websiteDao.selectWebsite(req);
        return res;
    }
}
