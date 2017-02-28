package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/24.
 */
@Data
public class BankBranchRequest {
    /**
     * 查询条件
     */
    private String contions;
    /**
     * 地区
     */
    private String district;
}
