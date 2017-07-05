package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/11/28.
 */
@Data
public class MyMerchantResponse {

    /**
     *  店铺名字
     */
    private String merchantName;

    /**
     * 编码
     */
    private String code;

    /**
     * 注册日期
     */
    private String registerDate;

    /**
     * 状态
     *
     * {@link com.jkm.hss.merchant.enums.EnumMerchantStatus}
     */
    private String status;
}
