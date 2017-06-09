package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Data
public class UserChannelPolicy extends BaseEntity{
    /**
     *用户编码
     */
    private long userId;
    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 通道唯一标示
     */
    private Integer channelTypeSign;
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

}
