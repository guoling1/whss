package com.jkm.hss.admin.helper.requestparam;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.admin.enums.EnumAdminUserStatus;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Data
public class AdminUserRequest{
    /**
     * 编码
     */
    private long id;

    /**
     * 登录名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 公司编码
     */
    private Long companyId;
    /**
     * 部门编码
     */
    private Long deptId;
    /**
     * 用户姓名
     */
    private String realname;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 身份证正面照
     */
    private String identityFacePic;
    /**
     * 身份证背面照
     */
    private String identityOppositePic;
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
    private Long roleId;

}
