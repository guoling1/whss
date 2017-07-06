package com.jkm.hss.merchant.entity;

import lombok.Data;

/**
 * Created by zhangbin on 2017/7/6.
 */
@Data
public class SaveLineNoRequest {

    private String branchCode; //支行号

    private String bankName; //支行名称

    private String branchProvinceName; //所属省份名称

    private String branchProvinceCode; //省份编码

    private String branchCityCode; //所属市编码

    private String branchCityName; //所属市名称

    private String branchCountyCode; //所属区县编码

    private String branchCountyName; //所属区县名称

}
