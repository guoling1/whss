package com.jkm.hss.bill.entity;

import com.jkm.hss.product.enums.EnumUpperChannel;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/24.
 *
 * 代付 请求支付中心 请求参数
 */
@Data
public class PaymentSdkDaiFuRequest {


    /**
     * 系统标识
     */
    private String appId;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 支付中心支付单流水号
     */
    private String payOrderSn;

    /**
     * 总金额
     */
    private String totalAmount;

    /**
     * 交易类型（D0/T1）
     */
    private String tradeType;

    /**
     * 对公对私(1：对公，0：对私)
     */
    private String isCompany;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 银行名称
     */
    private String bankName;

    /**
     * 银行编码
     */
    private String bankCode;

    /**
     * 收款人名称
     */
    private String accountName;

    /**
     * 收款卡号
     */
    private String accountNumber;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 打款渠道
     *
     * {@link EnumUpperChannel}
     */
    private int playMoneyChannel;

    /**
     * 备注
     */
    private String note;

    /**
     * 自定义系统名称（非ygww）和appId一致
     */
    private String systemCode;

    /**
     * 业务方回调url
     */
    private String notifyUrl;
}
