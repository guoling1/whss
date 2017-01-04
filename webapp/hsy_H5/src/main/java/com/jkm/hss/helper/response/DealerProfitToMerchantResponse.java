package com.jkm.hss.helper.response;

import com.jkm.hss.dealer.entity.DailyProfitDetail;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by yuxiang on 2016-11-29.
 */
@Data
public class DealerProfitToMerchantResponse {

    /**
     * .分润日期
     */
    private String profitDate;

    /**
     * 收单分润总额
     */
    private BigDecimal dayCollectTotalMoney;

    /**
     * 体现分润总额
     */
    private BigDecimal daywithdrawTotalMoney;

    /**
     * 分润总额
     */
    private BigDecimal dayTotalMoney;
    /**
     * 改日分润记录列表
     */
    private List<DailyProfitDetail> list;

    /**
     * 每日分润记录的创建时间
     */
    private Date createTime;
}
