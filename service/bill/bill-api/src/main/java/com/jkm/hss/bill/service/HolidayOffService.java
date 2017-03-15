package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.HolidayOff;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/15.
 */
public interface HolidayOffService {

    /**
     * 查询所有
     *
     * @return
     */
    List<HolidayOff> getAll();
}
