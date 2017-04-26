package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/15.
 */
@Data
public class DealerMarkCodeRequest {
    /**
     * 二代编码
     */
    private long secondDealerId;
    /**
     * 代理商编码
     */
    private String markCode;
}
