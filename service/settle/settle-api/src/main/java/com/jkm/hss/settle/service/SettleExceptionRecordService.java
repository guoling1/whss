package com.jkm.hss.settle.service;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.helper.requestparam.QuerySettlementRecordParams;
import com.jkm.hss.settle.entity.SettleExceptionRecord;
import org.apache.commons.lang3.tuple.Pair;

/**
 * Created by yuxiang on 2017-07-21.
 */
public interface SettleExceptionRecordService {

    void insert(SettleExceptionRecord settleExceptionRecord);

    void update(SettleExceptionRecord settleExceptionRecord);

    PageModel<SettleExceptionRecord> listSettlementRecordByParam(QuerySettlementRecordParams querySettlementRecordParams);

    SettleExceptionRecord selectByIdWithLock(long id);

    Pair<Integer, String> handleRecord(long settleExceptionId);
}
