package com.jkm.hss.helper;

import lombok.Data;

/**
 * Created by yuxiang on 2016-12-08.
 */
@Data
public class DealerProfitQueryParam extends PageQueryParam {

    //查询条件：分润日期，代理商编号，代理商名称
    /**
     * 开始分润日期
     */
    private String beginProfitDate;

    /**
     * 结束分润日期
     */
    private String endProfitDate;

    /**
     * 代理商名称
     */
    private String dealerName;

    /**
     *每日分润id
     */
    private long dailyProfitId;
}
