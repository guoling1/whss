package com.jkm.hss.merchant.service;

import com.jkm.hss.merchant.helper.request.CompanyPrifitRequest;
import com.jkm.hss.merchant.helper.response.CompanyProfitResponse;

import java.text.ParseException;
import java.util.List;

/**
 * Created by zhangbin on 2017/2/19.
 */
public interface AllProfitService {

    /**
     * 查询公司分润
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectCompanyProfit(CompanyPrifitRequest req) throws ParseException;

    /**
     * 查询总数
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectCompanyProfitCount(CompanyPrifitRequest req);

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
    List<CompanyProfitResponse> selectOneProfitCount(CompanyPrifitRequest req);

    /**
     * 二级代理商分润
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectTwoProfit(CompanyPrifitRequest req);

    /**
     * 公司分润详情
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectCompanyProfitDetails(CompanyPrifitRequest req) throws ParseException;

    /**
     * 一级代理商分润详情
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectOneProfitDetails(CompanyPrifitRequest req);

    /**
     * 二级代理商分润详情
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectTwoProfitDetails(CompanyPrifitRequest req);

    /**
     * 公司分润详情总数
     * @param req
     * @return
     */
    int selectCompanyProfitDetailsCount(CompanyPrifitRequest req);

    /**
     * 一级代理商分润详情总数
     * @param req
     * @return
     */
    int selectOneProfitDetailsCount(CompanyPrifitRequest req);

    /**
     * 二级代理商分润详情总数
     * @param req
     * @return
     */
    int selectTwoProfitDetailsCount(CompanyPrifitRequest req);

    /**
     * 二级代理商分润所有无分页
     * @param req
     * @return
     */
    List<CompanyProfitResponse> selectTwoAll(CompanyPrifitRequest req);

    /**
     * 导出公司分润
     * @param req
     * @param baseUrl
     * @return
     * @throws ParseException
     */
    String downloadExcel(CompanyPrifitRequest req, String baseUrl) throws ParseException;

    /**
     * 导出一级代理分润
     * @param req
     * @param baseUrl
     * @return
     * @throws ParseException
     */
    String downloadExcelOneDealer(CompanyPrifitRequest req, String baseUrl) throws ParseException;

    /**
     * 导出二级代理分润
     * @param req
     * @param baseUrl
     * @return
     */
    String downloadExcelTwoDealer(CompanyPrifitRequest req, String baseUrl) throws ParseException;

    /**
     * 公司分润统计
     * @param req
     * @return
     */
    String companyAmount(CompanyPrifitRequest req);

    /**
     * 一级分润统计
     * @param req
     * @return
     */
    String getFirstAmount(CompanyPrifitRequest req);

    /**
     * 二级分润统计
     * @param req
     * @return
     */
    String secondAmount(CompanyPrifitRequest req);
}
