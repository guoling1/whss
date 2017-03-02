package com.jkm.hss.settle.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;


/**
 * Created by yulong.zhang on 2017/1/20.
 */
@Data
public class ListSettleAuditRecordRequest extends PageQueryParams {

    /**
     * 编号
     */
    private String userNo;

    /**
     * 商户名称
     */
    private String userName;

    /**
     *
     * {@link com.jkm.hss.account.enums.EnumAccountUserType}
     */
    private int userType;

    /**
     * 开始日期
     */
    private String startSettleDate;

    /**
     * 结束日期
     */
    private String endSettleDate;

    /**
     * 对账状态
     *
     * {@link com.jkm.hss.settle.enums.EnumAccountCheckStatus}
     */
    private int checkedStatus;

    /**
     * 结算状态
     *
     * {@link com.jkm.hss.settle.enums.EnumSettleStatus}
     */
    private int settleStatus;

    private int count;

    private int offset;
}
