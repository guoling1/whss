package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/23.
 */
@Data
public class DealerLoginRequest {
    /**
     * 手机号
     */
    private String mobile;

    /**
     * code
     */
    private String code;
}
