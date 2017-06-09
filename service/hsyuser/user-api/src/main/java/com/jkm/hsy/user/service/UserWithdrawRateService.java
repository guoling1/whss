package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.UserWithdrawRate;

/**
 * Created by xingliujie on 2017/6/9.
 */
public interface UserWithdrawRateService {
    /**
     * 新增
     * @param userWithdrawRate
     */
    void insert(UserWithdrawRate userWithdrawRate);
    /**
     * 根据用户编码查询
     * @param userId
     * @return
     */
    UserWithdrawRate selectByUserId(long userId);
}
