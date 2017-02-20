package com.jkm.hss.merchant.dao;

import com.jkm.hss.merchant.helper.request.CompanyPrifitRequest;
import com.jkm.hss.merchant.helper.response.CompanyProfitResponse;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by zhangbin on 2017/2/19.
 */
@Repository
public interface AllProfitDao {

    /**
     * 查询公司分润
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectCompanyProfit(CompanyPrifitRequest req);

    /**
     * 查询总数
     * @param req
     * @return
     */
    int selectCompanyProfitCount(CompanyPrifitRequest req);

    /**
     * 一级代理商分润
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectOneProfit(CompanyPrifitRequest req);

    /**
     * 一级代理商分润总数
     * @param req
     * @return
     */
    int selectOneProfitCount(CompanyPrifitRequest req);

    /**
     * 二级代理商分润
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectTwoProfit(CompanyPrifitRequest req);

    /**
     * 二级代理商分润总数
     * @param req
     * @return
     */
    int selectTwoProfitCount(CompanyPrifitRequest req);

    /**
     * 公司分润详情
     * @param accId
     * @return
     */
    CompanyProfitResponse selectCompanyProfitDetails(long accId);

    /**
     * 一级代理商分润详情
     * @param receiptMoneyAccountId
     * @return
     */
    CompanyProfitResponse selectOneProfitDetails(long receiptMoneyAccountId);

    /**
     * 二级代理商分润详情
     * @param receiptMoneyAccountId
     * @return
     */
    CompanyProfitResponse selectTwoProfitDetails(long receiptMoneyAccountId);
}
