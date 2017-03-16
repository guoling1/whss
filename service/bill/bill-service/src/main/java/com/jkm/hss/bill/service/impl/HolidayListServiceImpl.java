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
     * {@inheritDoc}
     */
    @Override
    public void init() {
        if (isNotInit()) {
            synchronized (HolidayListServiceImpl.class) {
                if (isNotInit()) {
                    final List<HolidayList> holidayLists = this.getAll();
                    for (HolidayList holiday : holidayLists) {
                        vector.add(Triple.of(holiday.getBeganDate(), holiday.getEndDate(), holiday.getWorkDate()));
                    }
                    isInit.set(true);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param tradeDate
     * @return
     */
    @Override
    public Date getWorkDay(final Date tradeDate) {
        this.assertInit();
        for (Triple<Date, Date, Date> triple : vector) {
            if (triple.getLeft().compareTo(tradeDate) <= 0
                    && triple.getMiddle().compareTo(tradeDate) >= 0) {
                return triple.getLeft();
            }
        }
        return null;
    }


    /**
     * 初始化
     */
    private void assertInit() {
        if (isNotInit()) {
            this.init();
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
