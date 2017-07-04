package com.jkm.hss.dealer.helper.response;

import lombok.Data;

/**
 * Created by wayne on 17/5/9.
 */
@Data
public class DealerRegCheck {
    /**
     * 代理商ID
     */
    private int dealerId;
    /**
     * 数量
     */
    private int mCount;
}
