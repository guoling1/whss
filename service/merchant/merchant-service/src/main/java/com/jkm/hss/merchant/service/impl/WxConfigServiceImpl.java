package com.jkm.hss.merchant.service.impl;

import com.jkm.hss.merchant.dao.WxConfigDao;
import com.jkm.hss.merchant.entity.WxConfig;
import com.jkm.hss.merchant.service.WxConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 13:04
 */
@Slf4j
@Service
public class WxConfigServiceImpl implements WxConfigService{
    @Autowired
    private WxConfigDao wxConfigDao;
    @Override
    public WxConfig selectTop1() {
        return wxConfigDao.selectTop1();
    }

    @Override
    public WxConfig selectByPrimaryKey(Long id) {
        return wxConfigDao.selectByPrimaryKey(id);
    }

    @Override
    public int insertSelective(WxConfig record) {
        return wxConfigDao.insertSelective(record);
    }

    @Override
    public int updateByPrimaryKeySelective(WxConfig record) {
        return wxConfigDao.updateByPrimaryKeySelective(record);
    }
}
