package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/27.
 */
@Data
public class MerchantGetRateRequest {
    /**
     * 商户id
     */
    private long merchantId;

    /**
     * 产品id
     */
    private long productId;

    /**
     * 公司名
     */
    private String thirdCompany;
}
