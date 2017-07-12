package com.jkm.hss.bill.helper;

import com.jkm.hss.product.enums.EnumBalanceTimeType;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import com.jkm.hss.product.enums.EnumUpperChannel;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2017/5/17.
 */
@Data
public class WithdrawParams {

    /**
     * 业务方
     *
     * {@link com.jkm.hss.account.enums.EnumAppType}
     */
    private String appId;
    /**
     * 账户id(基础账户)
     */
    private long accountId;
    /**
     * 结算类型，默认D0
     */
    private String settleType = EnumBalanceTimeType.D0.getType();
    /**
     * 提现渠道方，默认扫米
     */
    private int channel = EnumPayChannelSign.YG_WECHAT.getId();
    /**
     * 提现金额
     */
    private BigDecimal withdrawAmount;
    /**
     * 对公/对私，默认对私
     */
    private int toPublic = 0;
    /**
     * 银行预留手机号
     */
    private String mobile;
    /**
     * 银行名字
     */
    private String bankName;
    /**
     * 银行预留真实名字
     */
    private String userName;
    /**
     * 银行卡号
     */
    private String bankCardNo;
    /**
     * 身份证号
     */
    private String identityCardNo;
    /**
     * 备注:例如商户名，店铺名字
     */
    private String note;
}
