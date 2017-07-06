package com.jkm.hss.bill.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2017/3/15.
 *
 * tb_holiday_off
 *
 * 假日调休
 */
@Data
public class HolidayOff extends BaseEntity {

    /**
     * 日期
     */
    private Date offDate;
    /**
     * 周-至周日
     */
    private String name;
    /**
     * 调休原因
     */
    private String reason;
}
