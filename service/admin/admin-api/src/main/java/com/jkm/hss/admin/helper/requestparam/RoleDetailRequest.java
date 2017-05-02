package com.jkm.hss.admin.helper.requestparam;

import com.jkm.hss.admin.helper.responseparam.AdminMenuOptRelListResponse;
import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/3/27.
 */
@Data
public class RoleDetailRequest {
    /**
     * 角色编码
     */
    private long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     *员工类型
     * {@link com.jkm.hss.admin.enums.EnumAdminType}
     */
    private int type;
    /**
     * 权限列表
     */
    private List<AdminMenuOptRelListResponse> list;
}
