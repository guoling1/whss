package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.HolidayOff;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/15.
 */
@Repository
public interface HolidayOffDao {

    List<HolidayOff> selectAll();
}
