package com.jkm.hss.dealer.helper.requestparam;

import lombok.Data;

import java.util.Date;

/**
 * Created by wayne on 17/4/28.
 *
 */
@Data
public class DealerReportRequest {
    private long dealerid;
    private Date startTime;
    private Date endTime;
}
