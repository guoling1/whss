package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.PcUserPassport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * Created by huangwei on 5/27/16.
 */
@Repository
public interface PcUserPassportDao {

    /**
     * 保存
     *
     * @param pcUserPassport
     * @return
     */
    int add(PcUserPassport pcUserPassport);

    /**
     * 更新
     *
     * @param adminUserToken
     * @return
     */
    int update(PcUserPassport adminUserToken);

    /**
     * 更新过期时间
     *
     * @param id
     * @param expireTime
     * @return
     */
    int updateExpireTime(@Param("id") long id, @Param("expireTime") long expireTime);

    /**
     * 更新状态
     *
     * @param auid
     * @param status
     * @return
     */
    int updateStatus(@Param("uid") long auid, @Param("status") int status);

    /**
     * 根据后台用户id查找
     *
     * @param uid
     * @return
     */
    PcUserPassport selectByUid(@Param("uid") long uid);

    /**
     * 根据token查找
     *
     * @param token
     * @return
     */
    PcUserPassport selectByToken(@Param("token") String token);

    /**
     * 更新登录时间
     *
     * @param id
     * @param date
     */
    void updateLastLoginDate(@Param("id") long id, @Param("date") Date date);

    /**
     * 登出
     *
     * @param id
     * @param status
     */
    void markLogout(@Param("id") long id, @Param("status") int status);
}
