package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/15.
 */
@Data
public class ProxyProductResponse {
    /**
     * 是否代理好收收
     */
    private long proxyHss;
    /**
     * 是否代理好收银
     */
    private long proxyHsy;
}
