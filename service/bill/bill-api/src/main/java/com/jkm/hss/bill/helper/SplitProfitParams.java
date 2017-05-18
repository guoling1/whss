package com.jkm.hss.bill.helper;

import com.jkm.hss.account.enums.EnumAccountUserType;
import com.jkm.hss.account.enums.EnumSplitBusinessType;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by yulong.zhang on 2017/5/12.
 *
 * 分润入参
 */
@Data
public class SplitProfitParams {

    /**
     * 交易单号
     */
    private String orderNo;
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
    private List<SplitProfitDetail>  splitProfitDetails;


    @Data
    public class SplitProfitDetail {
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

    /**
     * 获取通道的分润详情
     *
     * @return
     */
    public SplitProfitDetail getBasicChannelProfitDetail() {
        return this.getProfitDetail(EnumAccountUserType.COMPANY.getId(), 1);
    }

    /**
     * 获取产品分润详情
     *
     * @return
     */
    public SplitProfitDetail getProductProfitDetail() {
        return this.getProfitDetail(EnumAccountUserType.COMPANY.getId(), 2);
    }

    /**
     * 获取直推商户的分润详情
     *
     * @return
     */
    public SplitProfitDetail getDirectMerchantProfitDetail() {
        return this.getProfitDetail(EnumAccountUserType.MERCHANT.getId(), 1);
    }

    /**
     * 获取间推商户分润详情
     *
     * @return
     */
    public SplitProfitDetail getIndirectMerchantProfitDetail() {
        return this.getProfitDetail(EnumAccountUserType.MERCHANT.getId(), 2);
    }

    /**
     * 获取一级代理商的分润详情
     *
     * @return
     */
    public SplitProfitDetail getFirstLevelDealerProfitDetail() {
        return this.getProfitDetail(EnumAccountUserType.DEALER.getId(), 1);
    }

    /**
     * 获取二级代理分润详情
     *
     * @return
     */
    public SplitProfitDetail getSecondLevelDealerProfitDetail() {
        return this.getProfitDetail(EnumAccountUserType.DEALER.getId(), 2);
    }

    /**
     * 获取分润详情
     *
     * @param accountUserType
     * @param level
     * @return
     */
    private SplitProfitDetail getProfitDetail (final int accountUserType, final int level) {
        if (!CollectionUtils.isEmpty(this.splitProfitDetails)) {
            for (SplitProfitDetail splitProfitDetail : this.splitProfitDetails) {
                if (accountUserType == splitProfitDetail.getAccountUserType()) {
                    if (level == splitProfitDetail.getLevel()) {
                        return splitProfitDetail;
                    }
                }
            }
        }
        return null;
    }

}
