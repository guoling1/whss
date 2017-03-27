package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

import java.util.Date;

/**
 * Created by xingliujie on 2017/3/24.
 */
@Data
public class AdminRoleListPageResponse {
    /**
     * 角色编码
     */
    private long id;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     *
     */
    private String status;
}
