package com.jkm.hss.dealer.helper.response;

import lombok.Data;
import com.jkm.hss.dealer.helper.response.DealerReport;

import java.math.BigDecimal;

/**
 * Created by wayne on 17/4/27.
 * 代理商首页报表
 */
@Data
public class HomeReportResponse {
    /**
     * 昨日分润
     */
    private BigDecimal yDayProfit;
    /**
     * 历史分润总额
     */
    private BigDecimal allProfit;
    /**
     * 待结算金额
     */
    private BigDecimal duesettleAmount;
    /**
     * 可用余额
     */
    private BigDecimal availableAmount;
    /**
     * 代理商相关报表数据-好收收
     */
    private DealerReport dealerReporthss;
    /**
     * 代理商相关报表数据-好收银
     */
    private DealerReport dealerReporthsy;
}
