package com.jkm.hss.bill.service.impl;

import com.google.common.collect.Maps;
import com.jkm.base.common.util.DateFormatUtil;
import com.jkm.hss.bill.dao.MergeTableSettlementDateDao;
import com.jkm.hss.bill.entity.MergeTableSettlementDate;
import com.jkm.hss.bill.service.MergeTableSettlementDateService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by yulong.zhang on 2017/3/16.
 */
@Service
public class MergeTableSettlementDateServiceImpl implements MergeTableSettlementDateService {

    @Autowired
    private MergeTableSettlementDateDao mergeTableSettlementDateDao;

    private static final AtomicBoolean isInit = new AtomicBoolean(false);

    private static final ConcurrentMap<Integer, List<Triple<Date, Date, Date>>> map = Maps.newConcurrentMap();

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<MergeTableSettlementDate> getAll() {
        return this.mergeTableSettlementDateDao.selectAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        if (isNotInit()) {
            synchronized (MergeTableSettlementDateServiceImpl.class) {
                if (isNotInit()) {
                    final List<MergeTableSettlementDate> settlementDateList = this.getAll();
                    for (MergeTableSettlementDate settlementDate : settlementDateList) {
                        final List<Triple<Date, Date, Date>> tripleList = map.get(settlementDate.getUpperChannel());
                        if (null == tripleList) {
                            map.putIfAbsent(settlementDate.getUpperChannel(), new ArrayList<Triple<Date, Date, Date>>());
                        }
                        map.get(settlementDate.getUpperChannel())
                                .add(Triple.of(settlementDate.getBeganDate(), settlementDate.getEndDate(), settlementDate.getSettleDate()));
                    }
                    isInit.set(true);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param tradeDate
     * @param upperChannel
     * @return
     */
    @Override
    public Date getSettlementDate(Date tradeDate, final int upperChannel) {
        this.assertInit();
        tradeDate = DateFormatUtil.parse(DateFormatUtil.format(tradeDate, DateFormatUtil.yyyy_MM_dd), DateFormatUtil.yyyy_MM_dd);
        final List<Triple<Date, Date, Date>> triples = map.get(upperChannel);
        if (!CollectionUtils.isEmpty(triples)) {
            for (Triple<Date, Date, Date> triple : triples) {
                if (triple.getLeft().compareTo(tradeDate) <= 0
                        && triple.getMiddle().compareTo(tradeDate) >= 0) {
                    return triple.getRight();
                }
            }
        }
        return null;
    }

    /**
     * 初始化
     */
    private void assertInit() {
        if (isNotInit()) {
            this.init();
        }
    }
    /**
     * 未初始化
     *
     * @return
     */
    private boolean isNotInit() {
        return !isInit.get();
    }
}
