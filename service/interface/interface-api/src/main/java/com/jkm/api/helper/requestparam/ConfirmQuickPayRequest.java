package com.jkm.api.helper.requestparam;

import com.google.common.base.Objects;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.api.helper.sdk.serialize.annotation.SdkSerializeAlias;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/8/15.
 *
 * 快捷确认支付 请求参数
 */
@Data
public class ConfirmQuickPayRequest {

    /**
     * 商户编号
     */
    @SdkSerializeAlias(signSort = 1, needSign = true)
    private String merchantNo;
    /**
     * 代理商编号
     */
    @SdkSerializeAlias(signSort = 2, needSign = true)
    private String dealerMarkCode;
    /**
     * 商户订单号
     */
    @SdkSerializeAlias(signSort = 3, needSign = true)
    private String orderNo;
    /**
     * 平台流水号
     */
    @SdkSerializeAlias(signSort = 4, needSign = true)
    private String tradeOrderNo;
    /**
     * 订单金额
     */
    @SdkSerializeAlias(signSort = 5, needSign = true)
    private String orderAmount;
    /**
     * 短信验证码
     */
    @SdkSerializeAlias(signSort = 6, needSign = true)
    private String smsCode;
    /**
     * 签名
     */
    private String sign;

    /**
     * 校验签名
     *
     * @param key
     */
    public boolean verifySign(final String key) {
        if ("JKM-SIGN-TEST".equals(this.sign)) {
            return true;
        }
        return Objects.equal(SdkSignUtil.sign2(this, key), this.sign);
    }
}
