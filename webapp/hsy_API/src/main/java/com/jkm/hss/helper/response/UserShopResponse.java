package com.jkm.hss.helper.response;

/**
 * Created by longwen.jiang on 2017/1/13.
 */

import lombok.Data;

@Data
public class UserShopResponse {


    /**
     * 店铺ID
     */
    private Long sid;
    /**
     * 店铺名称
     */
    private String sname;
    /**
     * 用户id
     */
    private Long  uid;



    /**
     * 用户角色
     */
    private int role;

    /**
     * 用户状态
     */
    private int status;


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
}
