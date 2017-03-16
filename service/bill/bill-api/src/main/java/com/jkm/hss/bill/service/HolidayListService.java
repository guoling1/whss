package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.HolidayList;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/15.
 */
public interface HolidayListService {

    /**
     * 查询所有
     *
     * @return
     */
    List<HolidayList> getAll();

    /**
     * 初始化
     */
    void init();

    /**
     * 获取假日后，第一工作日
     *
     * @param tradeDate
     * @return
     */
    Date getWorkDay(final Date tradeDate);
}
