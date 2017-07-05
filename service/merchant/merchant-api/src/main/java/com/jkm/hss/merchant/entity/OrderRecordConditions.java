package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by lt on 2016/12/8.
 */
@Data
public class OrderRecordConditions extends BaseEntity{

    /**
     * 交易日期
     */
    private Date updateTime;

    /**
     * 查询条件：开始时间
     */
    private String startTime;

    /**
     * 查询条件：结束时间
     */
    private String endTime;

    /**
     * 查询条件：设备编号（订单号）
     */
    private String orderId;

    /**
     * 查询条件：商户号
     */
    private Long merchantId;

    /**
     * 查询条件：商户名称
     */
    private String subName;

    /**
     * 查询条件：交易金额（小）
     */
    private Double lessTotalFee;

    /**
     * 查询条件：交易金额（大）
     */
    private Double moreTotalFee;

    /**
     * 查询条件：订单状态
     */
    private String payResult;

    /**
     * 查询条件：业务
     */
    private int payChannel;

    /**
     * 查询条件：电话
     */
    private String mdMobile;

    /**
     * 交易金额
     */
    private BigDecimal totalFee;

    /**
     * 当前页数
     */
    private int pageNo;

    /**
     * 每页显示数量
     */
    private int pageSize;

    /**
     * 订单保存位置
     */
    private String saveUrl;
}
