package com.jkm.hsy.user.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hsy.user.Enum.EnumAct;
import com.jkm.hsy.user.Enum.EnumOpt;
import lombok.Data;

/**
 * Created by xingliujie on 2017/6/19.
 */
@Data
public class NetLog extends BaseEntity {
    /**
     * 操作人编码
     */
    private long adminId;
    /**
     * 用户编码
     */
    private long userId;
    /**
     * 通道编码
     */
    private Integer channelTypeSign;
    /**
     * 操作
     * {@link EnumOpt}
     */
    private String opt;
    /**
     * 动作
     * {@link EnumAct}
     */
    private String act;
    /**
     * 结果
     */
    private String result;
}
