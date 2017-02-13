package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 *  按手机号和名称模糊匹配二级代理商 入参
 */
@Data
public class SecondLevelDealerFindRequest {

    /**
     * 条件
     */
    private String condition;
}
