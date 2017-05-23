package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.account.entity.ProfitCountRequest;
import com.jkm.hss.account.entity.ProfitCountRespons;
import com.jkm.hss.account.entity.ProfitDetailCountRespons;
import com.jkm.hss.account.entity.SplitAccountRecord;
import org.apache.commons.lang3.tuple.Triple;

import java.math.BigDecimal;
import java.util.List;

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
    void addPaySplitAccountRecord(String splitBusinessType, String orderNo, String orderNo1, BigDecimal tradeAmount, BigDecimal poundage,
                                  Triple<Long, BigDecimal, BigDecimal> triple,
                                  String receiptMoneyUserName, String remark, int accountUserType, String settleType);

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
    void addMerchantUpgradePaySplitAccountRecord(String splitBusinessType,String orderNo, String orderNo1, BigDecimal tradeAmount, BigDecimal poundage,
                                  Triple<Long, BigDecimal, String> triple,
                                  String receiptMoneyUserName, String remark, int accountUserType);

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
    void addWithdrawSplitAccountRecord(String splitBusinessType, String orderNo, String orderNo1, BigDecimal tradeAmount, BigDecimal poundage,
                                  Triple<Long, BigDecimal, String> triple,
                                  String receiptMoneyUserName, String remark, int accountUserType);

    PageModel<SplitAccountRecord> selectByParam(int pageNo, int pageSize, long accountId, String orderNo, String businessType, String beginDate, String endDate);

    /**
     * 校验分账单流水号
     *
     * @param splitSn
     * @return
     */
    boolean checkExistBySplitSn(String splitSn);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @return
     */
    List<SplitAccountRecord> getByOrderNo(String orderNo);

    /**
     * dealer_pc分润统计
     * @param request
     * @return
     */
    List<ProfitCountRespons> getProfit(ProfitCountRequest request);

    /**
     * 分润统计总数
     * @param request
     * @return
     */
    int getProfitCount(ProfitCountRequest request);

    /**
     * dealer_pc 分润明细
     * @param req
     * @return
     */
    List<ProfitDetailCountRespons> getCountDetails(ProfitCountRequest req);

    /**
     * dealer_pc 分润明细总数
     * @param req
     * @return
     */
    int getCountDetailsNo(ProfitCountRequest req);
}
