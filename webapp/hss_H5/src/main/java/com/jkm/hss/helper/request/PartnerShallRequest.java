package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yuxiang on 2017-01-11.
 */
@Data
public class PartnerShallRequest {

    final long merchantId;

    final long shallId;

    final int pageNo;

    final int pageSize;
}
