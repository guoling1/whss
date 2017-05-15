package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.helper.SplitProfitParams;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2017/5/12.
 */
public interface BaseSplitProfitService {

    /**
     * 执行分账
     *
     * @param splitProfitParams
     * @return
     */
    Pair<Integer, String> exePaySplitAccount(SplitProfitParams splitProfitParams);


//    Pair<Integer, String> exeRefundSplitAccount();

    /**
     * 添加分润记录
     *
     * @param splitProfitDetail
     * @param order
     * @param businessType
     * @param remark
     */
    void addSplitAccountRecord(SplitProfitParams.SplitProfitDetail splitProfitDetail, Order order, String businessType, String remark);

    /**
     * 分润 可用余额增加
     *
     * @param splitProfitDetail
     * @param order
     * @param remark
     */
    void splitProfit4IncreaseAvailableAccount(SplitProfitParams.SplitProfitDetail splitProfitDetail, Order order, String remark);

    /**
     * 分润 待结算余额增加
     *
     * @param splitProfitDetail
     * @param order
     * @param remark
     */
    long splitProfit4IncreasePendingSettlementAccount(SplitProfitParams.SplitProfitDetail splitProfitDetail, Order order, String remark);

    /**
     * 分润 待结算余额减少
     *
     * @param splitProfitDetail
     * @param order
     * @param remark
     * @param settlementRecordId
     */
    void splitProfit4DecreasePendingSettlementAccount(SplitProfitParams.SplitProfitDetail splitProfitDetail, Order order, String remark, long settlementRecordId);
}
