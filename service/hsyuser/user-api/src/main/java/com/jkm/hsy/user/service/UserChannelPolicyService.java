package com.jkm.hsy.user.service;

import com.jkm.hsy.user.entity.UserChannelPolicy;

/**
 * Created by xingliujie on 2017/6/8.
 */
public interface UserChannelPolicyService {
    /**
     * 新增
     * @param userChannelPolicy
     */
    void insert(UserChannelPolicy userChannelPolicy);
    /**
     * 修改
     * @param userChannelPolicy
     */
    void updateInit(UserChannelPolicy userChannelPolicy);

    /**
     * 初始化商户通道
     * @param userId
     */
    void initChannel(long userId);
}
