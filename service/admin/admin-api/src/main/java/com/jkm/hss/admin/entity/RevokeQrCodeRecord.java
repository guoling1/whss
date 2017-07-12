package com.jkm.hss.admin.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by xingliujie on 2017/4/26.
 */
@Data
public class RevokeQrCodeRecord extends BaseEntity{
    /**
     * 开始码
     */
    private String startCode;
    /**
     * 结束码
     */
    private String endCode;
    /**
     * 成功数量
     */
    private long successCount;
    /**
     * 失败数量
     */
    private long failCount;
    /**
     * 产品类型
     */
    private String sysType;
    /**
     * 操作人编码
     */
    private long operatorId;
    /**
     * 平台类型
     */
    private String platformType;
}
