package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by zhangbin on 2016/12/27.
 *
 * 根据码段查激活分配状态
 */
@Data
public class CodeQueryRequest {

    /**
     * 码段
     */
    private String code;

//    /**
//     * 一级代理商id
//     */
//    private long firstLevelDealerId;
//
//    /**
//     * 二级代理商id
//     */
//    private long secondLevelDealerId;
//
//    /**
//     * 商户id
//     */
//    private long merchantId;
}
