package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by Thinkpad on 2016/12/20.
 */
@Data
public class JkmProfitRequest {
    /**
     * 页数
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer size;
    /**
     * 条数
     */
    private Integer offset;

}
