package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/6/8.
 */
@Data
public class UserCurrentChannelPolicy extends BaseEntity {
    /**
     *用户编码
     */
    private long userId;
    /**
     * 通道唯一标示
     */
    private Integer wechatChannelTypeSign;
    /**
     * 支付宝通道唯一标示
     */
    private Integer alipayChannelTypeSign;
}
