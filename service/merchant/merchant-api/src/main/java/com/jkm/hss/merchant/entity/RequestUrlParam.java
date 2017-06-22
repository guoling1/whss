package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by xingliujie on 2017/6/21.
 */
@Data
public class RequestUrlParam {
    /**
     * 唯一标示
     */
    private String uuid;
    /**
     * 请求地址
     */
    private String requestUrl;
}
