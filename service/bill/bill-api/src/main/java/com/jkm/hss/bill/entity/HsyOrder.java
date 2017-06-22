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
public class HsyOrder{
    private Long id;
    /**
     * 订单号
     */
    private String ordernumber;
    /**
     *订单状态
     */
    private Integer orderstatus;
    /**
     * 店铺ID
     */
    private Long shopid;

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
    private Long orderid;
    /**
     * 确认码
     */
    private String validationcode;
    private BigDecimal amount;
    private BigDecimal poundage;
    private String qrcode;
    private Integer cashierid;
    private String cashiername;
    /**
     * 第三方交易流水号
     */
    private String paysn;
    private String paytype;
    private Integer paychannelsign;
    private Date paysuccesstime;
    private BigDecimal refundamount;
    private Integer refundstatus;
    private Date refundtime;
    private String goodsname;
    private String goodsdescribe;
    private String remark;
    /**
     * 支付类型  1二维码付款/2主扫付款
     */
    private Integer sourcetype;

    /**
     * 扫码用户在微信/支付宝的标识
     */
    private String memberId;
    /**
     * 主扫时的用户的code码
     */
    private String authCode;

    private BigDecimal realAmount;//实际交易金额

    /**
     * {@link com.jkm.hss.product.enums.EnumPaymentChannel}
     *
     * 支付渠道
     */
    private Integer paymentChannel;

    /**
     * 结算类型
     *
     * {@link com.jkm.hss.product.enums.EnumBalanceTimeType}
     */
    private String settleType;

    private Long uid;
    private Long accountid;
    private Long dealerid;

    private Date createTime;
    private Date updateTime;

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

    /**
     * 是否已经请求过交易
     *
     * @return
     */
    public boolean isHaveRequestedTrade() {
        return EnumHsyOrderStatus.HAVE_REQUESTED_TRADE.getId() == this.orderstatus;
    }


}
