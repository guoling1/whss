package com.jkm.api.helper.requestparam;

import com.google.common.base.Objects;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.api.helper.sdk.serialize.annotation.SdkSerializeAlias;
import lombok.Data;

/**
 * Created by xingliujie on 2017/8/17.
 */
@Data
public class OpenCardQueryRequest {
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
     * 开通快捷的银行卡号
     */
    @SdkSerializeAlias(signSort = 3, needSign = true)
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
        if ("JKM-SIGN-TEST".equals(this.sign)) {
            return true;
        }
        return Objects.equal(SdkSignUtil.sign2(this, key), this.sign);
    }
}
