package com.jkm.hss.bill.dao;


import com.jkm.hss.account.entity.Account;
import com.jkm.hss.bill.entity.AccountDetailsResponse;
import com.jkm.hss.merchant.helper.request.JkmProfitRequest;
import com.jkm.hss.merchant.helper.request.ProfitDetailsRequest;
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
    List<Account> selectAccountList(JkmProfitRequest req);

    /**
     * 总条数
     * @param req
     * @return
     */
    int selectAccountListCount(JkmProfitRequest req);

    /**
     * 金开门公司账户明细
     * @param req
     * @return
     */
    List<AccountDetailsResponse> selectAccountDetails(ProfitDetailsRequest req);

    /**
     * 公司账户明细总数
     * @param req
     * @return
     */
    int selectAccountDetailsCount(ProfitDetailsRequest req);

}
