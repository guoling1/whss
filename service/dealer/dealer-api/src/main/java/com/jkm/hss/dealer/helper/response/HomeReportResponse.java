package com.jkm.hss.dealer.helper.response;

import lombok.Data;
import com.jkm.hss.dealer.helper.response.DealerReport;

/**
 * Created by wayne on 17/4/27.
 * 代理商首页报表
 */
@Data
public class HomeReportResponse {
    /**
     * 昨日分润
     */
    private String yDayProfit;
    /**
     * 历史分润总额
     */
    private String allProfit;
    /**
     * 待结算金额
     */
    private String duesettleAmount;
    /**
     * 可用余额
     */
    private String availableAmount;
    /**
     * 代理商相关报表数据-好收收
     */
    private DealerReport dealerReporthss;
    /**
     * 代理商相关报表数据-好收银
     */
    private DealerReport dealerReporthsy;
}
