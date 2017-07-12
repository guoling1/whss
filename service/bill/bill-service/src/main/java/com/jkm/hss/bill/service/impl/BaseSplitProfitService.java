package com.jkm.hss.bill.service.impl;

import com.jkm.hss.bill.entity.Order;
import com.jkm.hss.bill.entity.SettlementRecord;
import com.jkm.hss.bill.helper.RefundProfitParams;
import com.jkm.hss.bill.helper.SplitProfitParams;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yulong.zhang on 2017/5/12.
 */
public interface BaseSplitProfitService {

    /**
     * 执行支付/充值/升级分账
     *
     * @param splitProfitParams
     * @return
     */
    Pair<Integer, String> exePaySplitProfit(SplitProfitParams splitProfitParams);

    /**
     * 执行提现分账
     *
     * @param splitProfitParams
     * @return
     */
    Pair<Integer, String> exeWithdrawSplitProfit(SplitProfitParams splitProfitParams);

    /**
     * 执行升级分账
     *
     * @param splitProfitParams
     * @return
     */
    Pair<Integer, String> exeUpgradeSplitProfit(SplitProfitParams splitProfitParams);

    /**
     * 执行退分润
     *
     * @param refundProfitParams
     * @param refundOrderId
     * @return
     */
    Pair<Integer, String> exeRefundProfit(RefundProfitParams refundProfitParams, long refundOrderId);

    /**
     * 执行退分润-全额退
     *
     * @param refundProfitParams
     * @param refundOrderId
     * @return
     */
    Pair<Integer, String> exeRefundAllProfit(RefundProfitParams refundProfitParams, long refundOrderId);

    /**
     * 执行退分润-部分退
     *
     * @param refundProfitParams
     * @param refundOrderId
     * @return
     */
    Pair<Integer, String> exeRefundPartProfit(RefundProfitParams refundProfitParams, long refundOrderId);

    /**
     * 添加分润记录
     *
     * @param splitProfitDetail
     * @param order
     * @param splitProfitParams
     * @param remark
     */
    void addSplitAccountRecord(SplitProfitParams.SplitProfitDetail splitProfitDetail, Order order, SplitProfitParams splitProfitParams, String remark);

    /**
     * 按结算单添加分润记录
     *
     * @param splitProfitDetail
     * @param settlementRecord
     * @param splitProfitParams
     * @param remark
     */
    void addSplitAccountRecordBySettlementRecord(SplitProfitParams.SplitProfitDetail splitProfitDetail, SettlementRecord settlementRecord, SplitProfitParams splitProfitParams, String remark);

    /**
     * 分润 可用余额增加
     *
     * @param splitProfitDetail
     * @param orderNo
     * @param remark
     */
    void splitProfit4IncreaseAvailableAccount(SplitProfitParams.SplitProfitDetail splitProfitDetail, String orderNo, String remark);

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
