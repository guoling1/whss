package com.jkm.hss.helper.request;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/8.
 */
@Data
public class DistributeQrCodeRequest {
    /**
     * 产品类型
     */
    private String sysType;
    /**
     * 代理商编码
     */
    private long dealerId;
    /**
     * 类型
     * {@link com.jkm.hss.admin.enums.EnumQRCodeDistributeType}
     */
    private int type;
    /**
     * 分配方式(1按码段 2按个数)
     */
    private int distributeType;
    /**
     * 起始码
     */
    private String startCode;
    /**
     * 终止码
     */
    private String endCode;
    /**
     * 分配数量
     */
    private int count;
}
