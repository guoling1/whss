package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
@Data
public class ProductionQrCodeRequest {
    /**
     * 系统类型（hss,hsy）
     */
    private String sysType;
    /**
     * 二维码类型
     */
    private int type;
    /**
     * 生成二维码个数
     */
    private int count;
}
