package com.jkm.hss.dealer.service;

import com.jkm.base.common.entity.PageModel;
import com.jkm.hss.dealer.entity.DailyProfitDetail;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-25.
 */
public interface DailyProfitDetailService {

    /**
     * 日常收单分润统计(定时任务)
     * 每天凌晨0点跑定时任务,统计前一天的收单分润,提现分润
     */
    void dailyProfitCount();

    /**
     * 查询分润详情 to商户
     * @param dealerId
     * @return
     */
    List<DailyProfitDetail> toMerchant(long dealerId);

    /**
     * 查询分润详情, to二级
     * @param dealerId
     * @return
     */
    List<DailyProfitDetail> toDealer(long dealerId);

    /**
     * 查询
     * @param beginProfitDate
     * @param dealerName
     * @param endProfitDate
     * @param pageNO
     * @param pageSize
     * @return
     */
    PageModel<DailyProfitDetail> selectFirstByParam(String beginProfitDate, String dealerName, String endProfitDate, int pageNO, int pageSize);

    /**
     * 查询
     * @param beginProfitDate
     * @param dealerName
     * @param endProfitDate
     * @param pageNO
     * @param pageSize
     * @return
     */
    PageModel<DailyProfitDetail> selectSecondByParam(String beginProfitDate, String dealerName, String endProfitDate, int pageNO, int pageSize);

    /**
     * 查询公司分润
     * @param beginProfitDate
     * @param dealerName
     * @param endProfitDate
     * @param pageNO
     * @param pageSize
     * @return
     */
    PageModel<DailyProfitDetail> selectCompanyByParam(String beginProfitDate, String dealerName, String endProfitDate, int pageNO, int pageSize);

    /**
     * 查询
     * @param id
     * @return
     */
    DailyProfitDetail selectById(long id);
}
