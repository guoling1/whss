package com.jkm.hss.dealer.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by wayne on 17/5/5.
 */
@Data
public class STDealerHistory extends BaseEntity {
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
     * 历史分润
     */
    private BigDecimal allProfit;
    /**
     * 历史商户交易额-直属
     */
    private BigDecimal allMertradeAmountDir;
    /**
     *历史商户交易额-下级代理
     */
    private BigDecimal allMertradeAmountSub;
    /**
     *历史商户注册数-直属
     */
    private Integer allregMerNumberDir;
    /**
     * 历史商户注册数-下级代理
     */
    private Integer allregMerNumberSub;
    /**
     * 历史商户审核数-直属
     */
    private Integer allcheckMerNumberDir;
    /**
     * 历史商户审核数-下级代理
     */
    private Integer allcheckMerNumberSub;
}
