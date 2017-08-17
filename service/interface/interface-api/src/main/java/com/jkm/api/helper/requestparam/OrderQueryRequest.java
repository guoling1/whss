package com.jkm.api.helper.requestparam;

import com.google.common.base.Objects;
import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/8/16.
 *
 * 订单查询请求参数
 */
@Data
public class OrderQueryRequest {


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
