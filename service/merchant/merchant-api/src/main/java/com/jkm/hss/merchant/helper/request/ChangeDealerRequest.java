package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/3/21.
 */
@Data
public class ChangeDealerRequest {
    /**
     * 切换类型
     */
    private int changeType;
    /**
     * 代理商编码
     */
    private String markCode;
    /**
     * 商户编码
     */
    private long merchantId;
}
