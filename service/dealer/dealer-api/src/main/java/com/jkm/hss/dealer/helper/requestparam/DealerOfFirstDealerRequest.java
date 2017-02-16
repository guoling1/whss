package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/15.
 */
@Data
public class DealerOfFirstDealerRequest {
    /**
     * 代理商编码
     */
    private long dealerId;
    /**
     * 条件
     */
    private String condition;
    /**
     * 所属产品
     */
    private String sysType;
}
