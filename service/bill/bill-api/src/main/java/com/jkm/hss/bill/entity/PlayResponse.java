package com.jkm.hss.bill.entity;
import com.jkm.hss.bill.enums.EnumPlayStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 交易订单
 *
 * tb_order
 * {@link EnumPlayStatus}
 */
@Data
public class PlayResponse {

    /**
     * 打款流水号
     */
    private String sn;

    /**
     * 打款总额
     */
    private BigDecimal amount;

    /**
     * 打款发起时间
     */
    private Date requestTime;

    /**
     * 打款发起时间
     */
    private String requestTimes;

    /**
     * 打款状态
     */
    private int status;

    /**
     * 打款状态
     */
    private int statusValue;

    /**
     * 成功时间
     */
    private Date finishTime;

    /**
     * 成功时间
     */
    private String finishTimes;

    /**
     * 打款渠道
     * {@link com.jkm.hss.bill.enums.EnumChannel}
     */
    private int playMoneyChannel;

    /**
     * 渠道的原始的信息
     */
    private String message;

}

