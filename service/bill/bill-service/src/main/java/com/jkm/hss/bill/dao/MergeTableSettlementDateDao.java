package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.MergeTableSettlementDate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by yulong.zhang on 2017/3/16.
 */
@Repository
public interface MergeTableSettlementDateDao {

    /**
     * 查询所有
     *
     * @return
     */
    List<MergeTableSettlementDate> selectAll();
}
