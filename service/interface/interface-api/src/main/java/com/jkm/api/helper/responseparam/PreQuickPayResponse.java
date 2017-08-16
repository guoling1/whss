package com.jkm.api.helper.responseparam;

import com.jkm.base.common.util.BytesHexConverterUtil;
import com.jkm.base.common.util.Md5Util;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * Created by yulong.zhang on 2017/8/15.
 *
 * 快捷预下单 返回参数
 */
@Data
public class PreQuickPayResponse {

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
     * 交易订单号
     */
    private String tradeOrderNo;
    /**
     * 商户请求时间
     */
    private String merchantReqTime;
    /**
     * 订单金额
     */
    private String orderAmount;
    /**
     * 卡号
     */
    private String cardNo;
    /**
     * 订单状态
     */
    private String orderStatus;
    /**
     * 结算状态
     */
    private String settleStatus;
    /**
     * 错误码
     */
    private String returnCode;
    /**
     * 错误描述
     */
    private String returnMsg;
    /**
     * 签名
     */
    private String sign;

    public String createSign(final String key) {
        final String needSignStr = new StringBuilder()
                .append("dealerMarkCode").append("=").append(StringUtils.isEmpty(this.dealerMarkCode) ? "" : this.dealerMarkCode).append("&")
                .append("merchantNo").append("=").append(StringUtils.isEmpty(this.merchantNo) ? "" : this.merchantNo).append("&")
                .append("orderNo").append("=").append(StringUtils.isEmpty(this.orderNo) ? "" : this.orderNo).append("&")
                .append("tradeOrderNo").append("=").append(StringUtils.isEmpty(this.tradeOrderNo) ? "" : this.tradeOrderNo).append("&")
                .append("merchantReqTime").append("=").append(StringUtils.isEmpty(this.merchantReqTime) ? "" : this.merchantReqTime).append("&")
                .append("orderAmount").append("=").append(StringUtils.isEmpty(this.orderAmount) ? "" : this.orderAmount).append("&")
                .append("cardNo").append("=").append(StringUtils.isEmpty(this.cardNo) ? "" : this.cardNo).append("&")
                .append("orderStatus").append("=").append(StringUtils.isEmpty(this.orderStatus) ? "" : this.orderStatus).append("&")
                .append("settleStatus").append("=").append(StringUtils.isEmpty(this.settleStatus) ? "" : this.settleStatus).append("&")
                .append("returnCode").append("=").append(StringUtils.isEmpty(this.returnCode) ? "" : this.returnCode).append("&")
                .append("returnMsg").append("=").append(StringUtils.isEmpty(this.returnMsg) ? "" : this.returnMsg).append("&")
                .append("key").append("=").append(key)
                .toString();
        final String sign = BytesHexConverterUtil.bytesToHexStr(Md5Util.md5Digest(needSignStr
                .getBytes(Charset.forName("utf-8"))));
        return sign;
    }
}
