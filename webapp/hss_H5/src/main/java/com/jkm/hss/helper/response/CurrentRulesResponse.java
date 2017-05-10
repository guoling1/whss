package com.jkm.hss.helper.response;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by xingliujie on 2017/5/9.
 */
@Data
public class CurrentRulesResponse {
    /**
     * 合伙人编码
     */
    private long id;
    /**
     * 合伙人类型
     * {@link com.jkm.hss.product.enums.EnumUpGradeType}
     */
    private Integer type;
    /**
     * 合伙人名称
     */
    private String name;
    /**
     * 升级费
     */
    private BigDecimal needMoney;
    /**
     * 需要多少好友
     */
    private int needCount;
    /**
     * 还差多少好友
     */
    private int restCount;
}
