package com.jkm.hss.helper.response;

import com.jkm.hss.bill.helper.responseparam.PcStatisticsOrder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/7/5.
 */
@Data
public class PcStatisticsOrderResponse {

    /**
     * 收款笔数
     */
    private int payNumber;
    /**
     * 收款金额
     */
    private BigDecimal payAmount;
    /**
     * 退款笔数
     */
    private int refundNumber;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 净收款
     */
    private BigDecimal profit;

    private List<Detail> details = new ArrayList<>();


    public void addDetails(final PcStatisticsOrder statisticsOrder) {

    }


    @Data
    public class Detail {

        /**
         * 支付渠道
         */
        private int payChannel;
        /**
         * 支付渠道值
         */
        private int payChannelValue;
        /**
         * 收款笔数
         */
        private int payNumber;
        /**
         * 收款金额
         */
        private BigDecimal payAmount;
        /**
         * 退款笔数
         */
        private int refundNumber;
        /**
         * 退款金额
         */
        private BigDecimal refundAmount;
        /**
         * 净收款
         */
        private BigDecimal profit;
    }
}
