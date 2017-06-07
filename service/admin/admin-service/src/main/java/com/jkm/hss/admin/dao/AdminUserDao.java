package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.AdminUser;
import com.jkm.hss.admin.helper.requestparam.AdminDealerUserListRequest;
import com.jkm.hss.admin.helper.requestparam.AdminUserListRequest;
import com.jkm.hss.admin.helper.responseparam.AdminDealerUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * 根据用户名和类型获取
     *
     * @param username
     * type是多余数据
     * @return
     */
    AdminUser selectByUsernameAndType(@Param("username") String username,@Param("type") int type);
    /**
     * 根据用户名和类型获取
     *
     * @param username
     * @return
     */
    AdminUser selectDealerLoginNameByUsername(@Param("username") String username);
    /**
     * 根据用户名和类型获取
     *
     * @param username
     * @return
     */
    AdminUser getAdminUserByNameAndTypeUnIncludeNow(@Param("username") String username,@Param("type") int type,@Param("dealerId") long dealerId);
    /**
     * 代理商根据用户名和类型获取
     *
     * @param username
     * @return
     */
    AdminUser getAdminDealerUserByNameUnIncludeNow(@Param("username") String username,@Param("dealerId") long dealerId);

    /**
     * 修改密码
     * @param password
     * @param id
     * @return
     */
    int updatePwd(@Param("salt") String salt,@Param("password") String password,@Param("id") long id);
    /**
     * 禁用或启用
     *
     * @param adminUser
     * @return
     */
    int enableOrDisable(AdminUser adminUser);

    /**
     * 更新markCode
     * @param markCode
     * @param id
     * @return
     */
    int updateMarkCode(@Param("markCode") String markCode,@Param("id") long id);

    /**
     * 员工数量
     * @param adminUserListRequest
     * @return
     */
    long selectAdminUserCountByPageParams(AdminUserListRequest adminUserListRequest);

    /**
     * 分页查询员工列表
     * @param adminUserListRequest
     * @return
     */
    List<AdminUser> selectAdminUserListByPageParams(AdminUserListRequest adminUserListRequest);

    /**
     * 代理商员工数量
     * @param adminDealerUserListRequest
     * @return
     */
    long selectAdminUserDealerCountByPageParams(AdminDealerUserListRequest adminDealerUserListRequest);

    /**
     * 代理商分页查询员工列表
     * @param adminDealerUserListRequest
     * @return
     */
    List<AdminDealerUser> selectAdminUserDealerListByPageParams(AdminDealerUserListRequest adminDealerUserListRequest);

    /**
     * 分公司员工数量
     * @param adminDealerUserListRequest
     * @return
     */
    long selectAdminUserOemCountByPageParams(AdminDealerUserListRequest adminDealerUserListRequest);

    /**
     * 分公司分页查询员工列表
     * @param adminDealerUserListRequest
     * @return
     */
    List<AdminDealerUser> selectAdminUserOemListByPageParams(AdminDealerUserListRequest adminDealerUserListRequest);


    /**
     *
     * @param username
     * @param id
     * @return
     */
    Long selectDealerByUsernameUnIncludeNow(@Param("username") String username,@Param("id") long id);

    /**
     *
     * @param username
     * @param id
     * type是多余数据，因为登录名全局唯一
     * @return
     */
    Long selectByUsernameAndTypeUnIncludeNow(@Param("username") String username,@Param("type") int type,@Param("id") long id);

    /**
     * 最后一次登陆时间
     * @param id
     * @return
     */
    void updateLastLoginDate(@Param("id") long id);

    /**
     * 修改一级代理商管理账户
     *
     * @param adminUser
     * @return
     */
    void updateDealerUser(AdminUser adminUser);

    /**
     * 修改代理商登录密码
     * @param pwd
     * @param dealerId
     */
    void updateDealerUserPwd(@Param("pwd") String pwd, @Param("dealerId") long dealerId);

    /**
     * 根据用户编码修改密码
     * @param pwd
     * @param id
     */
    void updateDealerUserPwdById(@Param("pwd") String pwd, @Param("id") long id);

    /**
     * 根据代理商编码和是否有所有权限查询代理商
     * @param dealerId
     * @param isMaster
     * @return
     */
    AdminUser getAdminUserByDealerIdAndIsMaster(@Param("dealerId") long dealerId,@Param("isMaster") int isMaster);
    /**
     * 获取密码
     * @param id
     * @return
     */
    String getPwd(@Param("id") long id);

    /**
     * 获取密码
     * @param id
     * @return
     */
    String getPwd(@Param("id") long id);

}
