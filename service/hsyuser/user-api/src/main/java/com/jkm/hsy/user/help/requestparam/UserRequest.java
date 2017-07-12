package com.jkm.hsy.user.help.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by Administrator on 2017/1/17.
 */

@Data
public class UserRequest    extends PageQueryParams {

    /**
     * 用户ID
     */
    private String uid;


    private String  uname;

    private String role;

    private  String cellphone;

    private String sid;

    private String status;

    private int offset;


    private int count;


}
