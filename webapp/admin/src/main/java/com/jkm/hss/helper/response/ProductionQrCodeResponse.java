package com.jkm.hss.helper.response;

import lombok.Data;

import java.util.Date;

/**
 * Created by xingliujie on 2017/2/20.
 */
@Data
public class ProductionQrCodeResponse {
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 二维码类型
     */
    private String qrType;
    /**
     * 个数
     */
    private int count;
    /**
     * 生产时间
     */
    private Date productionTime;
    /**
     * 开始码号
     */
    private String startCode;
    /**
     * 结束码号
     */
    private String endCode;
    /**
     * 下载地址
     */
    private String downloadUrl;
}
