package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.bill.enums.EnumRefundOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/5/4.
 *
 * 退款单
 *
 * tb_refund_order
 *
 * {@link com.jkm.hss.bill.enums.EnumRefundOrderStatus}
 */
@Data
public class RefundOrder extends BaseEntity {

    /**
     * 业务线
     *
     * {@link com.jkm.hss.account.enums.EnumAppType}
     */
    private String appId;
    /**
     * 批次号
     */
    private String batchNo;
    /**
     * 退款单号
     */
    private String orderNo;
    /**
     * 支付交易单id
     */
    private long payOrderId;
    /**
     * 支付交易单号
     */
    private String payOrderNo;
    /**
     * 退款流水号
     */
    private String sn;
    /**
     * 退款账户id（商户）
     */
    private long refundAccountId;
    /**
     * 退款金额
     */
    private BigDecimal refundAmount;
    /**
     * 商户账户退款金额
     */
    private BigDecimal merchantRefundAmount;
    /**
     * 手续费账户退款金额
     */
    private BigDecimal poundageRefundAmount;
    /**
     * 退款成功/失败时间
     */
    private Date finishTime;
    /**
     * 备注信息
     */
    private String remark;
    /**
     * 渠道信息
     */
    private String message;
    /**
     * 退款渠道
     *
     * {@link com.jkm.hss.product.enums.EnumUpperChannel}
     */
    private int upperChannel;

    /**
     * 是否是退款中
     *
     * @return
     */
    public boolean isRefunding() {
        return EnumRefundOrderStatus.REFUNDING.getId() == this.status;
    }

    /**
     * 是否退款成功
     *
     * @return
     */
    public boolean isRefundSuccess() {
        return EnumRefundOrderStatus.REFUND_SUCCESS.getId() == this.status;
    }

    /**
     * 是否是退款失败
     *
     * @return
     */
    public boolean isRefundFail() {
        return EnumRefundOrderStatus.REFUND_FAIL.getId() == this.status;
    }
}
