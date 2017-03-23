package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.HolidayList;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/15.
 */
@Repository
public interface HolidayListDao {

    List<HolidayList> selectAll();
}
