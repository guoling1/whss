package com.jkm.hsy.user.service;

import com.google.common.base.Optional;
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
     * 修改
     * @param userWithdrawRate
     */
    void update(UserWithdrawRate userWithdrawRate);
    /**
     * 根据用户编码查询
     * @param userId
     * @return
     */
    Optional<UserWithdrawRate> selectByUserId(long userId);
}
