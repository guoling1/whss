package com.jkm.hss.admin.helper.responseparam;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

/**
 * Created by xingliujie on 2017/2/20.
 */
@Data
public class ProductionListResponse{
    /**
     * 主键id
     */
    protected long id;
    /**
     * 创建时间
     * datetime
     */
    protected Date createTime;
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
     * 项目类型
     * {@link com.jkm.hss.admin.enums.EnumQRCodeSysType}
     */
    private String sysType;
    /**
     * 操作人编码
     */
    private String operatorName;
}