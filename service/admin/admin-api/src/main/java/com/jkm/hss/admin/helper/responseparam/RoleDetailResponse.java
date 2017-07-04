package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/3/27.
 */
@Data
public class RoleDetailResponse {
    /**
     * 角色编码
     */
    private long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 权限列表
     */
    private List<AdminMenuOptRelListResponse> list;
}
