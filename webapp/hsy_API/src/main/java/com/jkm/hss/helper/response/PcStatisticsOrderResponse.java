package com.jkm.hss.helper.response;

import com.jkm.hss.bill.enums.EnumHsyOrderStatus;
import com.jkm.hss.bill.helper.responseparam.PcStatisticsOrder;
import com.jkm.hss.product.enums.EnumPaymentChannel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private BigDecimal payAmount = new BigDecimal("0.00");
    /**
     * 退款笔数
     */
    private int refundNumber;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount = new BigDecimal("0.00");
    /**
     * 净收款
     */
    private BigDecimal profit = new BigDecimal("0.00");

    private List<Detail> details = new ArrayList<>();


    public void addDetails(final Map<Integer, Detail> detailMap, final PcStatisticsOrder statisticsOrder) {
        if (statisticsOrder.getPaymentChannel() <= 0) {
            return;
        }
        Detail detail = detailMap.get(statisticsOrder.getPaymentChannel());
        if (null == detail) {
            detail = new Detail();
            detail.setPayChannel(statisticsOrder.getPaymentChannel());
            detail.setPayChannelValue(EnumPaymentChannel.of(statisticsOrder.getPaymentChannel()).getValue());
            detailMap.put(statisticsOrder.getPaymentChannel(), detail);
        }
        if (EnumHsyOrderStatus.PAY_SUCCESS.getId() == statisticsOrder.getOrderStatus()) {
            this.payNumber = this.payNumber + statisticsOrder.getNumber();
            this.payAmount = this.payAmount.add(statisticsOrder.getTotalAmount());
            this.profit = this.profit.add(statisticsOrder.getRealAmount().subtract(statisticsOrder.getPoundage()));
            detail.setPayNumber(detail.getPayNumber() + statisticsOrder.getNumber());
            detail.setPayAmount(detail.getPayAmount().add(statisticsOrder.getTotalAmount()));
            detail.setProfit(detail.getProfit().add(statisticsOrder.getRealAmount().subtract(statisticsOrder.getPoundage())));
        }
        if (EnumHsyOrderStatus.REFUND_SUCCESS.getId() == statisticsOrder.getOrderStatus()) {
            this.refundNumber = this.refundNumber + statisticsOrder.getNumber();
            this.refundAmount = this.refundAmount.add(statisticsOrder.getRefundAmount());
            detail.setRefundNumber(detail.getRefundNumber() + statisticsOrder.getNumber());
            detail.setRefundAmount(detail.getRefundAmount().add(null != statisticsOrder.getRefundAmount() ? statisticsOrder.getRefundAmount() : new BigDecimal("0.00")));
        }
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
        private String payChannelValue;
        /**
         * 收款笔数
         */
        private int payNumber;
        /**
         * 收款金额
         */
        private BigDecimal payAmount = new BigDecimal("0.00");
        /**
         * 退款笔数
         */
        private int refundNumber;
        /**
         * 退款金额
         */
        private BigDecimal refundAmount = new BigDecimal("0.00");
        /**
         * 净收款
         */
        private BigDecimal profit = new BigDecimal("0.00");
    }
}
