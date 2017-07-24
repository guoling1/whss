package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.ShopSocket;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/7/24.
 */
@Repository
public interface ShopSocketDao {
    /**
     * 插入
     *
     * @param shopSocket
     */
    void insert(ShopSocket shopSocket);

    /**
     * 更新
     *
     * @param shopSocket
     * @return
     */
    int update(ShopSocket shopSocket);

    /**
     * 更新
     *
     * @param shopSocket
     * @return
     */
    int updateByShopId(ShopSocket shopSocket);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    ShopSocket selectById(@Param("id") long id);

    /**
     * an店铺id查询
     *
     * @param shopId
     * @return
     */
    ShopSocket selectByShopId(@Param("shopId") long shopId);

    /**
     * 查询最近的1000
     *
     * @return
     */
    List<ShopSocket> selectLimit1000();
}
