package com.jkm.hss.admin.entity;

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
public class AdminUser extends BaseEntity {

    /**
     * 登录名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 密码hash salt
     */
    private String salt;
    /**
     * 用户姓名
     */
    private String realname;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;

    /**
     * 公司编码
     */
    private String companyId;
    /**
     * 部门编码
     */
    private String deptId;
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 上次登录时间
     */
    private Date lastLoginDate;
    /**
     * 角色编码
     */
    private Long roleId;
    /**
     * 员工编码
     */
    private String markCode;
    /**
     * 身份证正面照
     */
    private String identityFacePic;
    /**
     * 身份证背面照
     */
    private String identityOppositePic;

    /**
     * 用户是否被激活
     *
     * @return
     */
    public boolean isActive() {
        return this.status == EnumAdminUserStatus.NORMAL.getCode();
    }

    public boolean checkPassword(final String password) {
        return StringUtils.equals(DigestUtils.sha256Hex(password + this.getSalt()), this.password);
    }
}
