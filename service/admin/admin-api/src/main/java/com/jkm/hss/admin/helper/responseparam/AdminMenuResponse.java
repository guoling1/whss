package com.jkm.hss.admin.helper.responseparam;

import com.jkm.hss.admin.enums.EnumIsSelected;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/27.
 */
@Data
public class AdminMenuResponse {
    /**
     * 主键id
     */
    protected long id;
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
    /**
     * 是否选中（1选中 2未选中）
     * {@link EnumIsSelected}
     */
    private int isSelected;
}
