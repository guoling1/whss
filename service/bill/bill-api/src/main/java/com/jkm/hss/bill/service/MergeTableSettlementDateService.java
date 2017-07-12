package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.MergeTableSettlementDate;

import java.util.Date;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/16.
 */
public interface MergeTableSettlementDateService {

    /**
     * 查询所有
     *
     * @return
     */
    List<MergeTableSettlementDate> getAll();

    /**
     * 初始化
     */
    void init();

    /**
     * 获取结算日期
     *
     * @param tradeDate
     * @param upperChannel
     * @return
     */
    Date getSettlementDate(Date tradeDate, int upperChannel);
}
