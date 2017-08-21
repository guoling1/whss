package com.jkm.api.helper.responseparam;

import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.api.helper.sdk.serialize.annotation.SdkSerializeAlias;
import lombok.Data;

/**
 * Created by xingliujie on 2017/8/17.
 */
@Data
public class OpenCardResponse {
    /**
     * 错误码
     */
    @SdkSerializeAlias(signSort = 1, needSign = true)
    private String returnCode;
    /**
     * 错误描述
     */
    @SdkSerializeAlias(signSort = 2, needSign = true)
    private String returnMsg;
    /**
     * 代理商编号
     */
    @SdkSerializeAlias(signSort = 3, needSign = true)
    private String dealerMarkCode;
    /**
     * 商户编号
     */
    @SdkSerializeAlias(signSort = 4, needSign = true)
    private String merchantNo;
    /**
     * 绑卡流水
     */
    @SdkSerializeAlias(signSort = 5, needSign = true)
    private String bindCardReqNo;
    /**
     * 开通快捷的银行卡号
     */
    @SdkSerializeAlias(signSort = 6, needSign = true)
    private String cardNo;
    /**
     * 页面
     */
    @SdkSerializeAlias(signSort = 7, needSign = true)
    private String html;

    /**
     * 签名
     */
    private String sign;

    public String createSign(final String key) {
        return SdkSignUtil.sign2(this, key);
    }
}
