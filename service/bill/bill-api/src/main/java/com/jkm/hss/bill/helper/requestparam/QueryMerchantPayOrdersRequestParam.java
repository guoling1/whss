package com.jkm.hss.bill.helper.requestparam;

import com.jkm.base.common.entity.PageQueryParams;
import lombok.Data;

import java.util.List;

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
    private List<Integer> payStatus;

    /**
     * 支付方式
     *
     * {@link com.jkm.hss.product.enums.EnumPaymentChannel}
     */
    private List<Integer> payType;

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

    /**
     * 商户的账户id
     */
    private long accountId;

    private long offset;

    private int count;
}
