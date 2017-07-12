package com.jkm.hss.account.dao;

import com.jkm.hss.account.entity.SplitAccountRefundRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/5/4.
 */
@Repository
public interface SplitAccountRefundRecordDao {

    /**
     * 插入
     *
     * @param splitAccountRefundRecord
     */
    void insert(SplitAccountRefundRecord splitAccountRefundRecord);

    /**
     * 按退款单号查询分润退款记录
     *
     * @param refundOrderNo
     * @return
     */
    List<SplitAccountRefundRecord> selectByRefundOrderNo(@Param("refundOrderNo") String refundOrderNo);

    /**
     * 按支付交易单号查询分润退款记录
     *
     * @param payOrderNo
     * @return
     */
    List<SplitAccountRefundRecord> selectByPayOrderNo(@Param("payOrderNo") String payOrderNo);

    /**
     * 按分账退款单流水号查询
     *
     * @param splitSn
     * @return
     */
    int selectCountBySplitSn(@Param("splitSn") String splitSn);
}
