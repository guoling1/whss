package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/1/3.
 *
 * 打款单审核需要信息查询  入参
 */
@Data
public class QueryInfoByOrderNoRequest {

    /**
     * 交易订单号
     */
    private String orderNo;
}
