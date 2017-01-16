package com.jkm.hsy.user.service.impl;

import com.google.common.base.Optional;
import com.jkm.hsy.user.dao.AppQrcodeShopRelDao;
import com.jkm.hsy.user.entity.AppQrcodeShopRel;
import com.jkm.hsy.user.service.AppQrcodeShopRelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Thinkpad on 2017/1/16.
 */
@Slf4j
@Service
public class AppQrcodeShopRelServiceImpl implements AppQrcodeShopRelService{
    @Autowired
    private AppQrcodeShopRelDao appQrcodeShopRelDao;
    /**
     * 初始化
     *
     * @param appQrcodeShopRel
     */
    @Override
    public int insert(AppQrcodeShopRel appQrcodeShopRel) {
        return appQrcodeShopRelDao.insert(appQrcodeShopRel);
    }

    /**
     * 根据二维码查询店铺
     *
     * @param code
     * @return
     */
    @Override
    public Optional<AppQrcodeShopRel> selectByCode(String code) {
        return Optional.fromNullable(appQrcodeShopRelDao.selectByCode(code));
    }
}
