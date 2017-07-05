package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 * 绑定结算卡  入参
 */
@Data
public class DealerBankCardBindRequest {

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String identity;

    /**
     * 结算卡
     */
    private String bankCard;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 校验码
     */
    private String code;
}
