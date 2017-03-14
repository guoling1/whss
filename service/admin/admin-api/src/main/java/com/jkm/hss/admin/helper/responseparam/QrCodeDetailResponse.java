package com.jkm.hss.admin.helper.responseparam;

import lombok.Data;

/**
 * Created by xingliujie on 2017/2/16.
 */
@Data
public class QrCodeDetailResponse {
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 二维码编码
     */
    private String code;
    /**
     * 二维码地址
     */
    private String qrUrl;

}
