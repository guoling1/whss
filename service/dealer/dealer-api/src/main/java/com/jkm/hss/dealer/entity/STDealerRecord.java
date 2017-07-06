package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wayne on 17/5/2.
 * 代理商数据日统计
 */
@Data
public class STDealerRecord extends BaseEntity {
    /**
     * 经销商id
     */
    private long dealerId;
    /**
     * 代理商名称
     */
    private String proxy_name;
    /**
     * 统计日期
     */
    private Date recordDay;
    /**
     * 业务类型
     */
    private String sys_type;
    /**
     * 日分润
     */
    private BigDecimal yDayProfit;
    /**
     * 日商户交易额-直属
     */
    private BigDecimal yDayMertradeAmountDir;
    /**
     *日商户交易额-下级代理
     */
    private BigDecimal yDayMertradeAmountSub;
    /**
     *日商户注册数-直属
     */
    private Integer yDayregMerNumberDir;
    /**
     * 日商户注册数-下级代理
     */
    private Integer yDayregMerNumberSub;
    /**
     * 日商户审核数-直属
     */
    private Integer yDaycheckMerNumberDir;
    /**
     * 日商户审核数-下级代理
     */
    private Integer yDaycheckMerNumberSub;
}
