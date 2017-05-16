package com.jkm.hss.bill.helper;

import com.jkm.hss.account.enums.EnumSplitBusinessType;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/5/16.
 */
@Data
public class RefundProfitParams {

    /**
     * 交易单号
     */
    private String orderNo;
    /**
     * 全额退款
     *
     * {@link com.jkm.base.common.enums.EnumBoolean}
     */
    private int refundAll;
    /**
     * 分账类型
     *
     * {@link EnumSplitBusinessType}
     */
    private String splitType;
    /**
     * 成本
     *
     * 注：不要给null
     */
    private BigDecimal cost;
    /**
     * 分润方集合
     */
    private List<RefundProfitDetail> refundProfitDetails;


    @Data
    public class RefundProfitDetail {
        /**
         * 分润账户id
         */
        private long accountId;
        /**
         * 用户编号
         */
        private String userNo;
        /**
         * 分润方名字
         *
         * 分润方是通道时：通道账户，分润方是产品时：产品账户，分润方是商户时：代理名称等，分润方是代理商时：代理名称
         */
        private String userName;
        /**
         * 利润
         *
         * 注：不要给null
         */
        private BigDecimal profit;
        /**
         * 费率
         *
         * 注：不要给null
         */
        private BigDecimal rate;
        /**
         * 分润方账户类型
         *
         * {@link com.jkm.hss.account.enums.EnumAccountUserType}
         */
        private int accountUserType;
        /**
         * 当accountUserType=1时，level=1标识分润方是通道，level=2标识分润方是产品；
         * 当accountUserType=2时，level=1标识分润方是直推商户，level=2标识分润方是间推商户；
         * 当accountUserType=3时，level=1标识分润方是一级代理商，level=2标识分润方是耳机代理商；
         */
        private int level;
    }
}
