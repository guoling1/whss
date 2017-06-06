package com.jkm.hss.notifier.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.notifier.enums.EnumConsumeMsgFailRecordStatus;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/6/1.
 *
 * 分润-消息消费错误记录
 *
 * {@link EnumConsumeMsgFailRecordStatus}
 *
 * tb_consume_msg_fail_record
 */
@Data
public class ConsumeMsgFailRecord extends BaseEntity{

    /**
     * 请求参数
     */
    private String requestParam;
    /**
     * 消息tag
     */
    private String tag;
}
