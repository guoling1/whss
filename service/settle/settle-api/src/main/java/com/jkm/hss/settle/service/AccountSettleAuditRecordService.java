package com.jkm.hss.settle.service;

import com.google.common.base.Optional;
import com.jkm.hss.settle.entity.AccountSettleAuditRecord;
import org.apache.commons.lang3.tuple.Pair;

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
     * 处理 T1 结算审核, 生成记录
     */
    void handleT1SettleTask();

    /**
     * 结算
     *
     * @param recordId
     * @param option
     * @return
     */
    Pair<Integer, String> settle(long recordId, int option);

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
     * @return
     */
    Pair<Integer, String> settleCheckedPart(long recordId);

    /**
     * 结算全部
     *
     * @param recordId
     * @return
     */
    Pair<Integer, String> forceSettleAll(long recordId);
}
