package com.jkm.hss.bill.entity;

import com.jkm.hss.bill.enums.EnumOrderStatus;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/22.
 *
 * 交易订单
 *
 * tb_order
 *
 * {@link EnumOrderStatus}
 */
@Data
public class WithdrawRequest {

    /**
     * 账户名称
     */
    private String userName;

    /**
     * 业务订单号（下游）
     */
    private String businessOrderNo;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 流水号
     */
    private String sn;

    /**
     * 页数
     */
    private Integer pageNo;
    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     * 条数
     */
    private Integer offset;

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;
}
