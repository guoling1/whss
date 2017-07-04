package com.jkm.hss.product.servcie.impl;

import com.google.common.base.Optional;
import com.jkm.hss.product.dao.GlobalConfigDao;
import com.jkm.hss.product.entity.GlobalConfig;
import com.jkm.hss.product.servcie.GlobalConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Thinkpad on 2016/12/30.
 */
@Service
public class GlobalConfigServiceImpl implements GlobalConfigService{
    @Autowired
    private GlobalConfigDao globalConfigDao;
    /**
     * 初始化
     *
     * @param globalConfig
     */
    @Override
    public int insert(GlobalConfig globalConfig) {
        return globalConfigDao.insert(globalConfig);
    }

    /**
     * 修改
     *
     * @param globalConfig
     * @return
     */
    @Override
    public int update(GlobalConfig globalConfig) {
        return globalConfigDao.update(globalConfig);
    }

    /**
     * 根据编码查询
     *
     * @param id
     * @return
     */
    @Override
    public Optional<GlobalConfig> selectById(long id) {
        return Optional.fromNullable(globalConfigDao.selectById(id));
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    @Override
    public List<GlobalConfig> selectAll() {
        return globalConfigDao.selectAll();
    }
}
