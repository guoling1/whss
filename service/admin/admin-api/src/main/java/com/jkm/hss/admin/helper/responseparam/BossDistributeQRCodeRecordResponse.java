package com.jkm.hss.admin.helper.responseparam;

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
public class BossDistributeQRCodeRecordResponse {
    /**
     * 记录编码
     */
    private long id;
    /**
     * 分配时间
     */
    private Date distributeTime;
    /**
     * 代理商名称
     */
    private String proxyName;
    /**
     * 代理商编码
     */
    private String markCode;
    /**
     * 上级代理名称
     */
    private String fristProxyName;
    /**
     * 上级代理商编码
     */
    private String firstMarkCode;


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

    /**
     *
     */
    private String operateUser;

}
