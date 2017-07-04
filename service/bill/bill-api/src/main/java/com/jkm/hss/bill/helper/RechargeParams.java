package com.jkm.hss.bill.helper;

import com.jkm.hss.product.enums.EnumMerchantPayType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/3.
 */
@Data
public class RechargeParams {

    /**
     * 业务订单号
     */
    private String businessOrderNo;
    /**
     * 通道标识
     */
    private int channel;
    /**
     * 公众号或者扫码支付
     */
    private EnumMerchantPayType merchantPayType;
    /**
     * 业务线
     *
     * {@link com.jkm.hss.account.enums.EnumAppType}
     */
    private String appId;
    /**
     * 交易金额，两位小数
     */
    private BigDecimal tradeAmount;
    /**
     * 实付金额，两位小数
     */
    private BigDecimal realPayAmount;
    /**
     * 营销金额
     */
    private BigDecimal marketingAmount;
    /**
     * 收款人账户id（商户基础账户）
     */
    private long payeeAccountId;
    /**
     * 会员账户id（会员账户）
     */
    private long memberAccountId;
    /**
     * 商户收款账户id(统计账户)
     */
    private long merchantReceiveAccountId;
    /**
     * 商品名字
     */
    private String goodsName;
    /**
     * 商品描述
     */
    private String goodsDescribe;
    /**
     * 会员id
     */
    private String memberId;
    /**
     * 商户号
     */
    private String merchantNo;
    /**
     * 商户名
     */
    private String merchantName;
    /**
     * 主扫时的用户的code码
     */
    private String authCode;
}
