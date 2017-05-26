package com.jkm.hss.dealer.helper.requestparam;
import lombok.Data;

/**
 * Created by xingliujie on 2017/5/24.
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
