package com.jkm.hss.dealer.helper.response;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/15.
 */
@Data
public class DealerOfFirstDealerResponse {
    /**
     * 代理商编码
     */
    private long id;
    /**
     * 代理商名称
     */
    private String proxyName;
    /**
     * 代理商手机号
     */
    private String mobile;
    /**
     * 是否能分配该产品二维码
     */
    private int isDistribute;
}
