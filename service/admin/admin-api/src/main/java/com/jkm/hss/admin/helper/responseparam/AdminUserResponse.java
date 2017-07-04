package com.jkm.hss.admin.helper.responseparam;

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
public class AdminUserResponse {
    /**
     * 主键id
     */
    protected long id;
    /**
     * 状态
     * tinyint
     */
    protected int status;

    /**
     * 登录名
     */
    private String username;
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
     * 角色编码
     */
    private Long roleId;

    /**
     * 身份证正面照
     */
    private String identityFacePic;
    /**
     * 身份证背面照
     */
    private String identityOppositePic;

    /**
     * 真实身份证背面
     */
    private String realIdentityOppositePic;
    /**
     * 真实身份证正面
     */
    private String realIdentityFacePic;
    /**
     *
     */
    private int type;

}
