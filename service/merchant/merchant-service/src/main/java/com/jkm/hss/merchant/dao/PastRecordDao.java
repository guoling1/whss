package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.entity.PastRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @desc:
 * @author:xlj
 * @mail:java_xlj@163.com
 * @create:2016-11-28 15:35
 */
@Repository
public interface PastRecordDao {
    /**
     *
     * 查询（根据主键ID查询）
     *
     **/
    PastRecord selectByPrimaryKey (@Param("id") Long id );

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
     *已结算
     * @param accountId
     * @return
     */
    double selectSettleMoney(@Param("accountId") Long accountId );

    /**
     *未结算
     * @param accountId
     * @return
     */
    double selectUnSettleMoney(@Param("accountId") Long accountId );
}
