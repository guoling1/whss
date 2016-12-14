package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-12-05 11:24
 */
@Data
public class UserInfo extends BaseEntity {

    /**
     * OPENID 微信的
     */
    private String openId;
    /**
     * 商户编码
     */
    private long merchantId=0;

    /**
     * 角色编码
     */
    private long roleId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String pwd;

}
