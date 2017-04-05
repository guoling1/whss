package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.Website;
import com.jkm.hss.merchant.entity.WebsiteRequest;

/**
 * Created by zhangbin on 2017/4/5.
 */
public interface WebsiteService {

    /**
     * 查询用户是否提交资料
     * @param req
     * @return
     */
    Website selectWebsite(WebsiteRequest req);
}
