package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.AdminMenu;
import com.jkm.hss.admin.entity.AdminMenuOptRel;
import com.jkm.hss.admin.entity.AdminRole;
import com.jkm.hss.admin.entity.AdminRoleMenuRel;
import com.jkm.hss.admin.helper.requestparam.AdminRoleListRequest;
import com.jkm.hss.admin.helper.responseparam.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Repository
public interface AdminRoleDao {
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
    int enableRole(@Param("id") long id);

    /**
     * 禁用
     * @param id
     * @return
     */
    int disableRole(@Param("id") long id);

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
    List<AdminRoleListResponse> selectAdminRoleList(@Param("type") int type);

    /**
     * 查询角色信息
     * @param id
     * @return
     */
    AdminRole selectById(@Param("id") long id);

    /**
     * 根据父编码、类型查询菜单
     * @param parentId
     * @return
     */
    List<AdminMenu> getMenuByParentIdAndType(@Param("parentId") long parentId,@Param("type") int type);

    /**
     * 根据菜单编码、类型查询菜单
     * @param menuId
     * @return
     */
    List<AdminOptResponse> getOptByMenuIdAndType(@Param("menuId") long menuId, @Param("type") int type);

    /**
     * 根据父编码、类型、角色编码查询菜单
     * @param parentId
     * @return
     */
    List<AdminMenuResponse> getMenuByParentIdAndTypeAndRoleId(@Param("parentId") long parentId, @Param("type") int type, @Param("roleId") long roleId);
    /**
     * 根据菜单编码、类型、操作编码查询菜单
     * @param menuId
     * @return
     */
    List<AdminOptRelResponse> getOptByMenuIdAndTypeAndUserId(@Param("menuId") long menuId, @Param("type") int type);

    /**
     * 根据角色编码删除角色菜单关联
     * @param roleId
     */
    void deleteRoleAndMenuByRoleId(@Param("roleId") long roleId);
    /**
     * 根据角色编码删除菜单操作关联
     * @param roleId
     */
    void deleteMenuAndOptByRoleId(@Param("roleId") long roleId);

    /**
     * 新增角色菜单
     * @param adminRoleMenuRel
     */
    void insertRoleAndMenuRel(AdminRoleMenuRel adminRoleMenuRel);

    /**
     * 新增菜单权限
     * @param adminMenuOptRel
     */
    void insertMenuAndOptRel(AdminMenuOptRel adminMenuOptRel);

    /**
     * 修改角色名
     * @param roleName
     * @param id
     * @return
     */
    int updateRoleNameById(@Param("roleName") String roleName,@Param("id") long id);

    /**
     * 根据角色编码和类型查询登录菜单
     * @param parentId
     * @param type
     * @param roleId
     * @return
     */
    List<MenuResponse> getLoginMenu(@Param("parentId") long parentId, @Param("type") int type,@Param("roleId") long roleId);

    /**
     * 判断是否有接口访问权限
     * @param roleId
     * @param type
     * @param url
     * @param method
     * @return
     */
    int getPrivilegeByContions(@Param("roleId") long roleId,@Param("type") int type,@Param("url") String url,@Param("method") String method);
}
