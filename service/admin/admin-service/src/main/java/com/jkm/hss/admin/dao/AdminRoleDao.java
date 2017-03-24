package com.jkm.hss.admin.dao;

import com.jkm.hss.admin.entity.AdminRole;
import com.jkm.hss.admin.helper.requestparam.AdminRoleListRequest;
import com.jkm.hss.admin.helper.responseparam.AdminRoleListResponse;
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
    List<AdminRole> selectAdminRoleListByPageParams(AdminRoleListRequest adminRoleListRequest);

    /**
     *分类查询角色列表
     * @param type
     * @return
     */
    List<AdminRoleListResponse> selectAdminRoleList(@Param("id") int type);
}
