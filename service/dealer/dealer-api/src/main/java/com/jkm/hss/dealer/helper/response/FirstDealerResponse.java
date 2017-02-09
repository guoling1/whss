package com.jkm.hss.dealer.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by xingliujie on 2017/2/9.
 */
@Data
public class FirstDealerResponse {
    /**
     * 代理商编码
     */
    private long id;
    /**
     * 代理名称(唯一)
     */
    private String proxyName;


    /**
     * 代理手机号
     */
    private String markCode;

    /**
     * 经销商级别
     *
     * {@link com.jkm.hss.dealer.enums.EnumDealerLevel}
     */
    private int level;

    /**
     * 所在省code
     */
    private String belongProvinceCode;
    /**
     * 所在省
     */
    private String belongProvinceName;
    /**
     * 所在省code
     */
    private String belongCityCode;
    /**
     * 所在市
     */
    private String belongCityName;

    /**
     * 注册时间
     */
    protected Date createTime;

    /**
     * 代理手机号
     */
    private String mobile;

    /**
     * 好收收产品编码
     */
    private long hssProductId;

    /**
     * 好收银产品编码
     */
    private long hsyProductId;
}
