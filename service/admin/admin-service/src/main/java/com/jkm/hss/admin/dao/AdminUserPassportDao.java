package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.AdminUserPassport;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by huangwei on 5/27/16.
 */
@Repository
public interface AdminUserPassportDao {

    /**
     * 保存
     *
     * @param adminUserToken
     * @return
     */
    int save(AdminUserPassport adminUserToken);

    /**
     * 根据后台用户id查找
     *
     * @param auid
     * @return
     */
    AdminUserPassport selectByAuid(long auid);

    /**
     * 更新
     *
     * @param adminUserToken
     * @return
     */
    int update(AdminUserPassport adminUserToken);

    /**
     * 根据token查找
     *
     * @param token
     * @return
     */
    AdminUserPassport selectByToken(String token);

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
    int updateStatus(@Param("auid") long auid, @Param("status") int status);
}
