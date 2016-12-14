package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.TradeFeeRecord;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-28 15:53
 */

public interface TradeFeeRecordService {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    TradeFeeRecord selectByPrimaryKey(Long id);

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective(TradeFeeRecord record);

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKey(TradeFeeRecord record);
}
