package com.jkm.hsy.user.service;

import com.google.common.base.Optional;
import com.jkm.hsy.user.entity.UserCurrentChannelPolicy;

/**
 * Created by xingliujie on 2017/6/8.
 */
public interface UserCurrentChannelPolicyService {
    /**
     * 新增
     * @param userCurrentChannelPolicy
     */
    void insert(UserCurrentChannelPolicy userCurrentChannelPolicy);

    /**
     * 根据用户编码查询
     * @param userId
     * @return
     */
    Optional<UserCurrentChannelPolicy> selectByUserId(long userId);

    /**
     * 修改商户当前使用通道
     * @param userCurrentChannelPolicy
     */
    void updateByUserId(UserCurrentChannelPolicy userCurrentChannelPolicy);
}
