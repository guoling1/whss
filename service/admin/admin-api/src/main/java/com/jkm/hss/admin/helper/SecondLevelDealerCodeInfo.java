package com.jkm.hss.admin.helper;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/29.
 */
@Data
public class SecondLevelDealerCodeInfo {

    /**
     * 昨日激活二维码数
     */
    private int lastDayActivateCount;

    /**
     * 总数
     */
    private int codeCount;

    /**
     * 未激活数
     */
    private int unActivateCount;
}
