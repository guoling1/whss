package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/29.
 *
 * 我发展的二级代理
 */
@Data
public class MyDealerResponse {

    /**
     * 二级代理商id
     */
    private long dealerId;

    /**
     * 代理名称
     */
    private String proxyName;

    /**
     * 配码数
     */
    private int distributeCount;

    /**
     * 激活数
     */
    private int activeCount;
}
