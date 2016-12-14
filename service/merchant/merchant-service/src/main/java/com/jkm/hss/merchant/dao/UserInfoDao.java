package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-05 11:29
 */
@Repository
public interface UserInfoDao {
    /**
     * 根据openId查询
     * @param openId
     * @return
     */
    UserInfo selectByOpenId(@Param("openId") String openId);

    /**
     * 新增用户信息
     * @return
     */
    int insertUserInfo(UserInfo userInfo);

    /**
     * 根据openId查询
     * @param id
     * @return
     */
    UserInfo selectById(@Param("id") long id);
    /**
     * 修改用户信息
     * @param merchantId
     * @return
     */
    int updataUserInfo(@Param("merchantId") long merchantId,@Param("mobile") String mobile,@Param("id") long id);
    /**
     * 根据merchantId查询
     * @param merchantId
     * @return
     */
    UserInfo selectByMerchantId(@Param("merchantId") long merchantId);
}
