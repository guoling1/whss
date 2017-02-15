package com.jkm.hss.bill.dao;

import com.jkm.hss.bill.entity.MerchantTradeResponse;
import com.jkm.hss.merchant.helper.request.OrderTradeRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangbin on 2017/2/13.
 */
@Repository
public interface ShareProfitDao {
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
