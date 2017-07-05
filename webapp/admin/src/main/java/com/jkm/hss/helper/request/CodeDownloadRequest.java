package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
@Data
public class CodeDownloadRequest {
    /**
     * 生成二维码个数
     */
    private int count;
    /**
     * 产品编码
     */
    private long productId;
    /**
     * 系统类型（hss,hsy）
     */
    private String sysType;
}
