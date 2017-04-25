package com.jkm.hss.admin.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Data
public class AdminRole extends BaseEntity {
    /**
     *角色名
     */
    private String roleName;
    /**
     *描述
     */
    private String descr;
    /**
     * 类型
     *{@link com.jkm.hss.admin.enums.EnumAdminType}
     */
    private int type;
}
