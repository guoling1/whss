package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/14.
 *
 * 按码段分配二维码 入参
 */
@Data
public class DistributeRangeQRCodeRequest {

    /**
     * 代理商id
     */
    private long dealerId;

    /**
     * 开始码段
     */
    private String startCode;

    /**
     * 结束码段
     */
    private String endCode;
}
