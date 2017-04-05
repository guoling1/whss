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
public class AdminDealerUser extends BaseEntity {
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
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String email;

    /**
     * 代理商名称
     */
    private String belongDealer;
    /**
     * 角色编码
     */
    private long roleId;

    /**
     * 是否有全部菜单权限
     * 1.是 2不是
     * {@link com.jkm.hss.admin.enums.EnumIsMaster}
     */
    private int isMaster;

}
