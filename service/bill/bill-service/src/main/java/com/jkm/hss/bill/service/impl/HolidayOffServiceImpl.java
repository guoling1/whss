package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.dao.HolidayOffDao;
import com.jkm.hss.bill.entity.HolidayOff;
import com.jkm.hss.bill.service.HolidayOffService;
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
public class HolidayOffServiceImpl implements HolidayOffService {
    @Autowired
    private HolidayOffDao holidayOffDao;

    private static final AtomicBoolean isInit = new AtomicBoolean(false);

    private static final Vector<Date> vector = new Vector();

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<HolidayOff> getAll() {
        return this.holidayOffDao.selectAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        if (isNotInit()) {
            synchronized (HolidayOffServiceImpl.class) {
                if (isNotInit()) {
                    final List<HolidayOff> holidayOffs = this.getAll();
                    for (HolidayOff holidayOff: holidayOffs) {
                        vector.add(holidayOff.getOffDate());
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
    public boolean contains(final Date tradeDate) {
        return vector.contains(tradeDate);
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
