package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.JkmProfitDetailsResponse;
import com.jkm.hss.bill.entity.ProfitResponse;
import com.jkm.hss.merchant.entity.ProfitDetailsRequest;

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

    /**
     * 分润统计
     * @param req
     * @return
     */
    JkmProfitDetailsResponse profitAmount(ProfitDetailsRequest req);

    /**
     * 查询分润（提现）
     * @param businessOrderNo
     * @return
     */
    List<ProfitResponse> getInfo(String businessOrderNo);

    /**
     * 导出分润明细
     * @param req
     * @param baseUrl
     * @return
     */
    String downloadExcel(ProfitDetailsRequest req, String baseUrl);
}
