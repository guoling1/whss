package com.jkm.hss.product.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/17.
 */
@Data
public class QuerySupportBankParams extends PageQueryParams {

    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 银行编码
     */
    private String bankCode;

    private long offset;

    private int count;
}
