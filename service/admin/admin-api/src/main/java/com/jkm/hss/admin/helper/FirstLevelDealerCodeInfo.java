package com.jkm.hss.admin.helper;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/29.
 */
@Data
public class FirstLevelDealerCodeInfo {

    /**
     * 昨日激活二维码
     */
    private int lastDayActivateCount;

    /**
     * 剩余数量
     */
    private int residueCount;

    /**
     * 已分配数
     */
    private int distributeCount;

    /**
     * 未激活数
     */
    private int unActivateCount;

    /**
     * 已激活数
     */
    private int activateCount;

    /**
     * 直客二维码（分配给自己的二维码）
     */
    private int distributeToSelfCount;
}
