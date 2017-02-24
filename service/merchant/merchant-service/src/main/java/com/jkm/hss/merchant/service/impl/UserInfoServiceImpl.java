package com.jkm.hss.merchant.service.impl;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.dao.UserInfoDao;
import com.jkm.hss.merchant.entity.MerchantInfoCheckRecord;
import com.jkm.hss.merchant.entity.UserInfo;
import com.jkm.hss.merchant.service.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-05 11:40
 */
@Slf4j
@Service
public class UserInfoServiceImpl implements UserInfoService{
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public Optional<UserInfo> selectById(long id) {
        return Optional.fromNullable(userInfoDao.selectById(id));
    }

    @Override
    public Optional<UserInfo> selectByOpenId(String openId) {
        return Optional.fromNullable(userInfoDao.selectByOpenId(openId));
    }

    @Override
    public int insertUserInfo(UserInfo userInfo) {
        return userInfoDao.insertUserInfo(userInfo);
    }

    @Override
    public Optional<UserInfo> selectByMerchantId(long merchantId) {
        return Optional.fromNullable(userInfoDao.selectByMerchantId(merchantId));
    }

    /**
     * 插入markCode
     *
     * @param markCode
     * @param id
     * @return
     */
    @Override
    public int updatemarkCode(String markCode, long id) {
        return userInfoDao.updatemarkCode(markCode,id);
    }

    /**
     * 根据手机号查询
     *
     * @param mobile
     * @return
     */
    @Override
    public Optional<UserInfo> selectByMobile(String mobile) {
        return Optional.fromNullable(userInfoDao.selectByMobile(mobile));
    }

    /**
     * 清除openId
     *
     * @param id
     * @return
     */
    @Override
    public int cleanOpenId(long id) {
        return userInfoDao.cleanOpenId(id);
    }

    /**
     * 绑定openId
     * @param id
     * @param openId
     * @return
     */
    @Override
    public int bindOpenId(long id,String openId) {
        return userInfoDao.bindOpenId(id,openId);
    }

    @Override
    public MerchantInfoCheckRecord selectDesr(long merchantId) {
        MerchantInfoCheckRecord desrc=userInfoDao.selectDesr(merchantId);
        return desrc;
    }

    @Override
    public int uploadUserInfo(long merchantId,String mobile,long id) {
        return userInfoDao.updataUserInfo(merchantId,mobile,id);
    }
}
