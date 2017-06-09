package com.jkm.hsy.user.service.impl;

import com.jkm.hsy.user.dao.UserCurrentChannelPolicyDao;
import com.jkm.hsy.user.entity.UserCurrentChannelPolicy;
import com.jkm.hsy.user.service.UserCurrentChannelPolicyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Slf4j
@Service
public class UserCurrentChannelPolicyServiceImpl implements UserCurrentChannelPolicyService {
    @Autowired
    private UserCurrentChannelPolicyDao userCurrentChannelPolicyDao;
    /**
     * 新增
     *
     * @param userCurrentChannelPolicy
     */
    @Override
    public void insert(UserCurrentChannelPolicy userCurrentChannelPolicy) {
        userCurrentChannelPolicyDao.insert(userCurrentChannelPolicy);
    }
}
