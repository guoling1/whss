package com.jkm.hss.notifier.entity;

import com.jkm.base.common.entity.BaseEntity;
import com.jkm.hss.notifier.enums.EnumConsumeMsgSplitProfitRecordStatus;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/6/1.
 *
 * 分润-消息消费分润记录
 *
 * {@link EnumConsumeMsgSplitProfitRecordStatus}
 *
 * tb_consume_msg_split_profit_record
 */
@Data
public class ConsumeMsgSplitProfitRecord extends BaseEntity{

    /**
     * 订单id
     */
    private long hsyOrderId;
    /**
     * 请求参数
     */
    private String requestParam;
    /**
     * 消息tag
     */
    private String tag;

    /**
     * 是否是待发送
     *
     * @return
     */
    public boolean isPendingSend() {
        return EnumConsumeMsgSplitProfitRecordStatus.PENDING_SEND.getId() == this.status;
    }
}
