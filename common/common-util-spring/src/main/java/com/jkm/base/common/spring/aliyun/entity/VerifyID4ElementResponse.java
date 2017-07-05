package com.jkm.base.common.spring.aliyun.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/12/9.
 *
 * 校验四要素 返回值
 */
@Data
public class VerifyID4ElementResponse {

    private String status;

    private String msg;

    private message result;

    @Data
    public class message{
        /**
         * 银行卡
         */
        private String bankcard;

        /**
         * 真实姓名
         */
        @JsonProperty("realname")
        private String realName;

        /**
         * 身份证
         */
        @JsonProperty("idcard")
        private String idCard;

        /**
         * 手机号
         */
        private String mobile;

        /**
         * 验证状态
         */
        @JsonProperty("verifystatus")
        private String verifyStatus;

        /**
         * 校验信息
         */
        @JsonProperty("verifymsg")
        private String verifyMsg;
    }
}
