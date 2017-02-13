package com.jkm.hss.bill.service;

import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;

import java.util.List;

/**
 * Created by zhangbin on 2017/2/13.
 */
public interface ShareProfitService {

    /**
     * jkm利润列表
     * @param req
     * @return
     */
    List<MerchantTradeResponse> selectAccountList(OrderTradeRequest req);

    /**
     * 总条数
     * @param req
     * @return
     */
    long selectAccountListCount(OrderTradeRequest req);
}
