package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.bill.enums.EnumHsyOrderStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wayne on 17/5/15
 *
 * 好收银业务订单
 *
 * tb_hsy_order
 *
 * {@link EnumHsyOrderStatus}
 */
@Data
public class HsyOrder extends BaseEntity {
    /**
     * 订单号
     */
    private String ordernumber;
    /**
     *订单状态
     */
    private int orderstatus;
    /**
     * 店铺ID
     */
    private long shopid;

    private String shopname;

    /**
     * 商户在平台的唯一编号
     */
    private String merchantNo;

    private String merchantname;
    /**
     * 交易订单
     */
    private String orderno;
    /**
     * 交易订单ID
     */
    private long orderid;
    /**
     * 确认码
     */
    private String validationcode;
    private BigDecimal amount;
    private BigDecimal poundage;
    private String qrcode;
    private int cashierid;
    private String cashiername;
    /**
     * 第三方交易流水号
     */
    private String paysn;
    private String paytype;
    private int paychannelsign;
    private Date paysuccesstime;
    private BigDecimal refundamount;
    private int refundstatus;
    private Date refundtime;
    private String goodsname;
    private String goodsdescribe;
    private String remark;
    /**
     * 支付类型  1二维码付款/2充值
     */
    private int sourcetype;

    /**
     * 扫码用户在微信/支付宝的标识
     */
    private String memberId;

    /**
     * {@link com.jkm.hss.product.enums.EnumPaymentChannel}
     *
     * 支付渠道
     */
    private int paymentChannel;

    /**
     * 结算类型
     *
     * {@link com.jkm.hss.product.enums.EnumBalanceTimeType}
     */
    private String settleType;

    /**
     * 是否是 已经退款成功
     *
     * @return
     */
    public boolean isRefundSuccess() {
        return EnumHsyOrderStatus.REFUND_SUCCESS.getId()==this.orderstatus;
    }

    /**
     * 是否是 部分退款
     *
     * @return
     */
    public boolean isRefundPart() {
        return EnumHsyOrderStatus.REFUND_PART.getId()==this.orderstatus;
    }

    /**
     * 是否可退款
     * @return
     */
    public boolean isRefund(){
        return EnumHsyOrderStatus.DUE_PAY.getId()!=this.orderstatus
                &&EnumHsyOrderStatus.PAY_FAIL.getId()!=this.orderstatus
                &&!isRefundSuccess()
                &&!isRefundPart();
    }

    /**
     * 是否是待支付
     *
     * @return
     */
    public boolean isPendingPay() {
        return EnumHsyOrderStatus.DUE_PAY.getId() == this.orderstatus;
    }


}
