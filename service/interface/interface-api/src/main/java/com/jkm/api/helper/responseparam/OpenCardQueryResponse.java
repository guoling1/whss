package com.jkm.api.helper.responseparam;

import com.jkm.api.helper.sdk.serialize.SdkSignUtil;
import com.jkm.api.helper.sdk.serialize.annotation.SdkSerializeAlias;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by xingliujie on 2017/8/17.
 */
@Data
public class OpenCardQueryResponse {
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
     * 卡列表
     */
    private List<Map> cardList;

    /**
     * 签名
     */
    private String sign;

    public String createSign(final String key) {
        return SdkSignUtil.sign2(this, key);
    }
}
