package com.jkm.hss.admin.entity;

import com.jkm.base.common.entity.BaseEntity;
import lombok.Data;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 * 二维码分配记录(一级代理商分配)
 *
 * tb_distribute_qr_code_record
 */
@Data
public class DistributeQRCodeRecord extends BaseEntity {

    /**
     * 一级代理商id
     */
    private long firstLevelDealerId;

    /**
     * 二级代理商id
     */
    private long secondLevelDealerId;

    /**
     * 个数
     */
    private int count;

    /**
     * 开始码号
     */
    private String startCode;

    /**
     * 结束码号
     */
    private String endCode;
    /**
     * 类型
     * {@link com.jkm.hss.admin.enums.EnumQRCodeDistributeType}
     */
    private int type;

    /**
     * 分配方
     * {@link com.jkm.hss.admin.enums.EnumQRCodeDistributeType2}
     */
    private int distributeType;
    /**
     *
     */
    private long operatorId;

}
