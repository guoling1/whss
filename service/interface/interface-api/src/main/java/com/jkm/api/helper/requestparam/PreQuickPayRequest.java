package com.jkm.api.helper.requestparam;

import com.google.common.base.Objects;
import com.jkm.api.enums.JKMTradeErrorCode;
import com.jkm.api.exception.JKMTradeServiceException;
import com.jkm.api.helper.sdk.serialize.SdkSerializeUtil;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.base.common.util.ValidateUtils;
import com.jkm.hss.product.enums.EnumPayChannelSign;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yulong.zhang on 2017/8/15.
 *
 * 快捷预下单 请求参数
 */
@Data
public class PreQuickPayRequest {

    /**
     * 代理商编号
     */
    private String dealerMarkCode;
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
    private String orderCurrency;
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
     * 银行预留手机号
     */
    private String mobile;
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
        if ("CNY".equals(this.orderCurrency)) {
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
        if ("CREDIT_CARD".equals(this.cardType)) {
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
        if (StringUtils.isEmpty(this.cerType)) {
            throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "cerType不能为空");
        } else {
            if ("ID_CARD".equals(this.cerType)) {
                throw new JKMTradeServiceException(JKMTradeErrorCode.REQUEST_MESSAGE_ERROR, "cerType不合法");
            }
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
        final String sign = SdkSignUtil.sign(SdkSerializeUtil.convertObjToMap(this), key);
        return Objects.equal(sign, this.sign);
    }
}
