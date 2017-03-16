package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.dao.HolidayListDao;
import com.jkm.hss.bill.entity.HolidayList;
import com.jkm.hss.bill.service.HolidayListService;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yulong.zhang on 2017/3/15.
 */
@Service
public class HolidayListServiceImpl implements HolidayListService {
    @Autowired
    private HolidayListDao holidayListDao;

    private static final AtomicBoolean isInit = new AtomicBoolean(false);

    private static final Vector<Triple<Date, Date, Date>> vector = new Vector();

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<HolidayList> getAll() {
        return this.holidayListDao.selectAll();
    }

    /**
     * 初始化
     */
    private void assertInit() {
        if (isNotInit()) {
//            this.init();
        }
    }
    /**
     * 未初始化
     *
     * @return
     */
    private boolean isNotInit() {
        return !isInit.get();
    }
}
