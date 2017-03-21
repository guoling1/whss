package com.jkm.hss.merchant.helper.request;


import com.jkm.hss.merchant.enums.EnumChangeType;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/21.
 */
@Data
public class ChangeDealerRequest {
    /**
     * 切换类型
     * {@link EnumChangeType}
     */
    private int changeType;
    /**
     * 代理商编码
     */
    private String markCode;
    /**
     * 商户编码
     */
    private long merchantId;
    /**
     * 当前代理商编码
     */
    private long currentDealerId;
    /**
     * 一级代理商编码
     */
    private long firstDealerId;
    /**
     * 二级代理商编码
     */
    private long secondDealerId;
}
