package com.jkm.hss.admin.helper.responseparam;

import com.jkm.hss.admin.enums.EnumIsSelected;
import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Data
public class AdminMenuOptRelListResponse {
    /**
     * 编码
     */
    private long id;
    /**
     * 是否选中（1选中 2未选中）
     * {@link EnumIsSelected}
     */
    private int isSelected;
    /**
     *菜单名
     */
    private String menuName;
    /**
    /**
     * 子菜单
     */
    private List<Menu> children;

    /**
     * 操作
     */
    private List<Opt> opts;

    @Data
    public static class Menu {
        /**
         * 编码
         */
        private long id;
        /**
         * 是否选中（1选中 2未选中）
         * {@link EnumIsSelected}
         */
        private int isSelected;
        /**
         *菜单名
         */
        private String menuName;
        /**
         * 操作集合
         */
        private List<Opt> opts;
    }

    @Data
    public static class Opt {
        /**
         * 编码
         */
        private long id;
        /**
         * 是否选中（1选中 2未选中）
         * {@link EnumIsSelected}
         */
        private int isSelected;

        /**
         * 操作集合
         */
        private String optName;

    }

}
