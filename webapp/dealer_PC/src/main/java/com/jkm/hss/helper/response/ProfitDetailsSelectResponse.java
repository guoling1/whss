package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by yuxiang on 2017-02-13.
 */
@Data
public class ProfitDetailsSelectResponse {

    private String splitOrderNo;

    private String businessType;

    private Date splitCreateTime;

    private String orderNo;

    private String splitSettlePeriod;

    private Date settleTime;

    private String dealerName;

    private String splitAmount;

    private String remark;
}
