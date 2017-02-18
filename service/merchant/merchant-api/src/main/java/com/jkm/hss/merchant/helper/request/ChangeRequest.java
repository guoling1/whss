package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by zhangbin on 2017/2/18.
 */
@Data
public class ChangeRequest {
    /**
     * 商户id
     */
    private long id;

    /**
     * 要更新的商户名
     */
    private String merchantChangeName;
}
