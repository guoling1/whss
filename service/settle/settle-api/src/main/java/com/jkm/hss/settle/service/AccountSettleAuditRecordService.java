package com.jkm.hss.settle.service;

import com.google.common.base.Optional;
import com.jkm.hss.account.entity.SettleAccountFlow;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/1/12.
 */
public interface AccountSettleAuditRecordService {

    /**
     * 插入
     *
     * @param record
     */
    void add(AccountSettleAuditRecord record);

    /**
     * 更新对账状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateAccountCheckStatus(long id, int status);

    /**
     * 更新结算状态
     *
     * @param id
     * @param status
     * @return
     */
    int updateSettleStatus(long id, int status);

    /**
     * 按id查询
     *
     * @param id
     * @return
     */
    Optional<AccountSettleAuditRecord> getById(long id);

    /**
     * 加锁按id查询
     *
     * @param id
     * @return
     */
    Optional<AccountSettleAuditRecord> getByIdWithLock(long id);

    /**
     * 按ids查询
     *
     * @param recordIds
     */
    List<AccountSettleAuditRecord> getByIds(List<Long> recordIds);

    /**
     * 处理 T1 结算审核, 生成记录
     */
    void handleT1SettleTask();

    /**
     * 正常结算
     *
     * @param recordId
     * @return
     */
    Pair<Integer, String> normalSettle(long recordId);

    /**
     * 结算已经对账部分
     *
     * @param recordId
     * @param checkedOrderNos
     * @return
     */
    Pair<Integer, String> settleCheckedPart(long recordId, List<String> checkedOrderNos);

    /**
     * 结算全部
     *
     * @param recordId
     * @return
     */
    Pair<Integer, String> forceSettleAll(long recordId);

    /**
     * 批量结算
     *
     * @param recordIds
     * @return
     */
    Pair<Integer,String> batchSettle(List<Long> recordIds);

    /**
     * 商户结算
     *
     * @param flows
     * @param merchantNo
     * @param accountId
     */
    void merchantSettle(List<SettleAccountFlow> flows, String merchantNo, long accountId);

    /**
     * 单笔结算
     *
     * @param flow
     * @param orderNo
     * @param merchantNo
     * @param accountId
     */
    void merchantSettleImpl(SettleAccountFlow flow, String orderNo, String merchantNo, long accountId);

    /**
     * 手续费结算(发消息实现)
     *
     * @param orderNo
     * @return
     */
    Pair<Integer, String> poundageSettle(String orderNo);
}
