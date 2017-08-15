package com.jkm.api.helper.requestparam;

import com.google.common.base.Objects;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/8/15.
 *
 * 快捷预下单 请求参数
 */
@Data
public class PreQuickPayRequest {

    /**
     * 商户编号
     */
    private String merchantNo;
    /**
     * 商户订单号
     */
    private String orderNo;
    /**
     * 商户请求时间
     */
    private String merchantReqTime;
    /**
     * 通道编码
     */
    private String channelCode;
    /**
     * 订单币种
     */
    private String orderCurrency = "RMB";
    /**
     * 订单金额
     */
    private String orderAmount;
    /**
     * 持卡人姓名
     */
    private String cardByName;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 卡类型
     */
    private String cardType;
    /**
     * 有效期
     */
    private String expireDate;
    /**
     * cvv2
     */
    private String cvv;
    /**
     * 证件类型
     */
    private String cerType;
    /**
     * 证件号
     */
    private String cerNumber;
    /**
     * 订单标题
     */
    private String subject;
    /**
     * 订单详情
     */
    private String goodsDetail;
    /**
     * 页面通知地址
     */
    private String pageNotifyUrl;
    /**
     * 后台回调地址
     */
    private String callbackUrl;
    /**
     * 结算通知地址
     */
    private String settleNotifyUrl;
    /**
     * 签名
     */
    private String sign;

    public void validateParam() {

    }

    /**
     * 校验签名
     *
     * @param key
     */
    public boolean verifySign(final String key) {
        final String sign = SdkSignUtil.sign(SdkSerializeUtil.convertObjToMap(this), key);
        return Objects.equal(sign, this.sign);
    }
}
