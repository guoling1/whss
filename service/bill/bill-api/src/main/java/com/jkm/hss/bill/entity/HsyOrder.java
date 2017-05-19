package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wayne on 17/5/15.
 * 好收银业务订单
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

}
