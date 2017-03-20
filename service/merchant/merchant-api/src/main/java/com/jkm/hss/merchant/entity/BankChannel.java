package com.jkm.hss.merchant.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/3/7.
 */
@Data
public class BankChannel extends BaseEntity {
    /**
     * 银行账户编码
     */
    private Long accountBankId;
    /**
     * 通道编码
     */
    private Integer channelTypeSign;
    /**
     * 通道名称
     */
    private String channelName;
    /**
     * 通道来源
     */
    private String channelSource;
}
