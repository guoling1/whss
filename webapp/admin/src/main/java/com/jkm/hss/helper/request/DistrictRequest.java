package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/8.
 */
@Data
public class DistrictRequest {
    private String belongCityName;
    private String belongProvinceName;
    private String branchCode;//联行号
    private String branchName;//联行名称
    private String bankName;//所属银行
    private String province;//省
    private String city;//市
    private String districtCode;//地区代码
    private String code;
    /**
     * 条数
     */
    private Integer offset;
    /**
     * 当前页数
     */
    private int pageNo;
    /**
     * 每页显示页数
     */
    private int pageSize;

}
