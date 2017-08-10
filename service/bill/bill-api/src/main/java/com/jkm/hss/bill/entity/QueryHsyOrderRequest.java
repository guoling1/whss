package com.jkm.hss.bill.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/7/24.
 */
@Data
public class QueryHsyOrderRequest {

    private String ordernumber;//订单号
    private String proxyName;//一级代理
    private String proxyName1;//二级代理
    private String realname;//报单员
    private String username;//报单员真实姓名
    private String merchantName;//商户名称
    private String merchantNo;//商户编号
    private String shortName;//店铺名称
    private String shortNo;//店铺编号
    private String orderno;//交易订单
    private String paysn;//第三方交易流水号
    private Integer orderstatus;//订单状态
    private int source;//来源
    private String startTime;
    private String endTime;

    /**
     * {@link com.jkm.hss.product.enums.EnumPaymentChannel}
     * 支付方式
     */
    private int paymentChannel;//1, "微信" 2, "支付宝" 3, "快捷" 4, "QQ钱包" 5, "会员卡" 6,"银联扫码"

    /**
     * 条数
     */
    private Integer offset;

    /**
     * 页数
     */
    private Integer page;
    /**
     * 每页条数
     */
    private Integer size;
}
