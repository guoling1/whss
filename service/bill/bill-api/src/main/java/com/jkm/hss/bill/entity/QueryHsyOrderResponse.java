package com.jkm.hss.bill.entity;

import lombok.Data;

import java.util.Date;

/**
 * Created by zhangbin on 2017/7/24.
 */
@Data
public class QueryHsyOrderResponse {

    private String ordernumber;//订单号
    private String proxyName;//一级代理
    private String proxyName1;//二级代理
    private String realname;//报单员真实姓名
    private String username;//报单员
    private String merchantName;//商户名称
    private String shortName;//店铺名称
    private String orderno;//交易订单
    private String paysn;//第三方交易流水号
    /**
     * {@link com.jkm.hss.product.enums.EnumPaymentChannel}
     * 支付方式
     */
    private int paymentChannel;//1, "微信" 2, "支付宝" 3, "快捷" 4, "QQ钱包" 5, "会员卡" 6,"银联扫码"
    private String paymentChannels;
    private Date paysuccesstime;//支付成功时间
    private String paysuccesstimes;//支付成功时间
    private String amount;//订单金额
    private String poundage;//手续费金额
    private Integer orderstatus;//订单状态
    private String orderstatuss;//订单状态

}
