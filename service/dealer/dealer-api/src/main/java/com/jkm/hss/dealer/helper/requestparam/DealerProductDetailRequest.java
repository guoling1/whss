package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/14.
 */
@Data
public class DealerProductDetailRequest {
    /**
     * 代理商编码
     */
    private long dealerId;

    /**
     * 产品类型
     */
    private String sysType;

    /**
     * 代理商编码
     */
    private long productId;
}
