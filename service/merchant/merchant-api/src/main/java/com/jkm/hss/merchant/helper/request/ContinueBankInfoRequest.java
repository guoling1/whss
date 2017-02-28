package com.jkm.hss.merchant.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/24.
 */
@Data
public class ContinueBankInfoRequest {
    /**
     * 商户编码
     */
    private long id;
    /**
     * 支行编码
     */
    private String branchCode;
    /**
     * 支行名称
     */
    private String branchName;
    /**
     * 所在省份编码
     */
    private String provinceCode;
    /**
     * 所在省份名称
     */
    private String provinceName;
    /**
     * 所在城市编码
     */
    private String cityCode;
    /**
     * 所在城市名称
     */
    private String cityName;
    /**
     * 所在县编码
     */
    private String countyCode;
    /**
     * 所在县名称
     */
    private String countyName;
}
