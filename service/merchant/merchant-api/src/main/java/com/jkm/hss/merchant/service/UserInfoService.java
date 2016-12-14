package com.jkm.hss.merchant.service;

import com.google.common.base.Optional;
import com.jkm.hss.merchant.entity.UserInfo;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-05 11:39
 */
public interface UserInfoService {
    /**
     * 根据id查询
     * @param id
     * @return
     */
    Optional<UserInfo> selectById(long id);
    /**
     * 根据openId查询
     * @param openId
     * @return
     */
    Optional<UserInfo> selectByOpenId(String openId);

    /**
     * 新增用户信息
     * @return
     */
    int insertUserInfo(UserInfo userInfo);
    /**
     * 修改商户信息
     * @return
     */
    int uploadUserInfo(long merchantId,String mobile,long id);

    /**
     * 根据merchantId查询
     * @param merchantId
     * @return
     */
    Optional<UserInfo> selectByMerchantId(long merchantId);
}
