package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.List;

/**
 * Created by yulong.zhang on 2016/11/24.
 *
 * 后台登录 入参
 */
@Data
public class AdminUserLoginResponse {
    /**
     * 编码
     */
    private long id;
    /**
     *菜单名
     */
    private String menuName;
    /**
     * 路由地址
     */
    private String url;
    /**
     * 子菜单
     */
    private List<Menu> children;
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
        /**
         * 路由地址
         */
        private String url;
    }
}
