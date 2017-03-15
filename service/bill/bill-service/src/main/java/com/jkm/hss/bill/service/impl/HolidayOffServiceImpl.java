package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.dao.HolidayOffDao;
import com.jkm.hss.bill.entity.HolidayOff;
import com.jkm.hss.bill.service.HolidayOffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/15.
 */
@Service
public class HolidayOffServiceImpl implements HolidayOffService {
    @Autowired
    private HolidayOffDao holidayOffDao;

    @Override
    public List<HolidayOff> getAll() {
        return null;
    }
}
