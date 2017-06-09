package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.UserChannelPolicy;
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
    UserChannelPolicy selectByChannelTypeSign(int channelTypeSign);
}
