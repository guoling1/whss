package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wayne on 17/5/20.
 */
@Data
public class HsyRefundOrder extends BaseEntity {
    private long hsyorderid;
    private String refundno;
    private BigDecimal refundamount;
    private BigDecimal refundpoundagea;
    private int refundstatus;
    private int upperchannel;
    private String remark;
    private Date refundtime;

}
