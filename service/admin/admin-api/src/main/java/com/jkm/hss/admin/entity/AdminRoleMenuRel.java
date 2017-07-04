package com.jkm.hss.admin.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/27.
 */
@Data
public class AdminRoleMenuRel extends BaseEntity {
    /**
     * 角色编码
     */
    private long roleId;

    /**
     * 菜单编码
     */
    private long menuId;

}
