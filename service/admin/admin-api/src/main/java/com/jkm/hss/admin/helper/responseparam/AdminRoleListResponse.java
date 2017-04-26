package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Data
public class AdminRoleListResponse {
    /**
     * 角色编码
     */
    private long id;
    /**
     * 角色名称
     */
    private String roleName;
}
