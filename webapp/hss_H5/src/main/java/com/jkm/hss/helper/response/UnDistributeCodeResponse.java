package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/19.
 *
 * 未分配，未激活的二维码
 */
@Data
public class UnDistributeCodeResponse {
    /**
     * 开始
     */
    private String startCode;
    /**
     * 结束
     */
    private String endCode;
    /**
     * 个数
     */
    private int count;
}
