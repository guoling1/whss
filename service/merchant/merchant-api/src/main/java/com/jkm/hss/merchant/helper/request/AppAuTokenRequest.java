package com.jkm.hss.merchant.helper.request;

import lombok.Data;

import java.util.Date;

@Data
public class AppAuTokenRequest {
    private Long id;
    private String accessToken;//令牌
    private String encryptKey;//加密秘钥
    private String deviceId;//设备号
    private String clientId;//推送号
    private String deviceName;//设备名称
    private String appType;//APP类型
    private String osVersion;//系统版本号
    private String appCode;//APP编号
    private String appVersion;//APP版本
    private String appChannel;//app来源
    private Integer isAvoidingTone;//是否消除提示音 0 否 1是
    private Date createTime;
    private Date updateTime;
    private String oemNo;//分公司标示
}
