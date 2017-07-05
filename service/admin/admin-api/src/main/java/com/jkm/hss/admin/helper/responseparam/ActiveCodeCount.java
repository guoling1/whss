package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/29.
 */
@Data
public class ActiveCodeCount {

    /**
     * 二级代理商id
     */
    private long secondLevelDealerId;

    /**
     * 个数
     */
    private int count;
}
