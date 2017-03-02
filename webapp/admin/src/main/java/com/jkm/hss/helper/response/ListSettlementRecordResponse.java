package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/3/1.
 */
@Data
public class ListSettlementRecordResponse {

    private long id;

    /**
     * 结算单号
     */
    private String settleNo;

    /**
     * 编号
     */
    private String userNo;

    /**
     * 名称
     */
    private String userName;

    /**
     * 业务线（hss,hsy）
     */
    private String appId;

    /**
     * 结算日期
     */
    private Date settleDate;

    /**
     * 结算笔数
     */
    private int tradeNumber;

    /**
     * 结算金额
     */
    private String settleAmount;

    /**
     * 结算目的地(结算类型)
     *
     * {@link com.jkm.hss.bill.enums.EnumSettleDestinationType}
     */
    private int settleDestination;

    private String settleDestinationValue;

    /**
     * 结算方式
     *
     * {@link com.jkm.hss.bill.enums.EnumSettleModeType}
     */
    private int settleMode;

    private String settleModeValue;
    /**
     * 结算状态
     *
     * {@link com.jkm.hss.bill.enums.EnumSettleStatus}
     */
    private int settleStatus;

    private String settleStatusValue;
}
