package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 * admin 给一级代理商分配二维码   入参
 */
@Data
public class DistributeQRCodeRequest {

    /**
     * 代理商id
     */
    private long dealerId;

    /**
     * 个数
     */
    private int count;
}
