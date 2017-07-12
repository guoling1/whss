package com.jkm.base.common.entity;

import com.google.common.base.Strings;
import com.jkm.base.common.util.DateFormatUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/11/15.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TimeRangeQueryParams extends PageQueryParams {
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 创建结束时间
     */
    private String endTime;

    public Date getStartTimeDate() {
        if (Strings.isNullOrEmpty(this.startTime)) {
            return null;
        }
        return DateFormatUtil.parse(this.startTime, DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
    }

    public Date getEndTimeDate() {
        if (Strings.isNullOrEmpty(this.endTime)) {
            return null;
        }
        return DateFormatUtil.parse(this.endTime, DateFormatUtil.yyyy_MM_dd_HH_mm_ss);
    }
}
