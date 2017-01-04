package com.jkm.hss.helper.response;

import com.jkm.hss.admin.helper.FirstLevelDealerCodeInfo;
import com.jkm.hss.admin.helper.MyMerchantCount;
import com.jkm.hss.admin.helper.SecondLevelDealerCodeInfo;
import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by yuxiang on 2016-11-29.
 */
@Data
public class DealerIndexInfoResponse {
    /**
     * 代理手机
     */
    private String mobile;

    /**
     * 是几级代理商
     */
    private int level;

    /**
     * 昨日分润总额
     */
    private BigDecimal yesterdayProfitMoney;

    /**
     * 昨日店铺交易总额
     */
    private BigDecimal yesterdayDealMoney;

    /**
     * 待结算分润
     */
    private BigDecimal waitBalanceMoney;

    /**
     * 已结分润
     */
    private BigDecimal alreadyBalanceMoney;

    /**
     * 历史分润总额
     */
    private BigDecimal historyProfitMoney;

    /**
     * 查询二级代理商二维码分配情况信息
     */
    private SecondLevelDealerCodeInfo secondLevelDealerCodeInfo;

    /**
     * 我发展的店铺统计
     */
    private MyMerchantCount myMerchantCount;

    //一级代理
    /**
     * 我的分润-二级代理
     * 昨日分润金额
     */
    private BigDecimal secondYesterdayProfitMoney;

    /**
     * 历史分润总额
     */
    private BigDecimal secondHistoryProfitMoney;

    /**
     * 我的分润-直管店铺
     */
    private BigDecimal merchantYesterdayProfitMoney;

    /**
     * 历史分润总额
     */
    private BigDecimal merchantHistoryProfitMoney;

    /**
     * 查询一级代理商二维码分配情况信息
     */
    private FirstLevelDealerCodeInfo firstLevelDealerCodeInfos;

    /**
     * 一级代理商发展二级代理的个数
     */
    private int mySecondDealerCount;

    /**
     * 一级代理商分配给二级代理商的二维码数
     */
    private int distributeToSecondDealerCodeCount;
}
