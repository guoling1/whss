package com.jkm.hsy.user.dao;

import com.jkm.hsy.user.entity.AppQrcodeShopRel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by Thinkpad on 2017/1/16.
 */
@Repository
public interface AppQrcodeShopRelDao {
    /**
     * 初始化
     * @param appQrcodeShopRel
     */
    int insert(AppQrcodeShopRel appQrcodeShopRel);

    /**
     * 根据二维码查询店铺
     * @param code
     * @return
     */
    AppQrcodeShopRel selectByCode(@Param("code") String code);
}
