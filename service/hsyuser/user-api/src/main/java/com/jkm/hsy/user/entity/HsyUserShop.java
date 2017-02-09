package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by longwen.jiang on 2017/1/12.
 */

@Data
public class HsyUserShop  extends BaseEntity {


    /**
     * 店铺ID
     */
    private long sid;
    /**
     * 店铺名称
     */
    private String sname;
    /**
     * 用户id
     */
    private long  uid;
    /**
     * 用户
     */
    private HsyUser user;
    /**
     * 用户角色
     */
    private int role;
}
