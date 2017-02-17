package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.merchant.helper.request.ProfitDetailsRequest;

import java.util.List;

/**
 * Created by zhangbin on 2017/2/13.
 */
public interface ProfitService {

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
