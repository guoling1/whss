package com.jkm.hsy.user.help.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/4/17.
 */
@Data
public class XmmsResponse {
    /**
     * 微信T1返回结果
     */
    private BaseResponse WxT1;
    /**
     * 微信D0返回结果
     */
    private BaseResponse WxD0;
    /**
     * 支付宝T1返回结果
     */
    private BaseResponse ZfbT1;
    /**
     * 支付宝D0返回结果
     */
    private BaseResponse ZfbD0;

    @Data
    public static class BaseResponse{
        private int code;
        /**
         *handling fail
         */
        private Result  result;
        /**
         * 返回描述信息
         */
        private String msg;
    }
    @Data
    public static class Result{
        /**
         * 商户编号
         */
        private String merchantNo;
        /**
         *handling fail
         */
        private String  status;
        /**
         * 渠道商户编号
         */
        private String smId;
        /**
         * 返回描述信息
         */
        private String msg;
    }
}
