package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Data
public class DealerNewPwdRequest {
    /**
     * 代理商编码
     */
    private long dealerId;

    /**
     * 原密码
     */
    private String pwd;
    /**
     * 新密码
     */
    private String newPwd;
    /**
     * 重复新密码
     */
    private String repeatNewPwd;

}
