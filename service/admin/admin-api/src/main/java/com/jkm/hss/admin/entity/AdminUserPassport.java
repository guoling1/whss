package com.jkm.hss.admin.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.admin.helper.AdminTokenHelper;
import lombok.Data;

/**
 * Created by huangwei on 6/3/15.
 * 后台用户登录信息
 * tb_admin_user_passport
 *
 * FIXME 用缓存
 */
@Data
public class AdminUserPassport extends BaseEntity {
    /**
     * 后台用户id
     */
    private long auid;
    /**
     * 加密token
     */
    private String token;
    /**
     * 过期时间
     */
    private long expireTime;

    public void setAuid(long auid) {
        this.auid = auid;
        this.token = AdminTokenHelper.generateToken(auid);
    }

    /**
     * 判断token是否已过期
     *
     * @return
     */
    public boolean isExpire() {
        return getExpireTime() < System.currentTimeMillis();
    }

    /**
     * 判断状态是否正常
     *
     * @return
     */
    public boolean isAvailable() {
        return status == 1;
    }
}
