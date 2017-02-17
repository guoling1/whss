package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yuxiang on 2017-02-13.
 */
@Data
public class ProfitDetailsSelectRequest {

    private int pageNo;

    private int pageSize;

    private String orderNo;

    /**
     * 业务类型
     * 好收收- 收款 hssPay
     * 好收收-提现  hssWithdraw
     * 好收收-升级费 hssPromote
     * 好收银-收款  hsyPay
     */
    private String businessType;

    /**
     * 2017-01-02
     */
    private String beginDate;

    /**
     * 2017-02-09
     */
    private String endDate;
}
