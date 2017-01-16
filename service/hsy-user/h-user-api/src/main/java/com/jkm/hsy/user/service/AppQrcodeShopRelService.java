package com.jkm.hsy.user.service;

import com.google.common.base.Optional;
import com.jkm.hsy.user.entity.AppQrcodeShopRel;

/**
 * Created by Thinkpad on 2017/1/16.
 */
public interface AppQrcodeShopRelService {
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
    Optional<AppQrcodeShopRel> selectByCode(String code);
}
