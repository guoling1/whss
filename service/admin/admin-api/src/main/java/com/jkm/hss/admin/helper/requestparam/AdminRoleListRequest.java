package com.jkm.hss.admin.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import com.jkm.hss.admin.enums.EnumAdminType;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Data
public class AdminRoleListRequest extends PageQueryParams {
    /**
     * 角色名
     */
    private String roleName;
    /**
     * 类型
     * {@link EnumAdminType}
     */
    private int type;

    private int offset;

    private int count;
}
