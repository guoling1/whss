package com.jkm.hss.bill.dao;


import com.jkm.hss.bill.entity.JkmProfitResponse;
import com.jkm.hss.merchant.helper.request.JkmProfitRequest;
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
    List<JkmProfitResponse> selectAccountList(JkmProfitRequest req);

    /**
     * 总条数
     * @param req
     * @return
     */
    int selectAccountListCount(JkmProfitRequest req);
}
