package com.jkm.base.common.ip;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created by huangwei on 6/2/15.
 */
@Data
public class IpInfo {
    private String country;
    @JSONField(name = "country_id")
    private String countryId;

    private String area;
    @JSONField(name = "area_id")
    private String areaId;

    private String region;
    @JSONField(name = "region_id")
    private String regionId;

    private String city;
    @JSONField(name = "city_id")
    private String cityId;

    private String county;
    @JSONField(name = "county_id")
    private String countyId;

    private String isp;
    @JSONField(name = "isp_id")
    private String ispId;

    private String ip;

    /**
     * <pre>
     *     获取详细地址
     *     国家 、地区、省（自治区或直辖市）、市（县）、运营商
     * </pre>
     *
     * @return
     */
    public String getLocation() {
        final StringBuilder sb = new StringBuilder();
        sb.append(getCountry()).append(' ')
                .append(getArea()).append(' ')
                .append(getRegion()).append(' ')
                .append(getCity()).append(' ')
                .append(getIsp());
        return sb.toString();
    }
}
