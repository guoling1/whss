package com.jkm.hss.bill.helper;

import com.jkm.hss.account.helper.AccountConstants;
import com.jkm.hss.bill.enums.EnumServiceType;
import com.jkm.hss.product.enums.EnumMerchantPayType;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/3.
 */
@Data
public class PayParams {
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
     * 业务类型
     *
     * {@link com.jkm.hss.bill.enums.EnumServiceType}
     */
    private int serviceType;
    /**
     * 收款人账户id（商户基础账户）
     *
     * 当且仅当 升级时， 收款人是，金开门利润账户
     * 其他，是商户账户id
     */
    private long payeeAccountId;
    /**
     * 付款人账户id（商户基础账户）
     *
     * 当且仅当 升级时，付款人是商户（仅限hss升级）
     */
    private long payerAccountId;
    /**
     * 商品名字
     */
    private String goodsName;
    /**
     * 商品描述
     */
    private String goodsDescribe;
    /**
     * 微信appId
     */
    private String wxAppId;
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

    //#######会员卡支付############

    /**
     * 会员卡支付
     */
    private Boolean memberCardPay = false;
    /**
     * 付款人账户id（会员账户）
     */
    private long memberAccountId;
    /**
     * 收会员款账户id
     */
    private long merchantReceiveAccountId;

    //#############特殊渠道-支付参数##########

    /**
     * 收款行联行号
     */
    private String bankBranchCode;

    /**
     * 入账卡号 DES加密
     */
    private String bankCardNo;

    /**
     * 入帐卡对应姓名
     *
     */
    private String realName;

    /**
     * 入帐卡对应身份证号 DES加密
     *
     */
    private String idCard;

    /**
     * 结算成功，回调url
     */
    private String settleNotifyUrl;

    public long getPayeeAccountId() {
        if (EnumServiceType.APPRECIATION_PAY.getId() == this.serviceType) {
            return AccountConstants.POUNDAGE_ACCOUNT_ID;
        }
        return payeeAccountId;
    }
}
