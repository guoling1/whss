package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.UserTradeRate;
import com.jkm.hsy.user.help.requestparam.UserTradeRateResponse;

/**
 * Created by xingliujie on 2017/6/9.
 */
public interface UserTradeRateService {
    /**
     * 新增
     * @param userTradeRate
     */
    void insert(UserTradeRate userTradeRate);

    /**
     * 查询T1结算费率
     * @param dealerId
     * @return
     */
    UserTradeRateResponse getRateRang(long dealerId);

}
