package com.jkm.hsy.user.help.requestparam;

import com.jkm.hsy.user.Enum.EnumAct;
import com.jkm.hsy.user.Enum.EnumOpt;
import lombok.Data;

import java.util.Date;

/**
 * Created by xingliujie on 2017/6/19.
 */
@Data
public class NetLogResponse {
    /**
     * 操作人名称
     */
    private String adminName;
    /**
     * 通道编码
     */
    private String channelTypeSignName;
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
    /**
     * 创建时间
     */
    private Date createTime;
}
