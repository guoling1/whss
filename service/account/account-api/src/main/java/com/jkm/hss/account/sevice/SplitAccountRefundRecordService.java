package com.jkm.hss.account.sevice;

import com.jkm.hss.account.entity.SplitAccountRefundRecord;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/5/4.
 */
public interface SplitAccountRefundRecordService {

    /**
     * 插入
     *
     * @param splitAccountRefundRecord
     */
    void add(SplitAccountRefundRecord splitAccountRefundRecord);

    /**
     * 按退款单号查询分润退款记录
     *
     * @param refundOrderNo
     * @return
     */
    List<SplitAccountRefundRecord> getByRefundOrderNo(String refundOrderNo);

    /**
     * 按支付交易单号查询分润退款记录
     *
     * @param payOrderNo
     * @return
     */
    List<SplitAccountRefundRecord> getByPayOrderNo(String payOrderNo);

    /**
     * 校验分账退款单流水号
     *
     * @param splitSn
     * @return
     */
    boolean checkExistBySplitSn(String splitSn);
}
