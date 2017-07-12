package com.jkm.hsy.user.help.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/4/17.
 */
@Data
public class CmbcProductResponse {
    /**
     * 返回值
     */
    private int code;
    /**
     * 返回描述信息
     */
    private String msg;
    /**
     * 渠道商户编号
     */
    private ProductResponse result;
    @Data
    public static class ProductResponse{
        private String  aliRespCode;
        private String  wxRespCode;
        private String  aliRespMsg;
        private String  wxRespMsg;
        private String  wxSmId;
        private String  smId;
    }
}
