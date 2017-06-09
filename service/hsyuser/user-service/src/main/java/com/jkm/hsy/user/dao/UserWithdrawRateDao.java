package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.UserWithdrawRate;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/6/9.
 */
@Repository
public interface UserWithdrawRateDao {
    /**
     * 新增
     * @param userWithdrawRate
     */
    void insert(UserWithdrawRate userWithdrawRate);
}
