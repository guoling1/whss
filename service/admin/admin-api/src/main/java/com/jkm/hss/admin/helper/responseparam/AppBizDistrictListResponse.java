package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

import java.util.List;

/**
 * Created by xingliujie on 2017/2/8.
 */
@Data
public class AppBizDistrictListResponse {
    private String code;
    private String name;
    private List<CityListResponse> cityList;

    @Data
    public static class CityListResponse{
        private String code;
        private String name;
        private List<AppBizDistrictResponse> countryList;
    }
}
