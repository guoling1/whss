package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yuxiang on 2017-01-11.
 */
@Data
public class PartnerShallRequest {

    private long merchantId;

    private long shallId;

    private int pageNo;

    private int pageSize;
}
