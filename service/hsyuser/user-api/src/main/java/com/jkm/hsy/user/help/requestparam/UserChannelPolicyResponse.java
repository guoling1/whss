package com.jkm.hsy.user.help.requestparam;

import com.jkm.hsy.user.Enum.EnumChannelIsUse;
import lombok.Data;

/**
 * Created by xingliujie on 2017/6/12.
 */
@Data
public class UserChannelPolicyResponse {
    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 结算时间
     */
    private String settleType;
    /**
     * 入网状态
     * {@link com.jkm.hsy.user.Enum.EnumNetStatus}
     */
    private Integer netStatus;
    /**
     * 入网信息
     */
    private String netMarks;
    /**
     * 开通产品状态
     * {@link com.jkm.hsy.user.Enum.EnumOpenProductStatus}
     */
    private Integer openProductStatus;
    /**
     *开通产品信息
     */
    private String openProductMarks;
    /**
     * 渠道商户编号
     */
    private String exchannelCode;
    /**
     * 公众号
     */
    private String appId;
    /**
     * 活动编号
     */
    private String exchannelEventCode;
    /**
     * 使用中
     */
    private int isUse;
    /**
     * 子公众号
     */
    private String subAppId;
}
