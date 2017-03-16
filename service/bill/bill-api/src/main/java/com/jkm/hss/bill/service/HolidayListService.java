package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.HolidayList;

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
}
