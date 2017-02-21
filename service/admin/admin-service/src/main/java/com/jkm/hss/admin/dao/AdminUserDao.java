package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by huangwei on 5/27/16.
 */
@Repository
public interface AdminUserDao {

    /**
     * 插入
     *
     * @param adminUser
     */
    void insert(AdminUser adminUser);

    /**
     * 更新
     *
     * @param adminUser
     * @return
     */
    int update(AdminUser adminUser);

    /**
     * 根据主键查找
     *
     * @param id
     * @return
     */
    AdminUser selectById(@Param("id") long id);

    /**
     * 根据用户名查找
     *
     * @param username
     * @return
     */
    AdminUser selectByUsername(@Param("username") String username);

    /**
     * 修改密码
     *
     * @param adminUser
     * @return
     */
    int updatePwd(AdminUser adminUser);
    /**
     * 禁用或启用
     *
     * @param adminUser
     * @return
     */
    int enableOrDisable(AdminUser adminUser);

}
