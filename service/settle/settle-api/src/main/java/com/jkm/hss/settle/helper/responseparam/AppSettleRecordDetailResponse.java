package com.jkm.hss.settle.helper.responseparam;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/4/20.
 */
@Data
public class AppSettleRecordDetailResponse {

    /**
     * 结算审核记录id
     */
    private long recordId;
    /**
     * 结算金额
     */
    private String settleAmount;
    /**
     * 交易金额
     */
    private String tradeAmount;
    /**
     * 手续费金额
     */
    private String feeAmount;
    /**
     * 结算日期
     */
    private Date settleDate;
    /**
     * 交易笔数
     */
    private int number;
}
