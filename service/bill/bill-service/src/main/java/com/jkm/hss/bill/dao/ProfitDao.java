package com.jkm.hss.bill.dao;


import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.merchant.helper.request.ProfitDetailsRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangbin on 2017/2/13.
 */
@Repository
public interface ProfitDao {

    /**
     * 分润明细列表
     * @param req
     * @return
     */
    List<JkmProfitDetailsResponse> selectProfitDetails(ProfitDetailsRequest req);

    /**
     * 分润明细总数
     * @param req
     * @return
     */
    int selectProfitDetailsCount(ProfitDetailsRequest req);
}
