package com.jkm.hsy.user.help.requestparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/6/12.
 */
@Data
public class UseChannelRequest {
    /**
     * 政策类型
     * {@link com.jkm.hsy.user.Enum.EnumPolicyType}
     */
    private String policyType;
    /**
     * 用户编码
     */
    private long userId;
}
