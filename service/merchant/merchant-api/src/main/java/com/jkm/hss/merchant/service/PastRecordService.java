package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.entity.PastRecord;
import net.sf.json.JSONObject;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-28 15:45
 */
public interface PastRecordService {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    PastRecord selectByPrimaryKey (Long id );

    /**
     *
     * 添加 （匹配有值的字段）
     *
     **/
    int insertSelective( PastRecord record );

    /**
     *
     * 修改 （匹配有值的字段）
     *
     **/
    int updateByPrimaryKey( PastRecord record );

    /**
     * 结算和未结算资金
     * @param accountId
     * @return
     */
    JSONObject settleMoney(long accountId);

}
