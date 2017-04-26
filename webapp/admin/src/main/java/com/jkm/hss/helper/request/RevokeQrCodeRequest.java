package com.jkm.hss.helper.request;

import com.jkm.hss.admin.enums.EnumQRCodeDistributeType;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/30.
 */
@Data
public class RevokeQrCodeRequest {
    /**
     * 系统类型（hss,hsy）
     */
    private String sysType;
    /**
     * 开始码段
     */
    private String startCode;
    /**
     * 结束码段
     */
    private String endCode;
}
