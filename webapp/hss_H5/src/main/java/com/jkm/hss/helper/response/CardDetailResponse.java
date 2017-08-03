package com.jkm.hss.helper.response;

import lombok.Data;

/**
 * Created by xingliujie on 2017/8/2.
 */
@Data
public class CardDetailResponse {
    private String bankNo;
    private String realBankNo;
    private String mobile;
    private String bankName;
    private String bankBin;
    private String provinceCode;
    private String provinceName;
    private String cityCode;
    private String cityName;
    private String countyCode;
    private String countyName;
    private String branchCode;
    private String branchName;
    private String branchShortName;
}
