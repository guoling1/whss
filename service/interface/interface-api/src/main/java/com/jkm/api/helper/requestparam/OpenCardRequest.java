package com.jkm.api.helper.requestparam;

import com.google.common.base.Objects;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.api.helper.sdk.serialize.annotation.SdkSerializeAlias;
import lombok.Data;

/**
 * Created by xingliujie on 2017/8/17.
 */
@Data
public class OpenCardRequest {
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
     * 前台通知地址
     */
    @SdkSerializeAlias(signSort = 3, needSign = true)
    private String frontUrl;
    /**
     * 绑卡流水
     */
    @SdkSerializeAlias(signSort = 4, needSign = true)
    private String bindCardReqNo;
    /**
     * 开通快捷的银行卡号
     */
    @SdkSerializeAlias(signSort = 5, needSign = true)
    private String cardNo;
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
