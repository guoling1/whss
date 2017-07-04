package com.jkm.hss.helper.request;

import lombok.Data;

import java.util.Date;

/**
 * Created by yulong.zhang on 2016/11/25.
 *
 * 二维码分配记录(一级代理商分配)
 *
 * tb_distribute_qr_code_record
 */
@Data
public class DistributeQRCodeRecordResponse {
    /**
     * 代理商名称
     */
    private String dealerName;
    /**
     * 代理商手机号
     */
    private String dealerMobile;
    /**
     * 分配时间
     */
    private Date distributeTime;
    /**
     * 类型
     * {@link com.jkm.hss.admin.enums.EnumQRCodeDistributeType}
     */
    private int type;

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

}
