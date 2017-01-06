package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by Thinkpad on 2017/1/6.
 */
@Data
public class RecommendRequest {
    private int page;
    private int size;
    private int offset;
    private long merchantId;
}
