package com.jkm.hss.admin.helper.responseparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/7.
 *
 * 代理商列表  入参
 */
@Data
public class AdminUserListResponse {
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
     * 公司名称
     */
    private String companyName;
    /**
     * 部门名称
     */
    private String deptName;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;
    /**
     * 角色名称
     */
    private String roleName;
    /**
     * 创建时间
     * datetime
     */
    protected Date createTime;
    /**
     * 状态
     * tinyint
     */
    protected int status;
}
