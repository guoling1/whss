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
     * 开通快捷的银行卡号
     */
    @SdkSerializeAlias(signSort = 4, needSign = true)
    private String cardNo;
    /**
     * 银行卡预留手机号
     */
    @SdkSerializeAlias(signSort = 5, needSign = true)
    private String mobile;
    /**
     * 银行名称
     */
    @SdkSerializeAlias(signSort = 6, needSign = true)
    private String bankName;
    /**
     * 银行编码
     */
    @SdkSerializeAlias(signSort = 7, needSign = true)
    private String bankBin;
    /**
     * 默认银行卡
     */
    @SdkSerializeAlias(signSort = 8, needSign = true)
    private String isDefault;
    /**
     * 绑定状态
     */
    @SdkSerializeAlias(signSort = 9, needSign = true)
    private String bindStatus;
    

    /**
     * 签名
     */
    private String sign;

    public String createSign(final String key) {
        return SdkSignUtil.sign2(this, key);
    }
}
