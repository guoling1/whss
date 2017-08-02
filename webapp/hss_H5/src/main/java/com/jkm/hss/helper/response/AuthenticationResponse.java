package com.jkm.hss.helper.response;

import lombok.Data;


/**
 * Created by xingliujie on 2017/8/2.
 */
@Data
public class AuthenticationResponse {
    private Integer isMerchantAuthen;//店铺是否认证 1已认证 0未认证
    private Integer isCreditAuthen;//信用卡是否认证 1已认证 0未认证
    private String merchantName;//店铺名
    private String district;//店铺所在地
    private String address;//详细地址
    private String createTime;//注册时间
    private String name;//店主姓名
    private String idCard;//身份证号
    private String creditCardName;//信用卡名称
    private String creditCardShort;//信用卡尾号
}
