package com.jkm.hss.admin.service;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.admin.entity.AdminRole;
import com.jkm.hss.admin.helper.requestparam.AdminRoleListRequest;
import com.jkm.hss.admin.helper.requestparam.RoleDetailRequest;
import com.jkm.hss.admin.helper.responseparam.AdminMenuOptRelListResponse;
import com.jkm.hss.admin.helper.responseparam.AdminRoleListPageResponse;
import com.jkm.hss.admin.helper.responseparam.AdminRoleListResponse;
import com.jkm.hss.admin.helper.responseparam.AdminUserLoginResponse;

import java.util.List;

/**
 * Created by xingliujie on 2017/3/24.
 */
public interface AdminRoleService {
    /**
     * 插入
     *
     * @param adminRole
     */
    void insert(AdminRole adminRole);

    /**
     * 启用
     * @param id
     * @return
     */
    int enableRole(long id);

    /**
     * 禁用
     * @param id
     * @return
     */
    int disableRole(long id);

    /**
     * 根据分页查询角色个数
     * @param adminRoleListRequest
     * @return
     */
    long selectAdminRoleCountByPageParams(AdminRoleListRequest adminRoleListRequest);

    /**
     * 分页查询角色列表
     * @param adminRoleListRequest
     * @return
     */
    List<AdminRoleListPageResponse> selectAdminRoleListByPageParams(AdminRoleListRequest adminRoleListRequest);

    /**
     *分类查询角色列表
     * @param type
     * @return
     */
    List<AdminRoleListResponse> selectAdminRoleList(int type);

    /**
     *权限集合
     * @param type
     * @return
     */
    List<AdminMenuOptRelListResponse> getPrivilege(int type,long id);

    /**
     * 根据编码查询角色
     * @param id
     * @return
     */
    Optional<AdminRole> selectById(long id);
    /**
     * 插入
     *
     * @param roleDetailRequest
     */
    void save(RoleDetailRequest roleDetailRequest);

    /**
     * 角色列表
     * @param adminRoleListRequest
     * @return
     */
    PageModel<AdminRoleListPageResponse> roleListByPage(AdminRoleListRequest adminRoleListRequest);

    /**
     * 根据角色编码和类型查询登录菜单
     * @param roleId
     * @param type
     * @return
     */
    List<AdminUserLoginResponse> getLoginMenu(long roleId,int type);

    /**
     * 判断是否有接口访问权限
     * @param roleId
     * @param type
     * @param url
     * @param method
     * @return
     */
    int getPrivilegeByContions(long roleId,int type,String url,String method);
}
