package com.jkm.hsy.user.service;

import com.google.common.base.Optional;
import com.jkm.hsy.user.entity.UserChannelPolicy;
import com.jkm.hsy.user.help.requestparam.UserChannelListResponse;
import com.jkm.hsy.user.help.requestparam.UserChannelPolicyResponse;
import com.jkm.hsy.user.help.requestparam.UserChannelPolicyUseResponse;

import java.util.List;

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

    /**
     * 用户通道列表
     * @param userId
     * @return
     */
    List<UserChannelPolicyResponse> getUserChannelList(long userId);
    /**
     * 查询用户通道列表
     * @param userId
     * @return
     */
    List<UserChannelPolicyUseResponse> getUserChannelByUserIdAndPolicyType(long userId, String policyType);
    /**
     * 当前用户通道名称
     * @param userId
     * @return
     */
    UserChannelListResponse getCurrentChannelName(long userId);

    /**
     * 根据用户编码和通道编码查询
     * @param userId
     * @param channelTypeSign
     * @return
     */
    Optional<UserChannelPolicy> selectByUserIdAndChannelTypeSign(long userId,int channelTypeSign);

}
