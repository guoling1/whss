package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2017/1/19.
 */
@Data
public class SettleRequest {

    /**
     * 记录id
     */
    private long recordId;

    /**
     * 选项
     *
     * {@link com.jkm.hss.settle.enums.EnumSettleOptionType}
     */
    private int option;
}
