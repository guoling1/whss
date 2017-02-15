package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/9.
 */
@Data
public class DealerPwdRequest {
    /**
     * 代理商编码
     */
    private long dealerId;

    /**
     * 一级代理商id
     */
    private String loginPwd;

}
