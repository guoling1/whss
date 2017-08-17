package com.jkm.api.helper.responseparam;

import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/8/16.
 *
 * 订单查询返回参数
 */
@Data
public class OrderQueryResponse {

    public String createSign(final String key) {
        return SdkSignUtil.sign2(this, key);
    }
}
