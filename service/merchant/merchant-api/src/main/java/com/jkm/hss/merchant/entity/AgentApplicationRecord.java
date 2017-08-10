package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by xingliujie on 2017/8/8.
 */
@Data
public class AgentApplicationRecord extends BaseEntity {
    /**
     * 商户编码
     */
    private Long merchantId;
    /**
     * 注册手机号
     */
    private String mobile;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 商户标示
     */
    private String markCode;
    /**
     * 分公司编码
     */
    private Long oemId;
    /**
     * 一级代理编码
     */
    private Long firstDealerId;
    /**
     * 二级代理编码
     */
    private Long secondDealerId;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    /**
     * 升级等级
     */
    private Integer level;
    /**
     * 是否处理 0未处理 1已处理
     */
    private Integer isDeal;

}
