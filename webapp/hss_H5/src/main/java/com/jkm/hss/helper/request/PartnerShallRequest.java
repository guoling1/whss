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
    /**
     * 分润类型
     * 1推荐人分润 2.合伙人分润 3其它分润
     */
    private int type;
}
