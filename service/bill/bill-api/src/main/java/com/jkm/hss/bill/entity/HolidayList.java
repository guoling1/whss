package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/3/15.
 *
 *  tb_holiday_list
 *
 * 假日表
 */
@Data
public class HolidayList extends BaseEntity {

    /**
     * 假期名字
     */
    private String name;
    /**
     * 天数
     */
    private String days;
    /**
     * 开始日期
     */
    private Date beganDate;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 假日后第一工作日
     */
    private Date workDate;
}
