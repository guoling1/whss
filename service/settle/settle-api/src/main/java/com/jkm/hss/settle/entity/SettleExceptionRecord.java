package com.jkm.hss.settle.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by yuxiang on 2017-07-21.
 *
 * {@link com.jkm.hss.settle.enums.EnumSettleExceptionStatus}
 */
@Data
public class SettleExceptionRecord extends BaseEntity {

    /**
     * 结算对象编号
     */
    private String settleTargetNo;

    private long withdrawOrderId;

    /**
     * 结算对象名称
     */
    private String settleTargetName;

    /**
     * 结算对象类型
     *  {@link com.jkm.hss.account.enums.EnumAccountUserType}
     */
    private int settleTargetType;

    /**
     * 挂起开始时间
     */
    private Date beginTime;

    /**
     * 挂起结束时间
     */
    private Date endTime;

    /**
     *
     */
    private String remarks;


}
