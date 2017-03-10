package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/3/3.
 */
@Data
public class MerchantListRequest {
    /**
     * 所属项目
     */
    private String sysType;
    /**
     * 商户编码
     */
    private String markCode;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 页码
     */
    private int pageNo;
    /**
     * 每页条数
     */
    private int pageSize;
}
