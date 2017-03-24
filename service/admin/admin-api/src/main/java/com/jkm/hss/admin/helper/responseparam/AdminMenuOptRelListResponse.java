package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Data
public class AdminMenuOptRelListResponse {
    /**
     *菜单名
     */
    private String menuName;
    /**
     * 菜单地址
     */
    private String menuUrl;
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
         *菜单名
         */
        private String menuName;
    }

    @Data
    public static class Opt {
        /**
         * 编码
         */
        private long id;

        /**
         *操作名
         */
        private String optName;

    }

}
