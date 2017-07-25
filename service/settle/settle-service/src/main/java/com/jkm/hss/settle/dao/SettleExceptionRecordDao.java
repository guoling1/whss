package com.jkm.hss.settle.dao;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.bill.helper.requestparam.QuerySettlementRecordParams;
import com.jkm.hss.settle.entity.SettleExceptionRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yuxiang on 2017-07-21.
 */
@Repository
public interface SettleExceptionRecordDao {

    void insert(SettleExceptionRecord settleExceptionRecord);

    void update(SettleExceptionRecord settleExceptionRecord);

    int selectCountByParam(QuerySettlementRecordParams querySettlementRecordParams);

    List<SettleExceptionRecord> selectByParam(QuerySettlementRecordParams querySettlementRecordParams);

    SettleExceptionRecord selectByIdWithLock(long id);
}
