package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/7/28.
 */
@Data
public class LoginCodeRequest {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 分公司编码
     */
    private String oemNo;
    /**
     * 类型（1注册 2登录）
     */
    private Integer type;
}
