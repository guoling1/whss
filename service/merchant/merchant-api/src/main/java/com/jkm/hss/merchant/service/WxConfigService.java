package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.WxConfig;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-27 13:03
 */
public interface WxConfigService {
    WxConfig selectTop1();
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    WxConfig selectByPrimaryKey(Long id);

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective(WxConfig record);

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKeySelective(WxConfig record);
}
