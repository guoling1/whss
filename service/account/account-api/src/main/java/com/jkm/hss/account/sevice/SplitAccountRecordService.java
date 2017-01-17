package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.SplitAccountRecord;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface SplitAccountRecordService {

    /**
     * 插入
     *
     * @param record
     */
    void add(SplitAccountRecord record);

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
    Optional<SplitAccountRecord> getById(long id);

    /**
     * 添加分账记录
     *
     * @param orderNo
     * @param orderNo1  分账单号（目前和交易订单号一致）
     * @param tradeAmount  交易总额
     * @param poundage   手续费
     * @param triple  <accountId, 分账金额， 分账费率>
     * @param receiptMoneyUserName  收钱人
     * @param remark
     */
    void addPaySplitAccountRecord(String orderNo, String orderNo1, BigDecimal tradeAmount, BigDecimal poundage,
                                  Triple<Long, BigDecimal, BigDecimal> triple,
                                  String receiptMoneyUserName, String remark);

    /**
     * 添加分账记录(商户升级支付)
     *
     * @param orderNo
     * @param orderNo1  分账单号（目前和交易订单号一致）
     * @param tradeAmount  交易总额
     * @param poundage   手续费
     * @param triple  <accountId, 分账金额， 分账费率>
     * @param receiptMoneyUserName  收钱人
     * @param remark
     */
    void addMerchantUpgradePaySplitAccountRecord(String orderNo, String orderNo1, BigDecimal tradeAmount, BigDecimal poundage,
                                  Triple<Long, BigDecimal, String> triple,
                                  String receiptMoneyUserName, String remark);

    /**
     * 添加分账记录
     *
     * @param orderNo
     * @param orderNo1  分账单号（目前和交易订单号一致）
     * @param tradeAmount  交易总额
     * @param poundage   手续费
     * @param triple  <accountId, 分账金额， 分账费率>
     * @param receiptMoneyUserName  收钱人
     * @param remark
     */
    void addWithdrawSplitAccountRecord(String orderNo, String orderNo1, BigDecimal tradeAmount, BigDecimal poundage,
                                  Triple<Long, BigDecimal, String> triple,
                                  String receiptMoneyUserName, String remark);
}
