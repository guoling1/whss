package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.MerchantInfoCheckRecord;
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

    /**
     * 插入markCode
     * @param markCode
     * @param id
     * @return
     */
    int updatemarkCode(@Param("markCode") String markCode,@Param("id") long id);

    /**
     * 根据手机号查询
     * @param mobile
     * @return
     */
    UserInfo selectByMobile(@Param("mobile") String mobile);

    /**
     * 清除openId
     *
     * @param id
     * @return
     */
    int cleanOpenId(@Param("id") long id);
    /**
     * 绑定openId
     * @param id
     * @param openId
     * @return
     */
    int bindOpenId(@Param("id") long id,@Param("openId") String openId);

    /**
     * 查询审核失败原因
     * @param merchantId
     * @return
     */
    MerchantInfoCheckRecord selectDesr(@Param("merchantId") long merchantId);
}
