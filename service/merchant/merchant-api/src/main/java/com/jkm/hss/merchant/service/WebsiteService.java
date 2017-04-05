package com.jkm.hss.merchant.service;

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
    int selectWebsite(WebsiteRequest req);

    /**
     * 保存用户提交信息
     * @param req
     */
    void insertWebsite(WebsiteRequest req);
}
