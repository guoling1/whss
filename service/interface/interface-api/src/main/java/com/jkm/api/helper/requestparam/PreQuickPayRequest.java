package com.jkm.api.helper.requestparam;

import com.google.common.base.Objects;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.api.helper.sdk.serialize.annotation.SdkSerializeAlias;
import com.jkm.base.common.util.BytesHexConverterUtil;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.Md5Util;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.Date;

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
     * 商户请求时间
     */
    @SdkSerializeAlias(signSort = 4, needSign = true)
    private String merchantReqTime;
    /**
     * 通道编码
     */
    @SdkSerializeAlias(signSort = 5, needSign = true)
    private String channelCode;
    /**
     * 订单币种
     */
    @SdkSerializeAlias(signSort = 6, needSign = true)
    private String orderCurrency;
    /**
     * 订单金额
     */
    @SdkSerializeAlias(signSort = 7, needSign = true)
    private String orderAmount;
    /**
     * 持卡人姓名
     */
    @SdkSerializeAlias(signSort = 8, needSign = true)
    private String cardByName;
    /**
     * 卡号
     */
    @SdkSerializeAlias(signSort = 9, needSign = true)
    private String cardNo;
    /**
     * 卡类型
     */
    @SdkSerializeAlias(signSort = 10, needSign = true)
    private String cardType;
    /**
     * 有效期
     */
    @SdkSerializeAlias(signSort = 11, needSign = true)
    private String expireDate;
    /**
     * cvv2
     */
    @SdkSerializeAlias(signSort = 12, needSign = true)
    private String cvv;
    /**
     * 证件类型
     */
    @SdkSerializeAlias(signSort = 13, needSign = true)
    private String cerType;
    /**
     * 证件号
     */
    @SdkSerializeAlias(signSort = 14, needSign = true)
    private String cerNumber;
    /**
     * 银行预留手机号
     */
    @SdkSerializeAlias(signSort = 15, needSign = true)
    private String mobile;
    /**
     * 订单标题
     */
    @SdkSerializeAlias(signSort = 16, needSign = true)
    private String subject;
    /**
     * 订单详情
     */
    @SdkSerializeAlias(signSort = 17, needSign = true)
    private String goodsDetail;
    /**
     * 页面通知地址
     */
    @SdkSerializeAlias(signSort = 18, needSign = true)
    private String pageNotifyUrl;
    /**
     * 后台回调地址
     */
    @SdkSerializeAlias(signSort = 19, needSign = true)
    private String callbackUrl;
    /**
     * 结算通知地址
     */
    @SdkSerializeAlias(signSort = 20, needSign = true)
    private String settleNotifyUrl;
    /**
     * 签名
     */
    private String sign;

    public void validateParam() {
        if (StringUtils.isEmpty(this.dealerMarkCode)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "dealerMarkCode不能为空");
        }
        if (StringUtils.isEmpty(this.merchantNo)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "merchantNo不能为空");
        }
        if (StringUtils.isEmpty(this.orderNo)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "orderNo不能为空");
        }
        if (StringUtils.isEmpty(this.merchantReqTime)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "merchantReqTime不能为空");
        } else {
            try {
                DateFormatUtil.parse(this.getMerchantReqTime(), DateFormatUtil.yyyyMMddHHmmssSSS);
            } catch (final Throwable e) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "merchantReqTime格式错误");
            }
        }
        if (StringUtils.isEmpty(this.channelCode)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "channelCode不能为空");
        } else {
            try {
                final EnumPayChannelSign enumPayChannelSign = EnumPayChannelSign.codeOf(this.channelCode);
                if (null == enumPayChannelSign) {
                    throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "channelCode不合法");
                }
            } catch (final Throwable e) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "channelCode不合法");
            }
        }
        if (!"CNY".equals(this.orderCurrency)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "orderCurrency不合法");
        }
        if (StringUtils.isEmpty(this.orderAmount)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "orderAmount不合法");
        } else {
            try {
                if (new BigDecimal("0.00").compareTo(new BigDecimal(this.orderAmount)) >= 0) {
                    throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "orderAmount不合法");
                }
            } catch (final Throwable e) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "orderAmount不合法");
            }
        }
        if (StringUtils.isEmpty(this.cardByName)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "cardByName不能为空");
        }
        if (StringUtils.isEmpty(this.cardNo)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "cardNo不能为空");
        }
        if (!"CREDIT_CARD".equals(this.cardType)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "cardType不合法");
        }
        if (!StringUtils.isEmpty(this.expireDate)) {
            if (this.expireDate.length() != 4) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "expireDate不合法");
            }
            if (!StringUtils.isNumeric(this.expireDate)) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "expireDate不合法");
            }
        }
        if (!StringUtils.isEmpty(this.cvv)) {
            if (this.cvv.length() != 3) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "cvv不合法");
            }
            if (!StringUtils.isNumeric(this.cvv)) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "cvv不合法");
            }
        }
        if (!"ID_CARD".equals(this.cerType)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "cerType不合法");
        }
        if (StringUtils.isEmpty(this.cerNumber)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "cerNumber不能为空");
        }
        if (!ValidateUtils.isMobile(this.mobile)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "mobile不合法");
        }
        if (!StringUtils.isEmpty(this.pageNotifyUrl) && !ValidateUtils.isUrl(this.pageNotifyUrl)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "pageNotifyUrl不合法");
        }
        if (StringUtils.isEmpty(this.callbackUrl)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "callbackUrl不能为空");
        } else {
            if (!ValidateUtils.isUrl(this.callbackUrl)) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "callbackUrl不合法");
            }
        }
        if (!StringUtils.isEmpty(this.settleNotifyUrl) && !ValidateUtils.isUrl(this.settleNotifyUrl)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "settleNotifyUrl不合法");
        }
    }

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
