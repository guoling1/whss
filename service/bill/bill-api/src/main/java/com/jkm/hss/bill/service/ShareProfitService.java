package com.jkm.hss.bill.service;

import com.jkm.hss.account.entity.Account;
import com.jkm.hss.bill.entity.AccountDetailsResponse;
import com.jkm.hss.merchant.entity.ProfitDetailsRequest;
import com.jkm.hss.merchant.helper.request.JkmProfitRequest;

import java.util.List;

/**
 * Created by zhangbin on 2017/2/13.
 */
public interface ShareProfitService {

    /**
     * 账户列表
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
     * 金开门账户明细
     * @param req
     * @return
     */
    List<AccountDetailsResponse> selectAccountDetails(ProfitDetailsRequest req);

    /**
     * 公司账户明细总条数
     * @param req
     * @return
     */
    int selectAccountDetailsCount(ProfitDetailsRequest req);


    Account getChannel();

    /**
     * 通道账户明细
     * @param req
     * @return
     */
    List<AccountDetailsResponse> selectChannelDetails(ProfitDetailsRequest req);

    /**
     * 通道账户明细总数
     * @param req
     * @return
     */
    int selectChannelDetailsCount(ProfitDetailsRequest req);
}
