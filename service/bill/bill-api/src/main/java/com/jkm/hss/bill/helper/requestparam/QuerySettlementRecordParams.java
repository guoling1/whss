package com.jkm.hss.bill.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/3/1.
 */
@Data
public class QuerySettlementRecordParams extends PageQueryParams {

    /**
     * 编号
     */
    private String userNo;

    /**
     * 名称
     */
    private String userName;

    /**
     * 用户类型
     *
     * {@link com.jkm.hss.account.enums.EnumAccountUserType}
     */
    private int userType;

    /**
     * 结算单号
     */
    private String settleNo;

    /**
     * 结算状态
     *
     * {@link com.jkm.hss.bill.enums.EnumSettleStatus}
     */
    private int settleStatus;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;

    private long offset;

    private int count;
}
