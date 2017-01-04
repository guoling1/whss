package com.jkm.hss.bill.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

/**
 * Created by yulong.zhang on 2017/1/4.
 */
@Data
public class QueryMerchantPayOrdersRequestParam extends PageQueryParams {

    /**
     * 支付状态
     *
     * {@link com.jkm.hss.bill.enums.EnumOrderStatus}
     */
    private int payStatus;

    /**
     * 支付方式
     *
     * {@link com.jkm.hss.bill.enums.EnumPaymentType}
     */
    private String payType;

    /**
     * 交易订单号
     */
    private String orderNo;

    /**
     * 开始日期
     */
    private String startDate;

    /**
     * 结束日期
     */
    private String endDate;
}
