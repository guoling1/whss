package com.jkm.hss.admin.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Data
public class AdminMenu extends BaseEntity {
    /**
     *父编码
     */
    private long parentId;
    /**
     *菜单名
     */
    private String menuName;

    /**
     * 菜单地址
     */
    private String menuUrl;

    /**
     * 描述
     */
    private String descr;

    /**
     * 排序
     */
    private int sn;

    /**
     * 类型
     * {@link com.jkm.hss.admin.enums.EnumAdminType}
     */
    private int type;
}
