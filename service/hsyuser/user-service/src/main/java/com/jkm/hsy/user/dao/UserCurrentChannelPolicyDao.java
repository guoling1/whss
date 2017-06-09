package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.UserCurrentChannelPolicy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Repository
public interface UserCurrentChannelPolicyDao {
    /**
     * 新增
     * @param userCurrentChannelPolicy
     */
    void insert(UserCurrentChannelPolicy userCurrentChannelPolicy);
    /**
     * 根据UserId查询当前法人所选通道
     * @param userId
     */
    UserCurrentChannelPolicy selectByUserId(@Param("userId") long userId);
}
