package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/3/28.
 */
@Data
public class MenuResponse {
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
    private String menuUrl;
}
