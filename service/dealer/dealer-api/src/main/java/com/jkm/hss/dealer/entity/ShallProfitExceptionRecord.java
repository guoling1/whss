package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by yuxiang on 2016-12-21.
 */
@Data
public class ShallProfitExceptionRecord extends BaseEntity {


    /**
     * orderRecord id
     */
    private long orderRecordId;

    /**
     * 订单号
     */
    private String orderId;

    /**
     *  收单 , 提现
     */
    private String type;

    /**
     * 异常信息
     */
    private String msg;
}
