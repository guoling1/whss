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
    private String bankCode;
    /**
     * 支行名称
     */
    private String bankBranch;
}
