package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by zhangbin on 2016/12/20.
 */
@Data
public class ProfitDetailsRequest {

    /**
     * 收益类型
     * {@link com.jkm.hss.account.enums.EnumSplitBusinessType}
     */
    private String businessType;

    /**
     * 交易单号
     */
    private String orderNo;

    /**
     * 收款用户名
     */
    private String receiptMoneyUserName;

    /**
     * 账户id
     */
    private int id;

    /**
     * 页数
     */
    private Integer pageNo;

    /**
     * 每页条数
     */
    private Integer pageSize;

    /**
     * 条数
     */
    private Integer offset;

    /**
     * 开始日期
     */
    private String startTime;

    /**
     * 结束日期
     */
    private String endTime;

}
