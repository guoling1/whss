package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/7/27.
 *
 * 好收银订单，打印小票记录
 *
 * tb_hsy_order_print_ticket_record
 *
 */
@Data
public class HsyOrderPrintTicketRecord extends BaseEntity {

    /**
     * 交易订单号
     */
    private String tradeOrderNo;

    /**
     * 打印信息
     */
    private String msg;
}
