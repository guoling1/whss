package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.UserChannelPolicy;
import com.jkm.hsy.user.help.requestparam.UserChannelListResponse;
import com.jkm.hsy.user.help.requestparam.UserChannelPolicyResponse;
import com.jkm.hsy.user.help.requestparam.UserChannelPolicyUseResponse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Repository
public interface UserChannelPolicyDao {
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
     * 根据编码查询通道
     * @param channelTypeSign
     * @return
     */
    UserChannelPolicy selectByUserIdAndChannelTypeSign(@Param("userId") long userId,@Param("channelTypeSign") int channelTypeSign);
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
    List<UserChannelPolicyResponse> getUserChannelList(@Param("userId") long userId);
    /**
     * 用户通道列表
     * @param userId
     * @return
     */
    List<UserChannelPolicyUseResponse> getUserChannelByUserIdAndPolicyType(@Param("userId") long userId, @Param("policyType") String policyType);
    /**
     * 当前用户通道名称
     * @param userId
     * @return
     */
    UserChannelListResponse getCurrentChannelName(@Param("userId") long userId);
}
