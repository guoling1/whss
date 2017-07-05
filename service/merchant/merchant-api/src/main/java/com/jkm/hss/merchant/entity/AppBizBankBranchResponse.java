package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/5/27.
 */
@Data
public class AppBizBankBranchResponse  {

    private Long id;
    private String branchCode;//联行号
    private String branchName;//联行名称
    private String bankName;//所属银行
    private String province;//省
    private String city;//市
    private Integer available;//可用状态
    private String districtCode;//地区代码
    private Integer bankIndex;//顺序

    private Integer currentPage;
}
