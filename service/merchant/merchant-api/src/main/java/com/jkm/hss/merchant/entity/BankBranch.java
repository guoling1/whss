package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/23.
 */
@Data
public class BankBranch {
    /**
     * 编码
     */
    private long id;
    /**
     * 支行编码
     */
    private String branchCode;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 支行名称
     */
    private String branchName;
    /**
     * 支行所属省
     */
    private String provinceName;
    /**
     * 支行所属市
     */
    private String cityName;

}
