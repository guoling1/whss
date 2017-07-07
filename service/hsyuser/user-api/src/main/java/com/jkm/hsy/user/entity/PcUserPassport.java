package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hsy.user.help.PcTokenHelper;
import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/7/4.
 *
 * app用户 pc端登录令牌
 *
 * tb_app_pc_user_passport
 */
@Data
public class PcUserPassport extends BaseEntity {

    /**
     * app用户id
     */
    private long uid;
    /**
     * 加密token
     */
    private String token;
    /**
     * 过期时间
     */
    private long expireTime;
    /**
     * 最后一次登录时间
     */
    private Date lastLoginDate;

    /**
     * 登录状态
     */
    private int loginStatus;

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
