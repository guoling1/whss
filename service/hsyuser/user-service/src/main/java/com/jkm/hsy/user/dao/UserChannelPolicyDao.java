package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.UserChannelPolicy;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
    UserChannelPolicy selectByChannelTypeSign(@Param("channelTypeSign") int channelTypeSign);
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
