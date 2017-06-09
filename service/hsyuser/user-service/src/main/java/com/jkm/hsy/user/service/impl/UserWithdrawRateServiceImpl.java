package com.jkm.hsy.user.service.impl;

import com.jkm.hsy.user.dao.UserWithdrawRateDao;
import com.jkm.hsy.user.entity.UserWithdrawRate;
import com.jkm.hsy.user.service.UserWithdrawRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xingliujie on 2017/6/9.
 */
@Service
public class UserWithdrawRateServiceImpl implements UserWithdrawRateService{
    @Autowired
    private UserWithdrawRateDao userWithdrawRateDao;
    /**
     * 新增
     *
     * @param userWithdrawRate
     */
    @Override
    public void insert(UserWithdrawRate userWithdrawRate) {
        userWithdrawRateDao.insert(userWithdrawRate);
    }
}
