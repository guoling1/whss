package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 * 按手机号和名称模糊匹配一级代理商 出参
 */
@Data
public class FirstLevelDealerFindResponse {

    /**
     * 代理商id
     */
    private long dealerId;

    /**
     * 代理手机号
     */
    private String mobile;

    /**
     * 代理名称
     */
    private String name;

}
