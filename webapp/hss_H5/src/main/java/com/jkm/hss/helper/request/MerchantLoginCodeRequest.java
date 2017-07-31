package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-24 21:01
 */
@Data
public class MerchantLoginCodeRequest {
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 分公司标示
     */
    private String oemNo;
}
