package com.jkm.hsy.user.help.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/6/12.
 */
@Data
public class UserChannelPolicyUseResponse {
    /**
     * 通道唯一标示
     */
    private Integer channelTypeSign;
    /**
     * 通道名称
     */
    private String channelName;
}
