package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.SplitAccountRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
@Repository
public interface SplitAccountRecordDao {

    /**
     * 插入
     *
     * @param record
     */
    void insert(SplitAccountRecord record);

    /**
     * 更新
     *
     * @param record
     * @return
     */
    int update(SplitAccountRecord record);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    SplitAccountRecord selectById(@Param("id") long id);

    List<SplitAccountRecord> selectByParam(@Param("firstIndex") Integer firstIndex, @Param("pageSize") Integer pageSize, @Param("accountId") Long accountId, @Param("orderNo") String orderNo,
                                           @Param("businessType") String businessType, @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    long selectCountByParam(@Param("accountId") Long accountId, @Param("orderNo") String orderNo, @Param("businessType") String businessType,
                            @Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    /**
     * 按分账单流水号查询
     *
     * @param splitSn
     * @return
     */
    int selectCountBySplitSn(@Param("splitSn") String splitSn);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @return
     */
    List<SplitAccountRecord> selectByOrderNo(@Param("orderNo") String orderNo);
}
