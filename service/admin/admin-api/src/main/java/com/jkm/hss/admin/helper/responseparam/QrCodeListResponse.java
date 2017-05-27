package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

import java.util.Date;

/**
 * Created by xingliujie on 2017/2/16.
 */
@Data
public class QrCodeListResponse {
    /**
     * 产品类型
     */
    private String sysTypeName;
    /**
     * 二维码编号
     */
    private String code;
    /**
     * 激活时间
     */
    private Date activateTime;
    /**
     * 子公司名称
     */
    private String subCompanyName;
    /**
     * 一代名称
     */
    private String firstDealerName;
    /**
     * 二代名称
     */
    private String secondDealerName;
    /**
     * 商户名称
     */
    private String merchantName;
    /**
     * 商户编码
     */
    private String merchantMarkCode;
    /**
     * 商户状态
     */
    private String merchantStatus;
    /**
     * 店铺名称
     */
    private String shopName;
}
