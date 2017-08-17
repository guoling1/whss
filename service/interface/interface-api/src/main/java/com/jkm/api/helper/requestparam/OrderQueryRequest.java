package com.jkm.api.helper.requestparam;

import com.google.common.base.Objects;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.api.helper.sdk.serialize.annotation.SdkSerializeAlias;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/8/16.
 *
 * 订单查询请求参数
 */
@Data
public class OrderQueryRequest {

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
     * 签名
     */
    private String sign;
    /**
     * 校验签名
     *
     * @param key
     */
    public boolean verifySign(final String key) {
        return Objects.equal(SdkSignUtil.sign2(this, key), this.sign);
    }
}
