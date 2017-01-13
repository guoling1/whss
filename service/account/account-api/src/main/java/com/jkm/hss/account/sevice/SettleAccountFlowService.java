package com.jkm.hss.account.sevice;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.account.enums.EnumAccountFlowType;

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
     * 保存结算审核记录
     *
     * @param orderNos
     * @param settleAuditRecordId
     * @return
     */
    int updateSettleAuditRecordIdByOrderNos(List<String> orderNos, long settleAuditRecordId);

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
    void addSettleAccountFlow(long accountId, String orderNo, BigDecimal changeAmount, String remark, EnumAccountFlowType type);

    /**
     * 查询上一个工作日的结算流水（未结算）
     *
     * 如果今日是周一查询的是（周五至周日）的；
     *
     * @param tradeDateList
     * @return
     */
    List<SettleAccountFlow> getMerchantLastWordDayRecord(List<Date> tradeDateList);
}
