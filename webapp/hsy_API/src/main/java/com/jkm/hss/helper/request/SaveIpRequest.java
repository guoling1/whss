package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/7/24.
 *
 * 店铺 所在公网ip, 端口
 */
@Data
public class SaveIpRequest {

    /**
     * 店铺id
     */
    private long shopId;
    /**
     * 端口
     */
    private int port;
}
