package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;
import com.jkm.hss.account.helper.selectresponse.SettleAccountFlowStatistics;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2016/12/22.
 */
public interface SettleAccountFlowService {

    /**
     * 插入
     *
     * @param accountFlow
     */
    void add(SettleAccountFlow accountFlow);

    /**
     * 保存结算单id
     *
     * @param id
     * @param settlementRecordId
     * @return
     */
    int updateSettlementRecordIdById(long id, long settlementRecordId);

    /**
     * 保存结算单id
     *
     * @param settleAuditRecordId  结算审核记录id
     * @param settlementRecordId  结算单id
     * @return
     */
    int updateSettlementRecordIdBySettleAuditRecordId(long settleAuditRecordId, long settlementRecordId);

    /**
     * 保存结算审核记录
     *
     * @param tradeDate
     * @param settleAuditRecordId
     * @param accountId
     * @return
     */
    int updateSettleAuditRecordIdByTradeDateAndAccountId(Date tradeDate, long accountId, long settleAuditRecordId);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<SettleAccountFlow> getById(long id);

    /**
     * 按交易订单号查询
     *
     * @param orderNo
     * @param accountId
     * @param type
     * @return
     */
    Optional<SettleAccountFlow> getByOrderNoAndAccountIdAndType(String orderNo, long accountId, int type);

    /**
     * 添加流水
     *
     * @param accountId
     * @param orderNo  交易订单号
     * @param changeAmount  变动金额
     * @param remark  备注
     * @param type 变更方向
     */
    long addSettleAccountFlow(long accountId, String orderNo, BigDecimal changeAmount, String remark, EnumAccountFlowType type,
            String appId, Date tradeDate, int accountUserType);

    /**
     * 统计上一日的结算流水（未结算）
     *
     * @param tradeDate
     * @return
     */
    List<SettleAccountFlowStatistics> statisticsYesterdayFlow(Date tradeDate);

    /**
     * 按审核记录id查询
     *
     * @param recordId
     * @return
     */
    List<SettleAccountFlow> getByAuditRecordId(long recordId);

    /**按交易订单号查询代理商和公司(等)的分润流水
     *
     *
     * @param orderNo
     * @return
     */
    List<SettleAccountFlow> getDealerOrCompanyFlowByOrderNo(String orderNo);

    /**
     * 按结算单id查询
     *
     * @param settlementRecordId
     * @return
     */
    List<SettleAccountFlow> getBySettlementRecordId(long settlementRecordId);

    /**
     * 查询昨日出账流水个数
     *
     * @param tradeDate
     * @return
     */
    int getYesterdayDecreaseFlowCount(Date tradeDate);
}
