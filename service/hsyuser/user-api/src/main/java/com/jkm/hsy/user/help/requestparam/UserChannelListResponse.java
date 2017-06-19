package com.jkm.hsy.user.help.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/6/12.
 */
@Data
public class UserChannelListResponse {
    /**
     * 微信通道名称
     */
    private String wxChannelName;
    /**
     * 支付宝通道名称
     */
    private String zfbChannelName;
}
