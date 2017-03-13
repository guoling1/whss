package com.jkm.hss.helper.request;

import com.jkm.hss.admin.enums.EnumQRCodeDistributeType;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
@Data
public class ProductionQrCodeRequest {
    /**
     * 系统类型（hss,hsy）
     */
    private String sysType;
    /**
     * 二维码类型
     * {@link EnumQRCodeDistributeType}
     */
    private int type;
    /**
     * 生成二维码个数
     */
    private int count;
}
