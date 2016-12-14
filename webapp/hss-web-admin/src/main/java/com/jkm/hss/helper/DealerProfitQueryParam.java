package com.jkm.hss.helper;

import lombok.Data;

/**
 * Created by yuxiang on 2016-12-08.
 */
@Data
public class DealerProfitQueryParam extends PageQueryParam {

    //查询条件：分润日期，代理商编号，代理商名称
    /**
     * 分润日期
     */
    private String profitDate;

    /**
     * 代理商编号
     */
    private long dealerId;

    /**
     * 代理商名称
     */
    private String dealerName;
}
