package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.UserWithdrawRate;
import org.apache.ibatis.annotations.Param;
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

    /**
     * 根据用户编码查询
     * @param userId
     * @return
     */
    UserWithdrawRate selectByUserId(@Param("userId") long userId);
}
