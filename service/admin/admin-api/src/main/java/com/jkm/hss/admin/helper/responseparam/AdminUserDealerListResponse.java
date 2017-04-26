package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/7.
 *
 * 代理商列表  入参
 */
@Data
public class AdminUserDealerListResponse {
    /**
     * 编号
     */
    private long id;
    /**
     * 员工编码
     */
    private String markCode;

    /**
     * 登录名
     */
    private String username;

    /**
     * 用户姓名
     */
    private String realname;
    /**
     * 所属代理商
     */
    private String belongDealer;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 角色编码
     */
    private long roleId;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 创建时间
     * datetime
     */
    private Date createTime;
    /**
     * 状态
     * tinyint
     */
    private int status;
    /**
     * 是否有全部菜单权限
     * 1.是 2不是
     * {@link com.jkm.hss.admin.enums.EnumIsMaster}
     */
    private int isMaster;
}
