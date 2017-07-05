package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.Advertisement;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by wayne on 17/5/22.
 */
@Repository
public interface AdvertisementDao {

    /**
     * 根据日期选择广告列表
     * @param time
     * @return
     */
    List<Advertisement> getByTime(Date time);
}
