package com.jkm.hss.admin.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/2/20.
 */
@Data
public class ProductionQrCodeRecord extends BaseEntity {
    /**
     * 开始码号
     */
    private String startCode;

    /**
     * 结束码号
     */
    private String endCode;
    /**
     * 个数
     */
    private int count;
    /**
     * 类型
     * {@link com.jkm.hss.admin.enums.EnumQRCodeDistributeType}
     */
    private int qrType;
    /**
     * 产品编码
     */
    private long productId;
    /**
     * 项目类型
     * {@link com.jkm.hss.admin.enums.EnumQRCodeSysType}
     */
    private String sysType;
    /**
     * 操作人编码
     */
    private long operatorId;
    /**
     * 下载地址
     */
    private String downloadUrl;
}