package com.jkm.hss.admin.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Data
public class AdminOpt extends BaseEntity {
    /**
     * 菜单编码
     */
    private long menuId;
    /**
     * 菜单名称
     */
    private String optName;
    /**
     * 展示名称
     */
    private String showName;
    /**
     * 请求地址
     */
    private String url;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 描述
     */
    private String descr;
    /**
     * 类型
     * {@link com.jkm.hss.admin.enums.EnumAdminType}
     */
    private int type;
}
