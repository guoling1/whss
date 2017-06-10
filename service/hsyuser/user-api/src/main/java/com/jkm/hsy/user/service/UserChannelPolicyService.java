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
    /**
     * 根据用户编码和通道编码修改
     * @param userChannelPolicy
     */
    void updateByUserIdAndChannelTypeSign(UserChannelPolicy userChannelPolicy);
    /**
     * 修改华夏入网信息
     * @param userChannelPolicy
     */
    void updateHxNetInfo(UserChannelPolicy userChannelPolicy);
    /**
     * 修改华夏开通产品信息
     * @param userChannelPolicy
     */
    void updateHxOpenProduct(UserChannelPolicy userChannelPolicy);


}
