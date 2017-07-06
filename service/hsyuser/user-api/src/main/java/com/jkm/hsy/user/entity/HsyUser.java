package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by longwen.jiang on 2017/1/12.
 *
 * 用户信息
 */

@Data
public class HsyUser  extends BaseEntity {



    /**
     * 电话
     */
    private String cellphone;
    /**
     * 密码
     */
    private String password;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 身份证号码
     */
    private  String idcard;
    /**
     * 身份证正面照
     */
    private String idcardf;
    /**
     * 身份证背面照
     */
    private String idcardb;
    /**
     * 身份证手持照
     */
    private String idcardh;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
