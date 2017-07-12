package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.servcie.BasicChannelService;
import com.jkm.hsy.user.dao.UserChannelPolicyDao;
import com.jkm.hsy.user.dao.UserCurrentChannelPolicyDao;
import com.jkm.hsy.user.entity.UserChannelPolicy;
import com.jkm.hsy.user.help.requestparam.UserChannelListResponse;
import com.jkm.hsy.user.help.requestparam.UserChannelPolicyResponse;
import com.jkm.hsy.user.help.requestparam.UserChannelPolicyUseResponse;
import com.jkm.hsy.user.service.UserChannelPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Slf4j
@Service
public class UserChannelPolicyServiceImpl implements UserChannelPolicyService{
    @Autowired
    private UserChannelPolicyDao userChannelPolicyDao;

    /**
     * 新增
     *
     * @param userChannelPolicy
     */
    @Override
    public void insert(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.insert(userChannelPolicy);
    }
    /**
     * 修改
     *
     * @param userChannelPolicy
     */
    @Override
    public void updateInit(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateInit(userChannelPolicy);
    }


    /**
     * 根据用户编码和通道编码修改
     * @param userChannelPolicy
     */
    @Override
    public void updateByUserIdAndChannelTypeSign(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateByUserIdAndChannelTypeSign(userChannelPolicy);
    }

    /**
     * 修改华夏入网信息
     *
     * @param userChannelPolicy
     */
    @Override
    public void updateHxNetInfo(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateHxNetInfo(userChannelPolicy);
    }

    /**
     * 修改华夏开通产品信息
     *
     * @param userChannelPolicy
     */
    @Override
    public void updateHxOpenProduct(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateHxOpenProduct(userChannelPolicy);
    }

    /**
     * 修改华夏开通微信产品信息
     *
     * @param userChannelPolicy
     */
    @Override
    public void updateHxOpenWxProduct(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateHxOpenWxProduct(userChannelPolicy);
    }
    /**
     * 修改华夏开通支付宝产品信息
     *
     * @param userChannelPolicy
     */
    @Override
    public void updateHxOpenAlipayProduct(UserChannelPolicy userChannelPolicy) {
        userChannelPolicyDao.updateHxOpenAlipayProduct(userChannelPolicy);
    }
    /**
     * 用户通道列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserChannelPolicyResponse> getUserChannelList(long userId) {
        return userChannelPolicyDao.getUserChannelList(userId);
    }

    /**
     * 用户通道列表
     *
     * @param userId
     * @param policyType
     * @return
     */
    @Override
    public List<UserChannelPolicyUseResponse> getUserChannelByUserIdAndPolicyType(long userId, String policyType) {
        return userChannelPolicyDao.getUserChannelByUserIdAndPolicyType(userId,policyType);
    }

    /**
     * 当前用户通道名称
     *
     * @param userId
     * @return
     */
    @Override
    public UserChannelListResponse getCurrentChannelName(long userId) {
        return userChannelPolicyDao.getCurrentChannelName(userId);
    }

    /**
     * 根据用户编码和通道编码查询
     *
     * @param userId
     * @param channelTypeSign
     * @return
     */
    @Override
    public Optional<UserChannelPolicy> selectByUserIdAndChannelTypeSign(long userId, int channelTypeSign) {
        return Optional.fromNullable(userChannelPolicyDao.selectByUserIdAndChannelTypeSign(userId,channelTypeSign));
    }

    /**
     * 根据AppId查找AppSecret
     *
     * @param appId
     * @return
     */
    @Override
    public String selectAppSecretByAppId(String appId) {
        return userChannelPolicyDao.selectAppSecretByAppId(appId);
    }


}
