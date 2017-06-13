package com.jkm.hsy.user.help.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/6/13.
 */
@Data
public class AddWxChannelRequest {
    /**
     * 用户编码
     */
    private long userId;
    /**
     * 微信子商户编号
     */
    private String exchannelCode;
    /**
     * 公众号
     */
    private String appId;
}
